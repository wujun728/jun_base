package ginyi.system.service;

import ginyi.system.domain.SysNotice;
import ginyi.system.domain.model.dto.NoticeDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.NoticeVo;

/**
 * 通知公告
 */
public interface ISysNoticeService {

    /**
     * 获取通知公告列表
     * @param noticeDto
     * @param page
     * @param pageSize
     * @return
     */
    public BaseVo<SysNotice> list(NoticeDto noticeDto, Long page, Long pageSize);

    /**
     * 发布通知公告
     * @param noticeDto
     */
    public void add(NoticeDto noticeDto);

    /**
     * 获取用户通知公告列表
     * @param page
     * @param pageSize
     * @return
     */
    public BaseVo<NoticeVo> getUserNoticeList(Long page, Long pageSize);

    /**
     * 确认收到通知公告
     * @param noticeId
     */
    public void haveRead(Long noticeId);

    /**
     * 删除通知公告
     * @param noticeId
     */
    public void remove(Long noticeId);

    /**
     * 更新通知公告
     * @param noticeDto
     */
    public void updateNotice(NoticeDto noticeDto);
}
