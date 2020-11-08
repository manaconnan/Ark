/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.mazexiang.application.service;

import com.mazexiang.application.mapper.ArkStockMapper;
import com.mazexiang.application.model.ArkStockDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author mazexiang
 * @version $Id: ArkNewBuyStockService, v 0.1 2020-11-07 9:42 PM mzx Exp $
 */
@Service
public class ArkStockService {
    @Autowired
    private ArkStockMapper arkStockMapper;

    public int insert(ArkStockDO buyStockDO){
        return arkStockMapper.insert(buyStockDO);
    }
    public ArkStockDO queryByTicker(String ticker){
        return  arkStockMapper.queryByTicker(ticker);
    }
    public List<ArkStockDO> queryAll(){
        return arkStockMapper.queryAll();
    }

    public List<ArkStockDO> queryByDate(String date) {
        return arkStockMapper.queryByDate(date);
    }
}
