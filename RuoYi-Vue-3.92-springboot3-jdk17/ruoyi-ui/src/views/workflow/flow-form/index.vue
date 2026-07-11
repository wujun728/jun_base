<template>
  <div class="app-container custom-tabs" :class="pageType === '2' ? 'done' : ''">
    <el-tabs tab-position="top" v-model="activeName" :before-leave="checkForm" @tab-click="handleClick">
      <!--表单信息-->
      <el-tab-pane label="基本信息" name="info" v-loading="loading">
        <el-row :gutter="24">
          <el-col :span="baseSpan">
            <div class="basic-title">{{ templateConf.name }} - {{ taskName }}</div>
            <div v-if="loadCompleted" class="basic-form">
              <!-- 动态表单、业务表单使用的解析方式 -->
              <ViewForm v-if="isDetail" :formConf="formConf" :formData="valData" />
              <Parser v-else :key="parserkey" :form-conf="formConf" :val-data="valData" @submit="submitForm" @getFormConf="getFormConf" ref="parser" />
              <!-- 自定义实现的表单，需要在这里自行扩展页面 -->
            </div>
          </el-col>
          <el-col :span="6" v-if="pageType != '2'">
            <el-card class="box-card">
              <div slot="header" class="flow clearfix">
                <div class="mb20 flow-h">
                  <svg-icon icon-class="flow" class="flow-svg"></svg-icon>
                  <span class="ml5">流程审批</span>
                </div>
                <div class="flow-c">
                  <span v-if="requiredCmt" class="cmt-require mr5">*</span>
                  <span>审批意见</span>
                  <i class="el-icon-edit-outline ml2"></i>
                </div>
              </div>
              <el-input v-model="comment" type="textarea" :rows="8" placeholder="请选择或输入意见..." maxlength="500" show-word-limit></el-input>
              <div class="mt10 mb10">
                <div class="mb5 comment">
                  <span class="ml2">常用意见</span>
                  <el-button class="ml10" icon="el-icon-plus" type="text" size="mini" @click="addOpinion">添加为常用意见</el-button>
                  <el-tooltip content="将审批意见框中输入的内容，保存为常用意见，仅本人可见。" placement="top">
                    <i class="el-icon-question ml5"></i>
                  </el-tooltip>
                </div>
                <div
                  class="comment-list pointer"
                  id="suggestionList"
                  v-infinite-scroll="loadMore"
                  :infinite-scroll-disabled="loadingCmt"
                  :infinite-scroll-distance="10"
                >
                  <div v-for="item in cmtList" :key="item.id" class="suggestion-item" @click="handleOpinion(item)">
                    <span>{{ item.comment }}</span>
                    <el-button class="delete-btn" type="danger" plain size="mini" icon="el-icon-delete" circle @click.stop="delCmt(item.id)"></el-button>
                  </div>
                  <!-- 加载状态提示 -->
                  <div v-if="loadingCmt" class="loading-tip">
                    <i class="el-icon-loading"></i>
                    加载中...
                  </div>
                </div>
              </div>
              <!-- 流程操作按钮 -->
              <div class="mb10 pull-right">
                <el-button v-if="isReturn" type="primary" plain size="mini" @click="returnBtn">退回</el-button>
                <el-button v-if="isReject" type="primary" plain size="mini" @click="rejectBtn">驳回</el-button>
                <!-- 取回提交 -->
                <el-button v-if="$route.query.handleType === '5'" class="mt10 mb10" type="primary" size="mini" @click="returnCompleteBtn">提交</el-button>
                <!-- 委派提交 -->
                <el-button v-else-if="$route.query.handleType === '7'" class="mt10 mb10" type="primary" size="mini" @click="delegateCompleteBtn">提交</el-button>
                <!-- 流程提交 -->
                <el-button v-else class="mt10 mb10" type="primary" size="mini" @click="completeBtn">提交</el-button>
              </div>
            </el-card>
          </el-col>
        </el-row>
      </el-tab-pane>
      <!-- 正文 -->
      <el-tab-pane v-if="showMainText" label="正文" name="mainText">
        <FlowMainText ref="mainText" :isDetail="isDetail" @handleButton="handleStampButton" />
      </el-tab-pane>
      <!-- 附件 -->
      <el-tab-pane v-if="showAttachment" label="附件" name="attach">
        <FlowAttachment ref="atts" :attachmentConf="attachmentConf" :isDetail="isDetail" />
      </el-tab-pane>
      <!--流程流转记录-->
      <el-tab-pane label="办理过程" name="flowRecord">
        <FlowProcess ref="flowProcess" :procInsId="taskForm.procInsId" :userId="taskForm.userId" />
      </el-tab-pane>
      <!--流程图-->
      <el-tab-pane label="流程定义" name="flowDef">
        <FlowDefinition ref="flowDefView" />
      </el-tab-pane>
    </el-tabs>
    <!-- 顶部按钮 -->
    <div class="btns" v-show="showTopBtns">
      <el-button v-for="btn in buttonList" :key="btn.code" type="primary" size="mini" @click="handleButtonClick(btn.code)">{{ btn.name }}</el-button>
    </div>
    <!--审批流程 -->
    <Complete ref="completeRef" :taskForm="taskForm" :completeTitle="completeTitle" />
    <!--退回流程-->
    <Return ref="returnRef" :taskForm="taskForm" :returnTitle="returnTitle" />
    <!--驳回流程-->
    <Reject ref="rejectRef" :taskForm="taskForm" :rejectTitle="rejectTitle" />
    <!--抄送-->
    <SelectUser ref="copySelectUserRef" :taskForm="taskForm" :selectType="copySelectType" @operfunc="copyTask" />
    <!--委派-->
    <SelectUser ref="delegateSelectUserRef" :taskForm="taskForm" :selectType="delegateSelectType" @operfunc="delegateTask" />
    <!--转办-->
    <SelectUser ref="assignSelectUserRef" :taskForm="taskForm" :selectType="assignSelectType" @operfunc="assignTask" />
    <!--加签-->
    <SelectUser ref="addMultiSelectUserRef" :taskForm="taskForm" :selectType="addMultiSelectType" @operfunc="addMultiTask" />
    <!--减签 -->
    <DeleteMulti ref="deleteMultiTaskRef" :taskForm="taskForm" />
  </div>
