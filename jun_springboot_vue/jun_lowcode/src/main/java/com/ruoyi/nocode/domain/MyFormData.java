package com.ruoyi.nocode.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mongodb.DBObject;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document(collection = "form_data")
public class MyFormData {

    String oid;
    String businessId;
    String procKey;
    String formId;
    String formName;
    DBObject data;
    String createName;
    String createBy;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    Date createTime;
    String instanceId;
    String withProc;
    String status;

    public String getOid() {
        return oid;
    }

    public void setOid(String oid) {
        this.oid = oid;
    }

    public String getBusinessId() {
        return businessId;
    }

    public void setBusinessId(String businessId) {
        this.businessId = businessId;
    }

    public String getProcKey() {
        return procKey;
    }

    public void setProcKey(String procKey) {
        this.procKey = procKey;
    }

    public String getFormId() {
        return formId;
    }

    public void setFormId(String formId) {
        this.formId = formId;
    }

    public DBObject getData() {
        return data;
    }

    public void setData(DBObject data) {
        this.data = data;
    }

    public String getCreateName() {
        return createName;
    }

    public void setCreateName(String createName) {
        this.createName = createName;
    }

    public String getCreateBy() {
        return createBy;
    }

    public void setCreateBy(String createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getFormName() {
        return formName;
    }

    public void setFormName(String formName) {
        this.formName = formName;
    }

    public String getWithProc() {
        return withProc;
    }

    public void setWithProc(String withProc) {
        this.withProc = withProc;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
