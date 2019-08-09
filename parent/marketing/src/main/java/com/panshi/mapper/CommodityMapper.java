package com.panshi.mapper;

import com.panshi.domain.Commodity;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author huangxiaolei
 */
@Mapper
@Repository
public interface CommodityMapper {

    /**
     *  通过商品id查询
     * @param id
     * @return
     */
    Commodity commodityIdQuery(long id);

    /**
     * 减库存
     * @param commodityId
     */
    int inventoryReduction(long commodityId,int version);
}
