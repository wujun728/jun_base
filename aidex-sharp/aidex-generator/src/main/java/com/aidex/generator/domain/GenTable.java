package com.aidex.generator.domain;

import com.aidex.common.constant.GenConstants;
import com.aidex.common.core.domain.BaseEntity;
import com.aidex.common.utils.StringUtils;
import com.alibaba.fastjson2.annotation.JSONField;
import org.apache.commons.lang3.ArrayUtils;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * 业务表 gen_table
 * 
 * @author ruoyi
 */
public class GenTable extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 编号 */
    private Long tableId;

    /** 表名称 */
    @NotBlank(message = "表名称不能为空")
    private String tableName;

    /** 表描述 */
    @NotBlank(message = "表描述不能为空")
    private String tableComment;

    /** 关联父表的表名 */
    private String subTableName;

    /** 本表关联父表的外键名 */
    private String subTableFkName;

    /** 实体类名称(首字母大写) */
    @NotBlank(message = "实体类名称不能为空")
    private String className;

    /** 使用的模板（crud单表操作 tree树表操作 sub主子表操作） */
    private String tplCategory;

    /** 使用的模板（crud单表操作 tree树表操作 sub主子表操作） */
    private String parentTplCategory;

    /** 生成包路径 */
    @NotBlank(message = "生成包路径不能为空")
    private String packageName;

    /** 生成模块名 */
    @NotBlank(message = "生成模块名不能为空")
    private String moduleName;

    /** 生成业务名 */
    @NotBlank(message = "生成业务名不能为空")
    private String businessName;

    /** 生成功能名 */
    @NotBlank(message = "生成功能名不能为空")
    private String functionName;

    /** 生成作者 */
    @NotBlank(message = "作者不能为空")
    private String functionAuthor;

    /** 邮箱 */
    private String functionAuthorEmail;

    /** 工作空间路径 */
    private String workspacePath;

    /** 前端工作空间路径 */
    private String webWorkspacePath;

    /** 生成代码方式（0zip压缩包 1自定义路径） */
    private String genType;

    /** 列数 */
    private String colSpans;

    /** 主键信息 */
    private GenTableColumn pkColumn;

    /** 子表信息 */
    private GenTable subTable;

    /** 表列信息 */
    @Valid
    private List<GenTableColumn> columns;

    /** 其它生成选项 */
    private String options;

    /** 树编码字段 */
    private String treeCode;

    /** 树父编码字段 */
    private String treeParentCode;

    /** 树名称字段 */
    private String treeName;

    /** 上级菜单ID字段 */
    private String parentMenuId;

    /** 上级菜单名称字段 */
    private String parentMenuName;

    /** 菜单ICON */
    private String menuIcon;

    /** 是否上传附件 */
    private String attachOption;

    /** 是否有停用启用 */
    private String disableEnableOption;

    /** 是否保存并继续添加 */
    private String saveAndAddOption;

    /** 是否有复制功能 */
    private String copyRecordOption;

    /** menuID */
    @JSONField(serialize = false)
    private String genCodeMenuId;

    public Long getTableId()
    {
        return tableId;
    }

    public void setTableId(Long tableId)
    {
        this.tableId = tableId;
    }

    public String getTableName()
    {
        return tableName;
    }

    public void setTableName(String tableName)
    {
        this.tableName = tableName;
    }

    public String getTableComment()
    {
        return tableComment;
    }

    public void setTableComment(String tableComment)
    {
        this.tableComment = tableComment;
    }

    public String getSubTableName()
    {
        return subTableName;
    }

    public void setSubTableName(String subTableName)
    {
        this.subTableName = subTableName;
    }

    public String getSubTableFkName()
    {
        return subTableFkName;
    }

    public void setSubTableFkName(String subTableFkName)
    {
        this.subTableFkName = subTableFkName;
    }

    public String getClassName()
    {
        return className;
    }

    public void setClassName(String className)
    {
        this.className = className;
    }

    public String getTplCategory()
    {
        return tplCategory;
    }

    public void setTplCategory(String tplCategory)
    {
        this.tplCategory = tplCategory;
    }

    public String getPackageName()
    {
        return packageName;
    }

    public void setPackageName(String packageName)
    {
        this.packageName = packageName;
    }

    public String getModuleName()
    {
        return moduleName;
    }

    public void setModuleName(String moduleName)
    {
        this.moduleName = moduleName;
    }

    public String getBusinessName()
    {
        return businessName;
    }

    public void setBusinessName(String businessName)
    {
        this.businessName = businessName;
    }

    public String getFunctionName()
    {
        return functionName;
    }

    public void setFunctionName(String functionName)
    {
        this.functionName = functionName;
    }

    public String getFunctionAuthor()
    {
        return functionAuthor;
    }

    public void setFunctionAuthor(String functionAuthor)
    {
        this.functionAuthor = functionAuthor;
    }

    public String getGenType()
    {
        return genType;
    }

    public void setGenType(String genType)
    {
        this.genType = genType;
    }

    public GenTableColumn getPkColumn()
    {
        return pkColumn;
    }

    public void setPkColumn(GenTableColumn pkColumn)
    {
        this.pkColumn = pkColumn;
    }

    public GenTable getSubTable()
    {
        return subTable;
    }

    public void setSubTable(GenTable subTable)
    {
        this.subTable = subTable;
    }

    public List<GenTableColumn> getColumns()
    {
        return columns;
    }

    public void setColumns(List<GenTableColumn> columns)
    {
        this.columns = columns;
    }

    public String getOptions()
    {
        return options;
    }

    public void setOptions(String options)
    {
        this.options = options;
    }

    public String getTreeCode()
    {
        return treeCode;
    }

    public void setTreeCode(String treeCode)
    {
        this.treeCode = treeCode;
    }

    public String getTreeParentCode()
    {
        return treeParentCode;
    }

    public void setTreeParentCode(String treeParentCode)
    {
        this.treeParentCode = treeParentCode;
    }

    public String getTreeName()
    {
        return treeName;
    }

    public void setTreeName(String treeName)
    {
        this.treeName = treeName;
    }

    public String getParentMenuId()
    {
        return parentMenuId;
    }

    public void setParentMenuId(String parentMenuId)
    {
        this.parentMenuId = parentMenuId;
    }

    public String getParentMenuName()
    {
        return parentMenuName;
    }

    public void setParentMenuName(String parentMenuName)
    {
        this.parentMenuName = parentMenuName;
    }

    public boolean isSub()
    {
        return isSub(this.tplCategory);
    }

    public static boolean isSub(String tplCategory)
    {
        return tplCategory != null && StringUtils.equals(GenConstants.TPL_SUB, tplCategory);
    }

    public boolean isTree()
    {
        return isTree(this.tplCategory);
    }

    public static boolean isTree(String tplCategory)
    {
        return tplCategory != null && (StringUtils.equals(GenConstants.TPL_TREE, tplCategory) || StringUtils.equals(GenConstants.TPL_TREEGRID, tplCategory));
    }

    public boolean isTreegrid()
    {
        return isTreegrid(this.tplCategory);
    }

    public static boolean isTreegrid(String tplCategory)
    {
        return tplCategory != null && StringUtils.equals(GenConstants.TPL_TREEGRID, tplCategory);
    }

    public boolean isCrudRowEdit()
    {
        return isCrudRowEdit(this.tplCategory);
    }

    public static boolean isCrudRowEdit(String tplCategory)
    {
        return tplCategory != null && StringUtils.equals(GenConstants.TPL_CRUD_ROW_EDIT, tplCategory);
    }

    public boolean isCrud()
    {
        return isCrud(this.tplCategory);
    }

    public static boolean isCrud(String tplCategory)
    {
        return tplCategory != null && StringUtils.equals(GenConstants.TPL_CRUD, tplCategory);
    }

    public boolean isSuperColumn(String javaField)
    {
        return isSuperColumn(this.tplCategory, javaField);
    }

    public String getWorkspacePath() {
        return workspacePath;
    }

    public void setWorkspacePath(String workspacePath) {
        this.workspacePath = workspacePath;
    }

    public String getWebWorkspacePath() {
        return webWorkspacePath;
    }

    public void setWebWorkspacePath(String webWorkspacePath) {
        this.webWorkspacePath = webWorkspacePath;
    }

    public static boolean isSuperColumn(String tplCategory, String javaField)
    {
        if (isTree(tplCategory) || isTreegrid(tplCategory))
        {
            return StringUtils.equalsAnyIgnoreCase(javaField,
                    ArrayUtils.addAll(GenConstants.TREE_ENTITY, GenConstants.BASE_ENTITY));
        }
        return StringUtils.equalsAnyIgnoreCase(javaField, GenConstants.BASE_ENTITY);
    }

    public String getFunctionAuthorEmail() {
        return functionAuthorEmail;
    }

    public void setFunctionAuthorEmail(String functionAuthorEmail) {
        this.functionAuthorEmail = functionAuthorEmail;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public String getAttachOption() {
        return attachOption;
    }

    public void setAttachOption(String attachOption) {
        this.attachOption = attachOption;
    }

    public boolean hasAttach()
    {
        return hasAttach(this.attachOption);
    }

    public boolean hasAttach(String attachOption)
    {
        return attachOption != null && StringUtils.equals("1", attachOption);
    }

    public boolean hasDisableEnable()
    {
        return hasDisableEnable(this.disableEnableOption);
    }

    public boolean hasDisableEnable(String disableEnableOption)
    {
        return disableEnableOption != null && StringUtils.equals("1", disableEnableOption);
    }

    public boolean hasSaveAndAddOption()
    {
        return hasSaveAndAddOption(this.saveAndAddOption);
    }

    public boolean hasSaveAndAddOption(String saveAndAddOption)
    {
        return saveAndAddOption != null && StringUtils.equals("1", saveAndAddOption);
    }

    public String getSaveAndAddOption() {
        return saveAndAddOption;
    }

    public void setSaveAndAddOption(String saveAndAddOption) {
        this.saveAndAddOption = saveAndAddOption;
    }

    public boolean hasCopyRecordOption()
    {
        return hasCopyRecordOption(this.copyRecordOption);
    }

    public boolean hasCopyRecordOption(String copyRecordOption)
    {
        return copyRecordOption != null && StringUtils.equals("1", copyRecordOption);
    }

    public String getCopyRecordOption() {
        return copyRecordOption;
    }

    public void setCopyRecordOption(String copyRecordOption) {
        this.copyRecordOption = copyRecordOption;
    }

    public String getDisableEnableOption() {
        return disableEnableOption;
    }

    public void setDisableEnableOption(String disableEnableOption) {
        this.disableEnableOption = disableEnableOption;
    }

    public String getGenCodeMenuId() {
        return genCodeMenuId;
    }

    public void setGenCodeMenuId(String genCodeMenuId) {
        this.genCodeMenuId = genCodeMenuId;
    }

    public String getColSpans() {
        return colSpans;
    }

    public void setColSpans(String colSpans) {
        this.colSpans = colSpans;
    }

    public String getParentTplCategory() {
        return parentTplCategory;
    }

    public void setParentTplCategory(String parentTplCategory) {
        this.parentTplCategory = parentTplCategory;
    }
}