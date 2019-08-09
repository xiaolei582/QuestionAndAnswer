package com.panshi.mapper;

import com.panshi.domain.QuaysMember;
import com.panshi.domain.QuaysQuestion;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
* @description: 会员mapper类
* @author: 黄小磊
* @create: 2019-07-19
*/
@Mapper
@Repository
public interface MemberMapper {

    /**
     *  增加会员
     * @param quaysMember 会员属性对象
     */
    void add(QuaysMember quaysMember);


    /**
     *  通过账号查询
     * @param username
     * @return
     */
    QuaysMember usernameQuery(String username);

    /**
     *  通过id查询用户信息
     * @param userId
     * @return
     */
    QuaysMember userIdQuery(long userId);
    /**
     *  通过用于id查询用户积分流水
     * @param id
     * @return
     */
    QuaysMember integralQuery(String id);

    /**
     *  减积分
     * @param quaysQuestion
     */
    void subtract(QuaysQuestion quaysQuestion);

    /**
     * 加积分
     * @param quaysQuestion
     */
    void plus(QuaysQuestion quaysQuestion);

    /**
     * 排行
     * @return
     */
    List<QuaysMember> ranking();
}
