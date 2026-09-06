package com.aidex.generator.service;

import com.aidex.common.constant.Constants;
import com.aidex.common.constant.GenConstants;
import com.aidex.common.core.text.CharsetKit;
import com.aidex.common.core.text.Convert;
import com.aidex.common.exception.CustomException;
import com.aidex.common.utils.SecurityUtils;
import com.aidex.common.utils.StringUtils;
import com.aidex.generator.domain.GenTable;
import com.aidex.generator.domain.GenTableColumn;
import com.aidex.generator.mapper.GenTableColumnMapper;
import com.aidex.generator.mapper.GenTableMapper;
import com.aidex.generator.util.GenUtils;
import com.aidex.generator.util.VelocityInitializer;
import com.aidex.generator.util.VelocityUtils;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * 业务 服务层实现
 *
 * @author ruoyi
 */
@Service
public class GenTableServiceImpl implements IGenTableService {

    private static final Logger log = LoggerFactory.getLogger(GenTableServiceImpl.class);

    @Autowired(required = false)
    private GenTableMapper genTableMapper;

    @Autowired(required = false)
    private GenTableColumnMapper genTableColumnMapper;

    /**
     * 查询业务信息
     *
     * @param id 业务ID
     * @return 业务信息
     */
    @Override
    public GenTable selectGenTableById(Long id) {
        GenTable genTable = genTableMapper.selectGenTableById(id);
        setTableFromOptions(genTable);
        return genTable;
    }

    /**
     * 查询业务列表
     *
     * @param genTable 业务信息
     * @return 业务集合
     */
    @Override
    public List<GenTable> selectGenTableList(GenTable genTable) {
        return genTableMapper.selectGenTableList(genTable);
    }

    /**
     * 查询据库列表
     *
     * @param genTable 业务信息
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> selectDbTableList(GenTable genTable) {
        return genTableMapper.selectDbTableList(genTable);
    }

    /**
     * 查询据库列表
     *
     * @param tableNames 表名称组
     * @return 数据库表集合
     */
    @Override
    public List<GenTable> selectDbTableListByNames(String[] tableNames) {
        return genTableMapper.selectDbTableListByNames(tableNames);
    }

    /**
     * 查询所有表信息
     *
     * @return 表信息集合
     */
    @Override
    public List<GenTable> selectGenTableAll() {
        return genTableMapper.selectGenTableAll();
    }

