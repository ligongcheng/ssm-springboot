package cn.it.ssm.common.util;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.bpmn.model.FlowNode;
import org.activiti.bpmn.model.SequenceFlow;
import org.activiti.engine.HistoryService;
import org.activiti.engine.ProcessEngine;
import org.activiti.engine.ProcessEngineConfiguration;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.history.HistoricActivityInstance;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.image.ProcessDiagramGenerator;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 流程工具类
 *
 * @author xiecong
 */
public class ActGeneratorImgUtils {

    private static Logger logger = LoggerFactory.getLogger(ActGeneratorImgUtils.class);

    private static RepositoryService repositoryService = SpringContextUtils.getBean(RepositoryService.class);
    private static HistoryService historyService = SpringContextUtils.getBean(HistoryService.class);
    private static ProcessEngine processEngine = SpringContextUtils.getBean(ProcessEngine.class);

    /**
     * 根据流程实例Id,获取实时流程图片 activiti6
     *
     * @param processInstanceId 实例ID
     * @param outputStream      输出流
     * @return
     */
    public static void getFlowImgByInstanceIdV6(String processInstanceId, OutputStream outputStream) {
        try {
            if (StringUtils.isEmpty(processInstanceId)) {
                logger.error("processInstanceId is null");
                return;
            }
            // 获取历史流程实例
            HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            // 获取流程中已经执行的节点，按照执行先后顺序排序
            List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceId().asc().list();
            // 高亮已经执行流程节点ID集合
            List<String> highLightedActivitiIds = new ArrayList<>();
            for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
                highLightedActivitiIds.add(historicActivityInstance.getActivityId());
            }

            ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
            ProcessDiagramGenerator processDiagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(historicProcessInstance.getProcessDefinitionId());
            // 高亮流程已发生流转的线id集合
            List<String> highLightedFlowIds = getHighLightedFlows(bpmnModel, historicActivityInstances);

            // 使用默认配置获得流程图表生成器，并生成追踪图片字符流
            InputStream imageStream = processDiagramGenerator.generateDiagram(bpmnModel, "png", highLightedActivitiIds, highLightedFlowIds, "宋体", "微软雅黑", "黑体", null, 1.0);
            // 输出图片内容
            byte[] b = new byte[1024];
            int len;
            while ((len = imageStream.read(b, 0, 1024)) != -1) {
                outputStream.write(b, 0, len);
            }
            outputStream.flush();
        } catch (Exception e) {
            logger.error("processInstanceId" + processInstanceId + "生成流程图失败，原因：" + e.getMessage(), e);
        }

    }


    /**
     * 获取已经流转的线
     *
     * @param bpmnModel
     * @param historicActivityInstances
     * @return
     */
    private static List<String> getHighLightedFlows(BpmnModel bpmnModel, List<HistoricActivityInstance> historicActivityInstances) {
        // 高亮流程已发生流转的线id集合
        List<String> highLightedFlowIds = new ArrayList<>();
        // 全部活动节点
        List<FlowNode> historicActivityNodes = new ArrayList<>();
        // 已完成的历史活动节点
        List<HistoricActivityInstance> finishedActivityInstances = new ArrayList<>();

        for (HistoricActivityInstance historicActivityInstance : historicActivityInstances) {
            FlowNode flowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(historicActivityInstance.getActivityId(), true);
            historicActivityNodes.add(flowNode);
            if (historicActivityInstance.getEndTime() != null) {
                finishedActivityInstances.add(historicActivityInstance);
            }
        }

        FlowNode currentFlowNode = null;
        FlowNode targetFlowNode = null;
        // 遍历已完成的活动实例，从每个实例的outgoingFlows中找到已执行的
        for (HistoricActivityInstance currentActivityInstance : finishedActivityInstances) {
            // 获得当前活动对应的节点信息及outgoingFlows信息
            currentFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(currentActivityInstance.getActivityId(), true);
            List<SequenceFlow> sequenceFlows = currentFlowNode.getOutgoingFlows();
            /**
             * 遍历outgoingFlows并找到已已流转的 满足如下条件认为已已流转：
             * 1.当前节点是并行网关或兼容网关，则通过outgoingFlows能够在历史活动中找到的全部节点均为已流转
             * 2.当前节点是以上两种类型之外的，通过outgoingFlows查找到的时间最早的流转节点视为有效流转???
             */
            if ("parallelGateway".equals(currentActivityInstance.getActivityType()) || "inclusiveGateway".equals(currentActivityInstance.getActivityType())) {
                // 遍历历史活动节点，找到匹配流程目标节点的
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    targetFlowNode = (FlowNode) bpmnModel.getMainProcess().getFlowElement(sequenceFlow.getTargetRef(), true);
                    if (historicActivityNodes.contains(targetFlowNode)) {
                        highLightedFlowIds.add(targetFlowNode.getId());
                    }
                }
            } else {
                List<Map<String, Object>> tempMapList = new ArrayList<>();
                for (SequenceFlow sequenceFlow : sequenceFlows) {
                    // 已存在 跳过
                    if (highLightedFlowIds.contains(sequenceFlow.getId())) {
                        continue;
                    }
                    // 查找连线两端的活动节点
                    List<HistoricActivityInstance> linedActivity = new ArrayList<>();
                    for (int i = 0; i < historicActivityInstances.size(); i++) {
                        if (historicActivityInstances.get(i).getActivityId().equals(sequenceFlow.getTargetRef())) {
                            if (historicActivityInstances.get(i - 1).getActivityId().equals(sequenceFlow.getSourceRef())) {
                                linedActivity.add(historicActivityInstances.get(i - 1));
                                linedActivity.add(historicActivityInstances.get(i));
                            }
                        }
                        // 查找到了
                        if (linedActivity.size() != 0) {
                            highLightedFlowIds.add(sequenceFlow.getId());
                            break;
                        }
                    }
                }
            }

        }
        return highLightedFlowIds;
    }

    /**
     * 根据流程实例Id,获取实时流程图片 activiti5
     *
     * @param processInstanceId
     * @return
     */
