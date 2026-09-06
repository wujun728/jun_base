package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.BusinessDemo;

public interface IBusinessDemoService
{
    public BusinessDemo selectBusinessDemoById(Long demoId);

    public List<BusinessDemo> selectBusinessDemoList(BusinessDemo demo);

    public int insertBusinessDemo(BusinessDemo demo);

    public int updateBusinessDemo(BusinessDemo demo);

    public int deleteBusinessDemoByIds(Long[] demoIds);
}
