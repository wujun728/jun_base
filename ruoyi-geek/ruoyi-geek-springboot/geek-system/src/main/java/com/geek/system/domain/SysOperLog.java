package com.geek.system.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.geek.common.annotation.Excel;
import com.geek.common.annotation.Excel.ColumnType;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 操作日志记录表 oper_log
 * 
 * @author geek
 */
@Schema(title = "操作日志记录表")
@Data
@Table("sys_oper_log")
public class SysOperLog {

    /** 日志主键 */
    @Schema(title = "操作序号")
    @Excel(name = "操作序号", cellType = ColumnType.NUMERIC)
    @Id
    private Long operId;

    /** 操作模块 */
    @Schema(title = "操作模块")
    @Excel(name = "操作模块")
    private String title;

    /** 业务类型（0其它 1新增 2修改 3删除） */
    @Schema(title = "业务类型")
    @Excel(name = "业务类型", readConverterExp = "0=其它,1=新增,2=修改,3=删除,4=授权,5=导出,6=导入,7=强退,8=生成代码,9=清空数据")
    private Integer businessType;

    /** 业务类型数组 */
    @Schema(title = "业务类型数组")
    @Column(ignore = true)
    private Integer[] businessTypes;

    /** 请求方法 */
    @Schema(title = "请求方法")
    @Excel(name = "请求方法")
    private String method;

    /** 请求方式 */
    @Schema(title = "请求方式")
    @Excel(name = "请求方式")
    private String requestMethod;

    /** 操作类别（0其它 1后台用户 2手机端用户） */
    @Schema(title = "操作类别")
    @Excel(name = "操作类别", readConverterExp = "0=其它,1=后台用户,2=手机端用户")
    private Integer operatorType;

    /** 操作人员 */
    @Schema(title = "操作人员")
    @Excel(name = "操作人员")
    private String operName;

    /** 部门名称 */
    @Schema(title = "部门名称")
    @Excel(name = "部门名称")
    private String deptName;

    /** 请求url */
    @Schema(title = "请求地址")
    @Excel(name = "请求地址")
    private String operUrl;

    /** 操作地址 */
    @Schema(title = "操作地址")
    @Excel(name = "操作地址")
    private String operIp;

    /** 操作地点 */
    @Schema(title = "操作地点")
    @Excel(name = "操作地点")
    private String operLocation;

    /** 请求参数 */
    @Schema(title = "请求参数")
    @Excel(name = "请求参数")
    private String operParam;

    /** 返回参数 */
    @Schema(title = "返回参数")
    @Excel(name = "返回参数")
    private String jsonResult;

    /** 操作状态（0正常 1异常） */
    @Schema(title = "操作状态")
    @Excel(name = "操作状态", readConverterExp = "0=正常,1=异常")
    private Integer status;

    /** 错误消息 */
    @Schema(title = "错误消息")
    @Excel(name = "错误消息")
    private String errorMsg;

    /** 操作时间 */
    @Schema(title = "操作时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Excel(name = "操作时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date operTime;

    /** 消耗时间 */
    @Schema(title = "消耗时间")
    @Excel(name = "消耗时间", suffix = "毫秒")
    private Long costTime;

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
