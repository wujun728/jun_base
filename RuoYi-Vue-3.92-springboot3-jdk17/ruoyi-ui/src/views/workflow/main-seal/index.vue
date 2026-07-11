<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px" @submit.native.prevent>
      <el-form-item label="印章名" prop="sealName">
        <el-input v-model="queryParams.sealName" placeholder="请输入印章名" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['workflow:seal:add']">添加印章</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="sealList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="印章名" align="left" prop="sealName" width="200" show-overflow-tooltip />
      <el-table-column label="环绕文字" align="left" prop="surroundWord" show-overflow-tooltip />
      <el-table-column label="印章类型" align="center" prop="type" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.esign_seal_type" :value="scope.row.type" />
        </template>
      </el-table-column>
      <el-table-column label="样式" align="center" prop="style" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.esign_seal_style" :value="scope.row.style" />
        </template>
      </el-table-column>
      <el-table-column label="颜色" align="center" prop="color" width="90">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.esign_seal_color" :value="scope.row.color" />
        </template>
      </el-table-column>
      <el-table-column label="启停状态" prop="enableFlag" align="center" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.enableFlag" active-value="1" inactive-value="0" @change="handleEnableFlagChange(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="100">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['workflow:seal:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['workflow:seal:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改印章对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body :close-on-click-modal="false">
      <el-divider />
      <div class="form-preview-container">
        <div class="form-section">
          <el-form ref="form" :model="form" :rules="rules" label-width="92px">
            <el-form-item label="印章名" prop="sealName">
              <el-input v-model="form.sealName" placeholder="用于区分印章，非印章内文字，例：公司发票章" />
            </el-form-item>
            <el-form-item label="环绕文字" prop="surroundWord">
              <el-input v-model="form.surroundWord" placeholder="请输入营业执照登记的公司全称" />
            </el-form-item>
            <el-form-item label="印章类型" prop="type">
              <el-select v-model="form.type" placeholder="请选择印章类型章">
                <el-option v-for="dict in dict.type.esign_seal_type" :key="dict.value" :label="dict.label" :value="dict.value"></el-option>
              </el-select>
            </el-form-item>
            <el-form-item label="印章样式" prop="style">
              <el-radio-group v-model="form.style">
                <div class="seal-style">
                  <div>
                    <p class="border">
                      <img src="@/views/workflow/mainSeal/images/gongzhang.png" width="120" height="120" class="img-cricle" />
                    </p>
                    <p class="cricle">
                      <el-radio label="circle">圆形章</el-radio>
                    </p>
                  </div>
                  <div style="margin-left: 24px; ">
                    <p class="border">
                      <img src="@/views/workflow/mainSeal//images/fapiaozhang.png" width="120" class="img-oval" />
                    </p>
                    <p class="oval">
                      <el-radio label="oval">椭圆形章</el-radio>
                    </p>
                  </div>
                </div>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="印章颜色" prop="color">
              <el-radio-group v-model="form.color" size="mini" fill="red">
                <el-radio-button label="red">红色</el-radio-button>
              </el-radio-group>
              <el-radio-group v-model="form.color" size="mini">
                <el-radio-button label="blue">蓝色</el-radio-button>
              </el-radio-group>
            </el-form-item>
            <el-form-item label="横排文字" prop="centreWord">
              <el-input v-model="form.centreWord" placeholder="印章内横排文字" clearable />
            </el-form-item>
            <el-form-item label="下弦文字" prop="underWord">
              <el-input v-model="form.underWord" placeholder="一般为统一信用码" clearable />
            </el-form-item>
          </el-form>
        </div>
        <div class="preview-section">
          <span class="preview-title">印章预览</span>
          <div class="preview">
            <img v-show="previewSealData" :src="previewSealData" alt width="120" height="120" />
          </div>
          <div class="preview-btn">
            <el-button type="primary" plain @click="previewSeal">预 览</el-button>
          </div>
        </div>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listSeal, getSeal, delSeal, updateSeal, previewSeal, createSeal, changeEnableFlag } from "@/api/workflow/mainSeal";

export default {
  name: "MainSeal",
  dicts: ["sys_yes_or_no", "esign_seal_style", "esign_seal_color", "esign_seal_type"],
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
      // 正文印章表格数据
      sealList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 印章预览数据
      previewSealData: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        sealName: null,
        enableFlag: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        sealName: [{ required: true, message: "印章名不能为空", trigger: "blur" }],
        surroundWord: [{ required: true, message: "环绕文字不能为空", trigger: "blur" }],
        type: [{ required: true, message: "请选择印章类型", trigger: "blur" }],
        style: [{ required: true, message: "请选择印章样式", trigger: "blur" }],
        color: [{ required: true, message: "请选择颜色", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询正文印章列表 */
    getList() {
      this.loading = true;
      listSeal(this.queryParams).then((response) => {
        this.sealList = response.rows;
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
        fileId: null,
        sealName: null,
        type: "0",
        style: "circle",
        surroundWord: null,
        centreWord: null,
        underWord: null,
        color: "red",
        width: 32,
        height: 32,
        enableFlag: "1",
      };
      this.previewSealData = null;
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加正文印章";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      this.open = true;
      const id = row.id || this.ids;
      getSeal(id).then((response) => {
        this.form = response.data;
        this.previewSealData = this.form.previewBase64;
        this.title = "修改正文印章";
      });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateSeal(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            createSeal(this.form).then((response) => {
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
        .confirm("是否确认删除此印章？删除后不可恢复，若要停用，请直接操作启停状态！")
        .then(function () {
          return delSeal(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    // 状态切换
    handleEnableFlagChange(row) {
      let text = row.enableFlag === "0" ? "停用" : "启用";
      this.$modal
        .confirm('确认要"' + text + '"当前配置吗？')
        .then(function () {
          return changeEnableFlag(row.id, row.enableFlag);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
          this.getList();
        })
        .catch(function () {
          row.enableFlag = row.enableFlag === "0" ? "1" : "0";
        });
    },
    // 预览印章
    previewSeal() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          previewSeal(this.form).then((res) => {
            this.previewSealData = "data:image/jpeg;base64," + res.data.previewBase64;
          });
        }
      });
    },
  },
};
</script>

<style type="scss" scoped>
.seal-style {
  display: flex;
  justify-content: center;
}
.border {
  height: 140px;
  width: 140px;
  border: 1px solid rgb(218, 218, 218);
}
.img-cricle {
  margin-left: 10px;
  margin-top: 10px;
}
.img-oval {
  margin-left: 10px;
  margin-top: 20px;
}
.cricle {
  margin-left: 30px;
  margin-top: 12px;
}

.oval {
  margin-left: 20px;
  margin-top: 12px;
}
.form-preview-container {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  padding-bottom: 20px;
  height: 580px;
}

.form-section {
  flex: 1;
  max-width: calc(100% - 160px);
  border: 1px solid #e6e6e6;
  padding: 12px;
}

.preview-section {
  width: 170px;
  height: 50%;
  text-align: center;
  align-items: center;
  margin-left: 20px;
  border: 1px solid #e6e6e6;
  padding-top: 12px;
  background-color: #f3f3f3;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.05);
  padding: 14px;
}

.preview-title {
  font-size: 14px;
  font-weight: 600;
}
.preview {
  width: 140px;
  height: 140px;
  border: 1px solid #e8e8e8;
  background-color: #ffffff;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 12px;
}

.preview-btn {
  margin-top: 32px;
}

::v-deep .button-1 {
  --el-radio-button-checked-bg-color: var(--el-color-info);
  --el-radio-button-checked-text-color: var(--el-color-white);
  --el-radio-button-checked-border-color: var(--el-color-info);
  --el-radio-button-disabled-checked-fill: var(--el-border-color-extra-light);
}
.button-2 {
  --el-radio-button-checked-bg-color: var(--el-color-primary);
  --el-radio-button-checked-text-color: var(--el-color-white);
  --el-radio-button-checked-border-color: var(--el-color-primary);
  --el-radio-button-disabled-checked-fill: var(--el-border-color-extra-light);
}
</style>