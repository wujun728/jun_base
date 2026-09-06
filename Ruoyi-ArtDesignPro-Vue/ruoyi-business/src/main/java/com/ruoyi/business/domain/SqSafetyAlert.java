package com.ruoyi.business.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ溜井作业监测对象 a_alert
 */
public class SqSafetyAlert extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;
    @Excel(name = "摄像头IP")
    private String ip;
    @Excel(name = "溜井")
    private String name;
    @Excel(name = "溜井名称")
    private String chuteName;
    @Excel(name = "所属单位")
    private String tnameLabel;
    @Excel(name = "所属中段")
    private String company;
    @Excel(name = "检测类型")
    private String status;
    private String createUser;
    private String createDept;
    private String updateUser;
    private String isDeleted;
    @Excel(name = "图片")
    private String img;
    @Excel(name = "报警状态")
    private String state;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "处理时间", dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date handleTime;
    @Excel(name = "处理记录")
    private String handle;
    private String beginTime;
    private String endTime;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIp() { return ip; }
    public void setIp(String ip) { this.ip = ip; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getChuteName() { return chuteName; }
    public void setChuteName(String chuteName) { this.chuteName = chuteName; }
    public String getTnameLabel() { return tnameLabel; }
    public void setTnameLabel(String tnameLabel) { this.tnameLabel = tnameLabel; }
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getCreateUser() { return createUser; }
    public void setCreateUser(String createUser) { this.createUser = createUser; }
    public String getCreateDept() { return createDept; }
    public void setCreateDept(String createDept) { this.createDept = createDept; }
    public String getUpdateUser() { return updateUser; }
    public void setUpdateUser(String updateUser) { this.updateUser = updateUser; }
    public String getIsDeleted() { return isDeleted; }
    public void setIsDeleted(String isDeleted) { this.isDeleted = isDeleted; }
    public String getImg() { return img; }
    public void setImg(String img) { this.img = img; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public Date getHandleTime() { return handleTime; }
    public void setHandleTime(Date handleTime) { this.handleTime = handleTime; }
    public String getHandle() { return handle; }
    public void setHandle(String handle) { this.handle = handle; }
    public String getBeginTime() { return beginTime; }
    public void setBeginTime(String beginTime) { this.beginTime = beginTime; }
    public String getEndTime() { return endTime; }
    public void setEndTime(String endTime) { this.endTime = endTime; }
}
