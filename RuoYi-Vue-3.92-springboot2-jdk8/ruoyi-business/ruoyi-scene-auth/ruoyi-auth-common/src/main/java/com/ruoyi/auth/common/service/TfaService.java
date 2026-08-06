package com.ruoyi.auth.common.service;

import com.ruoyi.common.core.domain.model.LoginBody;
import com.ruoyi.common.core.domain.model.RegisterBody;

/**
 * 双因素认证（TFA）操作的服务接口。
 * 该接口提供处理TFA绑定、注册和登录流程的方法，
 * 包括它们的验证步骤。
 *
 * <p>
 * 双因素认证通过要求用户提供两种不同的认证因素，
 * 为认证过程增加了额外的安全层。
 * </p>
 */
public interface TfaService {

    /**
     * 处理包含TFA设置的注册流程。
     *
     * @param registerBody 包含用户详情和TFA设置的注册信息
     */
    public void doRegister(RegisterBody registerBody);

    /**
     * 验证包含TFA设置的注册流程。
     *
     * @param registerBody 包含验证数据的注册信息
     */
    public void doRegisterVerify(RegisterBody registerBody);

    /**
     * 启动TFA登录流程的第一步。
     *
     * @param loginBody 包含用户凭证的登录信息
     */
    public void doLogin(LoginBody loginBody, boolean autoRegister);

    /**
     * 验证TFA登录流程的第二步并完成认证。
     *
     * @param loginBody 包含TFA验证码的登录信息
     * @return 已认证会话的字符串令牌或会话标识符
     */
    public String doLoginVerify(LoginBody loginBody, boolean autoRegister);

    /**
     * 启动将TFA方法绑定到用户账户的流程。
     *
     * @param loginBody 包含TFA绑定所需数据的登录信息
     */
    public void doBind(LoginBody loginBody);

    /**
     * 使用验证码或其他确认方式验证TFA绑定流程。
     *
     * @param loginBody 包含验证数据的登录信息
     */
    public void doBindVerify(LoginBody loginBody);

    /**
     * 启动TFA重置流程的第一步。
     *
     * @param registerBody 包含用户凭证的注册信息
     */
    public void doReset(RegisterBody registerBody);

    /**
     * 验证TFA重置流程的第二步并完成重置。
     *
     * @param registerBody 包含TFA验证码的注册信息
     */
    public void doResetVerify(RegisterBody registerBody);
}
