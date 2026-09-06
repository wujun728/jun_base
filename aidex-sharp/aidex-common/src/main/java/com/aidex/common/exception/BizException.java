package com.aidex.common.exception;

/**
 * BizException is known Exception, no need retry
 *
 * @author Frank Zhang
 */
public class BizException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    private static final String DEFAULT_ERR_CODE = "BIZ_ERROR";

    private String errCode;

    private String errDesc;

    public BizException(String errCode, String errDesc) {
        super(errDesc);
        this.errCode = errCode;
        this.errDesc = errDesc;
    }

    public BizException(ErrorCodeI errorCode) {
        super(errorCode.getErrDesc());
        this.errCode = errorCode.getErrCode();
        this.errDesc = errorCode.getErrDesc();
    }

    public BizException(ErrorCodeI errorCode,String errDesc) {
        super(errorCode.getErrDesc());
        this.errCode = errorCode.getErrCode();
        this.errDesc = errDesc;
    }

    public BizException(String errDesc) {
        super(errDesc);
        this.errCode = DEFAULT_ERR_CODE;
        this.errDesc = errDesc;
    }

    public String getErrCode() {
        return errCode;
    }

    public void setErrCode(String errCode) {
        this.errCode = errCode;
    }

    public String getErrDesc() {
        return errDesc;
    }

    public void setErrDesc(String errDesc) {
        this.errDesc = errDesc;
    }
}