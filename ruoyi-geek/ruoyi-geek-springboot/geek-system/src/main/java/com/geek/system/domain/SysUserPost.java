package com.geek.system.domain;

import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 用户和岗位关联 sys_user_post
 * 
 * @author geek
 */
@Schema(title = "用户和岗位关联")
@Data
@Table("sys_user_post")
public class SysUserPost {
    /** 用户ID */
    @Schema(title = "用户ID")
    private Long userId;

    /** 岗位ID */
    @Schema(title = "岗位ID")
    private Long postId;

}
