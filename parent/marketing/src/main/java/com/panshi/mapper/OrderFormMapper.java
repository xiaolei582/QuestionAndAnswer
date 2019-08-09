package com.panshi.mapper;

import com.panshi.domain.OrderForm;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author huangxiaolei
 */
@Mapper
@Repository
public interface OrderFormMapper {

    OrderForm userIdAndCommodityIdOrderFormQuery(long userId,long commodityId);


    void insert(long commodityId,long userId);

}
