package com.danny.common.core.domain;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.danny.common.annotation.Excel;
import com.danny.common.core.domain.model.LoginUser;
import com.danny.common.core.page.PageDomain;
import com.danny.common.utils.SecurityUtils;
import com.danny.common.utils.StringUtils;
import com.danny.common.utils.spring.SpringUtils;
import com.danny.common.utils.uuid.IdUtils;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.core.env.Environment;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Entity基类
 *
 * @author ruoyi
 */
@Data
public class BasePlusEntity<T> implements Serializable {
    /**
     * 删除标记（0：正常；1：删除；）
     */
    @TableField(exist = false)
    public static final String DEL_FLAG_NORMAL = "0";
    @TableField(exist = false)
    public static final String DEL_FLAG_DELETE = "1";
    @TableField(exist = false)
    public static final String STATUS_NORMAL = "0";
    @TableField(exist = false)
    public static final String STATUS_DISABLE = "1";
    @TableField(exist = false)
    public static final String STATUS_DELETE = "2";
    @TableField(exist = false)
    public static final String STATUS_FREEZE = "3";
    @TableField(exist = false)
    public static final String STATUS_AUDIT = "4";
    @TableField(exist = false)
    public static final String STATUS_AUDIT_BACK = "5";
    @TableField(exist = false)
    public static final String STATUS_DRAFT = "9";

    //写入日志时记录变更前对象，变更后对象，日志类型（添加，编辑，删除）
    @TableField(exist = false)
    public static final String LOG_OLD_DATA = "LOG_OLD_DATA";
    @TableField(exist = false)
    public static final String LOG_NEW_DATA = "LOG_NEW_DATA";
    @TableField(exist = false)
    public static final String LOG_TYPE = "LOG_TYPE";
    /**
     * 记录导入时错误数据
     */
    @Excel(name = "错误信息",width=100)
    @TableField(exist = false)
    private String importErrInfo;

    @TableField(exist = false)
    private static final Environment EVN;

    static {
        EVN = SpringUtils.getBean(Environment.class);
    }

    public BasePlusEntity() {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public BasePlusEntity(String id) {
        this();
        this.id = id;
    }

    /**
     * 获取数据库名称
     */
    @JsonIgnore
    @JSONField(serialize = false)
    public String getDbName(){
        return EVN.getProperty("mybatis.dbName");
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @JSONField(serialize = false)
    @TableField(exist = false)
    protected boolean isNewRecord = false;

    /**
     * 是否是新记录（默认：false），调用setIsNewRecord()设置新记录，使用自定义ID。
     * 设置为true后强制执行插入语句，ID不会自动生成，需从手动传入。
     * @return
     */
    public boolean getIsNewRecord() {
        return isNewRecord || StringUtils.isBlank(getId());
    }

    /**
     * 当前实体分页对象
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @TableField(exist = false)
    protected PageDomain page;

    /**
     * 主键ID
     */
    @TableId(value = "id",type = IdType.ASSIGN_UUID)
    private String id;

    /**
     * 搜索值
     */
    @TableField(exist = false)
    private String searchValue;

    /**
     * 创建者
     */
    @TableField("create_by")
    private String createBy;

    /**
     * 创建部门
     */
    @TableField("create_dept")
    private String createDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("create_time")
    private Date createTime;

    /**
     * 更新者
     */
    @TableField("update_by")
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    @TableField("update_time")
    private Date updateTime;

    /**
     * 更新IP
     */
    @TableField("update_ip")
    private String updateIp;

    /**
     * 备注
     */
    @TableField("remark")
    private String remark;

    /**
     * 版本
     */
    @TableField("version")
    private Integer version;

    /**
     * 不等于id，用于唯一性校验
     */
    @JsonIgnore
    @JSONField(serialize = false)
    @TableField(exist = false)
    private String notEqualId;

    /**状态[1:正常]**/
    //private String status;
    @TableField("del_flag")
    protected String delFlag;
    /**
     * 数据操作类型，行编辑时使用add为添加数据edit为编辑数据标记，delete为删除数据标记
     */
    @TableField(exist = false)
    protected String handleType;

    /**
     * 是否记录日志
     */
    @JsonIgnore
    @TableField(exist = false)
    protected boolean isRecordLog;

    /**
     * 请求参数
     */
    @TableField(exist = false)
    private Map<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>(16);
        }
        return params;
    }

    public void setParams(Map<String, Object> params) {
        this.params = params;
    }

    /**
     * 更新之前执行方法，需要手动调用
     */
    public void preUpdate() {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (null != loginUser.getUser()) {
            this.updateBy = loginUser.getUser().getId() + "";
            this.updateIp = loginUser.getIpaddr();
        } else {
            this.updateBy = "1";
            this.updateIp = "127.0.0.1";
        }
        this.updateTime = new Date();
    }

    public PageDomain getPage() {
        if (null == page) {
            return new PageDomain();
        }
        return page;
    }

    public void setPage(PageDomain page) {
        this.page = page;
    }

    /**
     * 插入之前执行方法，需要手动调用
     */
    public void preInsert() {
        // 不限制ID为UUID，调用setIsNewRecord()使用自定义ID
        if(StringUtils.isEmpty(this.getId())){
            setId(IdUtils.randomUUID());
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        if (null != loginUser.getUser()) {
            this.createBy = loginUser.getUser().getId() + "";
            this.updateBy = loginUser.getUser().getId() + "";
            this.createDept = loginUser.getUser().getDeptId() + "";
            this.updateIp = loginUser.getIpaddr();
        } else {
            this.updateBy = "1";
            this.createBy = "1";
            this.updateIp = "127.0.0.1";
            this.createDept="100";
        }
        this.delFlag = DEL_FLAG_NORMAL;
        this.version = 1;
        this.createTime = new Date();
        this.updateTime = this.createTime;
    }

    public boolean isRecordLog() {
        return true;
    }


    public void setRecordLog(boolean isRecordLog) {
        this.isRecordLog = isRecordLog;
    }
}
