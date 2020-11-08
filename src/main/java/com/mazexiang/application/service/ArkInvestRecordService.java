/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.mazexiang.application.service;

import com.mazexiang.application.mapper.ArkInvestRecordMapper;
import com.mazexiang.application.model.ArkInvestRecordDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mazexiang
 * @version $Id: ArkInvestRecord, v 0.1 2020-11-07 5:43 PM mzx Exp $
 */
@Service
public class ArkInvestRecordService {
    @Autowired
    private ArkInvestRecordMapper arkInvestRecordMapper;

    public   int insert(ArkInvestRecordDO recordDO){
        return  arkInvestRecordMapper.insert(recordDO);
    }
    public List<ArkInvestRecordDO> queryByTicker(String ticker){
        return arkInvestRecordMapper.selectByTicker(ticker);
    }

    public List<ArkInvestRecordDO> queryByDate(String date){
        return  arkInvestRecordMapper.selectByDate(date);
    }


    public int countByDateAndFund(String date, String fund) {
        return arkInvestRecordMapper.countByDateAndFund(date,fund);
    }
}
