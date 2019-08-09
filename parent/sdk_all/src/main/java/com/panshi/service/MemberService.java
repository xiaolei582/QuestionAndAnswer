package com.panshi.service;

import com.panshi.domain.QuaysMember;
import com.panshi.domain.QuaysQuestion;
import com.panshi.dta.Message;
import org.apache.zookeeper.KeeperException;
import org.springframework.cache.annotation.Cacheable;

import java.io.IOException;
import java.util.List;

/**
* @description 会员接口
* @author huangxiaolei
* @create 2019-07-19
*/
public interface MemberService {

    /**
     *  会员增加
     * @param username
     * @param password
     * @return
     */
    Message add(String username, String password);

    /**
     *  通过用户名查询
     * @param username
     * @return
     */
    QuaysMember usernameQuery(String username);


    QuaysMember userIdQuery(long userId);

    /**
     * 根据用户id查询积分流水
     * @param id
     * @return
     */
    QuaysMember integralQuery(String id);

    /**
     *  加积分
     * @param quaysQuestion
     */
    void plus(QuaysQuestion quaysQuestion);

    /**
     *  减积分
     * @param quaysQuestion
     */
    void subtract(QuaysQuestion quaysQuestion) throws KeeperException, InterruptedException;

    /**
     *  排行榜
     * @return
     */
    List<QuaysMember> ranking();

    @Cacheable(key = "'user:'+#userId" )
    List<QuaysMember> dataCache(long userId);
}
