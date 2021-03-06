/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.mazexiang.application.manager;

import com.google.common.collect.Lists;
import com.mazexiang.application.common.LogUtil;
import com.mazexiang.application.common.HttpUtil;
import com.mazexiang.application.common.StringUtil;
import com.mazexiang.application.model.ArkInvestRecordDO;
import com.mazexiang.application.model.ArkStockDO;
import com.mazexiang.application.service.ArkInvestRecordService;
import com.mazexiang.application.service.ArkStockService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author mazexiang
 * @version $Id: ArkManager, v 0.1 2020-11-07 6:17 PM mzx Exp $
 */
@Service
public class ArkManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(ArkManager.class);
    private static final String ARKK_URL =
            "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_INNOVATION_ETF_ARKK_HOLDINGS.csv";
    private static final String ARKW_URL =
            "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_NEXT_GENERATION_INTERNET_ETF_ARKW_HOLDINGS.csv";
    private static final String ARKQ_URL  =
            "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_AUTONOMOUS_TECHNOLOGY_&_ROBOTICS_ETF_ARKQ_HOLDINGS.csv";
    private static final String ARKG_URL =
            "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_GENOMIC_REVOLUTION_MULTISECTOR_ETF_ARKG_HOLDINGS.csv";
    private static final String ARKF_URL =
            "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_FINTECH_INNOVATION_ETF_ARKF_HOLDINGS.csv";
    private static final String PRNT=
            "https://ark-funds.com/wp-content/fundsiteliterature/csv/THE_3D_PRINTING_ETF_PRNT_HOLDINGS.csv";
    private static final String IZRL =
            "https://ark-funds.com/wp-content/fundsiteliterature/csv/ARK_ISRAEL_INNOVATIVE_TECHNOLOGY_ETF_IZRL_HOLDINGS.csv";
    private static final List<String> urls = Lists.newArrayList(ARKK_URL,ARKW_URL,ARKQ_URL,ARKG_URL,ARKF_URL,PRNT,IZRL);

    @Autowired
    private ArkInvestRecordService arkInvestRecordService;
    @Autowired
    private ArkStockService arkStockService;
    /**
     *  每周二，三，四，五，六的10:15分运行
     */
    @Scheduled(cron = "0 15 10 ? * TUE-SAT")
    public void dailyCreateArkInvestRecord(){
       for (String url : urls){
           LogUtil.info(LOGGER,"start download ",url);
           downloadFile(url);
       }
       LogUtil.info(LOGGER,"download finish");
    }

    public  void downloadFile(String url){
        try {
            InputStream inputStream = HttpUtil.geInputStream(url);
            if (inputStream == null){
                LogUtil.error(LOGGER,"数据下载失败in downloadFile",url);
                return;
            }
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(inputStream));
            String str = null;
            // 提前读掉第一行
            reader.readLine();
            str =  reader.readLine();
            String[] split2Line = str.split(",");
            int count =  arkInvestRecordService.countByDateAndFund(split2Line[0],split2Line[1]);
            if (count >0){
                // 当前的文件信息已经是最新的，不需要更新
                LogUtil.info(LOGGER,split2Line[1],split2Line[0],"当前的文件信息已经是最新的");
                return;
            }
            while (!StringUtil.isEmpty(str)) {
                String[] split = str.split(",");
                if (split.length!=8){
                    continue;
                }
                LogUtil.info(LOGGER,str);
                ArkInvestRecordDO recordDO = new ArkInvestRecordDO();
                recordDO.setGmtCreated(SimpleDateFormat.getDateInstance().format(new Date()));
                recordDO.setDate(split[0]);
                recordDO.setFund(split[1]);
                recordDO.setCompany(split[2]);
                recordDO.setTicker(split[3]);
                recordDO.setCusip(split[4]);
                recordDO.setShares(Double.valueOf(split[5]).intValue());
                recordDO.setMarketValue(Double.valueOf(split[6]));
                recordDO.setWeight(Double.valueOf(split[7]));
                arkInvestRecordService.insert(recordDO);
                ArkStockDO arkStockDO = arkStockService.queryByTicker(split[3]);
                if (arkStockDO == null || arkStockDO.getId() == 0 ) {
                    arkStockDO = new ArkStockDO();
                    arkStockDO.setGmtCreated(SimpleDateFormat.getDateInstance().format(new Date()));
                    arkStockDO.setTicker(split[3]);
                    arkStockService.insert(arkStockDO);
                }
                str = reader.readLine();
                LogUtil.info(LOGGER,"str = ",str);
            }
            inputStream.close();
        }catch (Exception e   ){
            LogUtil.error(LOGGER,e);
        }
    }


    public List<ArkInvestRecordDO> queryByTicker(String ticker){
        return arkInvestRecordService.queryByTicker(ticker);
    }

    public Map<String, List< ArkInvestRecordDO>> queryByFundInDays(String fund,int days){
        return null;
    }

    public List<ArkStockDO> stockList(){

        return arkStockService.queryAll();
    }

    /**
     * 查询当日新购入的股票
     * @param date
     * @return
     */
    public List<ArkStockDO> queryNewBuyStock(String date){
        return arkStockService.queryByDate(date);

    }



}
