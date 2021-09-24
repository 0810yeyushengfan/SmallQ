package com.niuedu.model;

import java.util.Date;

public class Student {
    private int avaterFileId;
    private String name;
    private boolean sex;
    private Date birthday;

    public Student(int avaterFileId, String name, boolean sex, Date birthday) {
        this.avaterFileId = avaterFileId;
        this.name = name;
        this.sex = sex;
        this.birthday = birthday;
    }

    public int getAvaterFileId() {
        return avaterFileId;
    }

    public void setAvaterFileId(int avaterFileId) {
        this.avaterFileId = avaterFileId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isSex() {
        return sex;
    }

    public void setSex(boolean sex) {
        this.sex = sex;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
