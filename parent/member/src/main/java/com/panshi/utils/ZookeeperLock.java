package com.panshi.utils;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @description 分布式锁
 * @author huangxiaolei
 * @create 2019/07/13
 */
public class ZookeeperLock {

    @Value("${nodeLock}")
    private String nodeLock;

    @Value("${zookeeperHostPort}")
    private String hostPort;

    private String encodeName;

    private String lockName;

    // 创建客户端对象
    ZooKeeper zk;

    /**
     *  path 所有用户都用相同的节点和不同节点的区别
     *  1. 用相同的节点，那就是大家一起排队效率低，用户多次点击请求时
     *  导致用户的请求不光要跟自己的请求排队还要跟大家的请求一起排队
     *
     *  2. 用不同的节点 比如用用户id作为节点名那么效率会高一点
     */
    public ZookeeperLock(String path) throws IOException, KeeperException, InterruptedException {
        if(path == null){
            return;
        }
        path = nodeLock+path;
        this.lockName = path;

        zk = new ZooKeeper(hostPort, 2000, e ->{
            System.out.println("删除事件");
            //监听删除事件
            synchronized (this.lockName){
                this.lockName.notifyAll();
            }
        });

        //创建根节点
        create(nodeLock);

        //创建功能节点
        create(lockName);
    }

    /**
     * 创建的都是永久节点
     * @param path
     */
    public void create(String path) throws KeeperException, InterruptedException {
        //判断节点是否存在
        if(zk.exists(path,null) == null){
            zk.create(path,null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }
    }


    //获取锁
    public boolean getLock() throws KeeperException, InterruptedException {
        //给获取锁的线程排序，创建顺序节点
        this.encodeName = zk.create(lockName+"/n", null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL_SEQUENTIAL);
        System.out.println(this.encodeName+"--");
        while (true){
            //获取所有的节点
            List<String> list = zk.getChildren(lockName, null);
            //按升序排序
            list = list.stream().sorted().collect(Collectors.toList());
            System.out.println("当前竞争的节点："+list);
            //获取排序时最小节点
            String name = list.get(0);
            //把锁给最小的节点，拿到锁之后return 出去
            if (encodeName.endsWith(name)){
                return true;
            }

            //未获取到锁就沉睡1秒后继续排队
            synchronized (lockName){
                lockName.wait(1000);
            }
        }
    }

    public void deleteLock() throws KeeperException, InterruptedException {
        System.out.println("删除节点："+encodeName);
        zk.delete(encodeName,-1);
    }

}