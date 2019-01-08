package com.spider.entity;

import java.io.Serializable;

/**
 * spider_control
 * @author 
 */
public class SpiderControl implements Serializable {
    /**
     * 爬虫类型
     */
    private String spiderType;

    /**
     * 爬虫ip
     */
    private String spiderIp;

    private static final long serialVersionUID = 1L;

    public String getSpiderType() {
        return spiderType;
    }

    public void setSpiderType(String spiderType) {
        this.spiderType = spiderType;
    }

    public String getSpiderIp() {
        return spiderIp;
    }

    public void setSpiderIp(String spiderIp) {
        this.spiderIp = spiderIp;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        SpiderControl other = (SpiderControl) that;
        return (this.getSpiderType() == null ? other.getSpiderType() == null : this.getSpiderType().equals(other.getSpiderType()))
            && (this.getSpiderIp() == null ? other.getSpiderIp() == null : this.getSpiderIp().equals(other.getSpiderIp()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getSpiderType() == null) ? 0 : getSpiderType().hashCode());
        result = prime * result + ((getSpiderIp() == null) ? 0 : getSpiderIp().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", spiderType=").append(spiderType);
        sb.append(", spiderIp=").append(spiderIp);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}