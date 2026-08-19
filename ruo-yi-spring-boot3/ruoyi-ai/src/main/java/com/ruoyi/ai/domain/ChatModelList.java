package com.ruoyi.ai.domain;

import java.math.BigDecimal;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 模型列表对象 chat_model_list
 *
 * @author 欧阳日峰
 * @date 2025-02-10
 */
public class ChatModelList extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 聊天模型列表ID */
    private Long id;

    /** 配置URL */
    @Excel(name = "配置URL")
    private String baseUrl;

    /** apiKey */
    @Excel(name = "apiKey")
    private String apiKey;

    /** 模型版本 */
    @Excel(name = "模型版本")
    private String modelVersion;

    /** 模型名称 */
    @Excel(name = "模型名称")
    private String modelName;

    /** GPU 数量 */
    @Excel(name = "GPU 数量")
    private Integer numGpu;

    /** 随机性 */
    @Excel(name = "随机性")
    private BigDecimal temperature;

    /** 限制模型单词 */
    @Excel(name = "限制模型单词")
    private Integer topK;

    /** 核采样 */
    @Excel(name = "核采样")
    private BigDecimal topP;

    /** 防止重复 */
    @Excel(name = "防止重复")
    private BigDecimal repeatPenalty;

    /** 惩罚已出现的词 */
    @Excel(name = "惩罚已出现的词")
    private BigDecimal presencePenalty;

    /** 惩罚频繁出现的词 */
    @Excel(name = "惩罚频繁出现的词")
    private BigDecimal frequencyPenalty;

    /** 最大token 数 */
    @Excel(name = "最大token 数")
    private Integer numPredict;

    /** CPU线程数 */
    @Excel(name = "CPU线程数")
    private Integer numThread;

    /** 是否启用低显存模式 */
    @Excel(name = "是否启用低显存模式")
    private Boolean lowVram;

    /** 是否选择 */
    @Excel(name = "是否选择")
    private Boolean isChose;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setBaseUrl(String baseUrl)
    {
        this.baseUrl = baseUrl;
    }

    public String getBaseUrl()
    {
        return baseUrl;
    }
    public void setModelVersion(String modelVersion)
    {
        this.modelVersion = modelVersion;
    }
    public void setApiKey(String apiKey)
    {
        this.apiKey = apiKey;
    }
    public String getApiKey()
    {
        return apiKey;
    }
    public String getModelVersion()
    {
        return modelVersion;
    }
    public void setModelName(String modelName)
    {
        this.modelName = modelName;
    }

    public String getModelName()
    {
        return modelName;
    }
    public void setNumGpu(Integer numGpu)
    {
        this.numGpu = numGpu;
    }

    public Integer getNumGpu()
    {
        return numGpu;
    }
    public void setTemperature(BigDecimal temperature)
    {
        this.temperature = temperature;
    }

    public BigDecimal getTemperature()
    {
        return temperature;
    }
    public void setTopK(Integer topK)
    {
        this.topK = topK;
    }

    public Integer getTopK()
    {
        return topK;
    }
    public void setTopP(BigDecimal topP)
    {
        this.topP = topP;
    }

    public BigDecimal getTopP()
    {
        return topP;
    }
    public void setRepeatPenalty(BigDecimal repeatPenalty)
    {
        this.repeatPenalty = repeatPenalty;
    }

    public BigDecimal getRepeatPenalty()
    {
        return repeatPenalty;
    }
    public void setPresencePenalty(BigDecimal presencePenalty)
    {
        this.presencePenalty = presencePenalty;
    }

    public BigDecimal getPresencePenalty()
    {
        return presencePenalty;
    }
    public void setFrequencyPenalty(BigDecimal frequencyPenalty)
    {
        this.frequencyPenalty = frequencyPenalty;
    }

    public BigDecimal getFrequencyPenalty()
    {
        return frequencyPenalty;
    }
    public void setNumPredict(Integer numPredict)
    {
        this.numPredict = numPredict;
    }

    public Integer getNumPredict()
    {
        return numPredict;
    }
    public void setNumThread(Integer numThread)
    {
        this.numThread = numThread;
    }

    public Integer getNumThread()
    {
        return numThread;
    }
    public void setLowVram(Boolean lowVram)
    {
        this.lowVram = lowVram;
    }

    public Boolean getLowVram()
    {
        return lowVram;
    }
    public void setIsChose(Boolean isChose)
    {
        this.isChose = isChose;
    }

    public Boolean getIsChose()
    {
        return isChose;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("baseUrl", getBaseUrl())
                .append("apiKey",getApiKey())
                .append("modelVersion", getModelVersion())
                .append("modelName", getModelName())
                .append("numGpu", getNumGpu())
                .append("temperature", getTemperature())
                .append("topK", getTopK())
                .append("topP", getTopP())
                .append("repeatPenalty", getRepeatPenalty())
                .append("presencePenalty", getPresencePenalty())
                .append("frequencyPenalty", getFrequencyPenalty())
                .append("numPredict", getNumPredict())
                .append("numThread", getNumThread())
                .append("lowVram", getLowVram())
                .append("isChose", getIsChose())
                .toString();
    }
}
