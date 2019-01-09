package com.spider.entity;

public class RedisModel<T> {

    private String key;

    private T value;

    private Integer database = 0;

    private int expire = 0;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public Integer getDatabase() {
        return database;
    }

    public void setDatabase(Integer database) {
        this.database = database;
    }

    public int getExpire() {
        return expire;
    }

    public void setExpire(int expire) {
        this.expire = expire;
    }

    @Override
    public String toString() {
        return "RedisModel{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                ", database=" + database +
                ", expire=" + expire +
                '}';
    }
}
