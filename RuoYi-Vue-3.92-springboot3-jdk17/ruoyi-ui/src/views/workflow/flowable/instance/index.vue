<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="流程实例" prop="procInstId">
        <el-input v-model="queryParams.procInstId" placeholder="请输入流程实例" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="流程名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入实例名称" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="业务ID" prop="businessKey">
        <el-input v-model="queryParams.businessKey" placeholder="请输入业务ID" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />
    <el-table v-loading="loading" :data="expressionList" @selection-change="handleSelectionChange">
      <el-table-column label="序号" type="index" align="center" width="55">
        <template slot-scope="scope">
          <span>{{(queryParams.pageNum - 1) * queryParams.pageSize + scope.$index + 1}}</span>
        </template>
      </el-table-column>
      <el-table-column label="业务标题" align="left" prop="title" />
      <el-table-column label="业务ID" align="left" prop="businessKey" width="280" show-overflow-tooltip />
      <el-table-column label="流程实例" align="left" prop="processInstanceId" width="100" show-overflow-tooltip />
      <el-table-column label="流程名称" align="left" prop="name" width="200" show-overflow-tooltip />
      <el-table-column label="当前节点" align="left" prop="currentTask" width="120" show-overflow-tooltip />
      <el-table-column label="当前处理人" align="left" prop="assignee" width="100" show-overflow-tooltip />
      <el-table-column label="状态" align="center" prop="suspended" width="90">
        <template slot-scope="scope">
          <el-tag type="success" v-if="!scope.row.suspended">激活</el-tag>
          <el-tag type="warning" v-if="scope.row.suspended">挂起</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="开始时间" align="center" prop="startTime" width="160" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="280">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-s-operation" @click="showProcess(scope.row)">流程进度</el-button>
          <el-button size="mini" type="text" icon="el-icon-s-promotion" @click="handleJump(scope.row)">跳转</el-button>
          <el-button size="mini" type="text" icon="el-icon-remove-outline" @click="handleTerminate(scope.row)">终止</el-button>
          <el-button size="mini" type="text" icon="el-icon-refresh-right" @click="handleReturnTask(scope.row)">取回</el-button>
          <el-button v-if="!scope.row.suspended" size="mini" type="text" icon="el-icon-video-pause" @click="handleEnable(false, scope.row)">挂起</el-button>
          <el-button v-if="scope.row.suspended" size="mini" type="text" icon="el-icon-video-play" @click="handleEnable(true, scope.row)">唤醒</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 流程图 -->
    <el-dialog :title="readImage.title" :visible.sync="readImage.open" width="70%" append-to-body @opened="handleDialogOpened">
      <flow-view ref="flowView" />
    </el-dialog>

    <!-- 跳转环节 -->
    <el-dialog :title="jumpTitle" :visible.sync="jumpOpen" width="1000" append-to-body>
      <el-divider />
      <el-form ref="jumpActivityForm" :model="jumpActivityForm" label-width="80px" label-position="top">
        <el-form-item label="请选择跳转节点：" prop="targetKey">
          <el-radio-group v-model="jumpActivityForm.targetKey">
            <el-radio
              v-for="item in jumpActivityList"
              :key="item.id"
              :label="item.id"
              :disabled="disableJumpActivity.includes(item.id)"
            >{{ item.name }}</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
        <el-button @click="jumpOpen = false">取 消</el-button>
        <el-button type="primary" @click="selectActivityUser">确 定</el-button>
      </span>
    </el-dialog>

    <!--跳转环节选人 -->
    <jump-activity-user ref="jumpActivityUserRef" :taskForm="taskForm" :completeTitle="selectAssigneeTitle" @jumpSuccessOper="goBack" />

    <!--当前环节任务列表 -->
    <el-dialog :title="currentTaskTitle" :visible.sync="currentTaskOpen" width="52%" append-to-body>
      <el-table
        v-loading="loading"
        ref="currentTaskTable"
        element-loading-text="正在加载中..."
        element-loading-spinner="el-icon-loading"
        :data="currentTaskList"
        row-key="taskId"
        key="currentTaskTable"
        @selection-change="handleSelectTaskChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="流程环节" align="left" prop="taskName" width="300" show-overflow-tooltip />
        <el-table-column label="部门" align="left" prop="assigneeDeptName" width="200" show-overflow-tooltip />
        <el-table-column label="办理人" align="left" prop="assigneeName" width="140" />
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="currentTaskOpen = false">取 消</el-button>
        <el-button type="primary" @click="terminateTask">确 定</el-button>
      </span>
    </el-dialog>

    <!--已完成环节任务列表 -->
    <el-dialog :title="returnTaskTitle" :visible.sync="returnTaskOpen" width="63%" append-to-body>
      <el-table
        v-loading="loading"
        ref="returnTaskTable"
        element-loading-text="正在加载中..."
        element-loading-spinner="el-icon-loading"
        :data="returnTaskList"
        row-key="taskId"
        key="returnTaskTable"
        @selection-change="handleSelectFinishTaskChange"
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="流程环节" align="left" prop="taskName" width="300" show-overflow-tooltip />
        <el-table-column label="部门" align="left" prop="deptName" width="200" show-overflow-tooltip />
        <el-table-column label="办理人" align="left" prop="assigneeName" width="140" />
        <el-table-column label="办理时间" align="left" prop="finishTime" width="160">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.finishTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
          </template>
        </el-table-column>
      </el-table>
      <span slot="footer" class="dialog-footer">
        <el-button @click="returnTaskOpen = false">取 消</el-button>
        <el-button type="primary" @click="returnTask">确 定</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import FlowView from "@/views/workflow/flowable/definition/flow-view";