    /**
     * 修改业务
     *
     * @param genTable 业务信息
     * @return 结果
     */
    @Override
    @Transactional
    public void updateGenTable(GenTable genTable) {
        String options = JSON.toJSONString(genTable.getParams());
        genTable.setOptions(options);

        //如果页面是否有停用启用选择为是，则需要将状态列显示出来
        String disableEnableOption = Convert.toStr(genTable.getParams().get("disableEnableOption"));
        genTable.setUpdateTime(new Date());
        int row = genTableMapper.updateGenTable(genTable);
        if (row > 0) {
            for (GenTableColumn genTableColumn : genTable.getColumns()) {
                if (genTableColumn.isSuperColumn()) {
                    genTableColumn.setIsRequired("0");
                    genTableColumn.setIsInsert("0");
                    genTableColumn.setIsEdit("0");
                    genTableColumn.setIsList("0");
                    genTableColumn.setIsUnique("0");
                    genTableColumn.setIsLog("0");
                    genTableColumn.setQueryType("");
                    genTableColumn.setDictType("");
                    genTableColumn.setColCheck("");
                }
                //唯一性校验检测是否配置正确
                if (genTableColumn.isUnique()) {
                    if (!genTableColumn.getJavaType().equals(GenConstants.TYPE_STRING)) {
                        genTableColumn.setIsUnique("0");
                    }
                }
                //主键设置
                if ("id".equals(genTableColumn.getJavaField()) && !genTableColumn.isPk()) {
                    genTableColumn.setIsPk("1");
                }
                //是否查询设置
                if (genTableColumn.isQuery()) {
                    if ((genTableColumn.getJavaField() + ",").indexOf("id,createBy,createDept,createTime,updateBy,updateTime,updateIp,version,delFlag") != -1) {
                        genTableColumn.setIsQuery("0");
                    }
                }
                //查询条件设置
                if (genTableColumn.getQueryType().equals(GenConstants.QUERY_LIKE)) {
                    if (!genTableColumn.getJavaType().equals(GenConstants.TYPE_STRING)) {
                        genTableColumn.setQueryType(GenConstants.QUERY_EQ);
                    }
                }
                //如果列表不展示，则去掉必填校验，防止entity中生成必填校验
                if (!genTableColumn.isEdit()) {
                    //去掉必填
                    if (genTableColumn.isRequired()) {
                        genTableColumn.setIsRequired("0");
                    }
                    //去掉唯一性校验
                    if (genTableColumn.isUnique()) {
                        genTableColumn.setIsUnique("0");
                    }
                    //去掉新行
                    if (genTableColumn.isNewRow()) {
                        genTableColumn.setIsNewRow("0");
                    }
                    //去掉记录日志
                    if (genTableColumn.isLog()) {
                        genTableColumn.setIsLog("0");
                    }
                    //去掉数据字典
                    if (StringUtils.isNotEmpty(genTableColumn.getDictType())) {
                        genTableColumn.setDictType("");
                    }
                    if (StringUtils.isNotEmpty(genTableColumn.getColCheck())) {
                        genTableColumn.setColCheck("");
                    }
                }

                //生成停用和启用功能时，状态列必须要显示
                if("status".equals(genTableColumn.getJavaField())){
                    if("1".equals(disableEnableOption)){
                        genTableColumn.setIsList("1");
                    }
                }

                //列类型为复选框和下拉多选，不支持设置为查询条件
                if(GenConstants.HTML_CHECKBOX.equals(genTableColumn.getHtmlType()) || GenConstants.HTML_SELECT_MULTIPLE.equals(genTableColumn.getHtmlType())){
                    if("1".equals(genTableColumn.getIsQuery())){
                        genTableColumn.setIsQuery("0");
                    }
                }

                genTableColumnMapper.updateGenTableColumn(genTableColumn);
            }
        }
    }

    /**
     * 删除业务对象
     *
     * @param tableIds 需要删除的数据ID
     * @return 结果
     */
    @Override
    @Transactional
    public void deleteGenTableByIds(Long[] tableIds) {
        genTableMapper.deleteGenTableByIds(tableIds);
        genTableColumnMapper.deleteGenTableColumnByIds(tableIds);
    }

