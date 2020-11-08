/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.mazexiang.application.mapper;

import com.mazexiang.application.model.ArkInvestRecordDO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author mazexiang
 * @version $Id: ArkInvestRecordMapper, v 0.1 2020-11-07 5:47 PM mzx Exp $
 */
@Mapper
@Component
public interface ArkInvestRecordMapper {
    @Insert("INSERT INTO ark_invest_record(gmt_created,date, fund, company, ticker, cusip, shares, market_value, weight) VALUES(#{gmtCreated},#{date}, #{fund},#{company},#{ticker},#{cusip},#{shares},#{marketValue},#{weight})")
    @SelectKey(statement = "SELECT seq id FROM sqlite_sequence WHERE (name = 'ark_invest_record')", before = false, keyProperty = "id", resultType = int.class)
    int insert(ArkInvestRecordDO recordDO);

    @Select("SELECT id,gmt_created as gmtCreated,date, fund, company, ticker, cusip, shares, market_value as marketValue, weight FROM ark_invest_record WHERE id=#{id}")
    ArkInvestRecordDO select(int id);

    @Select("SELECT id,gmt_created as gmtCreated,date, fund, company, ticker, cusip, shares, market_value as marketValue, weight FROM ark_invest_record")
    List<ArkInvestRecordDO> selectAll();

    @Select("select id,gmt_created as gmtCreated,date, fund, company, ticker, cusip, shares, market_value as marketValue, weight from ark_invest_record where ticker = #{ticker} order by id desc")
    List<ArkInvestRecordDO> selectByTicker(String ticker);
    @Select("select id,gmt_created as gmtCreated,date, fund, company, ticker, cusip, shares, market_value as marketValue, weight from ark_invest_record where date = #{date} order by id desc")
    List<ArkInvestRecordDO> selectByDate(String date);

    @Select("select id,gmt_created as gmtCreated,date, fund, company, ticker, cusip, shares, market_value as marketValue, weight from ark_invest_record where ticker = #{ticker} and date = #{date} order by id desc")
    List<ArkInvestRecordDO> selectByTickerAndDate(String ticker,String date);

    @Select("select count(id) from  ark_invest_record where date=#{date} and fund = #{fund}")
    int countByDateAndFund(String date, String fund);
}