</template>

<script>
import Parser from "@/components/parser/Parser";
import ViewForm from "./component/view-form";
import FlowDefinition from "./component/flow-definition";
import FlowProcess from "./component/flow-process";
import Complete from "./component/complete";
import Return from "./component/return";
import Reject from "./component/reject";
import FlowAttachment from "./component/flow-attachment";
import FlowMainText from "./component/flow-mainText";
import { delegateTask, claimTask, unClaimTask, returnSubmit, copyTask, assignTask, addMultiTask } from "@/api/workflow/task";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";
import { getTask, getProcessVariables, getHistoryTask } from "@/api/workflow/task";
import { addForm, updateForm, getForm, getButtons } from "@/api/workflow/form";
import { getTemplate } from "@/api/workflow/template";
import { listComments, addComment, delComment } from "@/api/workflow/comment";
import { startFlow, commonSubmit } from "@/api/workflow/process";
import { restoreSeal } from "@/api/workflow/mainText";
import SelectUser from "./component/select-user";
import DeleteMulti from "./component/delete-multi.vue";

export default {
  name: "FlowForm",
  components: {
    Parser,
    ViewForm,
    FlowDefinition,
    FlowProcess,
    Complete,
    Return,
    Reject,
    FlowAttachment,
    FlowMainText,
    SelectUser,
    DeleteMulti,
  },
  props: {},
  data() {
    return {
      loading: true,
      loadCompleted: false, // 页面是否加载完成
      activeName: "info", // tab选项
      baseSpan: 18,
      parserkey: new Date().getTime(), // 用于控制parse首次加载完成后，避免每次渲染页面时，都重建parse，导致字段值丢失
      businessId: this.$route.query.businessId, // 业务id
      title: null, // 表单标题
      templateConf: {}, // 模板配置
      attachmentConf: {}, // 附件配置
      showMainText: false, // 是否显示正文tab
      showAttachment: true, // 是否显示附件tab
      showTopBtns: true, // 是否显示顶部按钮
      taskForm: {
        comment: "", // 意见内容
        procInsId: "", // 流程实例编号
        deployId: "", // 流程部署编号
        taskId: "", // 流程任务编号
        taskDefKey: "", //流程任务定义key
        defId: "", // 流程定义编号
        targetKey: "", //退回节点key
        isDraft: this.$route.query.draft, // 是否草稿
        businessId: this.$route.query.businessId, //业务ID
        todoId: this.$route.query.todoId, //待办ID
        templateId: this.$route.query.templateId, //模板ID
        userId: this.$route.query.userId, //处理人ID
        handleType: this.$route.query.handleType, //处理类型
        variables: {}, //流程变量
      },
      pageType: this.$route.query.pageType,
      isDetail: this.$route.query.pageType === "0" ? false : true, // 是否详情页
      completeTitle: "流程提交",
      returnTitle: "退回流程",
      rejectTitle: "驳回流程",
      formConf: {}, // 默认表单字段
      valData: {}, // 表单字段值
      variablesData: {}, // 流程变量数据
      taskName: null, // 任务节点
      bizName: null, // 业务名称
      startUser: null, // 发起人信息,
      // 常用意见查询参数
      commentParams: {
        pageNum: 1,
        pageSize: 10,
      },
      updateFormMsg: true, // 更新表单时，是否显示提示信息
      total: 0, // 常用意见总数据量
      loadingCmt: false, // 意见加载状态
      noMore: false, // 是否无更多数据
      requiredCmt: true, // 审批意见是否必填，默认必填
      comment: "", // 审批意见
      // 我的常用意见
      cmtList: [
        { id: Date.now() + Math.floor(Math.random() * 100), comment: "同意。" },
        { id: Date.now() + Math.floor(Math.random() * 100), comment: "不同意。" },
      ],
      isReturn: true, //是否显示退回按钮
      isReject: true, //是否显示驳回按钮
      buttonList: [], //按钮列表
      canStamp: true, // 能否盖章（控制已盖章的文件，必须还原印章才能再次盖章）
      copySelectType: "multiple", // 抄送选人方式
      delegateSelectType: "single", // 委派选人方式
      assignSelectType: "single", // 转办选人方式
      addMultiSelectType: "single", // 加签选人方式
      selectUserScope: "dept", // 选人范围，dept-部门级（当前操作人所属部门），corp-公司级
    };
  },
  watch: {
    pageType: {
      handler(val) {
        if (val && val === "2") {
          this.baseSpan = 24;
        }
      },
      immediate: true, // 立即生效
    },
  },
  mounted() {
    this.loadMore(); // 加载常用意见数据
  },
  created() {
    // pageType: 0-新启，1-审批， 2-查看
    if (this.$route.query) {
      this.getFormData(this.businessId);
      if (this.pageType === "2") {
        this.getHistoryFlowTask(this.$route.query.taskId);
        this.loadCompleted = true;
      } else if (this.pageType === "0" && !this.$route.query.todoId) {
        this.requiredCmt = false;
        this.startFlow();
        this.loadCompleted = true;
      } else {
        if (this.taskForm.isDraft && this.taskForm.isDraft === "1") {
          this.requiredCmt = false;
        }
        this.getFlowTask(this.$route.query.taskId);
      }
    }
  },
  methods: {
    /** 启动流程 */
    async startFlow() {
      if (this.businessId) return;
      let data = {
        templateId: this.taskForm.templateId,
      };
      // 启动流程，新增最近使用模板记录
      startFlow(data).then((res) => {
        this.taskName = res.data.taskName;
        this.taskForm.taskId = res.data.taskId;
        this.taskForm.taskDefKey = res.data.taskDefKey;
        this.taskForm.defId = res.data.procDefId;
        this.taskForm.deployId = res.data.deployId;
        this.taskForm.procInsId = res.data.procInsId;
        this.taskForm.executionId = res.data.executionId;
        this.getButtons();
        // 流程任务获取变量信息
        this.processVariables(this.taskForm.taskId);
      });
    },
    /** 按钮方法 */
    handleButtonClick(btnCode) {
      switch (btnCode) {
        case "save":
          this.saveForm(true);
          break;
        case "sign":
          this.handleClaim();
          break;
        case "unsigned":
          this.handleUnClaim();
          break;
        case "stamp":
          this.stamp();
          break;
        case "restoreStamp":
          this.restoreStamp();
          break;
        case "copy":
          this.handleCopyTask();
          break;
        case "delegate":
          this.handleDelegateTask();
          break;
        case "assign":
          this.handleAssignTask();
          break;
        case "addMulti":
          this.handleAddMultiTask();
          break;
        case "deleteMulti":
          this.handleDeleteMultiTask();
          break;
      }
    },
    /** 查询业务按钮 */
    async getButtons() {
      let params = {
        taskId: this.taskForm.taskId,
        procInsId: this.taskForm.procInsId,
        pageType: this.pageType,
      };
      getButtons(params).then((res) => {
        this.buttonList = res.data;
      });
    },
    /** 获取历史任务 */
    getHistoryFlowTask(taskId) {
      getHistoryTask({ taskId }).then((res) => {
        if (res.code == 200 && res.data) {
          this.taskName = res.data.taskName;
          this.taskForm.taskDefKey = res.data.taskDefKey;
          this.taskForm.defId = res.data.procDefId;
          this.taskForm.deployId = res.data.deployId;
          this.taskForm.procInsId = res.data.procInsId;
        }
      });
    },
    /** 获取任务 */
    async getFlowTask(taskId) {
      getTask({ taskId }).then((res) => {
        if (res.code == 200 && res.data) {
          this.taskName = res.data.taskName;
          this.taskForm.taskId = taskId;
          this.taskForm.taskDefKey = res.data.taskDefKey;
          this.taskForm.defId = res.data.procDefId;
          this.taskForm.deployId = res.data.deployId;
          this.taskForm.procInsId = res.data.procInsId;
          this.taskForm.executionId = res.data.executionId;
          this.getButtons();
        }
      });
      // 流程任务获取变量信息
      await this.processVariables(this.$route.query.taskId);
    },
    /** tab切换时，校验表单是否保存 */
    checkForm(name, oldname) {
      if (["mainText", "attach"].includes(name) && !this.businessId) {
        this.$modal.msgError("请先保存！");
        return false;
      }
      let flag = false;
      switch (name) {
        case "info": // 基本信息
          flag = true;
          break;
        case "mainText": // 正文
          flag = true;
          this.$refs.mainText.getMainText(this.businessId, this.taskForm.templateId);
          break;
        case "attach": // 附件
          this.$refs.atts.getAttachs(this.businessId);
          break;
        case "flowRecord": // 办理过程-默认加载意见
          this.$refs.flowProcess.initCmtData();
          break;
        case "flowDef": // 流程定义
          this.$refs.flowDefView.getFlowXmlAndNode(this.taskForm.procInsId, this.taskForm.deployId);
          break;
      }
      this.activeName = name;
      this.showTopBtns = flag;
    },
    /** 点击页签事件 */
    handleClick(tab, event) {
      // do something...
    },
    /** 获取流程变量内容 */
    async processVariables(taskId) {
      if (taskId) {
        const res = await getProcessVariables(taskId);
        if (res.code === 200) {
          this.variablesData = res.data || {};
          if (Object.keys(this.variablesData).length > 0) {
            //控制退回按钮显示
            if (this.pageType === "0" || (this.pageType === "1" && this.variablesData.returnBtn && this.variablesData.returnBtn === "0")) {
              this.isReturn = false;
            }
            //控制驳回按钮显示
            if (this.pageType === "0" || (this.pageType === "1" && this.variablesData.rejectBtn && this.variablesData.rejectBtn === "0")) {
              this.isReject = false;
            }
            //保存按钮，如果不显示保存按钮，则不能编辑表单
            if (this.variablesData.save && this.variablesData.save === "1") {
              this.isDetail = false;
            }
            // 选人范围控制
            if (this.variablesData.selectRange) {
              this.selectUserScope = this.variablesData.selectRange;
            }
          }
          this.loadCompleted = true;
        }
      }
    },
    /** 流程表单数据 */
    getFormData(formId) {
      // 获取模板配置
      getTemplate(this.taskForm.templateId).then((res) => {
        if (res.code === 200 && res.data) {
          this.templateConf = res.data;
          this.showAttachment = res.data.attachFlag === "1" ? true : false;
          this.attachmentConf = res.data.attachment;
          this.showMainText = res.data.mainTextFlag === "1" ? true : false;
        }
      });
      let params = {
        bizId: formId,
        templateId: this.taskForm.templateId,
      };
      getForm(params).then((res) => {
        if (res.code == 200 && res.data) {
          let formObj = JSON.parse(res.data.formData);
          this.formConf = formObj.formData;
          this.valData = formObj.valData;
          if (this.pageType == "2" || (this.variablesData.save && this.variablesData.save === "0")) {
            this.formConf.formBtns = false;
            this.formConf.disabled = true;
          }
          this.parserkey = new Date().getTime();
          if (this.pageType !== "0") {
            this.title = this.getTitle(formObj);
          }
          this.loading = false;
        }
      });
    },
    /** 保存或更新流程表单数据 */
    async submitForm(formData) { 
      this.loading = true;
      const title = this.getTitle(formData);
      this.taskForm.title = title;
      let data = {
        id: this.businessId,
        title: title,
        todoId: this.taskForm.todoId,
        taskId: this.taskForm.taskId,
        templateId: this.taskForm.templateId,
        procInstId: this.taskForm.procInsId,
        formData: JSON.stringify(formData),
      };
      let formParams = {
        bizId: this.businessId,
        templateId: this.taskForm.templateId,
        formData: data,
      }; 
      if (this.businessId) {
        const result = await updateForm(formParams) 
        if (result.code === 200 && this.updateFormMsg) {
          this.$modal.msgSuccess("保存成功");
        }
        this.loading = false;  
      } else {
        const result = await addForm(formParams) 
        if (result.code == 200 && result.data) {
          this.businessId = result.data;
          this.taskForm.businessId = this.businessId;
          this.$set(this.$route.query, "businessId", this.businessId);
          if (this.updateFormMsg) {
            this.$modal.msgSuccess("保存成功");
          }
          this.loading = false;  
        } 
      }
    },
    /** 获取表单标题 */
    getTitle(form) {
      let title = "";
      form.formData.fields.some((field) => {
        if (field.__config__) {
          let __config__ = field.__config__;
          if (__config__.label.indexOf("标题") != -1 || field.__vModel__.indexOf("title") != -1) {
            title = form.valData[field.__vModel__];
            return true;
          }
        }
      });
      return title;
    },
    /** 获取表单字段数据 */
    getFormConf(formConf) {
      this.formConf = formConf;
    },
    /** 返回页面 */
    goBack() {
      // 关闭当前标签页并返回上个页面
      const obj = { path: "/my/todo", query: { t: Date.now() } };
      this.$tab.closeOpenPage(obj);
    },
    /** 认领/签收 */
    handleClaim() {
      this.$confirm("确定签收吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          claimTask(this.taskForm).then((res) => {
            this.$modal.msgSuccess(res.msg);
          });
        })
        .catch(() => {});
    },
    /** 取消认领/签收 */
    handleUnClaim() {
      this.$confirm("确定取消签收吗？", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(() => {
          unClaimTask(this.taskForm).then((res) => {
            this.$modal.msgSuccess(res.msg);
          });
        })
        .catch(() => {});
    },
    /** 转办 */
    handleAssignTask() {
      this.$refs.assignSelectUserRef.selectUserOpen = true;
    },
    /** 执行转办 */
    assignTask(taskForm) {
      assignTask(taskForm).then((res) => {
        if (res.code === 200) {
          this.$modal.msgSuccess("转办成功！");
          this.goBack();
        }
      });
    },
    /** 驳回 */
    rejectBtn() {
      this.taskForm.comment = this.comment;
      this.$refs.rejectRef.rejectOpen = true;
    },
    /** 退回 */
    returnBtn() {
      this.taskForm.comment = this.comment;
      this.$refs.returnRef.handleReturn();
    },
    /** 委派 */
    handleDelegateTask() {
      this.$refs.delegateSelectUserRef.selectUserOpen = true;
    },
    /** 执行委派 */
    delegateTask(taskForm) {
      delegateTask(taskForm).then((res) => {
        if (res.code === 200) {
          this.$modal.msgSuccess("委派成功！");
          this.goBack();
        }
      });
    },
    /** 抄送 */
    handleCopyTask() {
      this.$refs.copySelectUserRef.selectUserOpen = true;
    },
    /** 执行抄送 */
    copyTask(taskForm) {
      copyTask(taskForm).then((res) => {
        if (res.code === 200) {
          this.$modal.msgSuccess("抄送成功！");
        }
      });
    },
    // 加签
    handleAddMultiTask() {
      this.$refs.addMultiSelectUserRef.selectUserOpen = true;
    },
    // 执行加签
    addMultiTask(taskForm) {
      addMultiTask(taskForm).then((res) => {
        if (res.code === 200) {
          this.$modal.msgSuccess("加签成功！");
        }
      });
    },
    // 减签
    handleDeleteMultiTask() {
      this.$refs.deleteMultiTaskRef.init();
    },
    /** 保存表单 */
    async saveForm(flag) { 
      if (this.isDetail) return;
      this.updateFormMsg = flag;
      this.$refs.parser.submitForm();
    },
    /** 常用意见-加载更多数据 */
    async loadMore() {
      if (this.loadingCmt || this.noMore) return;
      try {
        this.loadingCmt = true;
        const { rows, total } = await listComments(this.commentParams);
        if (rows.length === 0) {
          this.noMore = true;
          return;
        }
        this.cmtList = [...this.cmtList, ...rows];
        this.total = total;
        this.commentParams.pageNum++;
        // 判断是否还有更多数据
        if (this.cmtList.length >= total) {
          this.noMore = true;
        }
      } catch (err) {
        this.$message.error("加载常用意见失败");
      } finally {
        this.loadingCmt = false;
      }
    },
    /** 保存为常用意见 */
    async addOpinion() {
      if (!this.comment.trim()) {
        return;
      }
      const { data } = await addComment({ comment: this.comment });
      this.cmtList.push({
        id: data,
        comment: this.comment,
      });
    },
    /** 添加到审批意见 */
    handleOpinion(row) {
      this.comment += row.comment;
    },
    /** 删除常用意见 */
    async delCmt(id) {
      try {
        await this.$confirm("确定删除此常用意见吗？", "提示", { type: "warning" });
        await delComment(id);
        this.cmtList = this.cmtList.filter((item) => item.id !== id);
        this.$message.success("删除成功");
      } catch (err) {
        if (err !== "cancel") {
          this.$message.error("删除失败");
        }
      }
    },
    /** 提交 */
    async completeBtn() {  
      if (!this.isDetail) {
        const isValid = await this.$refs.parser.refValidateForm();
        if (!isValid) {
          this.$message.error("请先填写表单");
          return;
        }
      }
      if (this.requiredCmt && (!this.comment || this.comment.trim() === "")) {
        this.$message.error("请填写审批意见");
        return;
      }
      this.updateFormMsg = false; 
      if (!this.isDetail) { 
        const formModel = this.$refs.parser[this.$refs.parser.formConf.formModel];
        const formConfCopy = this.$refs.parser.formConfCopy;
        const formData = {
          formData: formConfCopy,
          valData: formModel,
        }; 
        await this.submitForm(formData); 
      } 
      this.taskForm.comment = this.comment;
      this.$refs.completeRef.initFlowNode();
    },
    /** 取回提交 */
    async returnCompleteBtn() {
      if (!this.businessId) {
        const isValid = await this.$refs.parser.refValidateForm();
        if (!isValid) {
          this.$message.error("请先填写表单");
          return;
        }
      }
      if (this.requiredCmt && (!this.comment || this.comment.trim() === "")) {
        this.$message.error("请填写审批意见");
        return;
      }
      this.saveForm(false);
      this.taskForm.comment = this.requiredCmt ? this.comment : "";
      returnSubmit(this.taskForm).then((res) => {
        if (res.code === 200) {
          this.$message.success("提交成功！");
          this.goBack();
        }
      });
    },
    // 委派提交
    async delegateCompleteBtn() {
      if (!this.businessId) {
        const isValid = await this.$refs.parser.refValidateForm();
        if (!isValid) {
          this.$message.error("请先填写表单");
          return;
        }
      }
      if (this.requiredCmt && (!this.comment || this.comment.trim() === "")) {
        this.$message.error("请填写审批意见");
        return;
      }
      this.saveForm(false);
      this.taskForm.comment = this.requiredCmt ? this.comment : "";
      let submitParams = {
        operateType: "200", //操作类型，提交
        flowTask: this.taskForm,
      };
      commonSubmit(submitParams).then((response) => {
        this.$message.success(response.msg);
        this.goBack();
      });
    },
    /** 盖章操作 */
    stamp() {
      if (this.activeName !== "mainText") {
        this.$message.error("请切换到正文再盖章！");
        return;
      }
      if (!this.canStamp) {
        this.$message.error("已盖章，请先还原印章！");
        return;
      }
      this.$refs.mainText.showStamp(this.title);
    },
    /** 还原印章 */
    restoreStamp() {
      restoreSeal(this.businessId).then((res) => {
        if (res.code === 200) {
          this.$message.success("操作成功！");
          this.canStamp = true;
          this.buttonList = this.buttonList.filter((item) => item.code !== "restoreStamp");
          this.$refs.mainText.getMainText(this.businessId, this.taskForm.templateId);
        }
      });
    },
    /** 处理还原印章按钮 */
    handleStampButton() {
      if (this.isDetail) return;
      this.canStamp = false;
      const isExist = this.buttonList.some((item) => item.code === "restoreStamp");
      if (!isExist) {
        this.buttonList.push({
          code: "restoreStamp",
          name: "还原印章",
        });
      }
    },
  },
};
</script>

