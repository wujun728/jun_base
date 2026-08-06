package com.ruoyi.generator.domain.vo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.generator.domain.GenColumn;
import com.ruoyi.generator.domain.GenJoin;
import com.ruoyi.generator.domain.GenTable;

import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;

/**
 * 业务表 gen_table
 * 
 * @author ruoyi
 */
@Data
@Setter
@EqualsAndHashCode(callSuper = true)
public class GenTableVo extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 业务表 */
    @Valid
    private GenTable table;

    /** 业务表的列 */
    @Valid
    private List<GenColumn> columns;

    /** 关联信息 */
    @Valid
    private List<GenJoin> joinTablesMate;

    /** 参与关联的表 */
    private Collection<GenTable> joinTables;

    /** 参与关联的列 */
    private List<GenColumn> joinColumns;

    /** 获取所有与本业务相关的表 */
    public List<GenTable> getAllGenTables() {
        List<GenTable> allGenTables = new ArrayList<>();
        allGenTables.add(table);
        allGenTables.addAll(joinTables);
        return allGenTables;
    }

    /** 获取所有与本业务相关的列 */
    public List<GenColumn> getAllGenTableColumns() {
        List<GenColumn> allGenTableColumns = new ArrayList<>();
        if (columns != null) {
            allGenTableColumns.addAll(columns);
        }
        if (joinColumns != null) {
            allGenTableColumns.addAll(joinColumns);
        }
        return allGenTableColumns;
    }

    /** 获取所有与本业务相关表的表ID与表对象映射 */
    public Map<Long, GenTable> getTableMap() {
        Map<Long, GenTable> tableMap = new HashMap<>();
        if (table != null) {
            tableMap.put(table.getTableId(), table);
        }
        if (joinTables != null) {
            for (GenTable genTable : joinTables) {
                if (genTable != null) {
                    tableMap.put(genTable.getTableId(), genTable);
                }
            }
        }
        return tableMap;
    }

    /** 获取所有与本业务相关表的表ID与表别名映射 */
    public Map<Long, String> getTableAliasMap() {
        Map<Long, String> tableMap = new HashMap<>();
        if (table != null) {
            tableMap.put(table.getTableId(), table.getTableAlias());
        }
        if (joinTablesMate != null) {
            for (GenJoin genTable : joinTablesMate) {
                if (genTable != null) {
                    tableMap.put(genTable.getLeftTableId(), genTable.getLeftTableAlias());
                    tableMap.put(genTable.getRightTableId(), genTable.getRightTableAlias());
                }
            }
        }
        return tableMap;
    }

    /** 获取所有与本业务相关列的列ID与列对象映射 */
    public Map<Long, GenColumn> getColumnMap() {
        Map<Long, GenColumn> columnMap = new HashMap<>();
        List<GenTable> genTables = getAllGenTables();
        for (GenTable genTable : genTables) {
            for (GenColumn genTableColumn : genTable.getColumns()) {
                columnMap.put(genTableColumn.getColumnId(), genTableColumn);
            }
        }
        return columnMap;
    }

}