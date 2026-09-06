package com.geek.system.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.geek.common.utils.poi.ExcelUtil;
import com.geek.system.domain.SysNotice;
import com.geek.system.mapper.SysNoticeMapper;
import com.geek.system.service.ISysNoticeService;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.spring.service.impl.ServiceImpl;

import jakarta.servlet.http.HttpServletResponse;

/**
 * 公告 服务层实现
 * 
 * @author geek
 */
@Service
public class SysNoticeServiceImpl extends ServiceImpl<SysNoticeMapper, SysNotice> implements ISysNoticeService {

    /**
     * 查询公告列表
     * 
     * @param notice 公告信息
     * @return 公告集合
     */
    private QueryChain<SysNotice> selectNoticeList(SysNotice notice) {
        return this.queryChain()
                .like(SysNotice::getNoticeTitle, notice.getNoticeTitle())
                .eq(SysNotice::getNoticeType, notice.getNoticeType())
                .eq(SysNotice::getStatus, notice.getStatus());
    }

    public Page<SysNotice> page(SysNotice notice, Integer pageNum, Integer pageSize) {
        return this.selectNoticeList(notice).page(Page.of(pageNum, pageSize));
    }

    public void export(SysNotice notice, HttpServletResponse response) {
        List<SysNotice> list = this.selectNoticeList(notice).list();
        ExcelUtil<SysNotice> util = new ExcelUtil<>(SysNotice.class);
        util.exportExcel(response, list, "公告数据");
    }
}
