package com.geek.common.core.domain.entity;

import com.geek.common.annotation.Excel;
import com.geek.common.annotation.Excel.ColumnType;
import com.geek.common.core.domain.BaseEntity;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.Table;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 字典数据表 sys_dict_data
 * 
 * @author geek
 */
@Table("sys_dict_data")
@Schema(title = "字典数据")
@Data
@EqualsAndHashCode(callSuper = true)
public class SysDictData extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 字典编码 */
    @Id
    @Schema(title = "字典编码")
    @Excel(name = "字典编码", cellType = ColumnType.NUMERIC)
    private Long dictCode;

    /** 字典排序 */
    @Schema(title = "字典排序")
    @Excel(name = "字典排序", cellType = ColumnType.NUMERIC)
    private Long dictSort;

    /** 字典标签 */
    @Schema(title = "字典标签")
    @Excel(name = "字典标签")
    @NotBlank(message = "字典标签不能为空")
    @Size(min = 0, max = 100, message = "字典标签长度不能超过100个字符")
    private String dictLabel;

    /** 字典键值 */
    @Schema(title = "字典键值")
    @Excel(name = "字典键值")
    @NotBlank(message = "字典键值不能为空")
    @Size(min = 0, max = 100, message = "字典键值长度不能超过100个字符")
    private String dictValue;

    /** 字典类型 */
    @Schema(title = "字典类型")
    @Excel(name = "字典类型")
    @NotBlank(message = "字典类型不能为空")
    @Size(min = 0, max = 100, message = "字典类型长度不能超过100个字符")
    private String dictType;

    /** 样式属性（其他样式扩展） */
    @Schema(title = "样式属性", description = "其他样式扩展")
    @Size(min = 0, max = 100, message = "样式属性长度不能超过100个字符")
    private String cssClass;

    /** 表格字典样式 */
    @Schema(title = "表格字典样式")
    private String listClass;

    /** 是否默认（Y是 N否） */
    @Schema(title = "是否默认", description = "Y=是,N=否")
    @Excel(name = "是否默认", readConverterExp = "Y=是,N=否")
    private String isDefault;

    /** 状态（0正常 1停用） */
    @Schema(title = "状态", description = "0=正常,1=停用")
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;
}
