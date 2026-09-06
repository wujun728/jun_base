package ginyi.common.result;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@Data
@ApiModel(value = "结果类")
public class CommonResult<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "状态码")
    private Integer code;

    @ApiModelProperty(value = "状态信息")
    private String msg;

    @ApiModelProperty(value = "返回数据")
    private T data;

    /**
     * 返回格式
     * @param state
     * @param data
     * @param <T>
     * @return
     */
    private static <T> CommonResult<T> res(StateCode state, T data){
        CommonResult<T> commonResult = new CommonResult<>();
        commonResult.setCode(state.getCode());
        commonResult.setMsg(state.getMessage());
        commonResult.setData(data);
        return commonResult;
    }

    /**
     * 成功的返回，不需要 data
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> success(){
        return res(StateCode.SUCCESS, null);
    }

    /**
     * 成功的返回，需要 data
     * @param data
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> success(T data){
        return res(StateCode.SUCCESS, data);
    }

    /**
     * 失败的返回，不需要 data
     * @param state
     * @return
     */
    public static <T> CommonResult<T> error(StateCode state){
        return res(state, null);
    }

    /**
     * 失败的返回，需要 data, 即错误信息
     * @param state
     * @param <T>
     * @return
     */
    public static <T> CommonResult<T> error(StateCode state, T data){
        return res(state, data);
    }

}