<style lang="scss" scoped>
::v-deep .el-tabs__item {
  font-size: 15px;
  color: #303133;
}
::v-deep .el-tabs__nav-wrap::after {
  height: 1px;
}

::v-deep .el-tabs__item.is-active {
  font-size: 16px;
  font-weight: 700;
  color: #303133;
}

// 表单样式调整
::v-deep .basic-form {
  .el-form-item {
    margin-bottom: 24px;
  }
}

::v-deep .el-input--small {
  font-size: 14px;
}

.basic-title {
  text-align: center;
  margin-top: 12px;
  margin-bottom: 28px;
  font-size: 22px;
  font-weight: 600;
}

.flow {
  font-weight: 600;
}

.flow-h {
  display: flex;
  align-items: center;
  background-color: #f3f9fe;

  span:nth-of-type(1) {
    color: rgb(104, 104, 104);
  }
}

.flow-c {
  font-size: 14px;
  .cmt-require {
    color: red;
  }
}

.comment {
  font-size: 14px;
  color: #909399;
}
.comment-list {
  font-size: 14px;
  height: 180px;
  overflow-y: auto;
  border: 1px solid #dcdfe6;
  background: #ffffff none;
  border-radius: 4px;
  line-height: 36px;
}

.flow-svg {
  width: 40px;
  height: 40px;
}
.flow-title {
  color: #909399;
}

