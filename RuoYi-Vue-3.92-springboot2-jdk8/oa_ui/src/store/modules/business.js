const state = {
  promoterDrawer: false,
  flowPermission: {
    depList: [],
    userList: [],
  },
  approverDrawer: false,
  approverConfig: {},
  copyerDrawer: false,
  copyerConfig: {},
  conditionDrawer: false, //审批设置的条件设置弹窗开启/关闭
  conditionDialog:false, // 低代码的条件设置弹窗开启/关闭
  fieldOptions:{}, // 低代码触发器的条件设置内容
  conditionsConfig: {
    conditionNodes: [],
  },
  formSettingProps: [],
  conditionsFields: [],
};
const mutations = {
  updatePromoter(status, promoterDrawer) {
    status.promoterDrawer = promoterDrawer;
  },
  updateFlowPermission(status, flowPermission) {
    status.flowPermission = flowPermission;
  },
  updateApprover(status, approverDrawer) {
    status.approverDrawer = approverDrawer;
  },
  updateApproverConfig(status, approverConfig) {
    status.approverConfig = approverConfig;
  },
  updateCopyer(status, copyerDrawer) {
    status.copyerDrawer = copyerDrawer;
  },
  updateCopyerConfig(status, copyerConfig) {
    status.copyerConfig = copyerConfig;
  },
  // 关闭审批流程的条件设置
  updateCondition(status, conditionDrawer) {
    status.conditionDrawer = conditionDrawer;
  },
  // 关闭低代码的条件设置弹窗
  updateConditionDialog(status, conditionDrawer) {
    status.conditionDialog = conditionDrawer;
  },

  uadatefieldOptions(status,fieldOptions){
  status.fieldOptions =fieldOptions
  },
  updateConditionsConfig(status, conditionsConfig) {
   
    status.conditionsConfig = conditionsConfig;
  },
  upDateFormSetting(status, props) {
    
    status.formSettingProps = props;
  },
  upDateConditionsField(status, props) {
    status.conditionsFields = props;
  },
};

const actions = {};

export default {
  state,
  mutations,
  actions,
};
