/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2020 All Rights Reserved.
 */
package com.mazexiang.application.common;

/**
 * @author mazexiang
 * @version $Id: StringUtil, v 0.1 2020-11-07 8:06 PM mzx Exp $
 */
public class StringUtil {
    public static boolean isEmpty(String str){
        return str == null || str == "";
    }

    public static boolean equals(String str1,String str2){
        if (str1!=null && str2!=null){
            return  str1.equals(str2);
        }
        return false;
    }
}
