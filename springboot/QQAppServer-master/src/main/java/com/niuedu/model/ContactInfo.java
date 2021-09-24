package com.niuedu.model;

public class ContactInfo {
    //头像URL
    //头像是一些在服务端存放的静态图片，以数字为文件名，跟据
    //登录用户的登录顺序，分配给用户。所以用户是以一个List保存的。
    private int id=0;
    private String name; //名字
    private String password;
    private String status; //状态

    public ContactInfo(int id,String name, String password, String status) {
        this.id=id;
        this.name = name;
        this.password = password;
        this.status = status;
    }

    public void setId(int id){
        this.id=id;
    }
    public int getId(){return this.id;}

    public String getName() {
        return name;
    }
    public void setName(String name){
        this.name = name;
    }

    public String getPassword() {
        return this.password;
    }
    public void setPassword(String password){
        this.password= password;
    }

    public String getStatus() {
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
}
