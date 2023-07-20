package com.lgfei.mytool.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DateUtil {
    public static String getCurrDate2yyyyMMdd(){
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }

    public static String getCurrTime2yyyyMMddHHmmss(){
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
