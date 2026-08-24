<template>
  <div>
    <el-table :data="tableData" tooltip-effect="light" :header-cell-style="{ background: 'rgba(245, 245, 245, .3)' }">
      <el-table-column v-for="(item, index) in tableLabel" show-overflow-tooltip :key="index" :label="item.name">
        <template slot-scope="scope">
          <template v-if="!item.slot">
            {{ scope.row[item.prop] }}
          </template>
          <template v-else>
            <span v-if="item.name == '任职状态'">{{ scope.row.status ? $t('hr.onob') : $t('hr.dimission') }}</span>
          </template>
        </template>
      </el-table-column>
      <el-table-column prop="address" :label="$t('public.operation')">
        <template slot-scope="scope">
          <el-button type="text" @click="handlePut(scope.row, scope.$index)">编辑</el-button>
          <el-button type="text" @click="handleDelete(scope.row, scope.$index)">{{ $t('public.delete') }}</el-button>
        </template>
      </el-table-column>
    </el-table>
  </div>
</template>

<script>
import {
  enterpriseWorkEditApi,
  getWorkList,
  enterpriseWorkDeleteApi,
  enterpriseEducationApi,
  educationDeleteApi,
  getEducationList
} from '@/api/enterprise'
import {
  userDeleteWork,
  userDeleteEducation,
  userPutWork,
  userPutEducation,
  userWorkList,
  userEducationList
} from '@/api/user'
export default {
  name: 'Table',
  props: {
    tableData: {
      type: Array,
      default: () => {
        return []
      }
    },
    tableLabel: {
      type: Array,
      default: () => {
        return []
      }
    },
    tableName: {
      type: String,
      default: ''
    },
    personId: {
      type: [String, Number],
      default: ''
    },
    id: {
      type: [String, Number, Boolean],
      default: () => false
    },
    user: {
      type: String,
      default: ''
    },
    type: {
      type: String,
      default: 'edit'
    }
  },

  methods: {
    // 删除
    handleDelete(row, index) {
      this.$modalSure(this.$t('hr.deletetitle')).then(() => {
        if (this.user == '个人简历') {
          if (this.tableName === 'works') {
            userDeleteWork(row.id).then((res) => {
              this.tableData.splice(index, 1)
            })
          }
          if (this.tableName === 'education') {
            userDeleteEducation(row.id).then((res) => {
              this.tableData.splice(index, 1)
            })
          }
        } else if (this.type === 'edit') {
          if (this.tableName === 'works') {
            let data = {
              card_id: this.id
            }
            enterpriseWorkDeleteApi(row.id, data).then((res) => {
              this.tableData.splice(index, 1)
            })
          }

          if (this.tableName === 'education') {
            educationDeleteApi(row.id).then((res) => {
              this.tableData.splice(index, 1)
            })
          }
        } else if (this.type === 'add') {
          if (this.tableName === 'works') {
            this.$emit('deleteWork', row, index, this.tableName)
          }
          if (this.tableName === 'education') {
            this.$emit('deleteWork', row, index, this.tableName)
          }
        }
      })
    },
    // 编辑
    handlePut(row, index) {
      if (this.user == '个人简历') {
        if (this.tableName === 'works') {
          this.$modalForm(userPutWork(row.id)).then((res) => {
            this.userWorkList()
          })
        }
        if (this.tableName === 'education') {
          this.$modalForm(userPutEducation(row.id)).then((res) => {
            this.userEducationList()
          })
        }
      } else if (this.type === 'edit') {
        if (this.tableName === 'works') {
          this.$modalForm(enterpriseWorkEditApi(row.id)).then((res) => {
            this.getEditWorkList()
          })
        }
        if (this.tableName === 'education') {
          this.$modalForm(enterpriseEducationApi(row.id)).then((res) => {
            this.getEducationList()
          })
        }
      } else if (this.type === 'add') {
        if (this.tableName === 'works') {
          this.$emit('putWork', row, index, this.tableName)
        }
        if (this.tableName === 'education') {
          this.$emit('putWork', row, index, this.tableName)
        }
      }
    },

    // 刷新工作经历列表
    async userWorkList() {
      const result = await userWorkList()
      this.tableData = result.data.list
    },

    // 刷新个人档案工作经历列表
    async getEditWorkList() {
      let data = {
        user_id: this.id
      }
      const result = await getWorkList(data)
      this.tableData = result.data.list
    },

    // 刷新个人档案教育经历列表
    async getEducationList() {
      let data = {
        user_id: this.id
      }
      const result = await getEducationList(data)
      this.tableData = result.data.list
    },

    // 刷新教育经历列表
    async userEducationList() {
      const result = await userEducationList()
      this.tableData = result.data.list
    }
  }
}
</script>

<style>
.el-tooltip__popper {
  max-width: 300px;
  border: none !important;
  font-size: 13px !important;
  line-height: 22px;
  font-family: Helvetica Neue, Helvetica, PingFang SC, Hiragino Sans GB, Microsoft YaHei, Arial, sans-serif;
  box-shadow: 0px 2px 14px 0px rgba(0, 0, 0, 0.1) !important;
}
</style>
<style scoped lang="scss">
::v-deep .el-table .cell {
  padding-right: 0;
}
::v-deep .el-textarea__inner {
  width: 92%;
}
</style>
