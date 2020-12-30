package com.example.qistone.entity;

import java.io.Serializable;
//定义了一个User对象并实现序列化接口。序列化Serizable一般用于将该类的对象持久化到存储设备，以备系统停机之后恢复。
public class User implements Serializable {
    //内含以下成员变量。
    private int id;
    private String username;
    private String phonenum;
    private String password;
    private String sex;
    private String old;
    private String address;
    private byte[] headImage;
    public User() {
        super();
        // TODO Auto-generated constructor stub
    }

    //自定义构造函数。
    public User(String username, String password) {
        super();
        this.username = username;
        this.password = password;
    }

    //并实现get和set方法。
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPhonenum(){ return phonenum; }
    public void setNum(String phonenum){this.phonenum=phonenum;}
    public String getPassword() { return password; }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getSex(){return sex;}
    public void setSex(String sex){this.sex=sex;}
    public String getOld() { return old; }
    public void setOld(String old) { this.old = old; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public byte[] getHeadImage() { return headImage; }
    public void setHeadImage(byte[] headImage) { this.headImage = headImage; }

//    //该对象重写了Object类的toString()方法。
//    @Override
//    public String toString() {
//        return "User [id=" + id + ", username=" + username + ", password="
//                + password + "]";
//    }

}