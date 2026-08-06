package com.ruoyi.modelMessage.service.impl;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ruoyi.common.utils.JSON;
import com.fasterxml.jackson.databind.JsonNode;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.modelMessage.domain.MessageSystem;
import com.ruoyi.modelMessage.domain.MessageTemplate;
import com.ruoyi.modelMessage.domain.MessageVariable;
import com.ruoyi.modelMessage.enums.BuiltInVariableType;
import com.ruoyi.modelMessage.enums.SendMode;
import com.ruoyi.modelMessage.mapper.MessageSystemMapper;
import com.ruoyi.modelMessage.mapper.MessageTemplateMapper;
import com.ruoyi.modelMessage.mapper.MessageVariableMapper;
import com.ruoyi.modelMessage.service.IMessageSystemService;
import com.ruoyi.tfa.email.utils.EmailUtil;
import com.ruoyi.tfa.phone.config.DySmsConfig;
import com.ruoyi.tfa.phone.domain.DySmsTemplate;
import com.ruoyi.tfa.phone.utils.DySmsUtil;

/**
 * 消息管理Service业务层处理
 * 
 */
@Service
public class MessageSystemServiceImpl implements IMessageSystemService {

    private static final Logger log = LoggerFactory.getLogger(MessageSystemServiceImpl.class);

    @Autowired
    private DySmsConfig dySmsConfig;

    @Autowired
    private MessageSystemMapper messageSystemMapper;

    @Autowired
    private MessageTemplateMapper messageTemplateMapper;

    @Autowired
    private MessageVariableMapper messageVariableMapper;

    /**
     * 查询消息管理
     * 
     * @param messageId 消息管理主键
     * @return 消息管理
     */
    @Override
    public MessageSystem selectMessageSystemByMessageId(Long messageId) {
        return messageSystemMapper.selectMessageSystemByMessageId(messageId);
    }

    /**
     * 查询消息管理列表
     * 
     * @param messageSystem 消息管理
     * @return 消息管理列表
     */
    @Override
    public List<MessageSystem> selectMessageSystemList(MessageSystem messageSystem) {
        return messageSystemMapper.selectMessageSystemList(messageSystem);
    }

    /**
     * 新增消息管理
     * 
     * @param messageSystem 消息管理
     * @return 结果
     */
    @Override
    public int insertMessageSystem(MessageSystem messageSystem) {
        return messageSystemMapper.insertMessageSystem(messageSystem);
    }

    /**
     * 修改消息管理
     * 
     * @param messageSystem 消息管理
     * @return 结果
     */
    @Override
    public int updateMessageSystem(MessageSystem messageSystem) {
        messageSystem.setUpdateTime(DateUtils.getNowDate());
        return messageSystemMapper.updateMessageSystem(messageSystem);
    }

    /**
     * 批量删除消息管理
     * 
     * @param messageIds 需要删除的消息管理主键
     * @return 结果
     */
    @Override
    public int deleteMessageSystemByMessageIds(Long[] messageIds) {
        return messageSystemMapper.deleteMessageSystemByMessageIds(messageIds);
    }

    /**
     * 删除消息管理信息
     * 
     * @param messageId 消息管理主键
     * @return 结果
     */
    @Override
    public int deleteMessageSystemByMessageId(Long messageId) {
        return messageSystemMapper.deleteMessageSystemByMessageId(messageId);
    }

    // 收件人为本人的话点击信息详情的时候然后把状态未读信息修改为已读
    @Override
    public int updateState(Long messageId) {
        int result = messageSystemMapper.updateState(messageId, SecurityUtils.getUsername());
        return result;
    }

    /**
     * 批量发送信息
     */
    @Override
    public int batchInsertMessageSystem(List<MessageSystem> messageSystemList) {
        for (MessageSystem messageSystem : messageSystemList) {
            messageSystem.setMessageStatus("0"); // 默认发送信息为未读状态
            messageSystem.setCreateBy(SecurityUtils.getUsername());
            messageSystem.setUpdateBy(SecurityUtils.getUsername());
            messageSystem.setCreateTime(DateUtils.getNowDate());
            messageSystem.setUpdateTime(DateUtils.getNowDate());
        }
        int result = messageSystemMapper.batchInsertMessageSystem(messageSystemList);
        if (result <= 0) {
            throw new ServiceException("消息发送失败!");
        }
        return result;
    }

