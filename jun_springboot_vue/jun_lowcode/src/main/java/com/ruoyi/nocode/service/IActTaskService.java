package com.ruoyi.nocode.service;

import com.github.pagehelper.Page;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.nocode.domain.dto.ActTaskDTO;

public interface IActTaskService {
    public Page<ActTaskDTO> selectTaskList(PageDomain pageDomain);
}
