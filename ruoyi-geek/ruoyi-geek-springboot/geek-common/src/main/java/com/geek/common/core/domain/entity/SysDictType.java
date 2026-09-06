package com.geek.common.core.domain.entity;

import com.geek.common.annotation.Excel;
import com.geek.common.annotation.Excel.ColumnType;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典类型表 sys_dict_type
 * 
 * @author geek
 */
@Table("sys_dict_type")
@Schema(title = "字典类型")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictType extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 字典主键 */
    @Id
    @Schema(title = "字典主键")
    @Excel(name = "字典主键", cellType = ColumnType.NUMERIC)
    private Long dictId;

    /** 字典名称 */
    @Schema(title = "字典名称")
    @Excel(name = "字典名称")
    @NotBlank(message = "字典名称不能为空")
    @Size(min = 0, max = 100, message = "字典类型名称长度不能超过100个字符")
    private String dictName;

    /** 字典类型 */
    @Schema(title = "字典类型")
    @Excel(name = "字典类型")
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型类型长度不能超过100个字符")
    @Pattern(regexp = "^[a-z][a-z0-9_]*$", message = "字典类型必须以字母开头，且只能为（小写字母，数字，下滑线）")
    private String dictType;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态", description = "0正常 1停用")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
