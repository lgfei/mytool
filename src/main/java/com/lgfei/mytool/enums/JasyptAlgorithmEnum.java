package com.lgfei.mytool.enums;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lgfei
 * @date 2025/2/13 14:45
 */
public enum JasyptAlgorithmEnum {
    MD5AndDES("md5+des", "PBEWithMD5AndDES"),
    SHA1AndDESede("sha1+3des", "PBEWithSHA1AndDESede"),
    HMACSHA512AndAES_256("sha512+aes256", "PBEWithHMACSHA512AndAES_256"),
    ;

    private String code;
    private String value;

    JasyptAlgorithmEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    public static JasyptAlgorithmEnum get(String code){
        for(JasyptAlgorithmEnum _enum : values()){
            if(_enum.getCode().equals(code)){
                return _enum;
            }
        }
        return null;
    }

    public static List<String> getAllCode(){
        List<String> list = new ArrayList<String>();
        for(JasyptAlgorithmEnum _enum : values()){
            list.add(_enum.getCode());
        }
        return list;
    }
}
