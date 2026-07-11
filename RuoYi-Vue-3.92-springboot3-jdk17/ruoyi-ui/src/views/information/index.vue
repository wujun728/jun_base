<template>
  <!-- 管理员 -->
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标题" prop="title">
        <el-input v-model="queryParams.title" placeholder="请输入标题" clearable @keyup.enter.native="handleQuery" />
      </el-form-item>
      <el-form-item label="状态" prop="status">
        <el-select v-model="queryParams.status" placeholder="请选择状态" clearable>
          <el-option v-for="dict in dict.type.sys_yes_or_no" :key="dict.value" :label="dict.label" :value="dict.value" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>
    <el-divider />

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['information:information:add']">新增</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="informationList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="标题" align="left" prop="title" show-overflow-tooltip />
      <el-table-column label="是否置顶" align="center" prop="topFlag" width="100">
        <template slot-scope="scope">
          <el-switch v-model="scope.row.topFlag" active-value="1" inactive-value="0" @change="top(scope.row)"></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="阅读总数" align="center" prop="readTotal" width="100" />
      <el-table-column label="是否长期有效" align="center" prop="validDate" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_yes_or_no" :value="scope.row.validDate" />
        </template>
      </el-table-column>
      <el-table-column label="生效时间" align="center" prop="validStartTime" width="100">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.validStartTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="排序号" align="center" prop="sort" width="100" />
      <el-table-column label="状态" align="center" prop="status" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.sys_news_status" :value="scope.row.status" />
        </template>
      </el-table-column>
      <el-table-column label="创建者" align="center" prop="createBy" width="120" />
      <el-table-column label="创建时间" align="center" prop="createTime" width="160">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width" width="220">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="goDetail(scope.row)">预览</el-button>
          <el-button
            v-if="scope.row.status == '0'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handlePublic(scope.row)"
            v-hasPermi="['system:notice:edit']"
          >发布</el-button>
          <el-button
            v-if="scope.row.status == '2'"
            size="mini"
            type="text"
            icon="el-icon-check"
            @click="handlePublic(scope.row)"
            v-hasPermi="['system:notice:edit']"
          >再次发布</el-button>
          <el-button
            v-if="scope.row.status == '1'"
            size="mini"
            type="text"
            icon="el-icon-close"
            @click="handleCanclePublic(scope.row)"
            v-hasPermi="['system:notice:edit']"
          >下架</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['system:notice:edit']">修改</el-button>
          <el-button
            v-if="scope.row.status == '0'"
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:notice:remove']"
          >删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <!-- 添加或修改新闻资讯对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body>
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="112px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="是否长期有效" prop="validDate">
          <el-radio-group v-model="form.validDate">
            <el-radio v-for="dict in dict.type.sys_yes_or_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="开始生效日期" prop="validStartTime">
              <el-date-picker clearable v-model="form.validStartTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择开始使用日期"></el-date-picker>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.validDate === '0'">
            <el-form-item label="结束日期" prop="validEndTime">
              <el-date-picker clearable v-model="form.validEndTime" type="date" value-format="yyyy-MM-dd" placeholder="请选择开始使用日期"></el-date-picker>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="排序号" prop="sort">
              <el-input v-model="form.sort" type="number" placeholder="请输入排序号" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item prop="topFlag">
              <template slot="label">
                是否置顶
                <span>
                  <el-tooltip content="如果置顶，请务必上传封面图片。" placement="top">
                    <i class="el-icon-question"></i>
                  </el-tooltip>
                </span>
              </template>
              <el-radio-group v-model="form.topFlag">
                <el-radio v-for="dict in dict.type.sys_yes_or_no" :key="dict.value" :label="dict.value">{{dict.label}}</el-radio>
              </el-radio-group>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="封面图片" prop="imgUrl">
          <file-upload :value="form.imgUrl" :fileSize="50" :fileType="['jpeg', 'jpg', 'png','webp']" :limit="1" @input="uploadSuccess">
            <i class="el-icon-picture-outline"></i>
          </file-upload>
        </el-form-item>
        <el-form-item label="内容" prop="content">
          <editor v-model="form.content" :min-height="192" />
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
import {
  listInformation,
  getInformation,
  delInformation,
  addInformation,
  updateInformation,
  changeStatus,
  toTop,
} from "@/api/information/information";

export default {
  name: "Information",
  dicts: ["sys_yes_or_no", "sys_news_status"],
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
      // 新闻资讯表格数据
      informationList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        title: null,
        status: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
        content: [{ required: true, message: "内容不能为空", trigger: "blur" }],
        validDate: [{ required: true, message: "请选择是否长期有效", trigger: "blur" }],
        validStartTime: [{ required: true, message: "开始生效日期不能为空", trigger: "blur" }],
        validEndTime: [{ required: true, message: "结束日期不能为空", trigger: "blur" }],
      },
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询新闻资讯列表 */
    getList() {
      this.loading = true;
      listInformation(this.queryParams).then((response) => {
        this.informationList = response.rows;
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
        title: null,
        validDate: "1",
        validStartTime: null,
        validEndTime: null,
        sort: null,
        topFlag: "0",
        imgUrl: null,
        content: null,
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
      this.ids = selection.map((item) => item.id);
      this.single = selection.length !== 1;
      this.multiple = !selection.length;
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "新增新闻资讯";
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset();
      const id = row.id || this.ids;
      getInformation(id).then((response) => {
        this.form = response.data;
        this.open = true;
        this.title = "修改新闻资讯";
      });
    },
    /** 发布 */
    handlePublic(row) {
      this.$confirm("确认发布此新闻资讯吗？", "提示", {
        confirmButtonText: "确 认",
        cancelButtonText: "取 消",
      })
        .then(() => {
          this.loading = true;
          changeStatus(row.id, "1").then((res) => {
            row.status = "1";
            this.loading = false;
            this.$modal.msgSuccess("发布成功");
          });
        })
        .catch(() => {});
    },
    /** 下架 */
    handleCanclePublic(row) {
      this.$confirm("确认下架此新闻资讯吗？", "提示", {
        confirmButtonText: "确 认",
        cancelButtonText: "取 消",
      })
        .then(() => {
          this.loading = true;
          changeStatus(row.id, "2").then((res) => {
            row.status = "2";
            this.loading = false;
            this.$modal.msgSuccess("下架成功");
          });
        })
        .catch(() => {});
    },
    /** 置顶 */
    top(row) {
      let text = row.topFlag === "0" ? "取消置顶" : "置顶";
      this.$modal
        .confirm('确认要"' + text + '"当前数据吗？')
        .then(function () {
          let data = {
            id: row.id,
            topFlag: row.topFlag,
          };
          return toTop(data);
        })
        .then(() => {
          this.$modal.msgSuccess(text + "成功");
          this.getList();
        })
        .catch(function () {
          row.topFlag = row.topFlag === "0" ? "1" : "0";
        });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          if (this.form.id != null) {
            updateInformation(this.form).then((response) => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addInformation(this.form).then((response) => {
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
        .confirm("是否确认删除此新闻资讯？")
        .then(function () {
          return delInformation(ids);
        })
        .then(() => {
          this.getList();
          this.$modal.msgSuccess("删除成功");
        })
        .catch(() => {});
    },
    /** 图片上传成功回调 */
    uploadSuccess(file) {
      this.form.imgUrl = file;
    },
    /** 预览 */
    goDetail(row) {
      let params = { id: row.id };
      this.$tab.openPage(row.title, "/news/information/i/detail/:t" + new Date().getTime(), params);
    },
  },
};
</script>
