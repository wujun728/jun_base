package ginyi.system.service;

import ginyi.system.domain.LoginUser;

import javax.servlet.http.HttpServletRequest;

public interface ITokenService {

    /**
     * 创建令牌
     *
     * @param loginUser 用户信息
     * @return 令牌
     */
    public String createToken(LoginUser loginUser);

    /**
     * 获取用户身份信息
     *
     * @return 用户信息
     */
    public LoginUser getLoginUser(HttpServletRequest request);

    /**
     * 删除用户身份信息
     */
    public void delLoginUser(String token);

    /**
     * 验证令牌有效期，相差不足20分钟，自动刷新缓存
     *
     * @param loginUser
     * @return 令牌
     */
    public void verifyToken(LoginUser loginUser);
}
