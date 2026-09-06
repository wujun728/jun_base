package ginyi.system.service;

public interface IVerifyService {

    /**
     * 图片验证码
     */
    public String captcha();

    /**
     * 校验验证码
     */
    public void checkImgCode(String code);

}
