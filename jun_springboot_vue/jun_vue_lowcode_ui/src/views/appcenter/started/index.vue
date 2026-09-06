<template>
  <div class="app-container">
    <el-table
      v-loading="loading"
      :data="formList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="流程名称" align="center" prop="formName" />
      <el-table-column
        label="提交时间"
        align="center"
        prop="createTime"
        width="180"
      />
      <el-table-column label="状态" align="center" prop="status">
        <template slot-scope="scope">
          <span v-if="scope.row.status == '0'" style="margin-left: 10px">
            {{ "审批中" }}
          </span>
          <span v-if="scope.row.status == '1'" style="margin-left: 10px">
            {{ "审批通过" }}
          </span>
          <span v-if="scope.row.status == '2'" style="margin-left: 10px">
            {{ "审批不通过" }}
          </span>
        </template>
      </el-table-column>

      <el-table-column
        label="操作"
        align="center"
        class-name="small-padding fixed-width"
      >
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="historyFory(scope.row)"
            >审批详情
          </el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="checkTheSchedule(scope.row)"
            >查看进度
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total > 0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />

    <el-dialog
      :visible.sync="modelVisible"
      title="进度查询"
      width="1680px"
      append-to-body
    >
      <div style="position: relative; height: 100%">
        <iframe
          id="iframe"
          :src="modelerUrl"
          frameborder="0"
          width="100%"
          height="720px"
          scrolling="auto"
        ></iframe>
      </div>
    </el-dialog>

    <!-- 审批对话框 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      v-if="open"
      width="800px"
      append-to-body
    >
      <fm-generate-form :data="jsonData" :value="editData" ref="generateForm">
      </fm-generate-form>

      <div class="block">
        <div class="el-form-item el-form-item--medium">
          <label class="el-form-item__label" style="width: 100px"
            >审批历史：</label
          >
        </div>
        <el-timeline>
          <el-timeline-item
            v-for="historyData in historyDatas"
            :key="historyData.id"
            v-bind:timestamp="historyData.createTime"
            placement="top"
          >
            <el-card>
              <h4>{{ historyData.taskNodeName }}</h4>
              <p>
                {{ historyData.createName }}
                <span v-if="historyData.pass === '1'">同意</span>
                <span v-if="historyData.pass === '0'">拒绝</span> 了申请
              </p>
              <p>审批意见： {{ historyData.comment }}</p>
            </el-card>
          </el-timeline-item>
        </el-timeline>
      </div>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="close">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getDefinitionsByInstanceId } from "@/api/flow/definition";
import { started, history } from "@/api/flow/flow";
import { getDef } from "@/api/form/form";

export default {
  name: "Leave",
  data() {
    return {
      editData: "",
      jsonData: "",
      historyDatas: [],
      modelVisible: false,
      modelerUrl: "",
      userName: "",
      createName: "",
      businessKey: "",
      //用户信息
      user: {},
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
      // 请假表格数据
      formList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      open2: false,
      // 请假类型字典
      typeOptions: [],
      // 状态字典
      stateOptions: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        type: null,
        title: null,
        reason: null,
        leaveStartTime: null,
        leaveEndTime: null,
        instanceId: null,
        state: null,
        createBy: null,
      },
      // 表单参数
      form: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    getList() {
      this.loading = true;
      started(this.queryParams).then((response) => {
        this.formList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    close() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.businessKey = "";
      this.editData = "";
      this.jsonData = "";
      this.historyDatas = [];
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 审批详情 */
    historyFory(row) {
      this.businessKey = row.businessId;
      this.editData = row.data;
      getDef(row.formId).then((response) => {
        this.jsonData = JSON.parse(response.data.defination);
      });
      history(row.businessId).then((response) => {
        this.historyDatas = response.data;
      });
      this.open = true;
      this.title = row.formName;
    },
    /** 进度查看 */
    checkTheSchedule(row) {
      getDefinitionsByInstanceId(row.instanceId).then((response) => {
        let data = response.data;
        // this.url = '/bpmnjs/index.html?type=lookBpmn&deploymentFileUUID='+data.deploymentID+'&deploymentName='+ encodeURI(data.resourceName);
        this.modelerUrl =
          "/bpmnjs/index.html?type=lookBpmn&instanceId=" +
          row.instanceId +
          "&deploymentFileUUID=" +
          data.deploymentID +
          "&deploymentName=" +
          encodeURI(data.resourceName);
        this.modelVisible = true;
      });
    },
  },
};
</script>
