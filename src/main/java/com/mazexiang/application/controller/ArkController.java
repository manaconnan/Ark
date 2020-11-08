/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.mazexiang.application.controller;

import com.mazexiang.application.manager.ArkManager;
import com.mazexiang.application.model.ArkInvestRecordDO;
import com.mazexiang.application.model.ArkStockDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * @author mazexiang
 * @version $Id: ArkController, v 0.1 2020-11-07 7:34 PM mzx Exp $
 */
@Controller
@RequestMapping("/ark")
public class ArkController {
    @Autowired
    private ArkManager arkManager;
    @RequestMapping(value = "triggerDownload")
    public void triggerDownload(){
         arkManager.dailyCreateArkInvestRecord();
    }
    @RequestMapping(value = "queryByTicker")
    @ResponseBody
    public List<ArkInvestRecordDO> queryByTicker( String ticker){
        return arkManager.queryByTicker(ticker);
    }


    @RequestMapping(value = "stockList")
    @ResponseBody
    public List<ArkStockDO> stockList(){
        return arkManager.stockList();
    }

    @RequestMapping(value = "queryNewBuyStock")
    @ResponseBody
    public List<ArkStockDO> queryNewBuyStock(String date){
        return arkManager.queryNewBuyStock(date);
    }
}
