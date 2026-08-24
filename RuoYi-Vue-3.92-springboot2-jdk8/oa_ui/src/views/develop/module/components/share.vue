<template>
  <div>
    <el-drawer
      title="数据共享协作"
      :visible.sync="drawer"
      direction="rtl"
      :show-close="true"
      :wrapper-closable="true"
      :append-to-body="true"
      :before-close="handleClose"
      :size="`55%`"
    >
      <div class="table-box">
        <div class="flex-between mb10">
          <div class="title-16">协作人列表</div>
          <el-button type="primary" size="small" @click="addShare">添加协作人</el-button>
        </div>
        <el-table :data="tableData" style="width: 100%" :height="height">
          <el-table-column prop="user.name" label="协作人员"> </el-table-column>
          <el-table-column prop="name" label="人员权限" width="280">
            <template slot-scope="scope">
              <el-select
                v-model="scope.row.role_type"
                placeholder="请选择"
                size="small"
                @change="roleTypeFn($event, scope.row.id)"
              >
                <el-option v-for="item in options" :key="item.value" :label="item.label" :value="item.value">
                </el-option>
              </el-select>
            </template>
          </el-table-column>
          <el-table-column prop="operate.name" label="操作人"> </el-table-column>
          <el-table-column prop="updated_at" label="最后操作时间" width="150"> </el-table-column>
          <el-table-column prop="address" label="操作" width="120">
            <template slot-scope="scope">
              <el-button type="text" @click="delModuleShare(scope.row.id)">删除</el-button>
            </template>
          </el-table-column>
        </el-table>
        <div class="page-fixed">
          <el-pagination
            :page-size="where.limit"
            :current-page="where.page"
            :page-sizes="[15, 20, 30]"
            layout="total,sizes, prev, pager, next, jumper"
            :total="count"
            @size-change="handleSizeChange"
            @current-change="pageChange"
          />
        </div>
      </div>
    </el-drawer>
    <oa-dialog
      ref="oaDialog"
      :formConfig="formConfig"
      :formDataInit="formDataInit"
      :formRules="formRules"
      :fromData="fromData"
      @submit="submit"
    ></oa-dialog>
  </div>
</template>
<script>
import { moduleShareListApi, delModuleShareApi, putModuleShareApi, moduleShareApi } from '@/api/develop'
import oaDialog from '@/components/form-common/dialog-form'
export default {
  name: '',
  components: { oaDialog },
  props: {},
  data() {
    return {
      drawer: false,
      keyName: '',
      count: 0,
      tableData: [],
      disabledList: [],
      height: `calc(100vh - 200px)`,
      options: [
        { label: '仅可查看', value: 0 },
        { label: '可查看、编辑', value: 1 },
        { label: '可查看、编辑、删除', value: 2 }
      ],
      formConfig: [
        {
          type: 'user_id',
          label: '选择人员：',
          placeholder: '请选择人员（多选）',
          key: 'user_ids',
          disabledList: [],
          disabled: true,
          only_one: false
        },
        {
          type: 'select',
          label: '共享权限：',
          placeholder: '请选择共享权限',
          key: 'role_type',
          options: [
            { label: '仅可查看', value: '0' },
            { label: '可查看、编辑', value: '1' },
            { label: '可查看、编辑、删除', value: '2' }
          ]
        }
      ],
      formDataInit: {
        user_id: ''
      },
      dropdownType: '',
      formRules: {
        user_ids: [{ required: true, message: '请选择人员', trigger: 'blur' }],
        role_type: [{ required: true, message: '请选择共享权限', trigger: 'blur' }]
      },
      fromData: {
        width: '600px',
        title: '数据共享协作',
        btnText: '确定',
        labelWidth: '100px',
        type: ''
      },
      rowData: {},
      where: {
        page: 1,
        limit: 15,
        data_id: 0
      }
    }
  },

  methods: {
    getList() {
      moduleShareListApi(this.keyName, this.rowData.id, this.where).then((res) => {
        this.tableData = res.data.list
        this.count = res.data.count
        if (this.tableData.length > 0) {
          this.disabledList = this.tableData.map((item) => item.user_id)
          this.formConfig[0].disabledList = this.disabledList
        }
      })
    },
    openBox(keyName, row) {
      this.keyName = keyName
      this.rowData = row
      this.getList()
      this.drawer = true
    },
    // 添加
    addShare() {
      this.$refs.oaDialog.openBox()
    },

    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    pageChange(val) {
      this.where.page = val
      this.getList()
    },
    submit(data) {
      let obj = {
        ids: [this.rowData.id],
        user_ids: data.user_ids,
        role_type: data.role_type
      }
      moduleShareApi(this.keyName, obj).then((res) => {
        if (res.status == 200) {
          this.$refs.oaDialog.handleClose()
          this.getList()
        }
      })
    },

    // 删除
    delModuleShare(id) {
      let data = {
        data_id: this.rowData.id
      }
      this.$modalSure('您确认要删除此数据吗').then(() => {
        delModuleShareApi(this.keyName, id, data).then((res) => {
          this.getList()
        })
      })
    },
    roleTypeFn(e, id) {
      putModuleShareApi(this.keyName, id, { role_type: e }).then((res) => {})
    },
    handleClose() {
      this.drawer = false
    }
  }
}
</script>
<style scoped lang="scss">
.table-box {
  padding: 20px;
}
</style>
