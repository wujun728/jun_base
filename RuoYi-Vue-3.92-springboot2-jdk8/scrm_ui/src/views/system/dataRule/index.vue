<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch">
      <el-form-item label="模型名称" prop="modelName">
        <el-input
          v-model="queryParams.modelName"
          placeholder="请输入模型名称"
          clearable
          style="width: 240px"
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
          v-hasPermi="['system:dataRule:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:dataRule:remove']"
        >删除</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="ruleList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="模型名称" align="center" prop="modelName" :show-overflow-tooltip="true" width="180" />
      <el-table-column label="适用角色" align="center" :show-overflow-tooltip="true" min-width="200">
        <template slot-scope="scope">
          <span>{{ scope.row.roleNames || '-' }}</span>
        </template>
      </el-table-column>
      <el-table-column label="状态" align="center" width="100">
        <template slot-scope="scope">
          <el-switch
            v-model="scope.row.enable"
            active-value="1"
            inactive-value="0"
            @change="handleStatusChange(scope.row)"
          ></el-switch>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:dataRule:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:dataRule:remove']"
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

    <el-dialog :title="title" :visible.sync="open" width="700px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="是否适用所有角色" prop="applicableAllRoles">
          <el-radio-group v-model="form.applicableAllRoles">
            <el-radio label="0">否</el-radio>
            <el-radio label="1">是</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="适用角色" prop="roleIds" v-if="form.applicableAllRoles === '0'">
          <el-select v-model="form.roleIds" multiple placeholder="请选择适用角色" style="width: 100%">
            <el-option
              v-for="item in roleOptions"
              :key="item.id"
              :label="item.name"
              :value="item.id"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="模型名称" prop="modelName">
          <el-select v-model="form.modelName" placeholder="请选择模型名称" style="width: 100%">
            <el-option
              v-for="item in modelOptions"
              :key="item.code"
              :label="item.name"
              :value="item.code"
            ></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="范围" prop="conditions" required>
          <div class="conditions-wrapper">
            <div class="condition-group" v-for="(condition, index) in form.conditions" :key="index">
              <el-row :gutter="10">
                <el-col :span="7">
                  <el-select v-model="condition.field" placeholder="字段" filterable clearable style="width: 100%">
                    <el-option
                      v-for="item in fieldOptions"
                      :key="item.code"
                      :label="(item.name || item.code) + ' - ' + item.code"
                      :value="item.code"
                    ></el-option>
                  </el-select>
                </el-col>
                <el-col :span="6">
                  <el-select v-model="condition.operator" placeholder="运算符" style="width: 100%">
                    <el-option
                      v-for="item in operatorOptions"
                      :key="item.value"
                      :label="item.label"
                      :value="item.value"
                    ></el-option>
                  </el-select>
                </el-col>
                <el-col :span="8">
                  <el-select
                    v-model="condition.value"
                    placeholder="请输入值或选择变量"
                    filterable
                    allow-create
                    clearable
                    style="width: 100%"
                  >
                    <el-option-group label="内置变量">
                      <el-option
                        v-for="v in variableOptions"
                        :key="v.code"
                        :label="v.code + ' - ' + v.name"
                        :value="v.code"
                      ></el-option>
                    </el-option-group>
                  </el-select>
                </el-col>
                <el-col :span="3" class="condition-actions">
                  <el-button
                    type="danger"
                    icon="el-icon-delete"
                    size="mini"
                    circle
                    @click="removeCondition(index)"
                    v-if="form.conditions.length > 1"
                  ></el-button>
                  <el-button
                    type="primary"
                    icon="el-icon-plus"
                    size="mini"
                    circle
                    @click="addCondition"
                    v-if="index === form.conditions.length - 1"
                  ></el-button>
                </el-col>
              </el-row>
            </div>
          </div>
        </el-form-item>
        <el-form-item label="是否启用" prop="status">
          <el-radio-group v-model="form.status">
            <el-radio label="0">启用</el-radio>
            <el-radio label="1">禁用</el-radio>
          </el-radio-group>
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
  listDataRule,
  getDataRule,
  delDataRule,
  addDataRule,
  updateDataRule,
  exportDataRule,
  getDataRuleModels,
  getDataRuleVariables,
  getDataRuleRoles,
  getDataRuleFields
} from "@/api/system/dataRule"

