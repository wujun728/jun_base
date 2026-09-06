package ginyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@ApiModel("登录日志返回数据")
@Document("sys_log_login")
public class SysLogLogin extends BaseEntity {
    private static final long serialVersionUID = 1L;

    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("用户账号")
    private String userName;

    @ApiModelProperty("登录状态 0成功 1失败")
    private String status;

    @ApiModelProperty("登录IP地址")
    private String ipaddr;

    @ApiModelProperty("登录地点")
    private String loginLocation;

    @ApiModelProperty("浏览器类型")
    private String browser;

    @ApiModelProperty("操作系统")
    private String os;

    @ApiModelProperty("提示消息")
    private String msg;

    @JsonFormat
    @ApiModelProperty("登录时间")
    private Date loginTime;

}
