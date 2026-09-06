package ginyi.system.service;

import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.SessionUserVo;

import java.util.Set;

public interface ISysOnlineService {

    /**
     * 当前在线用户列表
     */
    public BaseVo<SessionUserVo> getOnlineUserList(Long page, Long pageSize);

    /**
     * 强制用户下线
     * @param sessionId
     */
    public void removeUser(String sessionId);

    /**
     * 批量强制用户下线
     * @param ids
     */
    public void removeUser(Set<String> ids);
}
