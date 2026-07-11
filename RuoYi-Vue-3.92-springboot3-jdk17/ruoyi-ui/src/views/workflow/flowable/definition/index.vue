<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label="流程名称" prop="name">
        <el-input v-model="queryParams.name" placeholder="请输入名称" clearable size="small" @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />
    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-upload" size="mini" @click="handleImport">导入</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleLoadXml">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>
    <el-alert title="流程设计说明" type="info">
      <template slot="title">
        <p>流程设计说明:</p>
        <div>1、XML文件中的流程定义id属性用作流程定义的key参数。</div>
        <div>2、XML文件中的流程定义name属性用作流程定义的name参数。如果未给定name属性，会使用id作为name。</div>
        <div>3、当每个唯一key的流程第一次部署时，指定版本为1。对其后所有使用相同key的流程定义，部署时版本会在该key当前已部署的最高版本号基础上加1。key参数用于区分流程定义。</div>
        <div>4、id参数设置为{processDefinitionKey}:{processDefinitionVersion}:{generated-id}，其中generated-id是一个唯一数字，用以保证在集群环境下，流程定义缓存中，流程id的唯一性。</div>
      </template>
    </el-alert>
    <el-table v-loading="loading" fit :data="definitionList" border @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="流程名称" align="left" show-overflow-tooltip>
        <template slot-scope="scope">
          <el-button type="text" @click="handleReadImage(scope.row.deploymentId)">
            <span>{{ scope.row.name }}</span>
          </el-button>
        </template>
      </el-table-column>
      <el-table-column label="流程标识" align="left" prop="flowKey" width="200" show-overflow-tooltip />
      <!-- <el-table-column label="流程分类" align="center" prop="category" /> -->
      <el-table-column label="流程版本" align="center" width="120">
        <template slot-scope="scope">
          <el-tag size="medium">v{{ scope.row.version }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="120">
        <template slot-scope="scope">
          <el-tag type="success" v-if="scope.row.suspensionState === 1">激活</el-tag>
          <el-tag type="warning" v-if="scope.row.suspensionState === 2">挂起</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="发布时间" align="center" prop="deploymentTime" width="160" />
      <el-table-column label="操作" align="center" width="270" fixed="right" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button @click="handleLoadXml(scope.row)" icon="el-icon-edit-outline" type="text" size="small">设计</el-button>
          <el-button
            @click="updateRouteXml(scope.row)"
            icon="el-icon-refresh"
            type="text"
            size="small"
            v-hasPermi="['flowable:definition:updateRouteXml']"
          >更新在途流程</el-button>
          <el-button
            @click="handleUpdateSuspensionState(scope.row)"
            icon="el-icon-video-pause"
            type="text"
            size="small"
            v-if="scope.row.suspensionState === 1"
          >挂起</el-button>
          <el-button
            @click="handleUpdateSuspensionState(scope.row)"
            icon="el-icon-video-play"
            type="text"
            size="small"
            v-if="scope.row.suspensionState === 2"
          >激活</el-button>
          <el-button @click="handleDelete(scope.row)" icon="el-icon-delete" type="text" size="small" v-hasPermi="['flowable:definition:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- bpmn20.xml导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xml"
        :headers="upload.headers"
        :action="upload.url + '?name=' + upload.name+'&category='+ upload.category"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" slot="tip">
          流程名称：
          <el-input v-model="upload.name" />流程分类：
          <div>
            <!--          <el-input v-model="upload.category"/>-->
            <el-select v-model="upload.category" placeholder="请选择流程分类">
              <el-option v-for="dict in dict.type.sys_process_category" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
            </el-select>
          </div>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“bpmn20.xml”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 流程图 -->
    <el-dialog :title="readImage.title" :visible.sync="readImage.open" width="70%" append-to-body>
      <bpmn-viewer :flowData="flowData" />
    </el-dialog>
  </div>
</template>

<script>
import { listDefinition, updateState, delDeployment, exportDeployment, readXml, updateRouteXml } from "@/api/workflow/flowable/definition";
import { getToken } from "@/utils/auth";
import BpmnViewer from "@/components/Process/viewer";
import flow from "@/views/workflow/flowable/definition/flow";
import Model from "./model";

export default {
  name: "Definition",
  dicts: ["sys_process_category"],
  components: {
    BpmnViewer,
    flow,
    Model,
  },
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
      // 流程定义表格数据
      definitionList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      formConfOpen: false,
      formTitle: "",
      formDeployOpen: false,
      formDeployTitle: "",
      formList: [],
      formTotal: 0,
      formConf: {}, // 默认表单数据
      readImage: {
        open: false,
        src: "",
      },
      // bpmn.xml 导入
      upload: {
        // 是否显示弹出层（xml导入）
        open: false,
        // 弹出层标题（xml导入）
        title: "",
        // 是否禁用上传
        isUploading: false,
        name: null,
        category: null,
        // 设置上传的请求头部
        headers: { Authorization: "Bearer " + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + "/flowable/definition/import",
      },
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null,
        category: null,
        key: null,
        tenantId: null,
        deployTime: null,
        derivedFrom: null,
        derivedFromRoot: null,
        parentDeploymentId: null,
        engineVersion: null,
      },
      formQueryParams: {
        pageNum: 1,
        pageSize: 10,
      },
      deployId: "",
      currentRow: null,
      // xml
      flowData: {},
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
    };
  },
  created() {
    this.getList();
  },
  activated() {
    const time = this.$route.query.t;
    if (time != null) {
      this.getList();
    }
  },
  methods: {
    /** 查询流程定义列表 */
    getList() {
      this.loading = true;
      listDefinition(this.queryParams).then((response) => {
        this.definitionList = response.rows;
        this.total = response.total;
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
      this.ids = selection.map((item) => item.deploymentId);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加流程定义";
    },
    /** 跳转到流程设计页面 */
    handleLoadXml(row) {
      const path = "/flowable/definition/model";
      this.hasOpenedPage(path, () => {
        this.$router.push({ path: path, query: { title: row.name || "新增流程", deployId: row.deploymentId } });
      });
    },
    /** 流程图查看 */
    handleReadImage(deployId) {
      this.readImage.title = "流程图";
      this.readImage.open = true;
      readXml(deployId).then((res) => {
        this.flowData = { xmlData: res.data };
      });
    },
    handleCurrentChange(data) {
      if (data) {
        this.currentRow = JSON.parse(data.formContent);
      }
    },
    /** 挂起/激活流程 */
    handleUpdateSuspensionState(row) {
      let state = 1;
      if (row.suspensionState === 1) {
        state = 2;
      }
      const params = {
        deployId: row.deploymentId,
        state: state,
      };
      updateState(params).then((res) => {
        this.$modal.msgSuccess(res.msg);
        this.getList();
      });
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.deploymentId || this.ids;
      getDeployment(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改流程定义";
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const procDefKey = row.flowKey;
      const loading = this.$loading({
        lock: true,
        text: "正在删除流程，请稍候...",
        spinner: "el-icon-loading",
      });
      this.$confirm("是否确认删除此流程？删除后会影响已使用此定义的流程数据，请谨慎操作！", "警告", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning",
      })
        .then(function () {
          return delDeployment(procDefKey);
        })
        .then(() => {
          loading.close();
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {
          loading.close();
        });
    },
    /** 导入bpmn.xml文件 */
    handleImport() {
      this.upload.title = "bpmn20.xml文件导入";
      this.upload.open = true;
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true;
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false;
      this.upload.isUploading = false;
      this.$refs.upload.clearFiles();
      this.$message(response.msg);
      this.getList();
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit();
    },
    // 更新在途流程
    updateRouteXml(row) {
      let data = {
        procDefKey: row.flowKey,
      };
      const loading = this.$loading({
        lock: true,
        text: "正在更新在途流程，请稍候...",
        spinner: "el-icon-loading",
      });
      this.$modal
        .confirm("是否确认更新在途流程数据？")
        .then(function () {
          return updateRouteXml(data);
        })
        .then(() => {
          loading.close();
          this.$modal.msgSuccess("更新成功！");
        })
        .catch(() => {
          loading.close();
        });
    },
    // 是否打开过页面
    hasOpenedPage(path, callback) {
      this.$tab.hasVisitedView({ path: path }).then((visited) => {
        if (visited) {
          this.$modal
            .confirm("流程设计页面已存在，重复打开将被新页面覆蓋（注意保存数据），确定继续打开？")
            .then(() => {
              callback();
            })
            .catch(() => {});
        } else {
          callback();
        }
      });
    },
  },
};
</script>
