package com.ruoyi.generator.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.alibaba.druid.DbType;
import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlCreateTableStatement;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.text.Convert;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.sql.SqlUtil;
import com.ruoyi.generator.config.GenConfig;
import com.ruoyi.generator.domain.GenJoin;
import com.ruoyi.generator.domain.GenTable;
import com.ruoyi.generator.domain.vo.GenTableVo;
import com.ruoyi.generator.service.IGenColumnService;
import com.ruoyi.generator.service.IGenJoinService;
import com.ruoyi.generator.service.IGenTableService;

/**
 * 代码生成 操作处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/tool/gen")
public class GenController extends BaseController
{
    @Autowired
    private IGenTableService genTableService;

    @Autowired
    private IGenColumnService genTableColumnService;

    @Autowired
    private IGenJoinService genJoinTableService;

    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping("/list")
    public TableDataInfo genList(GenTable genTable)
    {
        startPage();
        List<GenTable> list = genTableService.selectGenTableList(genTable);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:query')")
    @GetMapping(value = "/{tableId}")
    public AjaxResult getInfo(@PathVariable Long tableId)
    {
        GenTable table = genTableService.selectGenTableById(tableId);
        List<GenTable> tables = genTableService.selectGenTableAll();
        GenTableVo genTableVo = genJoinTableService.selectGenJoinTableVoListByGenTable(table);
        genTableVo.setTable(table);
        genTableVo.setTables(tables);
        return success(genTableVo);
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping("/db/list")
    public TableDataInfo dataList(GenTable genTable)
    {
        startPage();
        List<GenTable> list = genTableService.selectDbTableList(genTable);
        return getDataTable(list);
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:list')")
    @GetMapping(value = "/column/{tableId}")
    public TableDataInfo columnList(Long tableId)
    {
        TableDataInfo dataInfo = new TableDataInfo();
        List<?> list = genTableColumnService.selectGenTableColumnListByTableId(tableId);
        dataInfo.setRows(list);
        dataInfo.setTotal(list.size());
        return dataInfo;
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:import')")
    @Log(title = "代码生成", businessType = BusinessType.IMPORT)
    @PostMapping("/importTable")
    public AjaxResult importTableSave(@RequestParam("tables") String tables, @RequestParam("tplWebType") String tplWebType)
    {
        String[] tableNames = Convert.toStrArray(tables);
        List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames);
        genTableService.importGenTable(tableList, tplWebType, SecurityUtils.getUsername());
        return success();
    }

    @PreAuthorize("@ss.hasRole('admin')")
    @Log(title = "创建表", businessType = BusinessType.OTHER)
    @PostMapping("/createTable")
    public AjaxResult createTableSave(@RequestParam("sql") String sql, @RequestParam("tplWebType") String tplWebType)
    {
        try
        {
            SqlUtil.filterKeyword(sql);
            List<SQLStatement> sqlStatements = SQLUtils.parseStatements(sql, DbType.mysql);
            List<String> tableNames = new ArrayList<>();
            for (SQLStatement sqlStatement : sqlStatements)
            {
                if (sqlStatement instanceof MySqlCreateTableStatement)
                {
                    MySqlCreateTableStatement createTableStatement = (MySqlCreateTableStatement) sqlStatement;
                    if (genTableService.createTable(createTableStatement.toString()))
                    {
                        String tableName = createTableStatement.getTableName().replaceAll("`", "");
                        tableNames.add(tableName);
                    }
                }
            }
            List<GenTable> tableList = genTableService.selectDbTableListByNames(tableNames.toArray(new String[tableNames.size()]));
            String operName = SecurityUtils.getUsername();
            genTableService.importGenTable(tableList, tplWebType, operName);
            return AjaxResult.success();
        }
        catch (Exception e)
        {
            logger.error(e.getMessage(), e);
            return AjaxResult.error("创建表结构异常");
        }
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:edit')")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult editSave(@Validated @RequestBody GenTableVo genTableVo)
    {
        GenTable genTable = genTableVo.getTable();
        genTableService.validateEdit(genTable);
        genTableService.updateGenTable(genTable);
        genJoinTableService.deleteGenJoinTableByTableId(genTable.getTableId());
        for (GenJoin genJoin : genTableVo.getJoinTablesMate())
        {
            genJoinTableService.insertGenJoinTable(genJoin);
        }
        return success();
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:remove')")
    @Log(title = "代码生成", businessType = BusinessType.DELETE)
    @DeleteMapping("/{tableIds}")
    public AjaxResult remove(@PathVariable Long[] tableIds)
    {
        genTableService.deleteGenTableByIds(tableIds);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:preview')")
    @GetMapping("/preview/{tableId}")
    public AjaxResult preview(@PathVariable("tableId") Long tableId) throws IOException
    {
        Map<String, String> dataMap = genTableService.previewCode(tableId);
        return success(dataMap);
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/download/{tableName}")
    public void download(HttpServletResponse response, @PathVariable("tableName") String tableName) throws IOException
    {
        byte[] data = genTableService.downloadCode(tableName);
        genCode(response, data);
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/genCode/{tableName}")
    public AjaxResult genCode(@PathVariable("tableName") String tableName)
    {
        if (!GenConfig.isAllowOverwrite())
        {
            return AjaxResult.error("【系统预设】不允许生成文件覆盖到本地");
        }
        genTableService.generatorCode(tableName);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:edit')")
    @Log(title = "代码生成", businessType = BusinessType.UPDATE)
    @GetMapping("/synchDb/{tableName}")
    public AjaxResult synchDb(@PathVariable("tableName") String tableName)
    {
        genTableService.synchDb(tableName);
        return success();
    }

    @PreAuthorize("@ss.hasPermi('tool:gen:code')")
    @Log(title = "代码生成", businessType = BusinessType.GENCODE)
    @GetMapping("/batchGenCode")
    public void batchGenCode(HttpServletResponse response, String tables) throws IOException
    {
        String[] tableNames = Convert.toStrArray(tables);
        byte[] data = genTableService.downloadCode(tableNames);
        genCode(response, data);
    }

    private void genCode(HttpServletResponse response, byte[] data) throws IOException
    {
        response.reset();
        response.addHeader("Access-Control-Allow-Origin", "*");
        response.addHeader("Access-Control-Expose-Headers", "Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=\"ruoyi.zip\"");
        response.addHeader("Content-Length", "" + data.length);
        response.setContentType("application/octet-stream; charset=UTF-8");
        IOUtils.write(data, response.getOutputStream());
    }
}
