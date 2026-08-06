package com.ruoyi.generator.mapper;

import java.util.List;

import com.ruoyi.generator.domain.GenJoin;

/**
 * 代码生成关联字段Mapper接口
 * 
 * @author ruoyi
 * @date 2025-02-19
 */
public interface GenJoinMapper {
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

    public int deleteGenJoinTableByTableId(Long tableId);

}
