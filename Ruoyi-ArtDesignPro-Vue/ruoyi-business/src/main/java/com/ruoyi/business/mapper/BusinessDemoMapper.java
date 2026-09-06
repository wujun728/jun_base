package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.BusinessDemo;

public interface BusinessDemoMapper
{
    public BusinessDemo selectBusinessDemoById(Long demoId);

    public List<BusinessDemo> selectBusinessDemoList(BusinessDemo demo);

    public int insertBusinessDemo(BusinessDemo demo);

    public int updateBusinessDemo(BusinessDemo demo);

    public int deleteBusinessDemoByIds(Long[] demoIds);
}
