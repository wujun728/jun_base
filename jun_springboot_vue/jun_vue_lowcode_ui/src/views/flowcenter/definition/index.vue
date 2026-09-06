<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="88px">
      <el-form-item label="流程KEY" prop="key">
        <el-input
          v-model="queryParams.key"
          placeholder="请输入流程KEY"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="名称" prop="name">
        <el-input
          v-model="queryParams.name"
          placeholder="请输入名称"
          clearable
          size="small"
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="cyan" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="OnlineDrawingProcess"
        >在线绘制流程
        </el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="primary"
          icon="el-icon-plus"
          size="mini"
          @click="handleImport"
        >部署流程
        </el-button>
      </el-col>

      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>


    <el-table v-loading="loading" :data="Modeler">
      <el-table-column label="流程ID" align="center" prop="id"/>
      <el-table-column label="流程KEY" align="center" prop="key"/>
      <el-table-column label="流程名称" align="center" prop="name"/>
      <el-table-column label="版本" align="center" prop="version"/>
      <el-table-column label="部署时间" align="center" prop="deploymentTime"/>
      <el-table-column label="部署ID" align="center" prop="deploymentId"/>

      <el-table-column label="状态" align="center" prop="suspendState">
        <template slot-scope="scope">
          <span>{{ scope.row.suspendState!=1?'挂起':'激活'}}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="OnlineModificationProcess(scope.row)"
          >查看
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="suspendOrActiveApply(scope.row)"
          >{{scope.row.suspendState==1?'挂起':'激活'}}
          </el-button>

          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
          >删除
          </el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    />
    <!--bpmnjs在线流程设计器-->
    <el-dialog
      :visible.sync="modelVisible"
      title="流程图"
      width="1680px"
      @close="modelCancel"
      append-to-body
    >
      <div style="position:relative;height: 100%;">
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


    <!-- 用户导入对话框 -->
    <el-dialog :title="upload.title" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload ref="upload" :limit="1" accept=".bpmn, .bar, .zip" :headers="upload.headers" :action="upload.url"
                 :disabled="upload.isUploading" :on-progress="handleFileUploadProgress" :on-success="handleFileSuccess"
                 :auto-upload="false" drag>
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">
          将文件拖到此处，或
          <em>点击上传</em>
        </div>
        <div class="el-upload__tip" style="color:red" slot="tip">提示：仅允许导入“bpmn”、“bar”或“zip”格式文件！</div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>

  </div>
</template>

<script>
  import {listDefinition, delDefinition, suspendOrActiveApply} from "@/api/flow/definition";
  import {getToken} from "@/utils/auth";

  export default {
    name: "ActIdGroup",
    data() {
      return {
        modelerUrl: "",
        modelVisible: false,
        // 遮罩层
        loading: true,
        // 非单个禁用
        single: true,
        // 非多个禁用
        multiple: true,
        // 显示搜索条件
        showSearch: true,
        // 总条数
        total: 0,
        // VIEW表格数据
        Modeler: [],
        // 弹出层标题
        title: "",
        // 是否显示弹出层
        open: false,
        src: "",
        // 查询参数
        queryParams: {
          pageNum: 1,
          pageSize: 10,
          id: null,
          rev: null,
          name: null,
          type: null
        },
        upload: {
          // 是否显示弹出层（用户导入）
          open: false,
          // 弹出层标题（用户导入）
          title: "",
          // 是否禁用上传
          isUploading: false,
          // 设置上传的请求头部
          headers: {Authorization: "Bearer " + getToken()},
          // 上传的地址
          url: process.env.VUE_APP_BASE_API + "/processDefinition/uploadStreamAndDeployment",
        },
      };
    },
    created() {
      this.getList();
    },
    methods: {
      /** 查询Definition列表 */
      getList() {
        this.loading = true;
        listDefinition(this.queryParams).then(response => {
          this.Modeler = response.rows;
          this.total = response.total;
          this.loading = false;
        });
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

      suspendOrActiveApply(row) {
        var suspendOrActive = row.suspendState === '2' ? '激活' : '挂起';
        this.$confirm('确认要' + suspendOrActive + '流程定义吗?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          var data = {"id": row.id, "suspendState": row.suspendState};
          return suspendOrActiveApply(data);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("转换成功");
        }).catch(function () {
        });


      },

      handleImport() {
        this.upload.title = "上传模型图";
        this.upload.open = true;
      },
      OnlineDrawingProcess() {
        this.modelVisible = true;
        localStorage.setItem("VUE_APP_BASE_API", process.env.VUE_APP_BASE_API)
        this.modelerUrl = "/bpmnjs/index.html?type=addBpmn";
      },
      OnlineModificationProcess(data) {
        this.modelVisible = true;
        localStorage.setItem("VUE_APP_BASE_API", process.env.VUE_APP_BASE_API)
        this.modelerUrl = '/bpmnjs/index.html?type=lookBpmn&deploymentFileUUID=' + data.deploymentId + '&deploymentName=' + encodeURI(data.resourceName);
      },

      // 提交上传文件
      submitFileForm() {
        this.$refs.upload.submit();
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
        this.$alert(response.msg, "导入结果", {dangerouslyUseHTMLString: true});
        this.getList();
      },
      /** 删除按钮操作 */
      handleDelete(row) {
        this.$confirm('是否确认删除编号为"' + row.key + '"的数据项?', "警告", {
          confirmButtonText: "确定",
          cancelButtonText: "取消",
          type: "warning"
        }).then(function () {
          return delDefinition(row.deploymentId);
        }).then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        }).catch(function () {
        });
      },
      modelCancel() {
        this.getList();
      }

    }
  };
</script>
