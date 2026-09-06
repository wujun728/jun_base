package ginyi.system.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import ginyi.system.domain.SysNotice;
import ginyi.system.domain.model.dto.NoticeDto;
import ginyi.system.domain.model.vo.NoticeVo;
import org.apache.ibatis.annotations.Param;


public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    /**
     * 获取通知公告列表
     */
    public IPage<SysNotice> list(@Param("noticeDto") NoticeDto noticeDto, Page page);

    /**
     * 发布通知公告
     */
    public void addNotice(@Param("noticeDto") NoticeDto noticeDto);

    /**
     * 获取用户通知公告列表
     * @param userId
     * @param page
     * @return
     */
    public IPage<SysNotice> getUserNoticeList(Long userId, Page page);

    /**
     * 确认收到通知公告
     * @param notice
     */
    public void haveRead(@Param("notice") SysNotice notice);

    /**
     * 根据 noticeId 查询通知公告
     * @param noticeId
     * @return
     */
    public SysNotice selectOne(Long noticeId);

    /**
     * 更新通知公告
     * @param noticeDto
     */
    public void updateNotice(@Param("noticeDto") NoticeDto noticeDto);
}
