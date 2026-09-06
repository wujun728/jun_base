package ginyi.common.exception;

import ginyi.common.result.StateCode;
import lombok.Data;

/**
 * 通用的异常处理
 */
@Data
public class CommonException extends RuntimeException{

    private StateCode state;
    private Object data;

    public CommonException(StateCode state, Object data) {
        this.state = state;
        this.data = data;
    }

    public CommonException(StateCode state) {
        this.state = state;
        this.data = data;
    }

}
