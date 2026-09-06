package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.business.domain.BusinessDemo;
import com.ruoyi.business.mapper.BusinessDemoMapper;
import com.ruoyi.business.service.IBusinessDemoService;

@Service
public class BusinessDemoServiceImpl implements IBusinessDemoService
{
    @Autowired
    private BusinessDemoMapper businessDemoMapper;

    @Override
    public BusinessDemo selectBusinessDemoById(Long demoId)
    {
        return businessDemoMapper.selectBusinessDemoById(demoId);
    }

    @Override
    public List<BusinessDemo> selectBusinessDemoList(BusinessDemo demo)
    {
        return businessDemoMapper.selectBusinessDemoList(demo);
    }

    @Override
    public int insertBusinessDemo(BusinessDemo demo)
    {
        return businessDemoMapper.insertBusinessDemo(demo);
    }

    @Override
    public int updateBusinessDemo(BusinessDemo demo)
    {
        return businessDemoMapper.updateBusinessDemo(demo);
    }

    @Override
    public int deleteBusinessDemoByIds(Long[] demoIds)
    {
        return businessDemoMapper.deleteBusinessDemoByIds(demoIds);
    }
}
