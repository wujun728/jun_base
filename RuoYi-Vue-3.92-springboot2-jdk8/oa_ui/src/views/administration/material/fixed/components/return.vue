<!-- 物资归还弹窗 -->
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
          <el-form-item label="归还对象:">
            <el-select
              v-model="index"
              filterable
              size="small"
              clearable
              placeholder="请选择归还部门/人员"
              @change="handleSearch"
              @clear="handleClear"
            >
              <el-option
                v-for="(item, index) in options"
                :key="item.value"
                :label="item.name"
                :value="index"
              ></el-option>
            </el-select>
          </el-form-item>
        </el-form>
        <div class="table-box" v-if="tableData.length">
          <el-table
            :data="tableData"
            style="width: 100%"
            row-key="id"
            default-expand-all
            @selection-change="handleSelectionChange"
          >
            <el-table-column type="selection" width="55" />
            <el-table-column prop="name" label="物资名称" min-width="100"></el-table-column>
            <el-table-column prop="number" label="物资编号" min-width="100" />
            <el-table-column prop="units" label="规格型号" min-width="80" />
            <el-table-column prop="cate.cate_name" label="物资分类" min-width="100" />
            <el-table-column prop="specs" label="计量单位" min-width="80" />
            <el-table-column prop="remark" label="重要信息" min-width="200" />
          </el-table>
          <el-form ref="form" class="mt14" label-width="80px">
            <el-form-item label="归还备注:" class="el-input--small">
              <el-input
                v-model="mark"
                type="textarea"
                maxlength="100"
                show-word-limit
                :rows="2"
                resize="none"
                :placeholder="$t('customer.placeholder18')"
              />
            </el-form-item>
          </el-form>
        </div>
        <default-page v-else :min-height="520" :index="14"></default-page>
        <div class="button from-foot-btn fix btn-shadow">
          <el-button @click="handleClose" size="small">{{ $t('public.cancel') }}</el-button>
          <el-button :loading="loading" size="small" type="primary" @click="handleConfirm('ruleForm')">
            {{ $t('public.ok') }}
          </el-button>
        </div>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { storageListApi, storageRecordUserApi, storageRecordSaveApi } from '@/api/administration'
export default {
  name: 'Return',
  components: {
    defaultPage: () => import('@/components/common/defaultPage')
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
      loading: false,
      searchWhere: {
        types: 1,
        page: 0,
        limit: 0,
        status: 1,
        receive: 1
      },
      index: '',
      tableData: [],
      options: [],
      multipleSelection: [],
      mark: ''
    }
  },
  methods: {
    handleClose() {
      this.drawer = false
      this.reset()
    },
    openBox() {
      this.drawer = true
      this.getOptionData()
    },
    reset() {
      this.index = null
      this.tableData = []
      this.mark = ''
    },
    handleSearch(e) {
      if (typeof e === 'number') {
        const data = this.options[e]
        if (data.types === 0) {
          this.searchWhere.frame_id = 0
          this.searchWhere.card_id = data.id
        } else {
          this.searchWhere.card_id = 0
          this.searchWhere.frame_id = data.id
        }
        this.getTableData()
      }
    },
    departmentClose() {
      this.openStatus = false
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    getOptionData() {
      storageRecordUserApi({ status: 1 }).then((res) => {
        this.options = res.data || []
      })
    },
    getTableData() {
      storageListApi(this.searchWhere).then((res) => {
        this.tableData = res.data.list || []
      })
    },
    handleClear() {
      this.tableData = []
    },
    // 提交
    handleConfirm() {
      if (typeof this.index !== 'number') {
        this.$message.error('请选择归还部门/人员')
      } else if (this.multipleSelection.length <= 0) {
        this.$message.error('请选择归还的物资')
      } else {
        let res = []
        this.multipleSelection.map((value) => {
          res.push(value.id)
        })
        const option = this.options[this.index]
        const data = {
          types: 2,
          storage: res,
          mark: this.mark,
          user_type: option.types,
          user_id: option.id
        }
        this.storageRecord(data)
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
          this.$message.success('物资归还成功')
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
  height: calc(100% - 130px);
}
::v-deep .el-select,
::v-deep .el-input-number {
  width: 50%;
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
</style>
