package com.ruoyi.web.service;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Service;
import com.alibaba.fastjson2.JSON;
import com.ruoyi.system.domain.SysNotice;
import com.ruoyi.web.websocket.SysNoticeWebSocketServer;

/**
 * 通知公告广播服务
 *
 * @author ruoyi
 */
@Service
public class SysNoticeBroadcastService
{
    public void created(SysNotice notice)
    {
        broadcast("notice:created", notice, null);
    }

    public void updated(SysNotice notice)
    {
        broadcast("notice:updated", notice, null);
    }

    public void deleted(Long[] noticeIds)
    {
        broadcast("notice:deleted", null, noticeIds);
    }

    private void broadcast(String type, SysNotice notice, Long[] noticeIds)
    {
        Map<String, Object> message = new HashMap<>();
        message.put("type", type);
        message.put("timestamp", System.currentTimeMillis());
        if (notice != null)
        {
            message.put("noticeId", notice.getNoticeId());
            message.put("noticeType", notice.getNoticeType());
            message.put("status", notice.getStatus());
        }
        if (noticeIds != null)
        {
            message.put("noticeIds", noticeIds);
        }
        SysNoticeWebSocketServer.broadcast(JSON.toJSONString(message));
    }
}
