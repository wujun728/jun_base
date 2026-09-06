package com.aidex.common.core.domain.entity;

import java.io.Serializable;

public class _BeanLis implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8340184163725609614L;
	private String userNum;
	private String ip;
	private String mac;
	private String hostIp;
	private String hostMac;
	private long lastDay=0;
	private String versionDes;

	private String randomKey;

    public String getRandomKey() {
        return randomKey;
    }

    public void setRandomKey(String randomKey) {
        this.randomKey = randomKey;
    }

    public String getVersionDes() {
		return versionDes;
	}
	public void setVersionDes(String versionDes) {
		this.versionDes = versionDes;
	}
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getHostMac() {
		return hostMac;
	}
	public void setHostMac(String hostMac) {
		this.hostMac = hostMac;
	}
	public long getLastDay() {
		return lastDay;
	}
	public void setLastDay(long lastDay) {
		this.lastDay = lastDay;
	}
	public String getHostIp() {
		return hostIp;
	}
	public void setHostIp(String hostIp) {
		this.hostIp = hostIp;
	}
	private String version;
	private String deadLine;
	
	private String customName;
	public String getCustomName() {
		return customName;
	}
	public void setCustomName(String customName) {
		this.customName = customName;
	}
	public String getUserNum() {
		return userNum;
	}
	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDeadLine() {
		return deadLine;
	}
	public void setDeadLine(String deadLine) {
		this.deadLine = deadLine;
	}
	
	

}
