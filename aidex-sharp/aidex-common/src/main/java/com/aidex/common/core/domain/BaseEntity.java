package com.aidex.common.core.domain;

import com.aidex.common.annotation.Excel;
import com.aidex.common.core.domain.model.LoginUser;
import com.aidex.common.core.page.PageDomain;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.common.utils.spring.SpringUtils;
import com.aidex.common.utils.uuid.IdUtils;
import com.alibaba.fastjson2.annotation.JSONField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class BaseEntity<T> implements Serializable {
    /**
     * 删除标记（0：正常；1：删除；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String STATUS_NORMAL = "0";
    public static final String STATUS_DISABLE = "1";
    public static final String STATUS_DELETE = "2";
    public static final String STATUS_FREEZE = "3";
    public static final String STATUS_AUDIT = "4";
    public static final String STATUS_AUDIT_BACK = "5";
    public static final String STATUS_DRAFT = "9";

    //写入日志时记录变更前对象，变更后对象，日志类型（添加，编辑，删除）
    public static final String LOG_OLD_DATA = "LOG_OLD_DATA";
    public static final String LOG_NEW_DATA = "LOG_NEW_DATA";
    public static final String LOG_TYPE = "LOG_TYPE";

    protected String createByName;
    protected String createDeptName;

    /**
     * 记录导入时错误数据
     */
    @Excel(name = "错误信息",width=100)
    private String importErrInfo;

    private static final Environment EVN;

    static {
        EVN = SpringUtils.getBean(Environment.class);
    }

    public BaseEntity() {
        super();
        this.delFlag = DEL_FLAG_NORMAL;
    }

    public BaseEntity(String id) {
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

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    @JSONField(serialize = false)
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
    protected PageDomain page;

    /**
     * 主键ID
     */
    private String id;

    /**
     * 搜索值
     */
    /** 请求参数 */
    @JsonIgnore
    private String searchValue;

    /**
     * 创建者
     */
    private String createBy;

    /**
     * 创建部门
     */
    private String createDept;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /**
     * 更新者
     */
    private String updateBy;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /**
     * 更新IP
     */
    private String updateIp;

    /**
     * 备注
     */
    private String remark;

    /**
     * 版本
     */
    private Integer version;

    /**
     * 不等于id，用于唯一性校验
     */
    @JsonIgnore
    @JSONField(serialize = false)
    private String notEqualId;

    /**状态[1:正常]**/
    //private String status;

    protected String delFlag;
    /**
     * 数据操作类型，行编辑时使用add为添加数据edit为编辑数据标记，delete为删除数据标记
     */
    protected String handleType;

    /**
     * 是否记录日志
     */
    @JsonIgnore
    protected boolean isRecordLog;

    /**
     * 请求参数
     */
    /** 请求参数 */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
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
