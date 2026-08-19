package com.ruoyi.ai.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.ai.mapper.ChatModelListMapper;
import com.ruoyi.ai.domain.ChatModelList;
import com.ruoyi.ai.service.IChatModelListService;

/**
 * 模型列表Service业务层处理
 * 
 * @author 欧阳日峰
 * @date 2025-02-06
 */
@Service
public class ChatModelListServiceImpl implements IChatModelListService 
{
    @Autowired
    private ChatModelListMapper chatModelListMapper;

    /**
     * 查询模型列表
     * 
     * @param id 模型列表主键
     * @return 模型列表
     */
    @Override
    public ChatModelList selectChatModelListById(Long id)
    {
        return chatModelListMapper.selectChatModelListById(id);
    }

    /**
     * 查询模型列表列表
     * 
     * @param chatModelList 模型列表
     * @return 模型列表
     */
    @Override
    public List<ChatModelList> selectChatModelListList(ChatModelList chatModelList)
    {
        return chatModelListMapper.selectChatModelListList(chatModelList);
    }

    /**
     * 新增模型列表
     * 
     * @param chatModelList 模型列表
     * @return 结果
     */
    @Override
    public int insertChatModelList(ChatModelList chatModelList)
    {
        return chatModelListMapper.insertChatModelList(chatModelList);
    }

    /**
     * 修改模型列表
     * 
     * @param chatModelList 模型列表
     * @return 结果
     */
    @Override
    public int updateChatModelList(ChatModelList chatModelList)
    {
        return chatModelListMapper.updateChatModelList(chatModelList);
    }

    /**
     * 批量删除模型列表
     * 
     * @param ids 需要删除的模型列表主键
     * @return 结果
     */
    @Override
    public int deleteChatModelListByIds(Long[] ids)
    {
        return chatModelListMapper.deleteChatModelListByIds(ids);
    }

    /**
     * 删除模型列表信息
     * 
     * @param id 模型列表主键
     * @return 结果
     */
    @Override
    public int deleteChatModelListById(Long id)
    {
        return chatModelListMapper.deleteChatModelListById(id);
    }

    @Override
    public ChatModelList selectEnableChatModel() {
        return chatModelListMapper.selectEnableChatModel();
    }
}