import { getListProcess, enableProcess, getJumpActivityList, getFlowNodeTasks, getFinishFlowNodeTasks } from "@/api/workflow/flowable/monitor";
import { terminateProcess, returnFinishTask } from "@/api/workflow/process";
import JumpActivityUser from "./jump-activity-user.vue";

export default {
  name: "instance",
  components: {
    FlowView,
    JumpActivityUser,
  },
  dicts: ["sys_common_status"],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 流程实例表格数据
      expressionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        procInstId: null,
        name: null,
        businessKey: null,
      },
      // 流程图
      readImage: {
        open: false,
        src: "",
      },
      //跳转标题
      jumpTitle: "流程跳转",
      //是否显示跳转弹框
      jumpOpen: false,
      //跳转环节form
      jumpActivityForm: {
        targetKey: "",
      },
      // 不可跳转的节点
      disableJumpActivity: [],
      //跳转环节列表
      jumpActivityList: [],
      //流程当前环节key
      currentTaskDefKeys: "",
      //选择处理人弹框标题
      selectAssigneeTitle: "选择处理人",
      //跳转提交参数
      taskForm: {
        procInsId: "", //流程实例ID
        defId: "", //流程定义ID
        deployId: "", // 流程部署ID
        taskId: "", //流程任务编号
        taskDefKey: "", //流程任务定义key
        targetKey: "", //跳转环节key
        variables: {}, //流程变量
      },
      //当前环节的任务列表
      currentTaskList: [],
      currentTaskTitle: "选择任务",
      currentTaskOpen: false,
      terminateTaskIds: [],

      //已完成环节任务列表
      returnTaskList: [],
      returnTaskTitle: "选择任务",
      returnTaskOpen: false,
      retrunTaskIds: [],
      returnSelection: [],
      processInstanceId: "",
      deployId: "",
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询流程实例列表 */
    getList() {
      this.loading = true;
      getListProcess(this.queryParams)
        .then((response) => {
          this.expressionList = response.rows;
          this.total = response.total;
          this.loading = false;
          this.$refs.flowView.resetFlowData();
        })
        .catch((e) => {
          this.loading = false;
        });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    //唤醒/挂起
    handleEnable(enable, row) {
      let msgText = enable == false ? "确认挂起此流程吗？" : "确认唤醒此流程吗？";
      this.$confirm(msgText, "提示", {
        confirmButtonText: "确 认",
        cancelButtonText: "取 消",
      })
        .then(() => {
          const { processInstanceId } = row;
          enableProcess(processInstanceId, enable).then((res) => {
            this.$message.success("操作成功！");
            this.getList();
          });
        })
        .catch(() => {});
    },
    handleDialogOpened() {
      this.$nextTick(() => {
        // 确保DOM更新完成
        this.$refs.flowView.getFlowXmlAndNode(this.processInstanceId, this.deployId);
      });
    },
    //查看流程图
    showProcess(row) {
      if (this.processInstanceId != row.processInstanceId && this.$refs.flowView) {
        this.$refs.flowView.resetFlowData();
      }
      this.processInstanceId = row.processInstanceId;
      this.deployId = row.deployId;
      this.readImage.title = "流程图";
      this.readImage.open = true;
    },
    //查询流程所有环节
    handleJump(row) {
      //重置
      this.jumpActivityList = [];
      this.jumpActivityForm.targetKey = "";
      this.resetTaskForm();

      //查询流程所有环节
      this.jumpOpen = true;
      const { processInstanceId, deployId, procDefId, currentTaskIds, currentTaskDefKeys } = row;
      this.disableJumpActivity = currentTaskDefKeys.trim().split(/\s*,\s*/);
      this.taskForm.procInsId = processInstanceId;
      this.taskForm.deployId = deployId;
      this.taskForm.taskId = currentTaskIds;
      this.taskForm.taskDefKey = currentTaskDefKeys;
      this.taskForm.defId = procDefId;
      getJumpActivityList(this.taskForm).then((res) => {
        this.jumpActivityList = res;
        this.jumpActivityForm.targetKey = currentTaskDefKeys;
        this.currentTaskDefKeys = currentTaskDefKeys;
      });
    },
    //动态选择跳转环节处理人
    selectActivityUser() {
      //如果是当前环节，提示用户重新选择
      if (this.jumpActivityForm.targetKey === this.currentTaskDefKeys) {
        this.$message.error("不能跳转到当前节点，请重新选择!");
        return;
      }
      this.taskForm.targetKey = this.jumpActivityForm.targetKey;
      this.$refs.jumpActivityUserRef.initFlowNode();
    },
    //重置跳转提交参数
    resetTaskForm() {
      this.taskForm = {
        procInsId: "", //流程实例ID
        defId: "", //流程定义ID
        deployId: "", // 流程部署ID
        taskId: "", //流程任务编号
        taskDefKey: "", //流程任务定义key
        targetKey: "", //跳转环节key
        variables: {}, //流程变量
      };
    },
    //跳转成功后操作
    goBack() {
      this.jumpOpen = false;
      this.getList();
    },
    //终止
    handleTerminate(row) {
      this.terminateTaskIds = [];
      const { processInstanceId, currentTaskIds } = row;
      //查询当前环节的任务
      let params = {
        procInsId: processInstanceId,
        taskId: currentTaskIds,
      };
      getFlowNodeTasks(params).then((res) => {
        if (res.data.length > 1) {
          this.currentTaskList = res.data;
          this.currentTaskOpen = true;
        } else {
          this.$confirm("终止后无法恢复，确认终止流程吗？", "提示", {
            confirmButtonText: "确 认",
            cancelButtonText: "取 消",
          })
            .then(() => {
              let data = {
                procInsId: processInstanceId,
              };
              //直接终止
              terminateProcess(data).then((res) => {
                if (res.code === 200) {
                  this.$message.success("操作成功！");
                  this.getList();
                }
              });
            })
            .catch(() => {});
        }
      });
    },
    //选择终止任务
    handleSelectTaskChange(selection) {
      this.terminateTaskIds = selection.map((item) => item.taskId);
    },
    //终止任务
    terminateTask() {
      this.$confirm("终止后无法恢复，确认终止任务吗？", "提示", {
        confirmButtonText: "确 认",
        cancelButtonText: "取 消",
      })
        .then(() => {
          let data = {
            taskId: this.terminateTaskIds.join(","),
          };
          //直接终止
          terminateProcess(data).then((res) => {
            if (res.code === 200) {
              this.currentTaskOpen = false;
              this.$message.success("操作成功！");
              this.getList();
            }
          });
        })
        .catch(() => {});
    },
    //选择取回任务
    handleSelectFinishTaskChange(selection) {
      this.returnTaskIds = selection.map((item) => item.taskId);
      this.returnSelection = selection;
    },
    //取回已完成的任务
    handleReturnTask(row) {
      this.returnTaskIds = [];
      this.returnSelection = [];
      const { processInstanceId, returnTaskIds } = row;
      //查询当前环节的任务
      let params = {
        procInsId: processInstanceId,
        taskId: returnTaskIds,
      };
      getFinishFlowNodeTasks(params).then((res) => {
        if (res.data.length > 1) {
          this.returnTaskList = res.data;
          this.returnTaskOpen = true;
        } else {
          this.$confirm("取回后无法恢复，确认取回吗？", "提示", {
            confirmButtonText: "确 认",
            cancelButtonText: "取 消",
          })
            .then(() => {
              let data = {
                procInsId: processInstanceId,
              };
              //直接终止
              returnFinishTask(data).then((res) => {
                if (res.code === 200) {
                  this.returnTaskOpen = false;
                  this.$message.success("操作成功！");
                  this.getList();
                }
              });
            })
            .catch(() => {});
        }
      });
    },
    //取回任务
    returnTask() {
      if (!this.returnTaskIds || this.returnTaskIds.length === 0) {
        this.$message.error("请选择取回的任务！");
        return;
      }
      for (const item of this.returnSelection) {
        if (!item.finishTime) {
          this.$message.error("请选择已完成的任务！");
          return;
        }
      }
      this.$confirm("确认取回任务吗？", "提示", {
        confirmButtonText: "确 认",
        cancelButtonText: "取 消",
      })
        .then(() => {
          let data = {
            taskId: this.returnTaskIds.join(","),
          };
          //直接取回
          returnFinishTask(data).then((res) => {
            if (res.code === 200) {
              this.returnTaskOpen = false;
              this.$message.success("操作成功！");
              this.getList();
            }
          });
        })
        .catch(() => {});
    },
  },
};
</script>