    /**
     * 导入表结构
     *
     * @param tableList 导入表列表
     */
    @Override
    @Transactional(readOnly = false)
    public void importGenTable(List<GenTable> tableList) {
        String operName = SecurityUtils.getUsername();
        Map<String, String> commonColumnMap = new LinkedHashMap<String, String>();
        for (String column : GenConstants.COLUMNNAME_COMMON_COLUMN) {
            commonColumnMap.put(column, column);
        }
        try {
            for (GenTable table : tableList) {
                String tableName = table.getTableName();
                GenUtils.initTable(table, operName);
                //默认为生成到磁盘
                table.setGenType("1");

                JSONObject option = new JSONObject();
                //默认菜单ID
                option.put("GenConstants.PARENT_MENU_ID",GenConstants.DEFAULT_PARENT_MENU_ID);
                //默认不上传附件
                option.put("attachOption","0");
                //默认不生成停用和启用功能
                option.put("disableEnableOption","0");
                //默认配置树结构信息
                option.put("treeCode","id");
                option.put("treeParentCode","parentId");
                table.setOptions(option.toJSONString());
                table.setColSpans("2");
                table.preInsert();

                int row = genTableMapper.insertGenTable(table);
                if (row > 0) {
                    // 保存列信息
                    List<GenTableColumn> genTableColumnsSorted = new ArrayList<>();
                    List<GenTableColumn> genTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);

                    List<String> requireColumnNamelist = Arrays.asList(GenConstants.COLUMNNAME_REQUIRE_COLUMN);
                    List<String> genTableColumnNameList = genTableColumns.stream().map(n->n.getColumnName()).collect(Collectors.toList());

                    //必填字段名的不在表字段名里面的
                    List<String> exceptColumnNameList = requireColumnNamelist.stream().filter(o->!genTableColumnNameList.contains(o)).collect(Collectors.toList());

                    if(!CollectionUtils.isEmpty(exceptColumnNameList)){
                        throw new CustomException("表中必须包含以下必须字段：" + requireColumnNamelist.toString());
                    }

                    Map<String, GenTableColumn> tableColumnMap = new HashMap<String, GenTableColumn>(16);
                    for (GenTableColumn column : genTableColumns) {
                        if (StringUtils.isEmpty(commonColumnMap.get(column.getColumnName()))) {
                            column.setSort(genTableColumnsSorted.size() * 10);
                            genTableColumnsSorted.add(column);
                        }
                        tableColumnMap.put(column.getColumnName(), column);
                    }

                    //遍历顺序的字段
                    for (Map.Entry<String, String> entry : commonColumnMap.entrySet()) {
                        if (null != tableColumnMap.get(entry.getKey())) {
                            tableColumnMap.get(entry.getKey()).setSort(genTableColumnsSorted.size() * 10);
                            genTableColumnsSorted.add(tableColumnMap.get(entry.getKey()));
                        }
                    }
                    int count = 0;
                    for (GenTableColumn column : genTableColumnsSorted) {
                        GenUtils.initColumnField(column, table);
                        if (column.isSuperColumn()) {
                            column.setIsRequired("0");
                            column.setIsInsert("0");
                            column.setIsEdit("0");
                            column.setIsList("0");
                            column.setIsUnique("0");
                            column.setIsLog("0");
                            column.setIsQuery("0");
                            column.setQueryType("");
                            column.setDictType("");
                            column.setColCheck("");
                            column.setColSpan(0);
                            column.setAlignType("");
                            column.setHtmlType("");
                        } else {
                            column.setColSpan(2);
                            count++;
                            //默认设置最多三个查询条件
                            if (count > 4) {
                                column.setIsQuery("");
                                column.setQueryType("");
                            }
                            //默认最多页面展示7列
                            if (count > 7) {
                                column.setIsList("");
                            }
                            //如果当前字段必填，则设置记录日志
                            column.setIsLog("1");
                        }
                        genTableColumnMapper.insertGenTableColumn(column);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new CustomException("导入失败：" + e.getMessage());
        }
    }

    /**
     * 预览代码
     *
     * @param tableId 表编号
     * @return 预览数据列表
     */
    @Override
    public Map<String, String> previewCode(Long tableId) {
        Map<String, String> dataMap = new LinkedHashMap<>();
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableById(tableId);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);
        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            dataMap.put(template, sw.toString());
        }
        return dataMap;
    }

    /**
     * 生成代码（下载方式）
     *
     * @param tableName 表名称
     * @return 数据
     */
    @Override
    public byte[] downloadCode(String tableName) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        generatorCode(tableName, zip);
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 生成代码（自定义路径）
     *
     * @param tableName 表名称
     */
    @Override
    public String generatorCode(String tableName) {
        StringBuilder result = new StringBuilder();
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            System.out.println(template);
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try {
                String path = VelocityUtils.getFileName(template, table);
                FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
                log.error("生成成功：" + path);
                result.append("生成成功：" + path.replaceAll("//", "\\") + "\n");
            } catch (IOException e) {
                e.printStackTrace();
                throw new CustomException("渲染模板失败，表名：" + table.getTableName());
            }
        }

