<!-- 物资领用弹窗 -->
<template>
  <div class="station">
    <el-drawer
      :title="formData.title"
      :visible.sync="drawer"
      :direction="direction"
      :modal="true"
      :wrapper-closable="false"
      :before-close="handleClose"
      :append-to-body="true"
      :size="formData.width"
    >
      <div class="invoice">
        <el-form ref="form" label-width="80px">
          <el-form-item label="选择方式：">
            <el-radio-group size="small" v-model="type">
              <el-radio label="0">按人员</el-radio>
              <el-radio label="1">按部门</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="领用对象：" class="el-input--small">
            <select-member
              v-if="type == 0"
              :only-one="true"
              :value="userList || []"
              @getSelectList="getSelectList"
              style="width: 100%"
            ></select-member>

            <select-department
              v-if="type == 1"
              :only-one="true"
              :value="frames || []"
              @changeMastart="changeMastart"
              style="width: 100%"
            ></select-department>
          </el-form-item>
        </el-form>
        <div class="table-box">
          <el-table :data="tableData" style="width: 100%" row-key="id" default-expand-all>
            <el-table-column prop="id" label="序号" min-width="45">
              <template slot-scope="scope">{{ scope.$index + 1 }}</template>
            </el-table-column>
            <el-table-column prop="name" label="物资名称" min-width="150">
              <template slot-scope="scope">
                <el-select
                  v-model="scope.row.name"
                  size="small"
                  filterable
                  placeholder="请选择物资"
                  @change="handleName($event, scope.$index)"
                >
                  <el-option
                    v-for="(item, index) in formData.selectData"
                    :key="index"
                    :value="index"
                    :label="
                      item.name + ' 规格型号: ' + item.units + (formData.type === 1 ? ' 物资编号: ' + item.number : '')
                    "
                    :disabled="ids.includes(item.id) && formData.type === 1"
                  ></el-option>
                </el-select>
              </template>
            </el-table-column>
            <el-table-column prop="number" v-if="formData.type === 1" label="物资编号" min-width="150" />
            <el-table-column prop="units" label="规格型号" min-width="80" />
            <el-table-column prop="cate.cate_name" label="物资分类" min-width="80" />
            <el-table-column prop="specs" label="计量单位" min-width="80" />
            <el-table-column prop="stock" v-if="formData.type === 0" label="库存数量" min-width="80" />
            <el-table-column prop="number" v-if="formData.type === 0" label="领用数量" min-width="150">
              <template slot-scope="scope">
                <el-input-number
                  v-model="scope.row.sNumber"
                  controls-position="right"
                  :precision="0"
                  size="small"
                  :min="1"
                  :max="scope.row.stock"
                ></el-input-number>
              </template>
            </el-table-column>
            <el-table-column label="" min-width="40">
              <template slot-scope="scope">
                <i class="el-icon-remove color-tab pointer" @click="handleRemove(scope.$index)"></i>
              </template>
            </el-table-column>
          </el-table>
        </div>
        <div class="mt14">
          <el-button type="text" icon="el-icon-plus" @click="addRow()">添加行</el-button>
        </div>
        <el-form ref="form" label-width="80px">
          <el-form-item label="备 注：" class="el-input--small">
            <el-input
              v-model="mark"
              type="textarea"
              maxlength="200"
              show-word-limit
              :rows="2"
              resize="none"
              :placeholder="$t('customer.placeholder18')"
            />
          </el-form-item>
        </el-form>
        <div class="button from-foot-btn fix btn-shadow">
          <el-button @click="handleClose" size="small">{{ $t('public.cancel') }}</el-button>
          <el-button :loading="loading" size="small" type="primary" @click="handleConfirm()">
            {{ $t('public.ok') }}
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { storageRecordSaveApi } from '@/api/administration'

