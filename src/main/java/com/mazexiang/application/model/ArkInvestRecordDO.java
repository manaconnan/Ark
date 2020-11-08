/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.mazexiang.application.model;

import lombok.Data;

/**
 * @author mazexiang
 * @version $Id: ArkInvestRecordDO, v 0.1 2020-11-07 5:44 PM mzx Exp $
 */
@Data
public class ArkInvestRecordDO {
    private int id;
    private String gmtCreated;
    private String date;
    private String fund;
    private String company;
    private String ticker;
    private String cusip;
    private int shares;
    private double marketValue;
    private double weight;
}
