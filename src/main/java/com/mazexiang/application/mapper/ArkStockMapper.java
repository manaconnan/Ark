/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.mazexiang.application.mapper;

import com.mazexiang.application.model.ArkStockDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mazexiang
 * @version $Id: ArkNewBuyStockMapper, v 0.1 2020-11-07 9:34 PM mzx Exp $
 */
@Mapper
@Component
public interface ArkStockMapper {

    @Insert("INSERT INTO ark_stock(gmt_created,ticker) VALUES(#{gmtCreated},#{ticker})")
    @SelectKey(statement = "SELECT seq id FROM sqlite_sequence WHERE (name = 'ark_stock')", before = false, keyProperty = "id", resultType = int.class)
    int insert(ArkStockDO buyStockDO);

    @Select("select id,gmt_created as gmtCreated , ticker  from ark_stock order by id desc")
    List<ArkStockDO> queryAll();

    @Select("select id,gmt_created as gmtCreated , ticker from ark_stock where ticker = #{ticker}")
    ArkStockDO queryByTicker(String ticker);

    @Select("select id,gmt_created as gmtCreated , ticker from ark_stock where date = #{date}")
    List<ArkStockDO> queryByDate(String date);
}