        // 如果是主子表，子表需要再次生成代码
        if(GenConstants.TPL_SUB.equals(table.getTplCategory()) || GenConstants.TPL_SUB_NESTED.equals(table.getTplCategory())){
            GenTable subTable = table.getSubTable();

            //将父表的菜单ID进行记录,在后面使用
            subTable.setGenCodeMenuId(table.getGenCodeMenuId());
            subTable.setBusinessName(table.getBusinessName());
            subTable.setModuleName(table.getModuleName());
            subTable.setPackageName(table.getPackageName());
            subTable.setWorkspacePath(table.getWorkspacePath());
            subTable.setWebWorkspacePath(table.getWebWorkspacePath());
            subTable.setTplCategory(table.getTplCategory());
            subTable.setSubTableFkName(table.getSubTableFkName());
            context = VelocityUtils.prepareContext(subTable);

            for (String template : templates) {
                System.out.println(template);
                // 渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                tpl.merge(context, sw);
                try {
                    String path = VelocityUtils.getFileName(template, subTable);
                    FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
                    log.error("生成成功：" + path);
                    result.append("生成成功：" + path.replaceAll("//", "\\") + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new CustomException("渲染模板失败，表名：" + subTable.getTableName());
                }
            }
        }

        // 如果是树列表，不但要生成treegrid树配置代码，还要生成左树右表的代码
        if(GenConstants.TPL_TREE.equals(table.getTplCategory())){
            GenTable subTable = table.getSubTable();
            subTable.setBusinessName(table.getBusinessName());
            subTable.setModuleName(table.getModuleName());
            subTable.setPackageName(table.getPackageName());
            subTable.setWorkspacePath(table.getWorkspacePath());
            subTable.setWebWorkspacePath(table.getWebWorkspacePath());
            subTable.setTplCategory(table.getTplCategory());
            subTable.setSubTableFkName(table.getSubTableFkName());
            subTable.setOptions(table.getOptions());
            subTable.setParentMenuId(table.getParentMenuId());
            subTable.setParentMenuName(table.getParentMenuName());
            //改变tplCategory,左树右表的VM中service,dto等应该继承BaseService,BaseEntity等
            subTable.setTplCategory(GenConstants.TPL_CRUD);
            subTable.setParentTplCategory(table.getTplCategory());
            String treeName = table.getTreeName();
            context = VelocityUtils.prepareContext(subTable);

            context.put("treeName",treeName);
            context.put("isCrudSub",true);
            context.put("isSub",true);

            //去除treegrid的vm
            templates.remove("vm/tree/vue/tree-index.vue.vm");
            templates.remove("vm/tree/vue/api.js.vm");
            templates.remove("vm/tree/controller.java.vm");
            templates.remove("vm/tree/service.java.vm");
            templates.remove("vm/tree/serviceImpl.java.vm");
            templates.remove("vm/tree/domain.java.vm");
            templates.remove("vm/tree/mapper.java.vm");
            templates.remove("vm/tree/mapper.xml.vm");
            templates.remove("vm/tree/vue/tree.vue.vm");

            //由于要生产左树右表代码，添加crud的vm
            templates.add("vm/tree/vue/sub-index.vue.vm");
            templates.add("vm/crud/vue/api.js.vm");
            templates.add("vm/crud/controller.java.vm");
            templates.add("vm/crud/service.java.vm");
            templates.add("vm/crud/serviceImpl.java.vm");
            templates.add("vm/crud/domain.java.vm");
            templates.add("vm/crud/mapper.java.vm");
            templates.add("vm/crud/mapper.xml.vm");

            String sysMenuComponent = (String) context.get("sysMenu_component");
            String BusinessName = (String) context.get("BusinessName");
            context.put("sysMenu_component",sysMenuComponent.replaceAll("[^/]+$",BusinessName + "Index"));

            for (String template : templates) {
                System.out.println(template);
                // 渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                tpl.merge(context, sw);
                try {
                    String path = VelocityUtils.getFileName(template, subTable);
                    FileUtils.writeStringToFile(new File(path), sw.toString(), CharsetKit.UTF_8);
                    log.error("生成成功：" + path);
                    result.append("生成成功：" + path.replaceAll("//", "\\") + "\n");
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new CustomException("渲染模板失败，表名：" + subTable.getTableName());
                }
            }
        }
        return result.toString();
    }

