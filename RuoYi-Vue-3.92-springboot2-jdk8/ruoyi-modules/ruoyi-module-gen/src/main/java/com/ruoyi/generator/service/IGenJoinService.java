package com.ruoyi.generator.service;

import java.util.List;

import com.ruoyi.generator.domain.GenJoin;
import com.ruoyi.generator.domain.GenTable;
import com.ruoyi.generator.domain.vo.GenTableVo;

/**
 * 代码生成关联字段Service接口
 * 
 * @author ruoyi
 * @date 2025-02-19
 */
public interface IGenJoinService {
    /**
     * 查询代码生成关联字段列表
     * 
     * @param genJoinTable 代码生成关联字段
     * @return 代码生成关联字段集合
     */
    public List<GenJoin> selectGenJoinTableList(GenJoin genJoinTable);

    /**
     * 新增代码生成关联字段
     * 
     * @param genJoinTable 代码生成关联字段
     * @return 结果
     */
    public int insertGenJoinTable(GenJoin genJoinTable);

    /**
     * 修改代码生成关联字段
     * 
     * @param genJoinTable 代码生成关联字段
     * @return 结果
     */
    public int updateGenJoinTable(GenJoin genJoinTable);

    /**
     * 根据tableId删除字段关联
     */
    public int deleteGenJoinTableByTableId(Long tableId);

    public GenTableVo selectGenJoinTableVoListByGenTable(GenTable table);
}