       /**
     * 统一查询系统资源信息（角色、部门、用户）
     *
     * @param type     查询类型：role-角色信息, dept-部门信息, user-用户信息, usersbyrole-根据角色查用户, usersbydept-根据部门查用户, usersbysendmode-根据发送方式查用户
     * @param id       可选参数，当查询特定角色或部门下的用户时使用
     * @param sendMode 可选参数，当查询特定发送方式的用户时使用（phone/email）
     * @return 查询结果
     */
    @Override
    public Object querySystemResource(String type, Long id, String sendMode) {
        switch (type.toLowerCase()) {
            case "role":
                return messageSystemMapper.selectRole();
            case "dept":
                return messageSystemMapper.selectDept();
            case "user":
                return messageSystemMapper.selectUser();
            case "usersbyrole":
                if (id == null) {
                    throw new ServiceException("查询角色，角色Id不能为空");
                }
                return messageSystemMapper.selectUsersByRoleId(id);
            case "usersbydept":
                if (id == null) {
                    throw new ServiceException("查询部门，部门Id不能为空");
                }
                return messageSystemMapper.getUserNameByDeptId(id);
            case "usersbysendmode":
                return getUsersBySendMode(sendMode);
            default:
                throw new ServiceException("不支持的查询类型: " + type);
        }
    }

    // 根据传递的值进行过滤筛选不符合的用户
    private List<SysUser> getUsersBySendMode(String sendMode) {
        if (sendMode == null || sendMode.trim().isEmpty()) {
            sendMode = "default";
        }
        switch (sendMode) {
            case "1": return messageSystemMapper.selectUserBySendMode("phone");
            case "2": return messageSystemMapper.selectUserBySendMode("email");
            default:  return messageSystemMapper.selectUser();
        }
    }

    /**
     * 根据 MessageSystem 对象的 sendMode 属性处理消息的发送方式
     */
    @Override
    public void processMessageSystem(MessageSystem messageSystem) {
        if (messageSystem == null || messageSystem.getSendMode() == null) {
            throw new ServiceException("无效的消息系统对象或发送方式！");
        }

        String sendModeStr = messageSystem.getSendMode();
        SendMode sendMode;
        try {
            sendMode = SendMode.fromCode(sendModeStr);
        } catch (IllegalArgumentException e) {
            throw new ServiceException("不支持的消息发送方式: " + sendModeStr);
        }
        switch (sendMode) {
            case PHONE:
                sendNotificationMessage(messageSystem);
                break;
            case EMAIL:
                handleEmailNotification(messageSystem);
                break;
            case PLATFORM:
                sendPlatformMessage(messageSystem);
                break;
            default:
                throw new ServiceException("不支持的发送方式: " + sendMode);
        }
    }

    /**
     * 发送平台消息
     */
    public void sendPlatformMessage(MessageSystem messageSystem) {
        String notificationContent = messageSystem.getMessageContent();
        try {
            String filledMessage = notificationContent.startsWith("SMS_")
                    ? processTemplateMessage(messageSystem, notificationContent)
                    : notificationContent; // 是自定义输入，使用用户输入的内容
            log.info("平台发送成功: {}", filledMessage);
            messageSystem.setMessageContent(filledMessage);
        } catch (Exception e) {
            log.error("发送平台消息时发生异常: ", e);
            throw new ServiceException("发送平台消息异常：" + e.getMessage());
        }
    }

    /**
     * 发送邮件通知
     */
    public void handleEmailNotification(MessageSystem messageSystem) {
        String email = messageSystem.getCode();
        if (StringUtils.isEmpty(email)) {
            throw new ServiceException("邮箱不能为空！");
        }
        try {
            String filledMessage = messageSystem.getMessageContent().startsWith("SMS_")
                    ? processTemplateMessage(messageSystem, messageSystem.getMessageContent())
                    : messageSystem.getMessageContent(); // 是自定义输入，则直接使用用户提供的内容
            log.info("邮件发送成功: {}", filledMessage);
            messageSystem.setMessageContent(filledMessage);
            EmailUtil.sendMessage(email, "通知", filledMessage);
        } catch (Exception e) {
            log.error("发送邮件时发生异常: ", e);
            throw new ServiceException("发送通知信息异常：" + e.getMessage());
        }
    }

