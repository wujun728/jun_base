package ginyi.framework.security.service;

import ginyi.system.domain.SysLogLogin;
import ginyi.system.service.ISysLogininforService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 系统访问日志情况信息 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysLoginLogServiceImpl implements ISysLogininforService {

    /**
     * 新增系统登录日志
     *
     * @param logininfor 访问日志对象
     */
    @Override
    public void insertLogininfor(SysLogLogin logininfor) {
        // todo 待写~
    }

    /**
     * 查询系统登录日志集合
     *
     * @param logininfor 访问日志对象
     * @return 登录记录集合
     */
    @Override
    public List<SysLogLogin> selectLogininforList(SysLogLogin logininfor) {
        // todo 待写~
        return null;
    }

    /**
     * 批量删除系统登录日志
     *
     * @param infoIds 需要删除的登录日志ID
     * @return 结果
     */
    @Override
    public int deleteLogininforByIds(Long[] infoIds) {
        // todo 待写~
        return 0;
    }

    /**
     * 清空系统登录日志
     */
    @Override
    public void cleanLogininfor() {
        // todo 待写~
    }
}
