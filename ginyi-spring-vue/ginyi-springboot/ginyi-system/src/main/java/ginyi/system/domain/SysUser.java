package ginyi.system.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
public class SysUser extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableId
    private Long userId;

    /**
     * 部门ID
     */
    private Long deptId;

    /**
     * 用户账号
     */
    private String userName;

    /**
     * 用户昵称
     */
    private String nickName;

    /**
     * 用户邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String phoneNumber;

    /**
     * 用户性别
     */
    private String sex;

    /**
     * 用户头像
     */
    private String avatar;

    /**
     * 密码
     */
    private String password;

    /**
     * 帐号状态（0正常 1停用）
     */
    private String status;

    /**
     * 删除标志（0代表存在 1代表删除）
     */
    @TableLogic
    private String deleted;

    /**
     * 最后登录IP
     */
    private String loginIp;

    /**
     * 最后登录时间
     */
    private Date loginDate;

    /**
     * 备注
     */
    private String remark;

    /**
     * 部门对象
     */
    @TableField(exist = false, select = false)
    private SysDept dept;

    /**
     * 角色对象
     */
    @TableField(exist = false, select = false)
    private List<SysRole> roles;

    /**
     * 角色组
     */
    @TableField(exist = false, select = false)
    private Long[] roleIds;

    /**
     * 岗位对象
     */
    @TableField(exist = false, select = false)
    private List<SysPost> posts;

    /**
     * 岗位组
     */
    @TableField(exist = false, select = false)
    private Long[] postIds;

    /**
     * 角色ID
     */
    @TableField(exist = false, select = false)
    private Long roleId;


    public boolean isAdmin() {
        return isAdmin(this.userId);
    }

    public static boolean isAdmin(Long userId) {
        return userId != null && 1L == userId;
    }

}
