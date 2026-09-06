package com.ruoyi.business.domain;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 旧系统SQ溜井信息对象 a_camera
 */
public class SqChute extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    @Excel(name = "ID")
    private Long id;

    @Excel(name = "矿种名称")
    private String shitype;

    @Excel(name = "矿种种类")
    private String shitypename;

    @Excel(name = "溜井名称")
    private String lname;

    @Excel(name = "所属中段")
    private String company;

    @Excel(name = "摄像机名称")
    private String carmname;

    @Excel(name = "摄像机IP")
    private String carmip;

    @Excel(name = "用户名")
    private String username;

    private String password;

    private String createUser;

    private Long createDept;

    private String updateUser;

    @Excel(name = "状态")
    private String status;

    private String isDeleted;

    @Excel(name = "视频类型")
    private String type;

    @Excel(name = "直播流通道")
    private String serverUrl;

    @Excel(name = "直播流通道2")
    private String serverUrl2;

    private String carmstar;

    private Integer px;

    private Integer dppx;

    private String srcimg;

    private String clist;

    private String rlist;

    @Excel(name = "当前高度")
    private String nowQuantity;

    @Excel(name = "溜井高度")
    private String allQuantity;

    @Excel(name = "矿石吨数")
    private String ton;

    private String locationx;

    private String locationy;

    private String width;

    private String height;

    @Excel(name = "深度")
    private String depth;

    private Boolean safeAlert;

    @Excel(name = "通道号")
    private String channel;

    @Excel(name = "所属工区")
    private String mine;

    @Excel(name = "所属单位")
    private String tname;

    private String config;

    @Excel(name = "溜井半径")
    private String rad;

    private String tnameLabel;

    private String mineLabel;

    private String shitypeLabel;

    public Long getId()
    {
        return id;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    @NotBlank(message = "矿种名称不能为空")
    public String getShitype()
    {
        return shitype;
    }

    public void setShitype(String shitype)
    {
        this.shitype = shitype;
    }

    @NotBlank(message = "矿种种类不能为空")
    @Size(max = 255, message = "矿种种类不能超过255个字符")
    public String getShitypename()
    {
        return shitypename;
    }

    public void setShitypename(String shitypename)
    {
        this.shitypename = shitypename;
    }

    @NotBlank(message = "溜井名称不能为空")
    @Size(max = 255, message = "溜井名称不能超过255个字符")
    public String getLname()
    {
        return lname;
    }

    public void setLname(String lname)
    {
        this.lname = lname;
    }

    @NotBlank(message = "所属中段不能为空")
    public String getCompany()
    {
        return company;
    }

    public void setCompany(String company)
    {
        this.company = company;
    }

    public String getCarmname()
    {
        return carmname;
    }

    public void setCarmname(String carmname)
    {
        this.carmname = carmname;
    }

    public String getCarmip()
    {
        return carmip;
    }

    public void setCarmip(String carmip)
    {
        this.carmip = carmip;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getCreateUser()
    {
        return createUser;
    }

    public void setCreateUser(String createUser)
    {
        this.createUser = createUser;
    }

    public Long getCreateDept()
    {
        return createDept;
    }

    public void setCreateDept(Long createDept)
    {
        this.createDept = createDept;
    }

    public String getUpdateUser()
    {
        return updateUser;
    }

    public void setUpdateUser(String updateUser)
    {
        this.updateUser = updateUser;
    }

    public String getStatus()
    {
        return status;
    }

    public void setStatus(String status)
    {
        this.status = status;
    }

    public String getIsDeleted()
    {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted)
    {
        this.isDeleted = isDeleted;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getServerUrl()
    {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl)
    {
        this.serverUrl = serverUrl;
    }

    public String getServerUrl2()
    {
        return serverUrl2;
    }

    public void setServerUrl2(String serverUrl2)
    {
        this.serverUrl2 = serverUrl2;
    }

    public String getCarmstar()
    {
        return carmstar;
    }

    public void setCarmstar(String carmstar)
    {
        this.carmstar = carmstar;
    }

    public Integer getPx()
    {
        return px;
    }

    public void setPx(Integer px)
    {
        this.px = px;
    }

    public Integer getDppx()
    {
        return dppx;
    }

    public void setDppx(Integer dppx)
    {
        this.dppx = dppx;
    }

    public String getSrcimg()
    {
        return srcimg;
    }

    public void setSrcimg(String srcimg)
    {
        this.srcimg = srcimg;
    }

    public String getClist()
    {
        return clist;
    }

    public void setClist(String clist)
    {
        this.clist = clist;
    }

    public String getRlist()
    {
        return rlist;
    }

    public void setRlist(String rlist)
    {
        this.rlist = rlist;
    }

    public String getNowQuantity()
    {
        return nowQuantity;
    }

    public void setNowQuantity(String nowQuantity)
    {
        this.nowQuantity = nowQuantity;
    }

    @NotBlank(message = "溜井高度不能为空")
    public String getAllQuantity()
    {
        return allQuantity;
    }

    public void setAllQuantity(String allQuantity)
    {
        this.allQuantity = allQuantity;
    }

    public String getTon()
    {
        return ton;
    }

    public void setTon(String ton)
    {
        this.ton = ton;
    }

    public String getLocationx()
    {
        return locationx;
    }

    public void setLocationx(String locationx)
    {
        this.locationx = locationx;
    }

    public String getLocationy()
    {
        return locationy;
    }

    public void setLocationy(String locationy)
    {
        this.locationy = locationy;
    }

    public String getWidth()
    {
        return width;
    }

    public void setWidth(String width)
    {
        this.width = width;
    }

    public String getHeight()
    {
        return height;
    }

    public void setHeight(String height)
    {
        this.height = height;
    }

    public String getDepth()
    {
        return depth;
    }

    public void setDepth(String depth)
    {
        this.depth = depth;
    }

    public Boolean getSafeAlert()
    {
        return safeAlert;
    }

    public void setSafeAlert(Boolean safeAlert)
    {
        this.safeAlert = safeAlert;
    }

    public String getChannel()
    {
        return channel;
    }

    public void setChannel(String channel)
    {
        this.channel = channel;
    }

    @NotBlank(message = "所属工区不能为空")
    public String getMine()
    {
        return mine;
    }

    public void setMine(String mine)
    {
        this.mine = mine;
    }

    @NotBlank(message = "所属单位不能为空")
    public String getTname()
    {
        return tname;
    }

    public void setTname(String tname)
    {
        this.tname = tname;
    }

    public String getConfig()
    {
        return config;
    }

    public void setConfig(String config)
    {
        this.config = config;
    }

    @NotBlank(message = "溜井半径不能为空")
    public String getRad()
    {
        return rad;
    }

    public void setRad(String rad)
    {
        this.rad = rad;
    }

    public String getTnameLabel()
    {
        return tnameLabel;
    }

    public void setTnameLabel(String tnameLabel)
    {
        this.tnameLabel = tnameLabel;
    }

    public String getMineLabel()
    {
        return mineLabel;
    }

    public void setMineLabel(String mineLabel)
    {
        this.mineLabel = mineLabel;
    }

    public String getShitypeLabel()
    {
        return shitypeLabel;
    }

    public void setShitypeLabel(String shitypeLabel)
    {
        this.shitypeLabel = shitypeLabel;
    }
}
