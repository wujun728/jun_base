<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="80px">
      <el-form-item label="配置URL" prop="baseUrl">
        <el-input
          v-model="queryParams.baseUrl"
          placeholder="请输入配置URL"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="apiKey" prop="apiKey">
        <el-input
          v-model="queryParams.apiKey"
          placeholder="请输入apiKey"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>

      <el-form-item label="模型版本" prop="modelVersion">
        <el-input
          v-model="queryParams.modelVersion"
          placeholder="请输入模型版本"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="模型名称" prop="modelName">
        <el-input
          v-model="queryParams.modelName"
          placeholder="请输入模型名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
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
          v-hasPermi="['ai:modelList:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['ai:modelList:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['ai:modelList:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['ai:modelList:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="modelListList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="配置URL" align="center" prop="baseUrl" />
      <el-table-column label="apiKey" align="center" prop="apiKey" />
      <el-table-column label="模型版本" align="center" prop="modelVersion" />
      <el-table-column label="模型名称" align="center" prop="modelName" />
      <el-table-column label="GPU 数量" align="center" prop="numGpu" />
      <el-table-column label="随机性" align="center" prop="temperature" />
      <el-table-column label="限制模型单词" align="center" prop="topK" />
      <el-table-column label="核采样" align="center" prop="topP" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['ai:modelList:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['ai:modelList:remove']"
          >删除</el-button>
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

    <!-- 添加或修改模型列表对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="150px">
        <el-form-item label="配置URL" prop="baseUrl">
          <el-input v-model="form.baseUrl" placeholder="请输入配置URL" />
        </el-form-item>
        <el-form-item label="apiKey" prop="apiKey">
          <el-input v-model="form.apiKey" placeholder="请输入apiKey" />
        </el-form-item>
        <el-form-item label="模型版本" prop="modelVersion">
          <el-input v-model="form.modelVersion" placeholder="请输入模型版本" />
        </el-form-item>
        <el-form-item label="模型名称" prop="modelName">
          <el-input v-model="form.modelName" placeholder="请输入模型名称" />
        </el-form-item>
        <el-form-item label="GPU 数量" prop="numGpu">
          <el-slider v-model="form.numGpu"></el-slider>
        </el-form-item>
        <el-form-item label="随机性" prop="temperature">
    <el-slider v-model="form.temperature" :min="0" :max="1" :step="0.01"></el-slider>
        </el-form-item>
        <el-form-item label="限制模型单词" prop="topK">
          <el-slider v-model="form.topK"></el-slider>
        </el-form-item>
        <el-form-item label="核采样" prop="topP">
          <el-slider v-model="form.topP" :min="0" :max="1" :step="0.01"></el-slider>
        </el-form-item>
        <el-form-item label="防止重复" prop="repeatPenalty">
          <el-slider v-model="form.repeatPenalty" :min="0" :max="1" :step="0.01"></el-slider>
        </el-form-item>
        <el-form-item label="惩罚已出现的词" prop="presencePenalty">
          <el-slider v-model="form.presencePenalty" :min="0" :max="1" :step="0.01"></el-slider>
        </el-form-item>
        <el-form-item label="惩罚频繁出现的词" prop="frequencyPenalty">
          <el-slider v-model="form.frequencyPenalty" :min="0" :max="1" :step="0.01"></el-slider>
        </el-form-item>
        <el-form-item label="最大token数" prop="numPredict">
          <el-slider v-model="form.numPredict"></el-slider>
        </el-form-item>
        <el-form-item label="CPU线程数" prop="numThread">
          <el-slider v-model="form.numThread"></el-slider>
        </el-form-item>
        <el-form-item label="是否启用低显存模式" prop="lowVram">
          <el-switch
  v-model="form.lowVram">
</el-switch>
        </el-form-item>
        <el-form-item label="是否选择" prop="isChose">
          <el-switch
  v-model="form.isChose">
</el-switch>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listModelList, getModelList, delModelList, addModelList, updateModelList } from "@/api/ai/modelList";

export default {
  name: "ModelList",
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
      // 模型列表表格数据
      modelListList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        modelVersion: null,
        modelName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
      }
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询模型列表列表 */
    getList() {
      this.loading = true;
      listModelList(this.queryParams).then(response => {
        this.modelListList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        baseUrl: null,
        apiKey: null,
        modelVersion: null,
        modelName: null,
        numGpu: null,
        temperature: null,
        topK: null,
        topP: null,
        repeatPenalty: null,
        presencePenalty: null,
        frequencyPenalty: null,
        numPredict: null,
        numThread: null,
        lowVram: null,
        isChose: null
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
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加模型";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids
      getModelList(id).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改模型";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateModelList(this.form).then(response => {
              console.log(this.form);
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addModelList(this.form).then(response => {
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
      this.$modal.confirm('是否确认删除模型列表编号为"' + ids + '"的数据项？').then(function() {
        return delModelList(ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('ai/modelList/export', {
        ...this.queryParams
      }, `modelList_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
