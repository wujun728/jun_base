package com.geek.common.core.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.mybatisflex.annotation.Column;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * Entity基类
 * 
 * @author geek
 */
@Schema(title = "基类")
@Data
public class BaseEntity implements Serializable {
    private static final long serialVersionUID = 1L;

    /** 创建者 */
    @Schema(title = "创建者")
    private String createBy;

    /** 创建时间 */
    @Schema(title = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新者 */
    @Schema(title = "更新者")
    private String updateBy;

    /** 更新时间 */
    @Schema(title = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    /** 备注 */
    @Schema(title = "备注")
    private String remark;

    /** 请求参数 */
    @Schema(title = "请求参数", example = "{'pageNum': 1, 'pageSize': 10, 'startXXX':'', 'endXXX':''}")
    @Column(ignore = true)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private HashMap<String, Object> params;

    public Map<String, Object> getParams() {
        if (params == null) {
            params = new HashMap<>();
        }
        return params;
    }

}
