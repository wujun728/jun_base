<template>
  <div class="pivot-table-widget" @click.stop="setSelected" v-resize="handleResize">
    <div class="table-box" v-if="tableColumn.length > 0" v-loading="tableLoading">
      <el-table
        size="small"
        :data="tableData"
        :border="true"
        style="width: 100%"
        :max-height="maxHeight"
        :show-summary="cutField.options && cutField.options.setChartConf.showSummary"
        :summary-method="getSummaries"
        class="tableAuto"
      >
        <el-table-column
          v-for="(column, inx) of tableColumn"
          :key="inx"
          show-overflow-tooltip
          :label="column.alias"
          :prop="column.fieldName"
        >
          <template #header>
            <div class="yichu">{{ column.alias }}</div>
          </template>
          <template #default="scope">
            <FormatRow :row="scope.row" :column="column" />
          </template>
        </el-table-column>
      </el-table>
    </div>
    <div class="no-data" v-else>
      请通过右侧
      <span class="lh">显示字段设置</span> 字段栏来添加数据
    </div>
  </div>
</template>

<script>
import FormatRow from '@/components/simpleTable/FormatRow.vue'
import { getDataList } from '@/api/chart'

export default {
  name: 'listTable-widget',
  components: {
    FormatRow
  },
  props: {
    field: {
      type: Object,
      default: () => ({})
    },
    designer: {
      type: Object,
      default: () => ({})
    },
    layout: {
      type: Array,
      default: () => []
    },
    indexOfParentList: {
      type: Number
    }
  },
  data() {
    return {
      cutField: '',
      tableColumn: [],
      tableData: [],
      tableLoading: false,
      fieldsList: [],
      sortFields: [],
      allEntityName: JSON.parse(localStorage.getItem('allEntityName')),
      maxHeight: '320',
      boxHeight: '100%'
    }
  },
  watch: {
    field: {
      handler(newVal) {
        this.cutField = newVal
        this.initOption()
      },
      deep: true,
      immediate: true
    },
    layout: {
      handler(newVal) {
        this.maxHeight = newVal[this.indexOfParentList]?.h * 58 - 60
      },
      deep: true,
      immediate: true
    }
  },
  mounted() {
    this.cutField = this.field
    this.initOption()
  },
  methods: {
    initOption() {
      const { options } = this.cutField
      if (options) {
        const { showFields } = options.setDimensional
        this.tableColumn = [...showFields]
        if (this.tableColumn.length > 0) {
          this.fieldsList = this.tableColumn.map((el) => el.field_name_en)
          this.sortFields = []
          this.tableColumn.forEach((el) => {
            el.prop = this.allEntityName[options.dataEntity] + '_' + el.field_name_en
            if (el.sort) {
              this.sortFields.push({
                fieldName: this.allEntityName[options.dataEntity] + '_' + el.field_name_en,
                type: el.sort
              })
            }
          })
          this.getTableData(options)
        }
      }
    },
    handleResize(data) {
      // console.log(data, 'data')
      // this.maxHeight = data.height + ''
      // this.boxHeight = data.height + 'px'
    },
    // 防抖只执行最后一次窗口大小变化后的样式赋值
    setSelected() {
      this.designer?.setSelected(this.field)
    },

    async getTableData(options) {
      this.tableLoading = true
      let listArr = []
      if (
        options.setChartFilter.list &&
        options.setChartFilter.list.length > 0 &&
        !options.setChartFilter.list[0].obj
      ) {
        options.setChartFilter.list.map((item) => {
          let obj = {
            operator: item.value,
            form_field: item.field,
            value: item.option,
            obj: item
          }
          if (!item.option && item.category === 2) {
            obj.value = []
            item.options.userList.map((i) => {
              obj.value.push(i.value)
            })
          } else if (!item.option && item.category === 1) {
            obj.value = []
            item.options.depList.map((i) => {
              obj.value.push(i.id)
            })
          }
          if (obj.operator === 'between') {
            if (item.type == 'date_time_picker' || item.type == 'date_picker') {
              let data = item.option[0] + '-' + item.option[1]
              obj.value = data
            } else {
              let data = {
                min: item.min,
                max: item.max
              }
              obj.value = data
            }
          }
          listArr.push(obj)
        })
        options.setChartFilter.list = listArr
      }
      const param = {
        uniqued: options.name,
        tableNameEn: this.allEntityName[options.dataEntity],
        type: options.type,
        showField: this.fieldsList,
        limit: options.setChartConf.pageSize,
        page: 1,
        additionalSearch_boolean: options.setChartFilter.additional_search_boolean || '',
        additionalSearch: options.setChartFilter.list || [],
        sortFields: this.sortFields
      }
      const res = await getDataList(param)
      if (res) {
        let list = res.data.list || []
        const showSumcol = this.cutField.options && this.cutField.options.setChartConf.showSumcol
        // 如果需要汇总列
        if (showSumcol && this.tableColumn[this.tableColumn.length - 1].fieldName !== 'sumcol') {
          list.forEach((rowEl) => {
            const values = this.tableColumn.map((item) => Number(rowEl[item.prop]))
            if (!values.every((value) => Number.isNaN(value))) {
              rowEl.sumcol = `${values.reduce((prev, curr) => {
                const value = Number(curr)
                if (!Number.isNaN(value)) {
                  return prev + curr
                } else {
                  return prev
                }
              }, 0)}`
            } else {
              rowEl.sumcol = 'N/A'
            }
            // this.tableData = list
          })
          this.tableColumn.push({
            alias: '汇总',
            fieldName: 'sumcol'
          })
          this.$nextTick(() => {
            this.$set(this, 'tableData', list)
          })
        } else if (showSumcol) {
          return
        } else {
          this.tableData = [...list]
        }
      }
      this.tableLoading = false
    },
    getSummaries(param) {
      const { columns, data } = param
      const sums = []
      this.tableColumn.forEach((column, index) => {
        if (index === 0) {
          sums[index] = '汇总：'
          return
        }
        const values = data.map((item) => Number(item[column.prop || column.fieldName]))
        if (!values.every((value) => Number.isNaN(value))) {
          sums[index] = `${values.reduce((prev, curr) => {
            const value = Number(curr)
            if (!Number.isNaN(value)) {
              const num = prev + curr
              return Number(num.toFixed(2))
            } else {
              return Number(prev.toFixed(2))
            }
          }, 0)}`
        } else {
          sums[index] = 'N/A'
        }
      })
      return sums
    }
  }
}
</script>

<style lang="scss" scoped>
.pivot-table-widget {
  height: calc(100% - 50px);
  width: 100%;
  .table-box {
    width: 100%;
    height: 100%;
  }
}
.no-data {
  font-size: 14px;
  .lh {
    color: var(--el-color-primary);
  }
}

// .el-table {
//     // 解决table组件内容滚动时页面滚动条同步滚动
//     overflow: auto;
//     // 必须设置
//     position: relative;

//     :deep(.el-table__fixed-header-wrapper) thead th > .cell {
//         white-space: nowrap !important; /* 禁止表头换行 */
//     }

//     :deep(.el-table__header-wrapper) thead th > .cell {
//         white-space: nowrap !important; /* 禁止表头换行 */
//     }

//     :deep(.el-table__body-wrapper) {
//         //height: 100% !important;
//     }
// }
// :deep(.el-table .cell) {
//   white-space: nowrap;
// }
</style>
