package com.ruoyi.tenant.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruoyi.common.core.domain.entity.SysTenant;
import com.ruoyi.common.utils.uuid.IdUtils;
import com.ruoyi.tenant.mapper.SysTenantMapper;
import com.ruoyi.tenant.service.ISysTenantService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class SysTenantServiceImpl extends ServiceImpl<SysTenantMapper, SysTenant> implements ISysTenantService {

    @Override
    public boolean save(SysTenant entity) {
        if (entity.getTenantId() == null || entity.getTenantId().isEmpty()) {
            entity.setTenantId(IdUtils.simpleUUID().substring(0, 8));
        }
        return super.save(entity);
    }
}