    /**
     * 同步数据库
     *
     * @param tableName 表名称
     */
    @Override
    @Transactional
    public void synchDb(String tableName) {
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        List<GenTableColumn> tableColumns = table.getColumns();
        List<String> tableColumnNames = tableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

        List<GenTableColumn> dbTableColumns = genTableColumnMapper.selectDbTableColumnsByName(tableName);
        if (StringUtils.isEmpty(dbTableColumns)) {
            throw new CustomException("同步数据失败，原表结构不存在");
        }
        List<String> dbTableColumnNames = dbTableColumns.stream().map(GenTableColumn::getColumnName).collect(Collectors.toList());

        dbTableColumns.forEach(column -> {
            if (!tableColumnNames.contains(column.getColumnName())) {
                GenUtils.initColumnField(column, table);
                //设置列规则
                if (column.isSuperColumn()) {
                    column.setIsRequired("0");
                    column.setIsInsert("0");
                    column.setIsEdit("0");
                    column.setIsList("0");
                    column.setIsUnique("0");
                    column.setIsLog("0");
                    column.setIsQuery("0");
                    column.setQueryType("");
                    column.setDictType("");
                    column.setColCheck("");
                    column.setColSpan(0);
                    column.setAlignType("");
                    column.setHtmlType("");
                } else {
                    column.setColSpan(2);
                }
                genTableColumnMapper.insertGenTableColumn(column);
            }
        });

        List<GenTableColumn> delColumns = tableColumns.stream().filter(column -> !dbTableColumnNames.contains(column.getColumnName())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delColumns)) {
            genTableColumnMapper.deleteGenTableColumns(delColumns);
        }
    }

    /**
     * 同步数据库
     *
     * @param tableName 表名称
     */
    @Override
    @Transactional
    public void addMenu(String tableName) {
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);
        VelocityInitializer.initVelocity();
        VelocityContext context = VelocityUtils.prepareContext(table);
        if (GenConstants.TPL_CRUD.equals(table.getTplCategory())) {
            String template = "vm/crud/sql.vm";
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try {
                String path = VelocityUtils.getFileName(template, table);
                genTableMapper.addMenu(sw.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof DuplicateKeyException){
                    throw new CustomException("菜单编码" + context.get("businessName").toString() + "已存在");
                }else{
                    throw new CustomException(e.getMessage());
                }
            }
        } else if (GenConstants.TPL_CRUD_ROW_EDIT.equals(table.getTplCategory())) {
            String template = "vm/crudrowedit/sql.vm";
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try {
                String path = VelocityUtils.getFileName(template, table);
                genTableMapper.addMenu(sw.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof DuplicateKeyException){
                    throw new CustomException("菜单编码" + context.get("businessName").toString() + "已存在");
                }else{
                    throw new CustomException(e.getMessage());
                }
            }
        } else if (GenConstants.TPL_SUB.equals(table.getTplCategory())) {
            String template = "vm/sub/sql.vm";
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try {
                String path = VelocityUtils.getFileName(template, table);
                genTableMapper.addMenu(sw.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof DuplicateKeyException){
                    throw new CustomException("菜单编码" + context.get("businessName").toString() + "已存在");
                }else{
                    throw new CustomException(e.getMessage());
                }
            }
            GenTable subTable = table.getSubTable();
            subTable.setGenCodeMenuId(table.getGenCodeMenuId());
            subTable.setBusinessName(table.getBusinessName());
            subTable.setModuleName(table.getModuleName());
            subTable.setPackageName(table.getPackageName());
            subTable.setWorkspacePath(table.getWorkspacePath());
            subTable.setWebWorkspacePath(table.getWebWorkspacePath());
            subTable.setTplCategory(table.getTplCategory());
            subTable.setSubTableFkName(table.getSubTableFkName());
            subTable.setOptions(table.getOptions());
            subTable.setParentMenuId(table.getParentMenuId());
            subTable.setParentMenuName(table.getParentMenuName());
            context = VelocityUtils.prepareContext(subTable);
            //在生成子表菜单SQL
            template = "vm/sub/sql.vm";
            // 渲染模板
            StringWriter subWw = new StringWriter();
            Template subTpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, subWw);
            try {
                String path = VelocityUtils.getFileName(template, subTable);
                genTableMapper.addMenu(subWw.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof DuplicateKeyException){
                    throw new CustomException("菜单编码" + context.get("businessName").toString() + "已存在");
                }else{
                    throw new CustomException(e.getMessage());
                }
            }
        }else if(GenConstants.TPL_TREE.equals(table.getTplCategory())){
            String template = "vm/tree/sql.vm";
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try {
                String path = VelocityUtils.getFileName(template, table);
                genTableMapper.addMenu(sw.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof DuplicateKeyException){
                    throw new CustomException("菜单编码" + context.get("businessName").toString() + "已存在");
                }else{
                    throw new CustomException(e.getMessage());
                }
            }

            GenTable subTable = table.getSubTable();
            subTable.setBusinessName(table.getBusinessName());
            subTable.setModuleName(table.getModuleName());
            subTable.setPackageName(table.getPackageName());
            subTable.setWorkspacePath(table.getWorkspacePath());
            subTable.setWebWorkspacePath(table.getWebWorkspacePath());
            subTable.setTplCategory(table.getTplCategory());
            subTable.setSubTableFkName(table.getSubTableFkName());
            subTable.setOptions(table.getOptions());
            subTable.setParentMenuId(table.getParentMenuId());
            subTable.setParentMenuName(table.getParentMenuName());
            //改变tplCategory,左树右表的VM中service,dto等应该继承BaseService,BaseEntity等
            subTable.setTplCategory(GenConstants.TPL_CRUD);

            //处理树管理模块菜单路径
            context = VelocityUtils.prepareContext(subTable);
            String sysMenuComponent = (String) context.get("sysMenu_component");
            String BusinessName = (String) context.get("BusinessName");
            context.put("sysMenu_component",sysMenuComponent.replaceAll("[^/]+$",BusinessName + "Index"));

            sw = new StringWriter();
            tpl.merge(context, sw);
            try {
                String path = VelocityUtils.getFileName(template, table.getSubTable());
                genTableMapper.addMenu(sw.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof DuplicateKeyException){
                    throw new CustomException("菜单编码" + context.get("businessName").toString() + "已存在");
                }else{
                    throw new CustomException(e.getMessage());
                }
            }
        }else if(GenConstants.TPL_TREEGRID.equals(table.getTplCategory())){
            String template = "vm/treegrid/sql.vm";
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try {
                String path = VelocityUtils.getFileName(template, table);
                genTableMapper.addMenu(sw.toString());
            } catch (Exception e) {
                e.printStackTrace();
                if(e instanceof DuplicateKeyException){
                    throw new CustomException("菜单编码" + context.get("businessName").toString() + "已存在");
                }else{
                    throw new CustomException(e.getMessage());
                }
            }
        }

    }

    /**
     * 批量生成代码（下载方式）
     *
     * @param tableNames 表数组
     * @return 数据
     */
    @Override
    public byte[] downloadCode(String[] tableNames) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ZipOutputStream zip = new ZipOutputStream(outputStream);
        for (String tableName : tableNames) {
            generatorCode(tableName, zip);
        }
        IOUtils.closeQuietly(zip);
        return outputStream.toByteArray();
    }

    /**
     * 查询表信息并生成代码
     */
    private void generatorCode(String tableName, ZipOutputStream zip) {
        // 查询表信息
        GenTable table = genTableMapper.selectGenTableByName(tableName);
        // 设置主子表信息
        setSubTable(table);
        // 设置主键列信息
        setPkColumn(table);

        VelocityInitializer.initVelocity();

        VelocityContext context = VelocityUtils.prepareContext(table);

        // 获取模板列表
        List<String> templates = VelocityUtils.getTemplateList(table.getTplCategory());
        for (String template : templates) {
            // 渲染模板
            StringWriter sw = new StringWriter();
            Template tpl = Velocity.getTemplate(template, Constants.UTF8);
            tpl.merge(context, sw);
            try {
                // 添加到zip
                zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                IOUtils.write(sw.toString(), zip, Constants.UTF8);
                IOUtils.closeQuietly(sw);
                zip.flush();
                zip.closeEntry();
            } catch (IOException e) {
                log.error("渲染模板失败，表名：" + table.getTableName(), e);
            }
        }

        if(GenConstants.TPL_SUB.equals(table.getTplCategory())){
            GenTable subTable = table.getSubTable();
            subTable.setBusinessName(table.getBusinessName());
            subTable.setModuleName(table.getModuleName());
            subTable.setPackageName(table.getPackageName());
            subTable.setWorkspacePath(table.getWorkspacePath());
            subTable.setWebWorkspacePath(table.getWebWorkspacePath());
            subTable.setTplCategory(table.getTplCategory());
            subTable.setSubTableFkName(table.getSubTableFkName());
            context = VelocityUtils.prepareContext(subTable);

            for (String template : templates) {
                // 渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                tpl.merge(context, sw);
                try {
                    // 添加到zip
                    zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                    IOUtils.write(sw.toString(), zip, Constants.UTF8);
                    IOUtils.closeQuietly(sw);
                    zip.flush();
                    zip.closeEntry();
                } catch (IOException e) {
                    log.error("渲染模板失败，表名：" + table.getTableName(), e);
                }
            }
        }

        // 如果是树列表，不但要生成treegrid树配置代码，还要生成左树右表的代码
        if(GenConstants.TPL_TREE.equals(table.getTplCategory())){
            GenTable subTable = table.getSubTable();
            subTable.setBusinessName(table.getBusinessName());
            subTable.setModuleName(table.getModuleName());
            subTable.setPackageName(table.getPackageName());
            subTable.setWorkspacePath(table.getWorkspacePath());
            subTable.setWebWorkspacePath(table.getWebWorkspacePath());
            subTable.setSubTableFkName(table.getSubTableFkName());
            //改变tplCategory,左树右表的VM中service,dto等应该继承BaseService,BaseEntity等
            subTable.setTplCategory(GenConstants.TPL_CRUD);

            String treeName = table.getTreeName();
            context = VelocityUtils.prepareContext(subTable);

            context.put("treeName",treeName);
            context.put("isCrudSub",true);
            context.put("isSub",true);

            //去除treegrid的vm
            templates.remove("vm/tree/vue/tree-index.vue.vm");
            templates.remove("vm/tree/vue/api.js.vm");
            templates.remove("vm/tree/controller.java.vm");
            templates.remove("vm/tree/service.java.vm");
            templates.remove("vm/tree/serviceImpl.java.vm");
            templates.remove("vm/tree/domain.java.vm");
            templates.remove("vm/tree/mapper.java.vm");
            templates.remove("vm/tree/mapper.xml.vm");
            templates.remove("vm/tree/vue/tree.vue.vm");

            //由于要生产左树右表代码，添加crud的vm
            templates.add("vm/tree/vue/sub-index.vue.vm");
            templates.add("vm/crud/vue/api.js.vm");
            templates.add("vm/crud/controller.java.vm");
            templates.add("vm/crud/service.java.vm");
            templates.add("vm/crud/serviceImpl.java.vm");
            templates.add("vm/crud/domain.java.vm");
            templates.add("vm/crud/mapper.java.vm");
            templates.add("vm/crud/mapper.xml.vm");

            //处理树管理模块菜单路径
            String sysMenuComponent = (String) context.get("sysMenu_component");
            String BusinessName = (String) context.get("BusinessName");
            context.put("sysMenu_component",sysMenuComponent.replaceAll("[^/]+$",BusinessName + "Index"));

            for (String template : templates) {
                // 渲染模板
                StringWriter sw = new StringWriter();
                Template tpl = Velocity.getTemplate(template, Constants.UTF8);
                tpl.merge(context, sw);
                try {
                    // 添加到zip
                    zip.putNextEntry(new ZipEntry(VelocityUtils.getFileName(template, table)));
                    IOUtils.write(sw.toString(), zip, Constants.UTF8);
                    IOUtils.closeQuietly(sw);
                    zip.flush();
                    zip.closeEntry();
                } catch (IOException e) {
                    log.error("渲染模板失败，表名：" + table.getTableName(), e);
                }
            }
        }
    }

    /**
     * 修改保存参数校验
     *
     * @param genTable 业务信息
     */
    @Override
    public void validateEdit(GenTable genTable) {

        String columnDict = "";
        String columnUnique = "";
        //校验数据字典的选择
        for (GenTableColumn genTableColumn : genTable.getColumns()) {
            if (genTableColumn.isEdit()) {
                if (("select".equals(genTableColumn.getHtmlType()) || "selectMultiple".equals(genTableColumn.getHtmlType()) || "checkbox".equals(genTableColumn.getHtmlType()) || "radio".equals(genTableColumn.getHtmlType()))
                        && StringUtils.isEmpty(genTableColumn.getDictType())
                ) {
                    if (StringUtils.isEmpty(columnDict)) {
                        columnDict = genTableColumn.getColumnComment();
                    } else {
                        columnDict = columnDict + "," + genTableColumn.getColumnComment();
                    }
                }

                if (!genTableColumn.getJavaType().equals(GenConstants.TYPE_STRING) && genTableColumn.isUnique()) {
                    if (StringUtils.isEmpty(columnUnique)) {
                        columnUnique = genTableColumn.getColumnComment();
                    } else {
                        columnUnique = columnUnique + "," + genTableColumn.getColumnComment();
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(columnDict)) {
            throw new CustomException("请设置字段【" + columnDict + "】字典类型");
        }
        if (StringUtils.isNotEmpty(columnUnique)) {
            throw new CustomException("只有Java类型为String的才可以设置唯一性校验，请重新设置【" + columnUnique + "】的唯一性校验");
        }

        if (GenConstants.TPL_TREE.equals(genTable.getTplCategory())) {
            String options = JSON.toJSONString(genTable.getParams());
            JSONObject paramsObj = JSON.parseObject(options);
            if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_CODE))) {
                throw new CustomException("树编码字段不能为空");
            } else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_PARENT_CODE))) {
                throw new CustomException("树父编码字段不能为空");
            } else if (StringUtils.isEmpty(paramsObj.getString(GenConstants.TREE_NAME))) {
                throw new CustomException("树名称字段不能为空");
            }
        } else if(GenConstants.TPL_TREEGRID.equals(genTable.getTplCategory())){
            String options = JSON.toJSONString(genTable.getParams());
            JSONObject paramsObj = JSONObject.parseObject(options);
        } else if (GenConstants.TPL_SUB.equals(genTable.getTplCategory())) {
            if (StringUtils.isEmpty(genTable.getSubTableName())) {
                throw new CustomException("关联子表的表名不能为空");
            } else if (StringUtils.isEmpty(genTable.getSubTableFkName())) {
                throw new CustomException("子表关联的外键名不能为空");
            }
        } else if (GenConstants.TPL_CRUD.equals(genTable.getTplCategory())) {

        }
    }

    /**
     * 设置主键列信息
     *
     * @param table 业务表信息
     */
    public void setPkColumn(GenTable table) {
        for (GenTableColumn column : table.getColumns()) {
            if (column.isPk()) {
                table.setPkColumn(column);
                break;
            }
        }
        if (StringUtils.isNull(table.getPkColumn())) {
            table.setPkColumn(table.getColumns().get(0));
        }
        if (GenConstants.TPL_SUB.equals(table.getTplCategory()) || GenConstants.TPL_TREE.equals(table.getTplCategory())) {
            for (GenTableColumn column : table.getSubTable().getColumns()) {
                if (column.isPk()) {
                    table.getSubTable().setPkColumn(column);
                    break;
                }
            }
            if (StringUtils.isNull(table.getSubTable().getPkColumn())) {
                table.getSubTable().setPkColumn(table.getSubTable().getColumns().get(0));
            }
        }
    }

    /**
     * 设置主子表信息
     *
     * @param table 业务表信息
     */
    public void setSubTable(GenTable table) {
        String subTableName = table.getSubTableName();
        if (StringUtils.isNotEmpty(subTableName)) {
            table.setSubTable(genTableMapper.selectGenTableByName(subTableName));
        }
    }

    /**
     * 设置代码生成其他选项值
     *
     * @param genTable 设置后的生成对象
     */
    public void setTableFromOptions(GenTable genTable) {
        JSONObject paramsObj = JSON.parseObject(genTable.getOptions());
        if (StringUtils.isNotNull(paramsObj)) {
            String treeCode = paramsObj.getString(GenConstants.TREE_CODE);
            String treeParentCode = paramsObj.getString(GenConstants.TREE_PARENT_CODE);
            String treeName = paramsObj.getString(GenConstants.TREE_NAME);
            String parentMenuId = paramsObj.getString(GenConstants.PARENT_MENU_ID);
            String parentMenuName = paramsObj.getString(GenConstants.PARENT_MENU_NAME);
            String menuIcon = paramsObj.getString(GenConstants.MENU_ICON);
            String attachOption = paramsObj.getString(GenConstants.ATTACH_OPTION);
            String disableEnableOption = paramsObj.getString(GenConstants.DISABLE_ENABLE_OPTION);
            String saveAndAddOption = paramsObj.getString(GenConstants.SAVE_AND_ADD_OPTION);
            String copyRecordOption = paramsObj.getString(GenConstants.COPY_RECORD_OPTION);

            genTable.setTreeCode(treeCode);
            genTable.setTreeParentCode(treeParentCode);
            genTable.setTreeName(treeName);
            genTable.setParentMenuId(parentMenuId);
            genTable.setParentMenuName(parentMenuName);
            genTable.setMenuIcon(menuIcon);
            genTable.setAttachOption(attachOption);
            genTable.setDisableEnableOption(disableEnableOption);
            genTable.setSaveAndAddOption(saveAndAddOption);
            genTable.setCopyRecordOption(copyRecordOption);
        }
    }

}