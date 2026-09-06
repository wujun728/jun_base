package com.ruoyi.business.mapper;

import java.util.List;
import com.ruoyi.business.domain.SqWxUser;

public interface SqWxUserMapper
{
    public SqWxUser selectSqWxUserById(Long userId);
    public List<SqWxUser> selectSqWxUserList(SqWxUser user);
    public int insertSqWxUser(SqWxUser user);
    public int updateSqWxUser(SqWxUser user);
    public int deleteSqWxUserByIds(Long[] userIds);
}
