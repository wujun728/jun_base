package ginyi.common.exception;

import ginyi.common.result.StateCode;
import lombok.Data;

/**
 * 用户错误最大次数异常类
 */
@Data
public class UserPasswordRetryLimitExceedException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private StateCode state;
    private Object data;

    public UserPasswordRetryLimitExceedException(StateCode state, Object data) {
        this.state = state;
        this.data = data;
    }

    public UserPasswordRetryLimitExceedException(StateCode state) {
        this.state = state;
        this.data = data;
    }
}
