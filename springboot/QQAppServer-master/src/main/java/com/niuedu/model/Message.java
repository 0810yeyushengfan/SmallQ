package com.niuedu.model;


import java.util.Date;

public class Message {
    private String contactName;//发出人的名字
    private long time;//发出消息的时间
    private String content;//消息的内容

    public Message(){

    }

    public Message(String contactName, long time, String content) {
        this.contactName = contactName;
        this.time = time;
        this.content = content;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
