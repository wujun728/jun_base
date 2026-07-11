<template>
  <div class="app-container">
    <el-form ref="queryForm" :model="queryParams" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="标签名" prop="tag">
        <el-select v-model="queryParams.tag" placeholder="请选择标签名" clearable size="small">
          <el-option v-for="dict in dict.type.online_api_tag" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item label="标签ID" prop="tagId">
        <el-input v-model="queryParams.tagId" placeholder="请输入标签ID" clearable size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="请求路径" prop="path">
        <el-input v-model="queryParams.path" placeholder="请输入请求路径" clearable size="small" @keyup.enter.native="handleQuery"/>
      </el-form-item>
      <el-form-item label="请求方式" prop="method">
        <el-select v-model="queryParams.method" placeholder="请选择" clearable size="small">
          <el-option v-for="dict in dict.type.online_api_method" :key="dict.value" :label="dict.label" :value="dict.value"/>
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button type="primary" plain icon="el-icon-plus" size="mini" @click="handleAdd" v-hasPermi="['online:mb:add']">新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="success" plain icon="el-icon-edit" size="mini" :disabled="single" @click="handleUpdate" v-hasPermi="['online:mb:edit']">修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="danger" plain icon="el-icon-delete" size="mini" :disabled="multiple" @click="handleDelete" v-hasPermi="['online:mb:remove']">删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button type="warning" plain icon="el-icon-download" size="mini" :loading="exportLoading" @click="handleExport" v-hasPermi="['online:mb:export']">导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="mbList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="标签名" align="center" prop="tag">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.online_api_tag" :value="scope.row.tag"/>
        </template>
      </el-table-column>
      <el-table-column label="标签ID" align="center" prop="tagId" :show-overflow-tooltip="true" />
      <el-table-column label="请求路径" align="center" prop="path" :show-overflow-tooltip="true" />
      <el-table-column label="请求方式" align="center" prop="method">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.online_api_method" :value="scope.row.method"/>
        </template>
      </el-table-column>
      <el-table-column label="响应类型" align="center" prop="resultType">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.online_api_result" :value="scope.row.resultType"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button size="mini" type="text" icon="el-icon-view" @click="handlePreview(scope.row)">预览</el-button>
          <el-button size="mini" type="text" icon="el-icon-edit" @click="handleUpdate(scope.row)" v-hasPermi="['online:mb:edit']">修改</el-button>
          <el-button size="mini" type="text" icon="el-icon-delete" @click="handleDelete(scope.row)" v-hasPermi="['online:mb:remove']">删除</el-button>
        </template>
      </el-table-column>
    </el-table>

    <pagination v-show="total>0" :total="total" :page.sync="queryParams.pageNum" :limit.sync="queryParams.pageSize" @pagination="getList" />

    <el-dialog :title="title" :visible.sync="open" width="750px" append-to-body :close-on-click-modal="false">
      <el-form ref="form" :model="form" :rules="rules" label-width="100px">
        <el-row>
          <el-col :span="12">
            <el-form-item label="标签名" prop="tag">
              <el-select v-model="form.tag" placeholder="请选择">
                <el-option v-for="dict in dict.type.online_api_tag" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="标签ID" prop="tagId">
              <el-input v-model="form.tagId" placeholder="请输入标签ID" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求路径" prop="path">
              <el-input v-model="form.path" placeholder="以 / 开头" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="请求方式" prop="method">
              <el-select v-model="form.method" placeholder="请选择">
                <el-option v-for="dict in dict.type.online_api_method" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="参数类型" prop="parameterType">
              <el-input v-model="form.parameterType" placeholder="如 java.util.Map" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="结果映射" prop="resultMap">
              <el-input v-model="form.resultMap" placeholder="如 BaseResultMap" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="响应类型" prop="resultType">
              <el-select v-model="form.resultType" placeholder="请选择">
                <el-option v-for="dict in dict.type.online_api_result" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="执行器" prop="actuator">
              <el-select v-model="form.actuator" placeholder="请选择">
                <el-option v-for="dict in dict.type.online_api_actuator" :key="dict.value" :label="dict.label" :value="dict.value"/>
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注入userId">
              <el-switch v-model="form.userId" active-value="1" inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="注入deptId">
              <el-switch v-model="form.deptId" active-value="1" inactive-value="0" />
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="鉴权类型" prop="permissionType">
              <el-select v-model="form.permissionType" placeholder="请选择" clearable>
                <el-option label="角色" value="role" />
                <el-option label="权限" value="permission" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="鉴权值" prop="permissionValue">
              <el-input v-model="form.permissionValue" placeholder="请输入鉴权值" />
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item label="SQL语句" prop="sqlText">
          <mybatis-input ref="sqlEditor" v-model="form.sqlText" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <online-preview ref="preview" />
  </div>
</template>

<script>
import { listMb, getMb, delMb, addMb, updateMb, exportMb } from "@/api/online/mb"
import MybatisInput from "./mybatis-input"
import OnlinePreview from "./online-preview"

export default {
  name: "OnlineMb",
  dicts: ['online_api_tag', 'online_api_method', 'online_api_result', 'online_api_actuator'],
  components: { MybatisInput, OnlinePreview },
  data() {
    return {
      loading: true,
      exportLoading: false,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      mbList: [],
      title: "",
      open: false,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        tag: null,
        tagId: null,
        path: null,
        method: null
      },
      form: {},
      rules: {
        tag: [{ required: true, message: "标签名不能为空", trigger: "blur" }],
        tagId: [{ required: true, message: "标签ID不能为空", trigger: "blur" }],
        path: [
          { required: true, message: "请求路径不能为空", trigger: "blur" },
          { pattern: /^[^<>"'|\\]+$/, message: "不能包含非法字符", trigger: "blur" },
          { validator: (rule, value, callback) => { if (value && value[0] !== '/') { callback(new Error('必须以斜杠开头')) } else { callback() } }, trigger: "blur" }
        ],
        method: [{ required: true, message: "请求方式不能为空", trigger: "change" }],
        sqlText: [{ required: true, message: "SQL语句不能为空", trigger: "blur" }]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    getList() {
      this.loading = true
      listMb(this.queryParams).then(response => {
        this.mbList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: null, tag: null, tagId: null, parameterType: null, resultMap: null,
        sqlText: null, path: null, method: null, resultType: null, actuator: null,
        userId: '0', deptId: '0', permissionType: null, permissionValue: null
      }
      this.resetForm("form")
    },
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加在线接口"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getMb(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改在线接口"
      })
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateMb(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addMb(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除选中数据项？').then(function() {
        return delMb(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.$modal.confirm('是否确认导出所有在线接口数据项？').then(() => {
        this.exportLoading = true
        return exportMb(this.queryParams)
      }).then(response => {
        this.$download.name(response.msg)
        this.exportLoading = false
      }).catch(() => {})
    },
    handlePreview(row) {
      getMb(row.id).then(response => {
        this.$refs.preview.open(response.data)
      })
    }
  }
}
</script>
