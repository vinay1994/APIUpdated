package com.model;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload implements Serializable{

	private String messageCode;
	private String ayid;
	private String schoolCode;
	private List<String> groupCodeList;
	private List<String> uuidList;
	private boolean smsEnabled;

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getAyid() {
		return ayid;
	}

	public void setAyid(String ayid) {
		this.ayid = ayid;
	}

	public String getSchoolCode() {
		return schoolCode;
	}

	public void setSchoolCode(String schoolCode) {
		this.schoolCode = schoolCode;
	}

	public List<String> getGroupCodeList() {
		return groupCodeList;
	}

	public void setGroupCodeList(List<String> groupCodeList) {
		this.groupCodeList = groupCodeList;
	}

	public List<String> getUuidList() {
		return uuidList;
	}

	public void setUuidList(List<String> uuidList) {
		this.uuidList = uuidList;
	}

	public boolean isSmsEnabled() {
		return smsEnabled;
	}

	public void setSmsEnabled(boolean smsEnabled) {
		this.smsEnabled = smsEnabled;
	}

}
