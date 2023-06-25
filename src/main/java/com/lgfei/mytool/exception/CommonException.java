package com.lgfei.mytool.exception;

public class CommonException extends RuntimeException{
    private String expMsg;
    public CommonException(Throwable e){
        super(e);
    }

    public CommonException(String expMsg){
        super(expMsg);
        this.expMsg = expMsg;
    }

    public CommonException(String expMsg, Throwable e){
        super(expMsg, e);
        StringBuilder sb = new StringBuilder(expMsg);
        if(null != e){
            sb.append("-->");
            sb.append(e.getMessage());
        }
        this.expMsg = sb.toString();
    }

    public String getExpMsg() {
        return expMsg;
    }

}
