package com.ruoyi.web.demo.table.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.web.demo.table.entity.Table;
import com.ruoyi.web.demo.table.mapper.TableMapper;
import com.ruoyi.web.demo.table.service.ITableService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * All rights Reserved, Designed By www.sunseagear.com
 *
 * @version V1.0
 * @package com.sunseagear.wind.modules.sys.service.impl
 * @title: 操作日志服务实现
 * @description: 操作日志服务实现 * @date: 2018-09-30 15:53:25
 * @copyright: 2018 www.sunseagear.com Inc. All rights reserved.
 */
@Transactional
@Service("tableService")
public class TableServiceImpl extends ServiceImpl<TableMapper, Table> implements ITableService {

}
