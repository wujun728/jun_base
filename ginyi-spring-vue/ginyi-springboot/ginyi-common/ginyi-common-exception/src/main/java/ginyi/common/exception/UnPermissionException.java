package ginyi.common.exception;

import ginyi.common.result.StateCode;
import lombok.Data;

/**
 * 没有对应的接口权限
 */
@Data
public class UnPermissionException extends RuntimeException{
    private static final long serialVersionUID = 1L;

}
