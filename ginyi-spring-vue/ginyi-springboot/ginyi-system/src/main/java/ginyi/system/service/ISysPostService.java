package ginyi.system.service;

import ginyi.system.domain.model.dto.PostDto;
import ginyi.system.domain.model.dto.RoleDto;
import ginyi.system.domain.model.vo.BaseVo;
import ginyi.system.domain.model.vo.PostVo;

import java.util.Set;

public interface ISysPostService {

    /**
     * 查询岗位列表
     *
     * @param postDto
     * @param page
     * @param pageSize
     * @return
     */
    public BaseVo<PostVo> list(PostDto postDto, Long page, Long pageSize);

    /**
     * 根据岗位id获取岗位详情
     *
     * @param postId
     * @return
     */
    public PostVo getPostByPostId(Long postId);

    /**
     * 添加岗位
     *
     * @param postDto
     */
    public void addPost(PostDto postDto);

    /**
     * 更新岗位
     *
     * @param postDto
     */
    public void updatePost(PostDto postDto);

    /**
     * 根据id删除岗位
     *
     * @param postId
     */
    public void removePostById(Long postId);

    /**
     * 批量删除岗位
     *
     * @param ids
     */
    void removeDeptByIds(Set<Long> ids);

    /**
     * 更改状态（0正常 1停用）
     * @param postDto
     */
    public void updateStatus(PostDto postDto);
}
