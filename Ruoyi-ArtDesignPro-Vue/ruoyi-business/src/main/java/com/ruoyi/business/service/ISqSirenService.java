package com.ruoyi.business.service;

import java.util.List;
import com.ruoyi.business.domain.SqSiren;

public interface ISqSirenService
{
    public SqSiren selectSqSirenById(Long id);
    public List<SqSiren> selectSqSirenList(SqSiren siren);
    public int insertSqSiren(SqSiren siren);
    public int updateSqSiren(SqSiren siren);
    public int deleteSqSirenByIds(Long[] ids);
}
