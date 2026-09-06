package ginyi.system.service;

import ginyi.system.domain.SysLogLogin;
import ginyi.system.domain.SysLogOperation;
import ginyi.system.domain.model.vo.BaseVo;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ISysLogService {

    /**
     * 获取登录日志
     * @param page
     * @param pageSize
     * @return
     */
    public BaseVo<SysLogLogin> getLoginLogList(Long page, Long pageSize);

    /**
     * 获取操作日志
     * @param page
     * @param pageSize
     * @return
     */
    public BaseVo<SysLogOperation> getOperationLogList(Long page, Long pageSize);
}
