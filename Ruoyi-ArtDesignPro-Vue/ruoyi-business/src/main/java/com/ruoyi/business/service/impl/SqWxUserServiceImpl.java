package com.ruoyi.business.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqWxUser;
import com.ruoyi.business.mapper.SqWxUserMapper;
import com.ruoyi.business.service.ISqWxUserService;

@Service
public class SqWxUserServiceImpl implements ISqWxUserService
{
    @Autowired
    private SqWxUserMapper sqWxUserMapper;

    @Override
    public SqWxUser selectSqWxUserById(Long userId) { return sqWxUserMapper.selectSqWxUserById(userId); }
    @Override
    public List<SqWxUser> selectSqWxUserList(SqWxUser user) { return sqWxUserMapper.selectSqWxUserList(user); }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqWxUser(SqWxUser user) { return sqWxUserMapper.insertSqWxUser(user); }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqWxUser(SqWxUser user) { return sqWxUserMapper.updateSqWxUser(user); }
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqWxUserByIds(Long[] userIds) { return sqWxUserMapper.deleteSqWxUserByIds(userIds); }
}
