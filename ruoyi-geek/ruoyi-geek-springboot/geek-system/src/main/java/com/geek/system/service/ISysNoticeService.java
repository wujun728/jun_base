package com.geek.system.service;

import com.geek.system.domain.SysNotice;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.service.IService;

/**
 * 公告 服务层
 * 
 * @author geek
 */
public interface ISysNoticeService extends IService<SysNotice> {

    Page<SysNotice> page(SysNotice notice, Integer pageNum, Integer pageSize);

}
