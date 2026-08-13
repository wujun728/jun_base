<template>
  <el-row>
    <el-col :span="6">
      <div class="app-container">
        <div id="treeBox" class="filter-container">
          <el-table
            v-loading="loading"
            :data="treeList"
            :show-header="false"
            row-key="id"
            default-expand-all
            :tree-props="{children: 'children', hasChildren: 'hasChildren'}"
            @row-click="getListByTreeId">
            <el-table-column prop="name" label="名称" sortable/>
          </el-table>
        </div>
      </div>
    </el-col>
    <el-col :span="18">
      <div class="app-container">
        <el-form :model="queryParams" ref="queryForm" :inline="true" v-show="showSearch" label-width="68px">
          <el-form-item label="部门名称" prop="name">
            <el-input
              v-model="queryParams.name"
              placeholder="请输入部门名称"
              clearable
              size="small"
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
              v-hasPermi="['system:treeandtable:add']"
            >新增
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="success"
              plain
              icon="el-icon-edit"
              size="mini"
              :disabled="single"
              @click="handleUpdate"
              v-hasPermi="['system:treeandtable:edit']"
            >修改
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="danger"
              plain
              icon="el-icon-delete"
              size="mini"
              :disabled="multiple"
              @click="handleDelete"
              v-hasPermi="['system:treeandtable:remove']"
            >删除
            </el-button>
          </el-col>
          <el-col :span="1.5">
            <el-button
              type="warning"
              plain
              icon="el-icon-download"
              size="mini"
              :loading="exportLoading"
              @click="handleExport"
              v-hasPermi="['system:treeandtable:export']"
            >导出
            </el-button>
          </el-col>
          <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
        </el-row>

        <el-table v-loading="loading" :data="treeandtableList" @selection-change="handleSelectionChange">
          <el-table-column type="selection" width="55" align="center"/>
          <el-table-column label="部门名称" align="center" prop="name"/>
          <el-table-column label="标签" align="center" prop="tag"/>
          <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
            <template slot-scope="scope">
              <el-button
                size="mini"
                type="text"
                icon="el-icon-edit"
                @click="handleUpdate(scope.row)"
                v-hasPermi="['system:treeandtable:edit']"
              >修改
              </el-button>
              <el-button
                size="mini"
                type="text"
                icon="el-icon-delete"
                @click="handleDelete(scope.row)"
                v-hasPermi="['system:treeandtable:remove']"
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

        <!-- 添加或修改左树右表对话框 -->
        <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
          <el-form ref="form" :model="form" :rules="rules" label-width="80px">
            <el-form-item label="部门名称" prop="name">
              <el-input v-model="form.name" placeholder="请输入部门名称"/>
            </el-form-item>
            <el-form-item label="区域" prop="areaId">
              <treeselect v-model="form.areaId" :options="treeList" :normalizer="normalizer" placeholder="请选择区域" />
            </el-form-item>
            <el-form-item label="标签" prop="tag">
              <el-input v-model="form.tag" placeholder="请输入标签"/>
            </el-form-item>
            <el-form-item label="备注" prop="remark">
              <el-input v-model="form.remark" placeholder="请输入备注"/>
            </el-form-item>
          </el-form>
          <div slot="footer" class="dialog-footer">
            <el-button type="primary" @click="submitForm">确 定</el-button>
            <el-button @click="cancel">取 消</el-button>
          </div>
        </el-dialog>
      </div>
    </el-col>
  </el-row>
</template>

<script>
import { listTreeandtable, getTreeandtable, delTreeandtable, addTreeandtable, updateTreeandtable, exportTreeandtable } from '@/api/demo/treeAndTable/treeAndTable'
import { listTreeTable } from '@/api/demo/treeTable/treeTable'
import Treeselect from "@riophae/vue-treeselect";
import "@riophae/vue-treeselect/dist/vue-treeselect.css";

export default {
  name: 'TreeAndTable',
  components: {
    Treeselect
  },
  data() {
    return {
      // 遮罩层
      loading: true,
      // 导出遮罩层
      exportLoading: false,
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
      // 左树右表表格数据
      treeandtableList: [],
      treeList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        name: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        tenantId: [
          {required: true, message: '区域不能为空', trigger: 'blur'}
        ]
      }
    }
  },
  created() {
    this.getTreeList()
    this.getList()
  },
  methods: {
    /** 查询树形表格列表 */
    getTreeList() {
      this.loading = true
      listTreeTable().then(response => {
        this.treeList = this.handleTree(response.data, 'id', 'parentId')
        this.loading = false
      })
    },
    /** 查询左树右表列表 */
    getList() {
      this.loading = true
      listTreeandtable(this.queryParams).then(response => {
        this.treeandtableList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    getListByTreeId(data) {
      this.queryParams.areaId = data.id
      this.getList()
    },
    /** 转换树形表格数据结构 */
    normalizer(node) {
      if (node.children && !node.children.length) {
        delete node.children;
      }
      return {
        id: node.id,
        label: node.name,
        children: node.children
      };
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        name: null,
        type: null,
        tag: null,
        areaId: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        delFlag: null,
        createBy: null,
        tenantId: null
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.id)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.form.areaId = this.queryParams.areaId
      this.title = '添加左树右表'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getTreeandtable(id).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改左树右表'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.id != null) {
            updateTreeandtable(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addTreeandtable(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const ids = row.id || this.ids
      this.$modal.confirm('是否确认删除左树右表编号为"' + ids + '"的数据项？').then(function () {
        return delTreeandtable(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      const queryParams = this.queryParams
      this.$modal.confirm('是否确认导出所有左树右表数据项？').then(() => {
        this.exportLoading = true
        return exportTreeandtable(queryParams)
      }).then(response => {
        this.$download.name(response.msg)
        this.exportLoading = false
      }).catch(() => {
      })
    }
  }
}
</script>
