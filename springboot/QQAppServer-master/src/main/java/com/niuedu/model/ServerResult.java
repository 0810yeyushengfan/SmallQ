package com.niuedu.model;

public class ServerResult<T> {
    //等于0时表示无错误，其余值表示有错误，错误时，errMsg有值，否则无值
    private int retCode;
    //出错时的信息
    private String errMsg;
    //真正返回的数据，其类型由参数T决定
    private T data;

    public ServerResult(int retCode) {
        this.retCode = retCode;
    }

    public ServerResult(int retCode, String errMsg) {
        this.retCode = retCode;
        this.errMsg = errMsg;
    }

    public ServerResult(int retCode, String errMsg, T data) {
        this.retCode = retCode;
        this.errMsg = errMsg;
        this.data = data;
    }

    public int getRetCode() {
        return retCode;
    }

    public void setRetCode(int retCode) {
        this.retCode = retCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    public void setErrMsg(String errMsg) {
        this.errMsg = errMsg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
