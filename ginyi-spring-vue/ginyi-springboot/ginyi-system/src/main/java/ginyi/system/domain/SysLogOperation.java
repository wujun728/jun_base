package ginyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;


@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("操作日志返回数据")
@Document("sys_log_operation")
public class SysLogOperation extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("操作模块")
    private String title;

    @ApiModelProperty("业务类型（0其它 1新增 2修改 3删除）")
    private Integer businessType;

    @ApiModelProperty("业务类型数组")
    private Integer[] businessTypes;

    @ApiModelProperty("请求方法")
    private String method;

    @ApiModelProperty("请求方式")
    private String requestMethod;

    @ApiModelProperty("操作类别（0其它 1后台用户 2手机端用户）")
    private Integer operatorType;

    @ApiModelProperty("操作人员")
    private String operationName;

    @ApiModelProperty("部门名称")
    private String deptName;

    @ApiModelProperty("请求URL")
    private String operationUrl;

    @ApiModelProperty("操作地址")
    private String operationIp;

    @ApiModelProperty("操作地点")
    private String operationLocation;

    @ApiModelProperty("请求参数")
    private String operationParam;

    @ApiModelProperty("返回参数")
    private String jsonResult;

    @ApiModelProperty("操作状态（0正常 1异常）")
    private Integer status;

    @ApiModelProperty("错误消息")
    private String errorMsg;

    @JsonFormat
    @ApiModelProperty("操作时间")
    private Date operationTime;

}
