package com.ruoyi.web.demo.treetable.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruoyi.common.core.service.impl.TreeCommonServiceImpl;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.web.demo.treetable.entity.TreeTable;
import com.ruoyi.web.demo.treetable.mapper.TreeTableMapper;
import com.ruoyi.web.demo.treetable.service.ITreeTableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package test.treetable
 * @title: 树形结构表控制器
 * @description: 树形结构表控制器
 * @author: admin
 * @date: 2019-11-13 21:38:34
 * @copyright: www.sunseagear.com Inc. All rights reserved.
 */
@Transactional
@Service("treeTableService")
public class TreeTableServiceImpl extends TreeCommonServiceImpl<TreeTableMapper, TreeTable> implements ITreeTableService {

}
