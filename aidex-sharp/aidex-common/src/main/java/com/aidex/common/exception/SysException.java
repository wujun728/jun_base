package com.aidex.common.exception;

/**
 * System Exception is unexpected Exception, retry might work again
 *
 * @author Danny.Lee 2018/1/27
 */
public class SysException extends BaseException {

    private static final long serialVersionUID = 4355163994767354840L;

    private static final String DEFAULT_ERR_CODE = "SYS_ERROR";

    public SysException(String errMessage) {
        super(DEFAULT_ERR_CODE, errMessage);
    }

    public SysException(int errCode, String errMessage) {
        super(String.valueOf(errCode), errMessage);
    }

}
