<template>
  <div class="app-container">
    <el-table v-loading="loading" :data="tastList">
      <el-table-column label="任务id" align="center" prop="id" v-if="false" />
      <el-table-column label="流程名称" align="center" prop="instanceName" />
      <el-table-column label="任务节点名称" align="center" prop="name" />
      <el-table-column label="任务状态" align="center" prop="status" />
      <el-table-column label="办理人" align="center" prop="assignee" />
      <el-table-column label="创建时间" align="center" prop="createdDate" />
      <el-table-column
        label="表单定义"
        align="center"
        prop="formDef"
        v-if="false"
      />
      <el-table-column
        label="业务key"
        align="center"
        prop="businessKey"
        v-if="false"
      />
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
            @click="examineAndApprove(scope.row)"
            >审批
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

      <el-form
        :model="form"
        ref="form"
        label-width="100px"
        class="demo-dynamic"
      >
        <el-form-item label="审批结论">
          <el-radio-group v-model="form.pass">
            <el-radio label="1">同意</el-radio>
            <el-radio label="0">不同意</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="审批意见">
          <el-input type="textarea" v-model="form.comment"></el-input>
        </el-form-item>
      </el-form>

      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="close">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listTask, formDataShow, formDataSave } from "@/api/flow/task";
import request from "@/utils/request";
import { formData, history, exec } from "@/api/flow/flow";

export default {
  name: "Todo",
  data() {
    return {
      jsonData: {},
      editData: {},
      historyDatas: [],
      taskID: "",
      taskNodeName: "",
      definitionKey: "",
      businessKey: "",
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
      tastList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      // 表单参数
      form: {
        comment: "",
        pass: "",
      },
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询列表 */
    getList() {
      this.loading = true;
      listTask(this.queryParams).then((response) => {
        this.tastList = response.rows;
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
      this.definitionKey = "";
      this.businessKey = "";
      this.jsonData = {};
      this.editData = {};
      this.taskID = "";
      this.taskNodeName = "";
      this.historyDatas = [];
      this.title = "";
      this.form.comment = "";
      this.form.pass = "";
    },

    /** 审批 */
    examineAndApprove(row) {
      this.definitionKey = row.definitionKey;
      this.businessKey = row.businessKey;
      this.taskID = row.id;
      this.taskNodeName = row.name;
      this.jsonData = JSON.parse(row.formDef);
      formData(row.businessKey).then((response) => {
        this.editData = response.data;
      });
      // request({
      //   url: "/my-act/data/" + row.businessKey,
      //   method: "get",
      // }).then((response) => {
      //   this.editData = response.data;
      // });
      history(row.businessKey).then((response) => {
        this.historyDatas = response.data;
      });
      // request({
      //   url: "/my-act/history/" + row.businessKey,
      //   method: "get",
      // }).then((response) => {
      //   this.historyDatas = response.data;
      // });
      this.open = true;
      this.title = row.instanceName;
    },
    /** 提交 */
    submitForm() {
      let data = {
        taskID: this.taskID,
        pass: this.form.pass,
        comment: this.form.comment,
        businessKey: this.businessKey,
        taskNodeName: this.taskNodeName,
      };
      exec(data).then((response) => {
        this.$modal.msgSuccess("提交成功");
        this.close();
        this.getList();
      });
      // request({
      //   url: "/my-act/exec",
      //   method: "post",
      //   data: {
      //     taskID: this.taskID,
      //     pass: this.form.pass,
      //     comment: this.form.comment,
      //     businessKey: this.businessKey,
      //     taskNodeName: this.taskNodeName,
      //   },
      // }).then((response) => {
      //   this.$modal.msgSuccess("提交成功");
      //   this.close();
      //   this.getList();
      // });
    },
  },
};
</script>
