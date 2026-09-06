package com.aidex.common.core.domain;

import com.aidex.common.constant.Constants;
import com.aidex.common.constant.HttpStatus;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 响应信息主体
 * @author ruoyi
 */
public class R<T>  implements Serializable
{
    private static final long serialVersionUID = 1L;

    /** 成功 */
    public static final int SUCCESS = HttpStatus.SUCCESS;

    /** 失败 */
    public static final int FAIL = HttpStatus.FAIL;

    private int code;

    private String msg;

    private T data;

    public R() {};

    public R(T data, int code, String msg) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public R(int code, String msg) {
        this.code = code;
        this.msg = msg;
        //this.data = data;
    }


    public static <T> R<T> success()
    {
        return new R(null, SUCCESS, null);
    }

    public static <T> R<T> success(String msg)
    {
        return new R(null, SUCCESS, msg);
    }

    public static <T> R<T> data(T data)
    {
        return new R(data, SUCCESS, null);
    }

    public static <T> R<T> data(T data, String msg)
    {
        return new R(data, SUCCESS, msg);
    }



    public static <T> R<T> fail(String msg)
    {
        return new R(FAIL, msg);
    }

    public static <T> R<T> fail(T data)
    {
        return new R(data, FAIL, null);
    }

    public static <T> R<T> fail(T data, String msg)
    {
        return new R(data, FAIL, msg);
    }

    public static <T> R<T> fail(int code, String msg)
    {
        return new R(null, code, msg);
    }

    public static <T> R<T> status(boolean flag) {
        return flag ? success(Constants.DEFAULT_SUCCESS_MESSAGE) : fail(Constants.DEFAULT_FAILURE_MESSAGE);
    }

    public static <T> R<T> status(int rows) {
        return rows > 0 ? success(Constants.DEFAULT_SUCCESS_MESSAGE) : fail(Constants.DEFAULT_FAILURE_MESSAGE);
    }

    public int getCode()
    {
        return code;
    }

    public void setCode(int code)
    {
        this.code = code;
    }

    public String getMsg()
    {
        return msg;
    }

    public void setMsg(String msg)
    {
        this.msg = msg;
    }

    public T getData()
    {
        return data;
    }

    public void setData(T data)
    {
        this.data = data;
    }

    public static <T> Boolean isError(R<T> ret)
    {
        return !isSuccess(ret);
    }

    public static <T> Boolean isSuccess(R<T> ret)
    {
        return R.SUCCESS == ret.getCode();
    }
}
