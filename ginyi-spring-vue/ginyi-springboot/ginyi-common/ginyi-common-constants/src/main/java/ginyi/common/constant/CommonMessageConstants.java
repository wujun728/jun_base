package ginyi.common.constant;

/**
 * 错误信息常量
 */
public class CommonMessageConstants {

    public static final String LOGIN_SUCCESS = "登录成功";
    public static final String REGISTER_SUCCESS = "注册成功";
    public static final String REDIS_KEY_NOT_EXIST = "该Key不存在，请仔细检查！";
    public static final String REDIS_VALUE_TYPE_NOT_MATCH = "数据类型不匹配，请联系管理人员！";

    public static final String SYS_ERROR = "请联系管理人员！";
    public static final String SYS_AUTHENTICATION_VALID = "权限不足，无法访问系统资源！";
    public static final String SYS_REQUEST_ILLEGAL = "请求参数错误，请仔细检查！";
    public static final String SYS_CAPTCHA_NOT_EXIST = "验证码不能为空！";
    public static final String SYS_PREVIEW_ENV = "演示环境不允许进行此操作！";
    public static final String SYS_BED_REQUEST = "恶意请求！该请求已被终止！";

    public static final String UPLOAD_FILE_ERROR = "文件上传业务异常";
    public static final String UPLOAD_SIZE_EXCEED = "单文件大小不得大于5MB，总文件大小不得大于50MB";

    public static final String VERIFY_EXPIRE = "验证码已失效！";
    public static final String VERiFY_INCORRECT = "验证码错误！";

    public static final String USER_PASSWORD_NOT_MATCH = "用户名密码不匹配！";
    public static final String USER_EXIST = "用户已存在！";
    public static final String USER_NOT_EXIST = "用户不存在！";
    public static final String USER_IS_DELETED = "用户已被注销！";
    public static final String USER_IS_FORBIDDEN = "用户已被停用！";
    public static final String USER_IS_LOCKED = "用户已被停用！";
    public static final String USER_NAME_USED = "用户名已被占用！";
    public static final String USER_NOT_FOUND = "请求参数错误，请仔细检查！";
    public static final String USER_ID_NOT_FOUND = "请求参数错误，用户ID不能为空！";
    public static final String USER_STATUS_ILLEGAL = "请求参数错误，用户状态不合法！";
    public static final String USER_IDS_ILLEGAL = "通知的用户不能为空！";


    public static final String MENU_COMPONENT_NOT_EXIST = "组件路径不存在！";
    public static final String MENU_PATH_NOT_EXIST = "路由地址不存在！";
    public static final String MENU_NAME_USED = "菜单名称已被使用！";
    public static final String MENU_NOT_EXIST = "菜单不存在！";
    public static final String MENU_PARENT_NOT_EXIST = "上级菜单不存在！";
    public static final String MENU_ID_NOT_FOUND = "请求参数错误，菜单ID不能为空！";
    public static final String MENU_STATUS_ILLEGAL = "请求参数错误，菜单状态不合法！";


    public static final String POST_NOT_EXIST = "岗位不存在！";
    public static final String POST_NAME_USED = "岗位名称已被使用！";
    public static final String POST_CODE_USED = "岗位编码已被使用！";
    public static final String POST_ID_NOT_FOUND = "请求参数错误，岗位ID不能为空！";
    public static final String POST_STATUS_ILLEGAL = "请求参数错误，岗位状态不合法！";


    public static final String ROLE_NOT_EXIST = "角色不存在！";
    public static final String ROLE_NAME_USED = "角色名称已被使用！";
    public static final String ROLE_PERMISSION_CODE_USED = "角色权限字符已被使用！";
    public static final String ROLE_MENU_NOT_EXIST = "菜单权限不存在！";
    public static final String ROLE_ID_NOT_FOUND = "请求参数错误，角色ID不能为空！";
    public static final String ROLE_STATUS_ILLEGAL = "请求参数错误，角色状态不合法！";


    public static final String DEPT_NOT_EXIST = "部门不存在！";
    public static final String DEPT_NAME_USED = "同分支下部门名称已被使用！";
    public static final String DEPT_PARENT_NOT_EXIST = "上级部门不存在！";
    public static final String DEPT_ID_NOT_FOUND = "请求参数错误，部门ID不能为空！";
    public static final String DEPT_STATUS_ILLEGAL = "请求参数错误，部门状态不合法！";


    public static final String NOTICE_NOT_EXIST = "通知公告不存在！";
    public static final String NOTICE_CURRENT_USER_NOT_EXIST = "当前用户不存在该消息！";
    public static final String NOTICE_HAVE_READ = "当前通知公告已确认，无需二次确认！";
    public static final String NOTICE_NOT_ALLOW = "当前通知公告无法被修改！";



}
