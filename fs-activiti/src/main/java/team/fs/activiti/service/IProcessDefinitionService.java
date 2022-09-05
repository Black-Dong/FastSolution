package team.fs.activiti.service;

import com.github.pagehelper.Page;
import team.fs.activiti.domain.ProcessDefinition;

/**
 * @author 一只闲鹿
 */
public interface IProcessDefinitionService {

    Page<ProcessDefinition> listProcessDefinition(ProcessDefinition processDefinition);
    void deployProcessDefinition(String filePath);
    int deleteProcessDeploymentByIds(String deploymentIds) throws Exception;
    void suspendOrActiveDefinition(String id, String suspendState);

}