export default {
  name: "DataRule",
  data() {
    return {
      loading: true,
      ids: [],
      single: true,
      multiple: true,
      showSearch: true,
      total: 0,
      ruleList: [],
      title: "",
      open: false,
      modelOptions: [],
      roleOptions: [],
      variableOptions: [],
      fieldOptions: [],
      operatorOptions: [
        { label: '等于', value: 'EQUAL' },
        { label: '不等于', value: 'NOT_EQUAL' },
        { label: '大于', value: 'GREATER_THAN' },
        { label: '大于等于', value: 'GREATER_THAN_OR_EQUAL' },
        { label: '小于', value: 'LESS_THAN' },
        { label: '小于等于', value: 'LESS_THAN_OR_EQUAL' },
        { label: '左边匹配', value: 'LEFT_LIKE' },
        { label: '右边匹配', value: 'RIGHT_LIKE' }
      ],
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        modelName: undefined
      },
      form: {},
      rules: {
        modelName: [
          { required: true, message: "模型名称不能为空", trigger: "change" }
        ],
        conditions: [
          {
            validator: (rule, value, callback) => {
              if (!value || value.length === 0 || !value[0].field) {
                callback(new Error("至少添加一组条件"))
              } else {
                callback()
              }
            },
            trigger: "change"
          }
        ]
      }
    }
  },
  watch: {
    'form.modelName'(val) {
      if (val) {
        getDataRuleFields(val).then(response => {
          this.fieldOptions = response.data || response.rows || response || []
        })
      } else {
        this.fieldOptions = []
      }
    }
  },
  created() {
    this.getList()
    this.getModelOptions()
    this.getRoleOptions()
    this.getVariableOptions()
  },
  methods: {
    getList() {
      this.loading = true
      listDataRule(this.queryParams).then(response => {
        this.ruleList = (response.rows || []).map(row => {
          if (row.roleIds && this.roleOptions.length) {
            const ids = row.roleIds.split(',')
            row.roleNames = ids.map(id => {
              const role = this.roleOptions.find(r => String(r.id) === String(id))
              return role ? role.name : id
            }).join(', ')
          }
          return row
        })
        this.total = response.total
        this.loading = false
      })
    },
    getModelOptions() {
      getDataRuleModels().then(response => {
        this.modelOptions = response.data || response.rows || response
      })
    },
    getRoleOptions() {
      getDataRuleRoles().then(response => {
        this.roleOptions = response.data || response.rows || response
      })
    },
    getVariableOptions() {
      getDataRuleVariables().then(response => {
        this.variableOptions = response.data || response.rows || response
        this.fieldOptions = this.variableOptions
      })
    },
    cancel() {
      this.open = false
      this.reset()
    },
    reset() {
      this.form = {
        id: undefined,
        modelName: '',
        applicableAllRoles: '0',
        roleIds: [],
        conditions: [{ field: '', operator: 'EQUAL', value: '' }],
        status: '0'
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
    handleStatusChange(row) {
      let text = row.status === "0" ? "启用" : "停用"
      this.$modal.confirm('确认要"' + text + '""' + row.modelName + '"吗？').then(function() {
        return updateDataRule(row)
      }).then(() => {
        this.$modal.msgSuccess(text + "成功")
      }).catch(function() {
        row.status = row.status === "0" ? "1" : "0"
      })
    },
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加数据权限"
    },
    handleUpdate(row) {
      this.reset()
      const id = row.id || this.ids
      getDataRule(id).then(response => {
        const data = response.data || response
        this.form = {
          id: data.id,
          modelName: data.modelName,
          applicableAllRoles: data.applicableAllRoles || '0',
          roleIds: data.roleIds ? (typeof data.roleIds === 'string' ? data.roleIds.split(',') : data.roleIds) : [],
          conditions: data.conditions
            ? (typeof data.conditions === 'string' ? JSON.parse(data.conditions) : data.conditions)
            : [{ field: '', operator: 'EQUAL', value: '' }],
          status: data.status || '0'
        }
        this.open = true
        this.title = "修改数据权限"
      })
    },
    addCondition() {
      this.form.conditions.push({ field: '', operator: 'EQUAL', value: '' })
    },
    removeCondition(index) {
      this.form.conditions.splice(index, 1)
    },
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          const submitData = {
            id: this.form.id,
            applyAllRole: this.form.applicableAllRoles,
            roleIds: this.form.applicableAllRoles === '1' ? '' : Array.isArray(this.form.roleIds) ? this.form.roleIds.join(',') : this.form.roleIds,
            modelName: this.form.modelName,
            rules: JSON.stringify(this.form.conditions),
            enable: this.form.status
          }
          if (this.form.id != null) {
            updateDataRule(submitData).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDataRule(submitData).then(response => {
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
      this.$modal.confirm('是否确认删除数据权限编号为"' + ids + '"的数据项？').then(function() {
        return delDataRule(ids)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    handleExport() {
      this.download('system/dataRule/export', {
        ...this.queryParams
      }, `dataRule_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>

<style scoped>
.conditions-wrapper {
  width: 100%;
}
.condition-group {
  margin-bottom: 10px;
}
.condition-group:last-child {
  margin-bottom: 0;
}
.condition-actions {
  display: flex;
  align-items: center;
  gap: 5px;
}
</style>