export default {
  name: 'Receive',
  components: {
    selectMember: () => import('@/components/form-common/select-member'),
    selectDepartment: () => import('@/components/form-common/select-department')
  },
  props: {
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      drawer: false,
      direction: 'rtl',
      type: '0',
      userList: [], // 考勤组成员
      frames: [], // 考勤组部门
      tableData: [{ name: '' }, { name: '' }, { name: '' }],
      selectedFn: [],
      mark: '',
      ids: [],
      loading: false
    }
  },
  methods: {
    handleClose() {
      this.drawer = false
      this.resize()
    },
    openBox() {
      this.drawer = true
    },

    resize() {
      this.userList = [] // 选择成员
      this.frames = []
      this.mark = ''
      this.tableData = [{ name: '' }, { name: '' }, { name: '' }]
    },

    close() {
      this.activeDepartment = null
      this.openStatus = false
    },
    getSelectList(data) {
      this.userList = data
    },
    // 选择成员完成回调
    changeMastart(data) {
      this.frames = data
    },

    cardTag(type, index) {
      this.type == 0 ? this.userList.splice(index, 1) : this.frames.splice(index, 1)
    },
    handleRemove(index) {
      this.tableData.splice(index, 1)
      this.getSelectChange()
    },
    addRow() {
      this.tableData.push({})
    },
    // 提交
    handleConfirm() {
      if (this.type == 0 && this.userList.length <= 0) {
        return this.$message.error('请选择人员')
      } else if (this.type == 1 && this.frames.length <= 0) {
        return this.$message.error('请选择部门')
      } else {
        var user_type = null
        var user_id = null
        var storage = []
        if (this.type == 0) {
          // 人员
          user_type = 0
          user_id = this.userList[0].value
        }
        if (this.type == 1) {
          // 部门
          user_type = 1
          user_id = this.frames[0].id
        }
        this.tableData.map((value) => {
          if (this.formData.type === 0) {
            // 消耗物资
            storage.push({ id: value.id, num: value.sNumber, types: 0 })
          } else {
            // 固定物资
            storage.push({ id: value.id, num: 1, types: 1 })
          }
        })
        const data = {
          types: 1,
          user_type: user_type,
          user_id: user_id,
          storage: storage,
          mark: this.mark
        }
        this.storageRecord(data)
      }
    },
    getInfo() {
      if (this.tableData.length <= 0) {
        return true
      } else {
        var i = 0
        this.tableData.map((value) => {
          if (!value.sNumber) {
            i++
          }
        })
        return i > 0
      }
    },
    handleName(e, index) {
      const data = JSON.parse(JSON.stringify(this.formData.selectData[e]))
      data.sNumber = 0
      this.$set(this.tableData, [index], data)
      this.getSelectChange()
    },
    getSelectChange() {
      if (this.tableData && this.tableData.length > 0) {
        this.ids = []
        this.tableData.forEach((value) => {
          this.ids.push(value.id)
        })
      }
    },
    storageRecord(data) {
      this.loading = true
      storageRecordSaveApi(data)
        .then((res) => {
          if (res.status == 200) {
            this.handleClose()
            this.$emit('isOk')
            this.resize()
          }
          this.loading = false
        })
        .catch((error) => {
          this.loading = false
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.station ::v-deep.el-drawer__body {
  padding: 20px 20px 50px 20px;
}

.invoice {
  margin: 20px;
  // height: calc(100% - 40px);
  .from-foot-btn button {
    width: auto;
    height: auto;
  }
}
.table-box {
  // height: calc(100% - 130px);
}
::v-deep .el-select,
::v-deep .el-input-number {
  width: 100%;
}
.flex-box {
  span {
    margin-right: 6px;
  }
  span:last-of-type {
    margin-left: 0;
  }
}
.dialog-footer {
  padding-top: 20px;
  border-top: 1px solid #e6ebf5;
  text-align: right;
}
::v-deep .el-radio-group {
  // margin-top: 10px;
}
</style>
