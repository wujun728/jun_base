<template>
  <div class="app-container">
    <el-form
      :model="queryParams"
      ref="queryForm"
      size="small"
      :inline="true"
      v-show="showSearch"
      label-width="68px"
    >
      <el-form-item label="知识库名称" prop="knowledgeBaseName">
        <el-input
          v-model="queryParams.knowledgeBaseName"
          placeholder="请输入知识库名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item>
        <el-button
          type="primary"
          icon="el-icon-search"
          size="mini"
          @click="handleQuery"
          >搜索</el-button
        >
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery"
          >重置</el-button
        >
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['ai:knowledgeBaseList:add']"
          >新增</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['ai:knowledgeBaseList:edit']"
          >修改</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['ai:knowledgeBaseList:remove']"
          >删除</el-button
        >
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ai:knowledgeBaseList:export']"
          >导出</el-button
        >
      </el-col>
      <right-toolbar
        :showSearch.sync="showSearch"
        @queryTable="getList"
      ></right-toolbar>
    </el-row>

    <el-table
      v-loading="loading"
      :data="knowledgeBaseListList"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="知识库ID" align="center" prop="id" />
      <el-table-column
        label="知识库名称"
        align="center"
        prop="knowledgeBaseName"
      />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="创建者" align="center" prop="createBy" />
      <el-table-column
        label="创建时间"
        align="center"
        prop="createTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
        </template>
      </el-table-column>
      <el-table-column label="更新者" align="center" prop="updateBy" />
      <el-table-column
        label="更新时间"
        align="center"
        prop="updateTime"
        width="180"
      >
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.updateTime, "{y}-{m}-{d}") }}</span>
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
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ai:knowledgeBaseList:edit']"
            >修改</el-button
          >
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ai:knowledgeBaseList:remove']"
            >删除</el-button
          >
          <el-dropdown
            size="mini"
            @command="(command) => handleCommand(command, scope.row)"
            v-hasPermi="['monitor:job:changeStatus', 'monitor:job:query']"
          >
            <el-button size="mini" type="text" icon="el-icon-d-arrow-right"
              >更多</el-button
            >
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="getLineList" icon="el-icon-share"
                >文档管理</el-dropdown-item
              >
              <el-dropdown-item
                command="openUploadFile"
                icon="el-icon-circle-plus-outline"
                >导入文档</el-dropdown-item
              >
            </el-dropdown-menu>
          </el-dropdown>
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

    <!-- 添加或修改知识库列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="知识库名称" prop="knowledgeBaseName">
          <el-input
            v-model="form.knowledgeBaseName"
            placeholder="请输入知识库名称"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看知识库单栏列表 -->
    <el-dialog
      title="知识库单栏"
      :visible.sync="openCard"
      width="80%"
      append-to-body
    >
      <el-form
        :model="queryParams"
        ref="queryParams"
        size="small"
        :inline="true"
        v-show="showSearch"
        label-width="90px"
      >
        <el-form-item label="名称" prop="name">
          <el-input
            v-model="queryParams.name"
            placeholder="请输入名称"
            clearable
            @keyup.enter.native="handleCardQuery"
          />
        </el-form-item>
        <el-form-item label="知识库内容" prop="context">
          <el-input
            v-model="queryParams.context"
            placeholder="请输入知识库内容"
            clearable
            @keyup.enter.native="handleCardQuery"
          />
        </el-form-item>
              <el-form-item label="创建者" prop="createBy">
        <el-input
          v-model="queryParams.createBy"
          placeholder="请输入创建者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="创建时间">
        <el-date-picker
          v-model="daterangeCreateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="更新者" prop="updateBy">
        <el-input
          v-model="queryParams.updateBy"
          placeholder="请输入更新者"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="更新时间">
        <el-date-picker
          v-model="daterangeUpdateTime"
          style="width: 240px"
          value-format="yyyy-MM-dd"
          type="daterange"
          range-separator="-"
          start-placeholder="开始日期"
          end-placeholder="结束日期"
        ></el-date-picker>
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option
            v-for="dict in dict.type.sys_knowledge_line_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
        <el-form-item>
          <el-button
            type="primary"
            icon="el-icon-search"
            size="mini"
            @click="handleCardQuery"
            >搜索</el-button
          >
          <!-- <el-button icon="el-icon-refresh" size="mini" @click="resetCardQuery"
            >重置</el-button
          > -->
        </el-form-item>
      </el-form>
      <el-table
        :data="lineList"
        border
        style="width: 100%"
        @row-click="handleRowClickCard"
      >
        <el-table-column prop="name" label="名称" align="center">
        </el-table-column>

        <el-table-column
          prop="context"
          label="知识库内容"
          align="center"
          show-overflow-tooltip
        >
        </el-table-column>
        <el-table-column label="状态" align="center" prop="status">
          <template slot-scope="scope">
    <dict-tag :options="dict.type.sys_knowledge_line_status" :value="scope.row.status"/>
  </template>
        </el-table-column>
        <el-table-column label="创建者" align="center" prop="createBy" />
        <el-table-column
          label="创建时间"
          align="center"
          prop="createTime"
          width="180"
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.createTime, "{y}-{m}-{d}") }}</span>
          </template>
        </el-table-column>
        <el-table-column label="更新者" align="center" prop="updateBy" />
        <el-table-column
          label="更新时间"
          align="center"
          prop="updateTime"
          width="180"
        >
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.updateTime, "{y}-{m}-{d}") }}</span>
          </template>
        </el-table-column>
        <el-table-column fixed="right" label="操作" align="center">
          <template slot-scope="scope">
            <el-button v-if="scope.row.status!=1"
              @click="handleLineWithDocumentStorageByPg(scope.row)"
              v-hasPermi="['ai:knowledgeBaseLine:edit']"
              type="text"
              size="small"
              >向量化</el-button
            >
            <el-button
              @click="handleLineUpdate(scope.row)"
              v-hasPermi="['ai:knowledgeBaseLine:edit']"
              type="text"
              size="small"
              >查看</el-button
            >
          </template>
        </el-table-column>
      </el-table>

      <!-- <pagination
        v-show="total > 0"
        :total="total"
        :page.sync="queryParams.pageNum"
        :limit.sync="queryParams.pageSize"
        @pagination="getLineInfoByBaseId"
      /> -->
      <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getLineInfoByBaseId"
    />
    </el-dialog>


    <!-- 添加或修改知识库单栏对话框 -->
    <el-dialog :title="title" :visible.sync="openLine" width="80" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-form-item label="知识库单栏名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入知识库单栏名称" />
        </el-form-item>
        <el-form-item label="知识库内容" prop="context">
          <el-input
            v-model="form.context"
            type="textarea"
            autosize
            placeholder="请输入内容"
          />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 上传文档 -->
    <el-dialog
      title="上传文档"
      :visible.sync="uploadWindow"
      width="80%"
      append-to-body
    >
      <el-form ref="form" :model="form" :rules="rules" label-width="200px">
        <el-form-item label="选择文档" prop="file" >
          <el-upload
            class="upload-demo"
            drag
            action="#"
            :http-request="customUpload"
        :show-file-list="false"
            multiple
          >
            <i class="el-icon-upload"></i>
            <div class="el-upload__text">
              将文件拖到此处，或<em>点击上传</em>
            </div>
          </el-upload>
        </el-form-item>
        <el-form-item label="文档名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入文档名称" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="uploadFile">确 定</el-button>
        <el-button @click="cancelUploadWindow">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listKnowledgeBaseList,
  getKnowledgeBaseList,
  delKnowledgeBaseList,
  addKnowledgeBaseList,
  updateKnowledgeBaseList,
  getLineInfo,
} from "@/api/ai/knowledgeBaseList";
import {
  upload,
} from "@/api/ai/knowledgeBaseLine";
import {
  getKnowledgeBaseLine,withDocumentStorageByPg
} from "@/api/ai/knowledgeBaseLine";

