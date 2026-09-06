
package com.aidex.common.core.service;

import com.aidex.common.constant.Constants;
import com.aidex.common.core.domain.BaseTreeEntity;
import com.aidex.common.core.mapper.BaseTreeMapper;
import com.aidex.common.exception.SysException;
import com.aidex.common.utils.reflect.ReflectUtils;
import com.aidex.common.utils.uuid.IdUtils;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Service基类
 * @author aidex
 * @email aidex@qq.com
 * @version 2020-02-27
 */
@Transactional(readOnly = false)
public abstract class BaseTreeServiceImpl<M extends BaseTreeMapper<T>, T extends BaseTreeEntity<T>> extends BaseServiceImpl<M, T> implements BaseTreeService<T> {

    /**
     * 根据父ID查询子节点
     * @param parentId 父ID
     * @return
     */
    @Override
    public List<T> findChildListByParentId(String parentId) {
        Class<T> entityClass = ReflectUtils.getClassGenricType(getClass(), 1);
        T entity = null;
        try {
            entity = entityClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SysException(e.getMessage());
        }
        entity.setParentId(parentId);
        return mapper.findList(entity);
    }

    /**
     * 根据父ID查询子节点的
     * @param parentId 父ID
     * @return
     */
    @Override
    public List<T> findChildListByParentIds(String parentId) {
        Class<T> entityClass = ReflectUtils.getClassGenricType(getClass(), 1);
        T entity = null;
        try {
            entity = entityClass.getConstructor().newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new SysException(e.getMessage());
        }
        entity.setParentIds(parentId);
        return mapper.findList(entity);
    }


    /**
     * 查询最大编号
     *
     * @param domain
     * @return
     */
    @Override
    public Integer findMaxSort(T domain) {
        return null;
    }

    /**
     * 更新下属所有节点的tree_path或者是tree_sorts
     *
     * @param parent
     * @param oldParent
     * @return
     */
    protected List<T> updateChildTreeProperties(T parent, T oldParent) {
        //原来的排序字段和树路径字段
        String oldTreeSorts = oldParent.getTreeSorts();
        String oldParentIds = oldParent.getParentIds();
        //现在的排序字段
        String currentTreeSorts = parent.getTreeSorts();
        String currentParentIds = parent.getParentIds();
        //查找当前节点下的所有子节点
        List<T> list = findChildListByParentIds(oldParentIds);
        for (T dto : list) {
            dto.setParentIds(dto.getParentIds().replace(oldParentIds, currentParentIds));
            dto.setTreeSorts(dto.getTreeSorts().substring(0, oldTreeSorts.length()).replace(oldTreeSorts, currentTreeSorts) + dto.getTreeSorts().substring(oldTreeSorts.length()));
            dto.setTreeLevel(dto.getParentIds().split(Constants.TREE_IDS_SPLIT_CHART).length);
        }
        return list;
    }

    /**
     * 自动计算树形结构其他属性值
     *
     * @param dto 修改后的对象
     * @param old 原对象（更新方法使用）
     * @return
     */
    protected void setTreeProperties(T dto, T old, Constants.OpType opType) {
        //获取父级节点
        T parentDTO = get(dto.getParentId());
        //非初始化根节点情况
        if (parentDTO != null) {
            //判断操作类型
            switch (opType) {
                case insert:
                    //新增
                    dto.setId(IdUtils.randomUUID());
                    dto.setNewRecord(true);
                    //设置当前树路经
                    dto.setParentIds(parentDTO.getParentIds() + Constants.TREE_IDS_SPLIT_CHART + dto.getId());
                    //设置当前树全路径排序
                    int treeSort = dto.getTreeSort();
                    String treeSorts = String.format("%06d", treeSort);
                    dto.setTreeSorts(parentDTO.getTreeSorts() + Constants.TREE_IDS_SPLIT_CHART + treeSorts);
                    //设置当前是否为叶子节点，新增全部为叶子节点
                    dto.setTreeLeaf(Constants.TREE_LEAF_Y);
                    //设置当前节点级别
                    dto.setTreeLevel(parentDTO.getTreeLevel() + 1);
                    //判断当前父节点是否为叶子节点，如果是，则更改为非叶子节点
                    if (Constants.TREE_LEAF_Y.equals(parentDTO.getTreeLeaf())) {
                        parentDTO.setTreeLeaf(Constants.TREE_LEAF_N);
                        this.save(parentDTO);
                    }
                    break;
                case update:
                    //修改
                    //主要针对于排序字段的修改
                    if (isUpdateTreeProperties(dto, old)) {
                        //排序字段被修改，则更改整体排序字段
                        //当前新的本级排序字段
                        String currentTreeSorts = String.format("%06d", dto.getTreeSort());
                        //当前新的总体排序字段
                        String newTreeSorts = parentDTO.getTreeSorts() + Constants.TREE_IDS_SPLIT_CHART + currentTreeSorts;
                        //赋值
                        dto.setTreeSorts(newTreeSorts);
                        //重新生成当前节点树路径
                        String currentParentIds = parentDTO.getParentIds() + Constants.TREE_IDS_SPLIT_CHART + dto.getId();
                        Integer currentTreeLevel = parentDTO.getTreeLevel() + 1;
                        //赋值
                        dto.setParentIds(currentParentIds);
                        dto.setTreeLevel(currentTreeLevel);
                    }
                    break;
                default:
                    break;
            }
        } else {
            //初始化树根节点
            dto.setParentIds(dto.getId());
            //设置当前树全路径排序
            Integer treeSort = dto.getTreeSort();
            String treeSorts = String.format("%06d", treeSort);
            dto.setTreeSorts(treeSorts);
            //设置当前是否为叶子节点，新增全部为叶子节点
            if (opType == Constants.OpType.insert) {
                dto.setTreeLeaf(Constants.TREE_LEAF_Y);
            }
            //设置当前节点级别.
            dto.setTreeLevel(1);
        }
    }


    /**
     * 判断是否修改过当前数据的排序号和调整过当前树节点的父级
     *
     * @param dto
     * @param old
     * @return
     */
    protected boolean isUpdateTreeProperties(T dto, T old) {
        return (!old.getTreeSort().equals(dto.getTreeSort()) || !old.getParentId().equals(dto.getParentId()));
    }

    /**
     * 判断是是否移动节点到自身或者子节点下
     *
     * @param dto 更新对象
     * @param old 源对象
     * @return boolean
     */
    protected boolean isMoveToSelfChildNode(T dto, T old) {
        if (!dto.getParentId().equals(old.getParentId())) {
            //判断是否移动到自身下面
            if (dto.getParentId().equals(old.getId())) {
                return true;
            }
            //判断是否移动到子节点下
            List<T> children = findChildListByParentIds(old.getParentIds());
            for (T child : children) {
                if (child.getId().equals(dto.getParentId())) {
                    return true;
                }
            }
        }
        return false;
    }

}