.basic-form {
  padding-top: 12px;
  padding-left: 12px;
}

.clearfix:before,
.clearfix:after {
  display: table;
  content: "";
}

.clearfix:after {
  clear: both;
}

.custom-tabs {
  position: relative;
  padding-bottom: 0px;
}

.done {
  padding-right: 40px;
}

.box-card {
  width: 100%;
  margin-bottom: 20px;
}

.suggestion-item:hover {
  background-color: #f3f9fe;
  cursor: pointer;
}
.suggestion-item:active {
  background-color: #f3f9fe;
}

.suggestion-item:hover .delete-btn {
  display: block;
  opacity: 1;
}

.suggestion-item {
  display: flex;
  align-items: flex-start; /* 顶部对齐 */
  min-height: 30px; /* 最小高度保证统一性 */
  padding: 6px 6px 6px 16px; /* 调整右侧留白 */
  border-radius: 1px;
  // background: #f8f9fa;
  transition: all 0.3s ease;
  position: relative;
  border-bottom: 1px solid #ececec;
  line-height: 1.6; /* 增加行高提升可读性 */
  // color: rgb(150, 150, 150);
  color: #909399;
}

/* 文字内容区域 */
.suggestion-item span {
  flex: 1;
  word-break: break-word; /* 允许长单词换行 */
  overflow-wrap: anywhere; /* 更智能的换行策略 */
  display: -webkit-box;
  -webkit-line-clamp: 2; /* 最多显示2行 */
  -webkit-box-orient: vertical;
  overflow: hidden;
  text-overflow: ellipsis;
  padding-right: 12px; /* 文字与按钮间距 */
}
.delete-btn {
  position: absolute;
  right: 6px;
  top: 50%;
  transform: translateY(-50%);
  display: none;
  border: none;
  padding: 4px 4px;
  border-radius: 20px;
  cursor: pointer;
  transition: opacity 0.3s;
  opacity: 0;
  flex-shrink: 0;
}

/* 鼠标悬停展开全文 */
// .suggestion-item:hover span {
//   -webkit-line-clamp: unset;
//   overflow: visible;
// }

.btns {
  position: absolute;
  right: 20px;
  top: 24px;
}

.loading-tip {
  text-align: center;
  padding: 10px;
  color: #909399;
  font-size: 12px;
}
</style>