export default {
  name: "KnowledgeBaseList",
  dicts: ['sys_knowledge_line_status'],
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
      // 知识库列表表格数据
      knowledgeBaseListList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 备注时间范围
      daterangeCreateTime: [],
      // 备注时间范围
      daterangeUpdateTime: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        knowledgeBaseName: null,
        createTime: null,
        name: null,
        context: null,
        createBy: null,
        updateBy: null,
        updateTime: null,
        status: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      // 列表展示弹窗
      openCard: false,
      //单栏列表
      lineList: [],
      // 上传文档展示窗
      uploadWindow: false,
      //单栏详情弹窗
      openLine:false,
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询知识库列表列表 */
    getList() {
      this.loading = true;
      this.queryParams.params = {};
      if (null != this.daterangeCreateTime && "" != this.daterangeCreateTime) {
        this.queryParams.params["beginCreateTime"] =
          this.daterangeCreateTime[0];
        this.queryParams.params["endCreateTime"] = this.daterangeCreateTime[1];
      }
      if (null != this.daterangeUpdateTime && "" != this.daterangeUpdateTime) {
        this.queryParams.params["beginUpdateTime"] =
          this.daterangeUpdateTime[0];
        this.queryParams.params["endUpdateTime"] = this.daterangeUpdateTime[1];
      }
      listKnowledgeBaseList(this.queryParams).then((response) => {
        this.knowledgeBaseListList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },

    // 取消上传文档按钮
    cancelUploadWindow() {
      this.uploadWindow = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        knowledgeBaseName: null,
        remark: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.daterangeCreateTime = [];
      this.daterangeUpdateTime = [];
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加知识库列表";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getKnowledgeBaseList(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改知识库列表";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateKnowledgeBaseList(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addKnowledgeBaseList(this.form).then((response) => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids;
      this.$modal
        .confirm('是否确认删除知识库列表编号为"' + ids + '"的数据项？')
        .then(function () {
          return delKnowledgeBaseList(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download(
        "ai/knowledgeBaseList/export",
        {
          ...this.queryParams,
        },
        `knowledgeBaseList_${new Date().getTime()}.xlsx`
      );
    },

    /* 更多操作触发 */
    handleCommand(command, row) {
      switch (command) {
        case "getLineList":
          this.getLineList(row);
          break;
        case "openUploadFile":
          this.openUploadFile(row);
          break;
        default:
          break;
      }
    },

    /* 打开上传文档窗口 */
    openUploadFile(row) {
      console.log(row);
      this.uploadWindow = true;
      this.title = "上传文档";
      this.id = row.id;
    },

    uploadFile(){
      const formData = new FormData();
      formData.append("file", this.form.file);
    formData.append("name", this.form.name);
    formData.append("knowledgeBaseId", this.id);
    if (this.id != null) {
            upload(formData).then((response) => {
              this.$modal.msgSuccess("解析成功");
              this.uploadWindow = false;
              this.getList();
            });
          } else {
            this.$modal.msgSuccess("没有选中知识库ID");
          }


      console.log(formData);
      //this.upload(this.form);
    },
    customUpload(request) {
      this.form.file = request.file;
      console.log(request);

    // this.$axios.post("/upload", formData, {
    //   headers: {
    //     "Content-Type": "multipart/form-data"
    //   }
    // }).then(res => {
    //   this.$message.success("上传成功");
    //   this.uploadWindow = false;
    // }).catch(err => {
    //   this.$message.error("上传失败：" + err);
    // });
  },

    /* 知识库单栏 */
    getLineList(row) {
      this.openCard = true;
      this.title = "知识库单栏";
      this.id = row.id;
      this.getLineInfoByBaseId();
    },
    // 获取知识库单栏
    getLineInfoByBaseId() {
      this.lineList = [];
      getLineInfo(this.id).then((res) => {
        console.log(res);
        this.lineList = res.rows;
        console.log(this.lineList);
      });
    },
    /** 搜索按钮操作 */
    handleCardQuery() {
      this.queryParams.pageNum = 1;
      this.getLineInfoByBaseId();
    },
    // 点击知识库单栏
    handleRowClickCard(row, column, event) {
      // 获取行数据
      this.row = row;
    },
    // 编辑行数据
    handleEditCard(row) {
      this.openEditNum = true;
      this.editNumForm = row;
    },
    /** 修改知识库单栏 */
  handleLineUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getKnowledgeBaseLine(id).then((response) => {
        console.log(response.data);
        this.form = response.data;
        this.openLine = true;
        this.title = "修改知识库单栏";
      });
    },

        /** 修改知识库单栏 */
  handleLineWithDocumentStorageByPg(row) {
      const id = row.id || this.ids;
      withDocumentStorageByPg(id).then((response) => {
        this.$modal.alert(response.msg);
        //console.log(response);
      });
    },
  },
  
};
</script>