    /**
     * 发送短信通知
     */
    @SuppressWarnings("unchecked")
    public void sendNotificationMessage(MessageSystem messageSystem) {
        String phone = messageSystem.getCode();
        if (StringUtils.isEmpty(phone)) {
            throw new ServiceException("手机号码不能为空！");
        }
        // 检查短信配置
        if (dySmsConfig == null || dySmsConfig.getTemplate() == null || dySmsConfig.getTemplate().isEmpty()) {
            throw new ServiceException("短信配置或模板未正确加载");
        }
        try {
            // 解析消息内容获取模板信息和参数
            Map<String,Object> parsedParams= parseInput(messageSystem.getMessageContent());
            String templateCode = (String) parsedParams.get("templateCode");
            if (templateCode == null) {
                throw new ServiceException("消息内容中未包含有效的模板代码");
            }
            // 查找匹配的模板
            DySmsTemplate dySmsTemplate = dySmsConfig.getTemplate().get(templateCode);
            // 如果直接匹配失败，尝试通过模板代码匹配
            if (dySmsTemplate == null) {
                for (DySmsTemplate template : dySmsConfig.getTemplate().values()) {
                    if (templateCode.equals(template.getTemplateCode())) {
                        dySmsTemplate = template;
                        break;
                    }
                }
            }
            // 如果还是没找到且只有一个模板，直接使用
            if (dySmsTemplate == null && dySmsConfig.getTemplate().size() == 1) {
                dySmsTemplate = dySmsConfig.getTemplate().values().iterator().next();
            }
            if (dySmsTemplate == null) {
                throw new ServiceException("未找到短信模板: " + templateCode + "，请检查配置");
            }
            // 获取并填充参数
            Map<String, String> params = (Map<String, String>) parsedParams.get("params");
            List<String> variableNames = (List<String>) parsedParams.get("variableNames");
            fillBuiltInVariables(params, messageSystem, new HashSet<>(variableNames));
            // 获取模板内容并填充变量，生成最终的消息内容
            String templateContent = (String) parsedParams.get("templateContent");
            if (templateContent != null) {
                String filledContent = fillTemplate(templateContent, params);
                messageSystem.setMessageContent(filledContent); // 更新为填充后的内容
            }
            // 构造 JSON 参数并发送短信
            JsonNode templateParamJson = JSON.parseObject(JSON.toJSONString(params));
            DySmsUtil.sendSms(phone, dySmsTemplate, templateParamJson);
            log.info("短信发送成功 - 手机号: {}, 模板: {}, 参数: {}", phone, templateCode, templateParamJson);
        } catch (Exception e) {
            log.error("发送短信时发生异常: ", e);
            throw new ServiceException("发送短信异常：" + e.getMessage());
        }
    }

    /**
     * 解析输入字符串，提取模板代码和参数
     */
    public Map<String, Object> parseInput(String input) {
        if (input == null || input.trim().isEmpty()) {
            throw new ServiceException("输入内容不能为空！");
        }
        String templateCode = null;
        String queryParams = "";
        // 支持 SMS_ 开头的模板代码或者配置中的模板名
        if (input.startsWith("SMS_")) {
            int templateCodeEndIndex = input.indexOf('?');
            if (templateCodeEndIndex != -1) {
                templateCode = input.substring(0, templateCodeEndIndex);
                queryParams = input.substring(templateCodeEndIndex + 1);
            } else {
                templateCode = input;
            }
        }
        MessageTemplate templateContent = null;
        List<String> variableNames = new ArrayList<>();
        if (templateCode != null) {
            templateContent = messageTemplateMapper.selectMessageTemplateByTemplateCode(templateCode);
            if (templateContent == null) {
                throw new ServiceException("未找到该模版签名！ " + templateCode);
            }
            variableNames = extractVariableNamesFromTemplate(templateContent);
        }
        Map<String, String> params = new HashMap<>();
        if (!queryParams.isEmpty()) {
            for (String param : queryParams.split("&")) {
                String[] keyValue = param.split("=", 2);
                if (keyValue.length != 2) {
                    throw new ServiceException("无效的参数格式: " + param + "，请使用 key=value 格式");
                }
                String value = URLDecoder.decode(keyValue[1], StandardCharsets.UTF_8);
                params.put(keyValue[0], value);
            }
        }
        if (templateContent != null) {
            for (String varName : variableNames) {
                params.putIfAbsent(varName, null);
            }
        }
        return Map.of( "templateCode", templateCode, "params", params,
            "templateContent", templateContent != null ? templateContent.getTemplateContent() : input,
            "variableNames", variableNames );
    }

    /**
     * 提取模板中的变量名
     */
    public List<String> extractVariableNamesFromTemplate(MessageTemplate templateContent) {
        List<String> variableNames = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\$\\{(.*?)\\}");
        Matcher matcher = pattern.matcher(templateContent.getTemplateContent());
        while (matcher.find()) {
            variableNames.add(matcher.group(1));
        }
        return variableNames;
    }

