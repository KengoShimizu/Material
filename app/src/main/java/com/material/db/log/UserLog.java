package com.material.db.log;

import java.io.Serializable;

/**
 * 用户日志数据库model
 *
 * Created by mojingtian on 15/5/19.
 */
public class UserLog implements Serializable {

    private static final long serialVersionUID = 366340676401409460L;
    private int id;
    private String userId;
    private String packageName;
    private String version;
    private String action;
    private String amount;
    private Integer isSuc;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public Integer getIsSuc() {
        return isSuc;
    }

    public void setIsSuc(Integer isSuc) {
        this.isSuc = isSuc;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "UserLog{" +
                "id=" + id +
                ", userId='" + userId + '\'' +
                ", packageName='" + packageName + '\'' +
                ", version='" + version + '\'' +
                ", action='" + action + '\'' +
                ", amount='" + amount + '\'' +
                ", isSuc=" + isSuc +
                ", date='" + date + '\'' +
                '}';
    }
}
