package com.danny.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * <p>
 * 预警组
 * </p>
 *
 * @author danny
 * @since 2021-11-22
 */
@TableName("sys_warming_group")
public class WarmingGroup implements Serializable {

    private static final long serialVersionUID = 1L;

      /**
     * 预警ID
     */
        private String id;

      /**
     * 预警表名
     */
      private String table;

      /**
     * 状态（0正常 1停用）
     */
      private String status;

      /**
     * 删除标志（0代表存在 2代表删除）
     */
      private String delFlag;

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
      private LocalDateTime createTime;

      /**
     * 更新者
     */
      private String updateBy;

      /**
     * 更新时间
     */
      private LocalDateTime updateTime;

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

    
    public String getId() {
        return id;
    }

      public void setId(String id) {
          this.id = id;
      }
    
    public String getTable() {
        return table;
    }

      public void setTable(String table) {
          this.table = table;
      }
    
    public String getStatus() {
        return status;
    }

      public void setStatus(String status) {
          this.status = status;
      }
    
    public String getDelFlag() {
        return delFlag;
    }

      public void setDelFlag(String delFlag) {
          this.delFlag = delFlag;
      }
    
    public String getCreateBy() {
        return createBy;
    }

      public void setCreateBy(String createBy) {
          this.createBy = createBy;
      }
    
    public String getCreateDept() {
        return createDept;
    }

      public void setCreateDept(String createDept) {
          this.createDept = createDept;
      }
    
    public LocalDateTime getCreateTime() {
        return createTime;
    }

      public void setCreateTime(LocalDateTime createTime) {
          this.createTime = createTime;
      }
    
    public String getUpdateBy() {
        return updateBy;
    }

      public void setUpdateBy(String updateBy) {
          this.updateBy = updateBy;
      }
    
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

      public void setUpdateTime(LocalDateTime updateTime) {
          this.updateTime = updateTime;
      }
    
    public String getUpdateIp() {
        return updateIp;
    }

      public void setUpdateIp(String updateIp) {
          this.updateIp = updateIp;
      }
    
    public String getRemark() {
        return remark;
    }

      public void setRemark(String remark) {
          this.remark = remark;
      }
    
    public Integer getVersion() {
        return version;
    }

      public void setVersion(Integer version) {
          this.version = version;
      }

    @Override
    public String toString() {
        return "WarmingGroup{" +
              "id=" + id +
                  ", table=" + table +
                  ", status=" + status +
                  ", delFlag=" + delFlag +
                  ", createBy=" + createBy +
                  ", createDept=" + createDept +
                  ", createTime=" + createTime +
                  ", updateBy=" + updateBy +
                  ", updateTime=" + updateTime +
                  ", updateIp=" + updateIp +
                  ", remark=" + remark +
                  ", version=" + version +
              "}";
    }
}
