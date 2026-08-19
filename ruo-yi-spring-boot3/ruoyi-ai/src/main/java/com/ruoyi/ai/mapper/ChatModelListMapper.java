package com.ruoyi.ai.mapper;

import java.util.List;
import com.ruoyi.ai.domain.ChatModelList;

/**
 * 模型列表Mapper接口
 * 
 * @author 欧阳日峰
 * @date 2025-02-06
 */
public interface ChatModelListMapper 
{
    /**
     * 查询模型列表
     * 
     * @param id 模型列表主键
     * @return 模型列表
     */
    public ChatModelList selectChatModelListById(Long id);

    /**
     * 查询模型列表列表
     * 
     * @param chatModelList 模型列表
     * @return 模型列表集合
     */
    public List<ChatModelList> selectChatModelListList(ChatModelList chatModelList);

    /**
     * 新增模型列表
     * 
     * @param chatModelList 模型列表
     * @return 结果
     */
    public int insertChatModelList(ChatModelList chatModelList);

    /**
     * 修改模型列表
     * 
     * @param chatModelList 模型列表
     * @return 结果
     */
    public int updateChatModelList(ChatModelList chatModelList);

    /**
     * 删除模型列表
     * 
     * @param id 模型列表主键
     * @return 结果
     */
    public int deleteChatModelListById(Long id);

    /**
     * 批量删除模型列表
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteChatModelListByIds(Long[] ids);

    ChatModelList selectEnableChatModel();
}