/*    public static InputStream getFlowImgByInstantIdV5(String processInstanceId) {
        TaskService taskService = processEngine.getTaskService();
        RuntimeService runtimeService = processEngine.getRuntimeService();
        ProcessEngineConfiguration processEngineConfiguration = processEngine.getProcessEngineConfiguration();
        if (StringUtils.isEmpty(processInstanceId)) {
            return null;
        }
        // 获取流程图输入流
        InputStream inputStream = null;
        // 查询历史
        HistoricProcessInstance historicProcessInstance = historyService.createHistoricProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
        if (historicProcessInstance.getEndTime() != null) { // 该流程已经结束
            ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().processDefinitionId(historicProcessInstance.getProcessDefinitionId())
                .singleResult();
            inputStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), processDefinition.getDiagramResourceName());
        } else {
            // 查询当前的流程实例
            ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
            BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
            ProcessDefinitionEntity processDefinitionEntity = (ProcessDefinitionEntity) repositoryService.createProcessDefinitionQuery()
                .processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
            List<String> highLightedFlows = new ArrayList<String>();
            List<HistoricActivityInstance> historicActivityInstances = historyService.createHistoricActivityInstanceQuery().processInstanceId(processInstanceId)
                .orderByHistoricActivityInstanceStartTime().asc().list();
            List<String> historicActivityInstanceList = new ArrayList<String>();
            for (HistoricActivityInstance hai : historicActivityInstances) {
                historicActivityInstanceList.add(hai.getActivityId());
            }
            List<String> highLightedActivities = runtimeService.getActiveActivityIds(processInstanceId);
            historicActivityInstanceList.addAll(highLightedActivities);
            for (ActivityImpl activity : processDefinitionEntity.getActivities()) {
                int index = historicActivityInstanceList.indexOf(activity.getId());
                if (index >= 0 && index + 1 < historicActivityInstanceList.size()) {
                    List<PvmTransition> pvmTransitionList = activity.getOutgoingTransitions();
                    for (PvmTransition pvmTransition : pvmTransitionList) {
                        String destinationFlowId = pvmTransition.getDestination().getId();
                        if (destinationFlowId.equals(historicActivityInstanceList.get(index + 1))) {
                            highLightedFlows.add(pvmTransition.getId());
                        }
                    }
                }
            }
            ProcessDiagramGenerator diagramGenerator = processEngineConfiguration.getProcessDiagramGenerator();
            List<String> activeActivityIds = new ArrayList<String>();
            List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstanceId).list();
            for (Task task : tasks) {
                activeActivityIds.add(task.getTaskDefinitionKey());
            }
            inputStream = diagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds, highLightedFlows, "宋体", "宋体", null, null, 1.0);
        }
        return inputStream;
    }*/

}
