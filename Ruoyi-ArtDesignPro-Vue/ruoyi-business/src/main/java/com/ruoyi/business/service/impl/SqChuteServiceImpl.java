package com.ruoyi.business.service.impl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.business.domain.SqChute;
import com.ruoyi.business.mapper.SqChuteMapper;
import com.ruoyi.business.service.ISqChuteService;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.StringUtils;

@Service
public class SqChuteServiceImpl implements ISqChuteService
{
    private static final AtomicInteger ID_SEQUENCE = new AtomicInteger(0);

    @Autowired
    private SqChuteMapper sqChuteMapper;

    @Override
    public SqChute selectSqChuteById(Long id)
    {
        return sqChuteMapper.selectSqChuteById(id);
    }

    @Override
    public List<SqChute> selectSqChuteList(SqChute chute)
    {
        normalizeDeletedFlag(chute);
        return sqChuteMapper.selectSqChuteList(chute);
    }

    @Override
    public List<SqChute> selectSqChuteHeightList(SqChute chute)
    {
        normalizeDeletedFlag(chute);
        return sqChuteMapper.selectSqChuteHeightList(chute);
    }

    @Override
    public List<SqChute> selectSqChuteOptions(SqChute chute)
    {
        normalizeDeletedFlag(chute);
        return sqChuteMapper.selectSqChuteOptions(chute);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int insertSqChute(SqChute chute)
    {
        if (StringUtils.isNotBlank(chute.getCarmip()) && sqChuteMapper.countSqChuteByIp(chute) > 0)
        {
            throw new ServiceException("新增IP已存在");
        }
        if (chute.getId() == null)
        {
            chute.setId(nextSafeLongId());
        }
        if (StringUtils.isBlank(chute.getIsDeleted()))
        {
            chute.setIsDeleted("0");
        }
        if (StringUtils.isBlank(chute.getStatus()))
        {
            chute.setStatus("start");
        }
        if (chute.getSafeAlert() == null)
        {
            chute.setSafeAlert(Boolean.TRUE);
        }
        return sqChuteMapper.insertSqChute(chute);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqChute(SqChute chute)
    {
        if (StringUtils.isNotBlank(chute.getCarmip()) && sqChuteMapper.countSqChuteByIp(chute) > 0)
        {
            throw new ServiceException("IP已存在");
        }
        return sqChuteMapper.updateSqChute(chute);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateSqChuteHeight(SqChute chute)
    {
        calculateTon(chute);
        return sqChuteMapper.updateSqChuteHeight(chute);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int deleteSqChuteByIds(Long[] ids)
    {
        return sqChuteMapper.deleteSqChuteByIds(ids);
    }

    private void calculateTon(SqChute chute)
    {
        if (StringUtils.isBlank(chute.getShitype()) || StringUtils.isBlank(chute.getRad())
                || StringUtils.isBlank(chute.getNowQuantity()))
        {
            return;
        }
        String shitypeLabel = sqChuteMapper.selectDictLabel("shitype", chute.getShitype());
        String density = StringUtils.isNotBlank(shitypeLabel)
                ? sqChuteMapper.selectDensityByLabel(shitypeLabel) : null;
        if (StringUtils.isBlank(density))
        {
            return;
        }
        BigDecimal radius = new BigDecimal(chute.getRad());
        BigDecimal height = new BigDecimal(chute.getNowQuantity());
        BigDecimal densityValue = new BigDecimal(density);
        BigDecimal sectionWeight = new BigDecimal("3.1415926")
                .multiply(radius)
                .multiply(radius)
                .multiply(densityValue)
                .setScale(2, RoundingMode.HALF_UP);
        chute.setTon(height.multiply(sectionWeight).setScale(2, RoundingMode.HALF_UP).toPlainString());
    }

    private void normalizeDeletedFlag(SqChute chute)
    {
        if (StringUtils.isBlank(chute.getIsDeleted()))
        {
            chute.setIsDeleted("0");
        }
    }

    private Long nextSafeLongId()
    {
        int sequence = ID_SEQUENCE.updateAndGet(value -> value >= 999 ? 0 : value + 1);
        return System.currentTimeMillis() * 1000L + sequence;
    }
}