    /**
     * 填充模板内容
     */
    public String fillTemplate(String template, Map<String, String> params) {
        StringBuilder sb = new StringBuilder(template);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String key = "${" + entry.getKey() + "}";
            String value = Optional.ofNullable(entry.getValue()).orElse("");

            int index;
            while ((index = sb.indexOf(key)) != -1) {
                sb.replace(index, index + key.length(), value);
            }
        }
        return sb.toString();
    }

    /**
     * 清除内置变量的随机数字
     */
    public void clearBuiltInVariables(Map<String, String> params) {
        List<MessageVariable> builtInVariables = messageVariableMapper.selectMessageVariable();
        for (MessageVariable variable : builtInVariables) {
            String variableContent = variable.getVariableContent();
            params.remove(variableContent);
        }
    }

    /**
     * 处理模板消息并填充所有类型的变量
     */
    @SuppressWarnings("unchecked")
    public String processTemplateMessage(MessageSystem messageSystem, String notificationContent) throws Exception {
        Map<String, Object> parsedParams = parseInput(notificationContent);
        String templateCode = (String) parsedParams.get("templateCode");
        MessageTemplate templateContent = null;
        if (templateCode != null) {
            templateContent = messageTemplateMapper.selectMessageTemplateByTemplateCode(templateCode);
        }
        Map<String, String> params = (Map<String, String>) parsedParams.get("params");
        List<String> variableNames = new ArrayList<>();
        if (templateContent != null) {
            variableNames = extractVariableNamesFromTemplate(templateContent);
        }
        Set<String> variableNameSet = new HashSet<>(variableNames);
        clearBuiltInVariables(params);
        fillBuiltInVariables(params, messageSystem, variableNameSet);
        for (String varName : variableNameSet) {
            if (!params.containsKey(varName) || params.get(varName) == null) {
                params.put(varName, "[变量未设置: " + varName + "]");
            }
        }
        String templateContentStr = templateContent != null ?
            templateContent.getTemplateContent() : notificationContent;
        return fillTemplate(templateContentStr, params);
    }

    /**
     * 填充所有类型的变量
     */
    public void fillBuiltInVariables(Map<String, String> params, MessageSystem message, Set<String> variableNameSet) {
        List<MessageVariable> allVariables = messageVariableMapper.selectMessageVariables(new ArrayList<>(variableNameSet));
        for (MessageVariable variable : allVariables) {
            String variableType = variable.getVariableType();
            String variableContent = variable.getVariableContent();
            String variableName = variable.getVariableName();
            if ("内置变量".equals(variableType)) {
                boolean matchByContent = variableNameSet.contains(variableContent);
                boolean matchByName = variableNameSet.contains(variableName);
                if (matchByContent || matchByName) {
                    String keyToUse = matchByContent ? variableContent : variableName;
                    // 处理内置变量
                    if (BuiltInVariableType.isBuiltInVariable(variableContent)) {
                        // 只有当参数不存在或为空时才生成内置变量的值
                        if (!params.containsKey(keyToUse) || params.get(keyToUse) == null || params.get(keyToUse).isEmpty()) {
                            params.put(keyToUse, BuiltInVariableType.fromIdentifier(variableContent).generateValue(message));
                        }
                    } else if ("recipients".equals(variableContent) || "收件人".equals(variableName)) {
                        // 收件人变量总是使用实际的收件人信息
                        params.put(keyToUse, message.getMessageRecipient());
                    }
                }
            } else if ("指定文本".equals(variableType)) {
                if (BuiltInVariableType.isBuiltInVariable(variableContent) && variableNameSet.contains(variableContent)) {
                    // 只有当参数不存在或为空时才生成内置变量的值
                    if (!params.containsKey(variableContent) || params.get(variableContent) == null || params.get(variableContent).isEmpty()) {
                        params.put(variableContent, BuiltInVariableType.fromIdentifier(variableContent).generateValue(message));
                    }
                } else if ("recipients".equals(variableContent) && variableNameSet.contains(variableName)) {
                    params.put(variableName, message.getMessageRecipient());
                } else if (variableNameSet.contains(variableName)) {
                    if (!params.containsKey(variableName) || params.get(variableName) == null) {
                        params.put(variableName, variableContent);
                    }
                }
            } else {
                if (BuiltInVariableType.isBuiltInVariable(variableContent) && variableNameSet.contains(variableContent)) {
                    // 只有当参数不存在或为空时才生成内置变量的值
                    if (!params.containsKey(variableContent) || params.get(variableContent) == null || params.get(variableContent).isEmpty()) {
                        params.put(variableContent, BuiltInVariableType.fromIdentifier(variableContent).generateValue(message));
                    }
                } else if ("recipients".equals(variableContent) && variableNameSet.contains(variableName)) {
                    params.put(variableName, message.getMessageRecipient());
                }
            }
        }
    }
}
