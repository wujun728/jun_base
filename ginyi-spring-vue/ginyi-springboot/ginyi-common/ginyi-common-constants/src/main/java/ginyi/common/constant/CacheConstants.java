package ginyi.common.constant;

/**
 * 缓存的key 常量
 *
 * @author ruoyi
 */
public class CacheConstants {
    /**
     * 参数管理 cache key
     */
    public static final String SYS_CONFIG_KEY = "sys_config:";

    /**
     * 验证码 redis key
     */
    public static final String CAPTCHA_CODE_KEY = "captcha_codes:";

    /**
     * 登录用户 redis key
     */
    public static final String LOGIN_TOKEN_KEY = "login_tokens:";

    /**
     * 登录账户密码错误次数 redis key
     */
    public static final String PWD_ERR_CNT_KEY = "pwd_err_cnt:";

    /**
     * 防重提交 redis key
     */
    public static final String REPEAT_SUBMIT_KEY = "repeat_submit:";

    /** 防止恶意请求 */
    public static final String API_REQUEST = "api_request:";



    /** 菜单前缀，用于批量删除 */
    public static final String MENU_KEY_PREFIX = "menu_*";
    /** 菜单列表 */
    public static final String MENU_LIST_KEY = "menu_list";
    /** 用户菜单的菜单列表 */
    public static final String MENU_USER_LIST_KEY = "menu_user_list:";
    /** 菜单不存在的key */
    public static final String MENU_NOT_EXIST_KEY = "menu_not_exist:";
    /** 菜单详情 */
    public static final String MENU_DETAILS_BY_ID_KEY = "menu_details_by_id:";


    /** 用户前缀，用于批量删除 */
    public static final String USER_KEY_PREFIX = "user_*";
    /** 用户列表的key */
    public static final String USER_LIST_KEY = "user_list";
    /** 用户不存在的key */
    public static final String USER_NOT_EXIST_KEY = "user_not_exist:";
    /** 用户详情 */
    public static final  String USER_DETAILS_BY_USERID_KEY = "user_details_by_userId:";


    /** 部门前缀，用于批量删除 */
    public static final String DEPT_KEY_PREFIX = "dept_*";
    /** 部门列表 */
    public static final String DEPT_LIST_KEY = "dept_list";
    /** 部门不存在的key */
    public static final String DEPT_NOT_EXIST_KEY = "dept_not_exist:";
    /** 部门详情 */
    public static final String DEPT_DETAILS_BY_DEPTID_KEY = "dept_details_by_deptId:";
    /** 同分支下部门名称已被使用 */
    public static final String DEPT_NAME_USED_KEY = "dept_name_used_key:";


    /** 岗位前缀，用于批量删除 */
    public static final String POST_KEY_PREFIX = "post_*";
    /** 岗位列表 */
    public static final String POST_LIST_KEY = "post_list";
    /** 岗位不存在大的key */
    public static final String POST_NOT_EXIST_KEY = "post_not_exist:";
    /** 岗位详情 */
    public static final String POST_DETAILS_BY_POSTID_KEY = "post_details_by_postId:";


    /** 角色前缀，用于批量删除 */
    public static final String ROLE_KEY_PREFIX = "role_*";
    /** 角色列表 */
    public static final String ROLE_LIST_KEY = "role_list";
    /** 角色不存在的key */
    public static final String ROLE_NOT_EXIST_KEY = "role_not_exist:";
    /** 角色名已被使用 */
    public static final String ROLE_NAME_USED_KEY = "role_name_used:";
    /** 角色编码已被使用 */
    public static final String ROLE_CODE_USED_KEY = "role_code_used:";
    /** 角色详情 */
    public static final String ROLE_DETAILS_BY_ROLEID_KEY = "role_details_by_roleId:";

    /** 通知公告不存在的key */
    public static final String NOTICE_NOT_EXIST_KEY = "notice_not_exist:";

}
