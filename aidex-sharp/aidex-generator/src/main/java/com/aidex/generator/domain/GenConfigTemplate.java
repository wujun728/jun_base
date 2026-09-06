package com.aidex.generator.domain;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.aidex.common.annotation.Excel;
import com.aidex.common.core.domain.BaseEntity;

/**
 * 模板配置对象 gen_config_template
 *
 * @author ruoyi
 * @date 2021-03-06
 */
public class GenConfigTemplate extends BaseEntity<GenConfigTemplate>
{
    private static final long serialVersionUID = 1L;

    /** 模板名称 */
    private String templateName;

    /** 作者 */
    private String functionAuthor;

    /** 邮箱 */
    private String functionAuthorEmail;

    /** 工作空间路径 */
    private String workspacePath;

    /** 模块名 */
    private String moduleName;

    /** 包路径 */
    private String packageName;

    /** 前端工作空间路径 */
    private String webWorkspacePath;

    /** 是否默认 */
    private String templateDefault;

    /** 排序 */
    private Integer sort;

    /** 状态（0正常 1 停用） */
    private String status;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getFunctionAuthor() {
        return functionAuthor;
    }

    public void setFunctionAuthor(String functionAuthor) {
        this.functionAuthor = functionAuthor;
    }

    public String getFunctionAuthorEmail() {
        return functionAuthorEmail;
    }

    public void setFunctionAuthorEmail(String functionAuthorEmail) {
        this.functionAuthorEmail = functionAuthorEmail;
    }

    public String getWorkspacePath() {
        return workspacePath;
    }

    public void setWorkspacePath(String workspacePath) {
        this.workspacePath = workspacePath;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getWebWorkspacePath() {
        return webWorkspacePath;
    }

    public void setWebWorkspacePath(String webWorkspacePath) {
        this.webWorkspacePath = webWorkspacePath;
    }

    public String getTemplateDefault() {
        return templateDefault;
    }

    public void setTemplateDefault(String templateDefault) {
        this.templateDefault = templateDefault;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}