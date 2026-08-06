package com.ruoyi.auth.common.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * 第三方认证对象 oauth_user
 * 
 * @author Dftre
 * @date 2024-01-18
 */
@Schema(description = "第三方认证对象")
public class OauthUser extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    @Schema(title = "主键")
    private Long id;

    /** 第三方系统的唯一ID，详细解释请参考：名词解释 */
    @Schema(title = "第三方系统的唯一ID，详细解释请参考：名词解释")
    @Excel(name = "第三方系统的唯一ID，详细解释请参考：名词解释")
    private String uuid;

    /** 用户ID */
    @Schema(title = "用户ID")
    @Excel(name = "用户ID")
    private Long userId;

    /** 第三方用户来源 */
    @Schema(title = "第三方用户来源")
    @Excel(name = "第三方用户来源")
    private String source;

    /** 用户的授权令牌 */
    @Schema(title = "用户的授权令牌")
    @Excel(name = "用户的授权令牌")
    private String accessToken;

    /** 第三方用户的授权令牌的有效期，部分平台可能没有 */
    @Schema(title = "第三方用户的授权令牌的有效期，部分平台可能没有")
    @Excel(name = "第三方用户的授权令牌的有效期，部分平台可能没有")
    private Long expireIn;

    /** 刷新令牌，部分平台可能没有 */
    @Schema(title = "刷新令牌，部分平台可能没有")
    @Excel(name = "刷新令牌，部分平台可能没有")
    private String refreshToken;

    /** 第三方用户的 open id，部分平台可能没有 */
    @Schema(title = "第三方用户的 open id，部分平台可能没有")
    @Excel(name = "第三方用户的 open id，部分平台可能没有")
    private String openId;

    /** 第三方用户的 ID，部分平台可能没有 */
    @Schema(title = "第三方用户的 ID，部分平台可能没有")
    @Excel(name = "第三方用户的 ID，部分平台可能没有")
    private String uid;

    /** 个别平台的授权信息，部分平台可能没有 */
    @Schema(title = "个别平台的授权信息，部分平台可能没有")
    @Excel(name = "个别平台的授权信息，部分平台可能没有")
    private String accessCode;

    /** 第三方用户的 union id，部分平台可能没有 */
    @Schema(title = "第三方用户的 union id，部分平台可能没有")
    @Excel(name = "第三方用户的 union id，部分平台可能没有")
    private String unionId;

    /** 第三方用户授予的权限，部分平台可能没有 */
    @Schema(title = "第三方用户授予的权限，部分平台可能没有")
    @Excel(name = "第三方用户授予的权限，部分平台可能没有")
    private String scope;

    /** 个别平台的授权信息，部分平台可能没有 */
    @Schema(title = "个别平台的授权信息，部分平台可能没有")
    @Excel(name = "个别平台的授权信息，部分平台可能没有")
    private String tokenType;

    /** id token，部分平台可能没有 */
    @Schema(title = "id token，部分平台可能没有")
    @Excel(name = "id token，部分平台可能没有")
    private String idToken;

    /** 小米平台用户的附带属性，部分平台可能没有 */
    @Schema(title = "小米平台用户的附带属性，部分平台可能没有")
    @Excel(name = "小米平台用户的附带属性，部分平台可能没有")
    private String macAlgorithm;

    /** 小米平台用户的附带属性，部分平台可能没有 */
    @Schema(title = "小米平台用户的附带属性，部分平台可能没有")
    @Excel(name = "小米平台用户的附带属性，部分平台可能没有")
    private String macKey;

    /** 用户的授权code，部分平台可能没有 */
    @Schema(title = "用户的授权code，部分平台可能没有")
    @Excel(name = "用户的授权code，部分平台可能没有")
    private String code;

    /** Twitter平台用户的附带属性，部分平台可能没有 */
    @Schema(title = "Twitter平台用户的附带属性，部分平台可能没有")
    @Excel(name = "Twitter平台用户的附带属性，部分平台可能没有")
    private String oauthToken;

    /** Twitter平台用户的附带属性，部分平台可能没有 */
    @Schema(title = "Twitter平台用户的附带属性，部分平台可能没有")
    @Excel(name = "Twitter平台用户的附带属性，部分平台可能没有")
    private String oauthTokenSecret;

    /** 用户名称 */
    @Schema(title = "用户名称")
    @Excel(name = "用户名称")
    private String userName;

    /** 部门名称 */
    @Schema(title = "部门名称")
    @Excel(name = "部门名称")
    private String deptName;

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSource() {
        return source;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setExpireIn(Long expireIn) {
        this.expireIn = expireIn;
    }

    public Long getExpireIn() {
        return expireIn;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setOpenId(String openId) {
        this.openId = openId;
    }

    public String getOpenId() {
        return openId;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setAccessCode(String accessCode) {
        this.accessCode = accessCode;
    }

    public String getAccessCode() {
        return accessCode;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public String getScope() {
        return scope;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

    public String getIdToken() {
        return idToken;
    }

    public void setMacAlgorithm(String macAlgorithm) {
        this.macAlgorithm = macAlgorithm;
    }

    public String getMacAlgorithm() {
        return macAlgorithm;
    }

    public void setMacKey(String macKey) {
        this.macKey = macKey;
    }

    public String getMacKey() {
        return macKey;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setOauthToken(String oauthToken) {
        this.oauthToken = oauthToken;
    }

    public String getOauthToken() {
        return oauthToken;
    }

    public void setOauthTokenSecret(String oauthTokenSecret) {
        this.oauthTokenSecret = oauthTokenSecret;
    }

    public String getOauthTokenSecret() {
        return oauthTokenSecret;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("uuid", getUuid())
                .append("userId", getUserId())
                .append("source", getSource())
                .append("accessToken", getAccessToken())
                .append("expireIn", getExpireIn())
                .append("refreshToken", getRefreshToken())
                .append("openId", getOpenId())
                .append("uid", getUid())
                .append("accessCode", getAccessCode())
                .append("unionId", getUnionId())
                .append("scope", getScope())
                .append("tokenType", getTokenType())
                .append("idToken", getIdToken())
                .append("macAlgorithm", getMacAlgorithm())
                .append("macKey", getMacKey())
                .append("code", getCode())
                .append("oauthToken", getOauthToken())
                .append("oauthTokenSecret", getOauthTokenSecret())
                .toString();
    }
}
