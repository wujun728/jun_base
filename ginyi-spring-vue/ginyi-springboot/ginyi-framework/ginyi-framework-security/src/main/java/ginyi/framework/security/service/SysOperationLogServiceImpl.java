package ginyi.framework.security.service;

import ginyi.system.domain.SysLogOperation;
import ginyi.system.service.ISysOperLogService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 操作日志 服务层处理
 *
 * @author ruoyi
 */
@Service
public class SysOperationLogServiceImpl implements ISysOperLogService {

    /**
     * 新增操作日志
     *
     * @param operLog 操作日志对象
     */
    @Override
    public void insertOperlog(SysLogOperation operLog) {
        // todo 待写~
    }

    /**
     * 查询系统操作日志集合
     *
     * @param operLog 操作日志对象
     * @return 操作日志集合
     */
    @Override
    public List<SysLogOperation> selectOperLogList(SysLogOperation operLog) {
        // todo 待写~
        return null;
    }

    /**
     * 批量删除系统操作日志
     *
     * @param operIds 需要删除的操作日志ID
     * @return 结果
     */
    @Override
    public int deleteOperLogByIds(Long[] operIds) {
        // todo 待写~
        return 0;
    }

    /**
     * 查询操作日志详细
     *
     * @param operId 操作ID
     * @return 操作日志对象
     */
    @Override
    public SysLogOperation selectOperLogById(Long operId) {
        // todo 待写~
        return null;
    }

    /**
     * 清空操作日志
     */
    @Override
    public void cleanOperLog() {
        // todo 待写~
    }
}
