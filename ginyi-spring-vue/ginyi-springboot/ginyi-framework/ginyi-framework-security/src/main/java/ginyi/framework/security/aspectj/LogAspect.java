package ginyi.framework.security.aspectj;

import cn.hutool.core.date.DateUtil;
import com.alibaba.fastjson2.JSON;
import ginyi.common.annotation.Log;
import ginyi.common.constant.CommonMessageConstants;
import ginyi.common.enums.BusinessStatus;
import ginyi.common.enums.HttpMethod;
import ginyi.common.exception.CommonException;
import ginyi.common.exception.UnPermissionException;
import ginyi.common.exception.UserPasswordNotMatchException;
import ginyi.common.exception.UserPasswordRetryLimitExceedException;
import ginyi.common.result.StateCode;
import ginyi.common.utils.ServletUtils;
import ginyi.common.utils.StringUtils;
import ginyi.common.utils.ip.IpUtils;
import ginyi.framework.security.filter.PropertyPreExcludeFilter;
import ginyi.framework.security.manager.AsyncManager;
import ginyi.framework.security.manager.factory.AsyncFactory;
import ginyi.framework.security.utils.SecurityUtils;
import ginyi.system.domain.LoginUser;
import ginyi.system.domain.SysLogOperation;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.fusesource.jansi.Ansi;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 操作日志记录处理
 *
 * @author ruoyi
 */
@Aspect
@Component
@Slf4j
public class LogAspect {
    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            // 获取当前的用户
            LoginUser loginUser = SecurityUtils.getLoginUser();
            // 数据库日志
            SysLogOperation operationLog = new SysLogOperation();
            operationLog.setStatus(BusinessStatus.SUCCESS.ordinal());
            // 请求的地址
            String ip = IpUtils.getIpAddr(ServletUtils.getRequest());
            operationLog.setOperationIp(ip);
            operationLog.setOperationUrl(StringUtils.substring(ServletUtils.getRequest().getRequestURI(), 0, 255));
            operationLog.setCreateTime(new Date());
            if (loginUser != null) {
                operationLog.setOperationName(loginUser.getUsername());
            }
            // 设置方法名称
            String className = joinPoint.getTarget().getClass().getName();
            String methodName = joinPoint.getSignature().getName();
            operationLog.setMethod(className + "." + methodName + "()");
            // 设置请求方式
            operationLog.setRequestMethod(ServletUtils.getRequest().getMethod());
            // 处理设置注解上的参数
            getControllerMethodDescription(joinPoint, controllerLog, operationLog, jsonResult);
            log.info("[ Log日志记录 ] 操作人员: {}", operationLog.getOperationName());
            log.info("[ Log日志记录 ] 操作主机: {}", operationLog.getOperationIp());
            log.info("[ Log日志记录 ] 请求时间: {}", DateUtil.date(System.currentTimeMillis()));
            log.info("[ Log日志记录 ] 请求模块: {}", operationLog.getTitle());
            log.info("[ Log日志记录 ] 请求地址: {}", operationLog.getOperationUrl());
            log.info("[ Log日志记录 ] 方法类型: {}", operationLog.getRequestMethod());
            log.info("[ Log日志记录 ] 操作类型: {}", operationLog.getBusinessType() == 0 ? "其他" :
                    operationLog.getBusinessType() == 1 ? "新增" : operationLog.getBusinessType() == 2 ? "修改" : "删除");
            log.info("[ Log日志记录 ] 方法名称: {}", operationLog.getMethod());
            log.info("[ Log日志记录 ] 请求参数: {}", operationLog.getOperationParam());

