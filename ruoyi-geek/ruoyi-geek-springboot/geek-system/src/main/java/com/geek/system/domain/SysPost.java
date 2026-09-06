package com.geek.system.domain;

import com.geek.common.annotation.Excel;
import com.geek.common.annotation.Excel.ColumnType;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 岗位表 sys_post
 * 
 * @author geek
 */
@Table("sys_post")
@Schema(title = "岗位表")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysPost extends BaseEntity {

    /** 岗位序号 */
    @Schema(title = "岗位序号")
    @Excel(name = "岗位序号", cellType = ColumnType.NUMERIC)
    @Id
    private Long postId;

    /** 岗位编码 */
    @Schema(title = "岗位编码")
    @Excel(name = "岗位编码")
    @NotBlank(message = "岗位编码不能为空")
    @Size(min = 0, max = 64, message = "岗位编码长度不能超过64个字符")
    private String postCode;

    /** 岗位名称 */
    @Schema(title = "岗位名称")
    @Excel(name = "岗位名称")
    @NotBlank(message = "岗位名称不能为空")
    @Size(min = 0, max = 50, message = "岗位名称长度不能超过50个字符")
    private String postName;

    /** 岗位排序 */
    @Schema(title = "岗位排序")
    @Excel(name = "岗位排序")
    @NotNull(message = "显示顺序不能为空")
    private Integer postSort;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 用户是否存在此岗位标识 默认不存在 */
    @Schema(title = "用户是否存在此岗位标识")
    @Column(ignore = true)
    private boolean flag = false;

}
