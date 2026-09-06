package com.ruoyi.nocode.domain.dto;

import org.activiti.engine.repository.ProcessDefinition;

public class DefinitionIdDTO {
    private String deploymentID;
    private String resourceName;

    public String getDeploymentID() {
        return deploymentID;
    }

    public void setDeploymentID(String deploymentID) {
        this.deploymentID = deploymentID;
    }

    public String getResourceName() {
        return resourceName;
    }

    public void setResourceName(String resourceName) {
        this.resourceName = resourceName;
    }

    public DefinitionIdDTO() {
    }

    public DefinitionIdDTO(ProcessDefinition processDefinition) {
        this.deploymentID = processDefinition.getDeploymentId();
        this.resourceName = processDefinition.getResourceName();
    }
}