            if (e != null) {
                String msg = handleException(e);
                operationLog.setStatus(BusinessStatus.FAIL.ordinal());
                operationLog.setErrorMsg(msg);
                log.info("[ Log日志记录 ] 请求结果: {}", operationLog.getStatus() == 0 ? colorPrint("成功", Ansi.Color.GREEN) : colorPrint("失败", Ansi.Color.RED));
                log.info("[ Log日志记录 ] 响应结果: {}\n", msg);
            } else {
                log.info("[ Log日志记录 ] 请求结果: {}", operationLog.getStatus() == 0 ? colorPrint("成功", Ansi.Color.GREEN) : colorPrint("失败", Ansi.Color.RED));
                log.info("[ Log日志记录 ] 响应结果: {}\n", operationLog.getJsonResult());
            }
            // 保存数据库
            AsyncManager.me().execute(AsyncFactory.recordOper(operationLog));
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("记录本地日志时发生异常，异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 处理并异常信息
     *
     * @param e
     * @return
     */
    private String handleException(Exception e) {
        HashMap<String, Object> map = new HashMap<>();
        // 公用异常
        if (e instanceof CommonException) {
            CommonException ce = (CommonException) e;
            map.put("code", ce.getState().getCode());
            map.put("msg", ce.getState().getMessage());
            map.put("data", ce.getData());
            return JSON.toJSONString(map);
        }
        // 请求参数不合法
        if (e instanceof HttpMessageNotReadableException) {
            HttpMessageNotReadableException he = (HttpMessageNotReadableException) e;
            map.put("code", StateCode.ERROR_REQUEST_PARAMS.getCode());
            map.put("msg", StateCode.ERROR_REQUEST_PARAMS.getMessage());
            map.put("data", CommonMessageConstants.SYS_REQUEST_ILLEGAL);
            return JSON.toJSONString(map);
        }
        // 用户名密码不匹配
        if (e instanceof UserPasswordNotMatchException) {
            UserPasswordNotMatchException ue = (UserPasswordNotMatchException) e;
            map.put("code", StateCode.ERROR_UNAUTHENTICATION.getCode());
            map.put("msg", StateCode.ERROR_UNAUTHENTICATION.getMessage());
            map.put("data", CommonMessageConstants.USER_PASSWORD_NOT_MATCH);
            return JSON.toJSONString(map);
        }
        // 用户登录失败次数超最大限制异常
        if (e instanceof UserPasswordRetryLimitExceedException) {
            UserPasswordRetryLimitExceedException upe = (UserPasswordRetryLimitExceedException) e;
            map.put("code", upe.getState().getCode());
            map.put("msg", upe.getState().getMessage());
            map.put("data", upe.getData());
            return JSON.toJSONString(map);
        }
        // 上传文件异常
        if (e instanceof MultipartException) {
            MultipartException mte = (MultipartException) e;
            map.put("code", StateCode.ERROR_MULTIPART.getCode());
            map.put("msg", StateCode.ERROR_MULTIPART.getMessage());
            if (mte instanceof MaxUploadSizeExceededException) {
                map.put("data", CommonMessageConstants.UPLOAD_SIZE_EXCEED);
                return JSON.toJSONString(map);
            }
            map.put("data", CommonMessageConstants.UPLOAD_FILE_ERROR);
            return JSON.toJSONString(map);
        }
        // 访问接口无权限
        if (e instanceof UnPermissionException) {
            UnPermissionException upe = (UnPermissionException) e;
            map.put("code", StateCode.ERROR_NOT_PERMISSION.getCode());
            map.put("msg", StateCode.ERROR_NOT_PERMISSION.getMessage());
            map.put("data", CommonMessageConstants.SYS_ERROR);
            return JSON.toJSONString(map);
        }
        // 处理其他所有未知异常
        map.put("code", StateCode.ERROR_SYSTEM.getCode());
        map.put("msg", StateCode.ERROR_SYSTEM.getMessage());
        map.put("data", CommonMessageConstants.SYS_ERROR);
        return JSON.toJSONString(map);
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log          日志
     * @param operationLog 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, SysLogOperation operationLog, Object jsonResult) throws Exception {
        // 设置action动作
        operationLog.setBusinessType(log.businessType().ordinal());
        // 设置标题
        operationLog.setTitle(log.title());
        // 设置操作人类别
        operationLog.setOperatorType(log.operatorType().ordinal());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operationLog);
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtils.isNotNull(jsonResult)) {
            operationLog.setJsonResult(JSON.toJSONString(jsonResult));
        }
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operationLog 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, SysLogOperation operationLog) throws Exception {
        String requestMethod = operationLog.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            operationLog.setOperationParam(params);
        } else {
            Map<?, ?> paramsMap = ServletUtils.getParamMap(ServletUtils.getRequest());
            operationLog.setOperationParam(StringUtils.substring(JSON.toJSONString(paramsMap, excludePropertyPreFilter()), 0, 2000));
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (StringUtils.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter());
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter() {
        return new PropertyPreExcludeFilter().addExcludes(EXCLUDE_PROPERTIES);
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }

    /**
     * 彩色打印字体
     */
    public static String colorPrint(String s, Ansi.Color color) {
        return Ansi.ansi().eraseScreen().fg(color).a(s).reset().toString();
    }
}
