package com.ruoyi.nocode.service;

import com.github.pagehelper.Page;
import com.ruoyi.common.core.page.PageDomain;
import com.ruoyi.nocode.domain.dto.DefinitionIdDTO;
import com.ruoyi.nocode.domain.dto.ProcessDefinitionDTO;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IProcessDefinitionService {
    /**
     * 获取流程定义集合
     * @param processDefinition
     * @return Page 分页信息
     */
    public Page<ProcessDefinitionDTO> selectProcessDefinitionList(ProcessDefinitionDTO processDefinition, PageDomain pageDomain);
    public DefinitionIdDTO getDefinitionsByInstanceId(String instanceId);
    /**
     * 删除流程定义
     * @param id
     * @return
     */
    public int deleteProcessDefinitionById(String id);
    /**
     * 上传并部署流程定义
     * @param file
     * @return
     * @throws IOException
     */
    public void uploadStreamAndDeployment(MultipartFile file) throws IOException;
    /**
     * 启动挂起流程流程定义
     * @param id 流程定义id
     * @param suspendState 流程状态
     * @return
     */
    public void suspendOrActiveApply(String id, Integer suspendState);

    /**
     * 上传流程流程定义
     * @param multipartFile
     * @return
     */
    public String upload(MultipartFile multipartFile) throws IOException;
    /**
     * 通过stringBPMN添加流程定义
     * @param stringBPMN
     * @return
     */
    public void addDeploymentByString(String stringBPMN );

    /**
     * 获取流程定义XML
     * @param response
     * @param deploymentId
     * @param resourceName
     */
    public void getProcessDefineXML(HttpServletResponse response, String deploymentId,String resourceName) throws IOException;
}
