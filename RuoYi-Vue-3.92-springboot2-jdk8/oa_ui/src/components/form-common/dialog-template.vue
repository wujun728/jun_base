<!-- @FileDescription:考核模板库弹窗页面 -->
<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      :modal="true"
      width="846px"
      :before-close="closeDialog"
      :show-close="true"
    >
      <div slot="title" class="dialog-title">
        <div class="dialog-title-title">{{ title }}</div>
      </div>
      <el-row>
        <el-col :span="4">
          <!-- 考核模板库与指标库导航 -->
          <div class="assess-left">
            <ul class="assess-left-ul">
              <li
                v-for="(item, index) in department"
                :key="index"
                :class="index == tabIndex ? 'active' : ''"
                @click="clickDepart(index, item.id)"
              >
                <span>{{ item.name }}</span>
              </li>
            </ul>
          </div>
        </el-col>
        <el-col :span="13">
          <div class="ml15">
            <div class="content-temp">
              <el-input
                v-model="where.name"
                @change="searchFn"
                size="small"
                placeholder="请输入模板名称"
                class="input-with-select"
              />
              <el-scrollbar>
                <ul class="content-temp-ul">
                  <li
                    v-for="(item, index) in tableData"
                    :key="index"
                    :class="[(index + 1) % 3 ? '' : 'mr0', index == clickIndex ? 'active' : '']"
                    @click="selectList(item, index)"
                    @mouseenter="showEnterItem(index)"
                    @mouseleave="showLeaveItem(index)"
                  >
                    <div class="temp-text" :style="{ color: item.color }">
                      <p class="title textover">{{ item.name }}</p>
                      <p class="caption">{{ item.info }}</p>
                      <p class="icon-star" v-if="item.way === 1">
                        <i class="el-icon-star-on" :class="setIsCollect(item)" @click.stop="statusStar(item)" />

                        <i class="icon-caret-star" />
                      </p>
                    </div>

                    <img v-if="item.cover != ''" :src="item.cover" alt="" />
                    <img v-else src="../../assets/images/default.jpg" alt="" />
                    <div
                      v-if="
                        hoverIndex === index &&
                        item.user !== undefined &&
                        $store.state.user.userInfo.uid === item.user.uid
                      "
                      class="footer"
                      @click.stop="
                        () => {
                          return false
                        }
                      "
                    >
                      <div class="pointer"><span @click.stop="setPreviewImage(item)">设置封面</span></div>
                      <div class="pointer"><span @click.stop="handleEdit(item)">编辑</span></div>
                      <div class="pointer" @click.stop="handleDelete(item)">
                        <span>删除</span>
                      </div>
                    </div>
                  </li>
                </ul>
              </el-scrollbar>
            </div>
            <!--            <el-pagination-->
            <!--              :current-page="where.page"-->
            <!--              :page-size="where.limit"-->
            <!--              :page-sizes="[3, 6, 9]"-->
            <!--              :total="total"-->
            <!--              layout="total, sizes,prev, pager, next, jumper"-->
            <!--              @size-change="handleSizeChange"-->
            <!--              @current-change="pageChange"-->
            <!--            />-->
            <el-pagination
              :page-size="where.limit"
              :current-page="where.page"
              layout="total, prev, pager, next"
              :total="total"
              @current-change="pageChange"
            />
          </div>
        </el-col>
        <el-col :span="7">
          <div class="ml15">
            <div class="template-preview">
              <el-scrollbar style="height: 100%">
                <p class="title">{{ selectTitle }}</p>
                <div v-for="(item, index) in selectDada" :key="index" class="template-preview-list">
                  <p class="caption">{{ item.name }}</p>
                  <ul class="template-preview-ul">
                    <li v-for="(items, indexs) in item.target" :key="indexs">{{ items.name }}</li>
                  </ul>
                </div>
              </el-scrollbar>
            </div>
          </div>
        </el-col>
      </el-row>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDialog">取消</el-button>
        <el-button size="small" type="primary" @click="handleConfirm">确定</el-button>
      </div>
    </el-dialog>
    <preview ref="preview" title="设置封面图" :data="itemData" @handlePreview="handlePreview" />
  </div>
</template>

<script>
import {
  assessTargetCateApi,
  assessTargetCateEditApi,
  assessTargetCateListApi,
  assessTemplateCollectApi,
  assessTemplateEditApi,
  assessTemplateListApi,
  targetCateDeleteApi,
  templateDeleteApi
} from '@/api/enterprise'
export default {
  name: 'SelectTemplate',
  components: {
    preview: () => import('@/views/hr/assess/check/components/preview')
  },
  props: {
    title: {
      type: String,
      default: ''
    },
    remindButton: {
      type: Boolean,
      default: false
    }
  },
  data() {
    return {
      dialogFormVisible: false,
      search: '',
      type: 1,
      where: {
        page: 1,
        limit: 9,
        name: ''
      },
      tableData: [],
      total: 0,
      selectDada: [],
      selectTitle: '',
      clickIndex: -1,
      hoverIndex: -1,
      cateId: 'template',
      itemData: {},
      configData: {
        edit: 1,
        data: {}
      },
      collects: [],
      templateValue: '',
      department: [],
      tabIndex: 0,
      activeValue: '',
      optionValue: {
        id: 'template',
        option: []
      },
      isTemplate: true
    }
  },
  watch: {},
  mounted() {
    this.getTargetCate()
    // this.getTableData()
    this.$nextTick(() => {
      if (this.tableData.length > 0) {
        this.selectList(this.tableData[0], 0)
      }
    })
  },
  methods: {
    // 点击分类获取模版数据
    clickDepart(index, id) {
      this.tabIndex = index
      this.optionValue.id = id
      this.eventOptionData(this.optionValue)
      // this.$emit('eventOptionData', this.optionValue)
    },
    // 编辑窗口显示
    handleShow(value) {
      this.activeValue = value
    },
    // 编辑窗口隐藏
    handleHide() {
      this.activeValue = ''
    },
    // 获取模板分类
    getTargetCate() {
      const data = {
        types: this.type
      }
      assessTargetCateListApi(data).then((res) => {
        res.data === undefined ? (this.department = []) : (this.department = res.data)
        if (this.isTemplate == false) this.department.unshift({ name: '全部', id: '' })
        if (this.type == 1 && this.isTemplate) {
          this.department.unshift({ name: '我的模板', id: 'template' }, { name: '我的收藏', id: 'collect' })
        }
        this.optionDate()
      })
    },
    addCate() {
      this.$modalForm(assessTargetCateApi(this.type)).then(({ message }) => {
        this.getTargetCate()
      })
    },
    addDivsion() {
      this.$modalForm(assessTargetCateEditApi(this.activeValue)).then(({ message }) => {
        this.getTargetCate()
      })
    },
    hanleDelete() {
      this.$modalSure('你确定要删除这条指标模板分类吗').then(() => {
        targetCateDeleteApi(this.activeValue).then((res) => {
          this.getTargetCate()
        })
      })
    },
    optionDate() {
      const option = []
      if (this.department.length <= 0) {
        this.optionValue.option = option
        this.eventOptionData(this.optionValue)
        // this.$emit('eventOptionData', this.optionValue)
      } else {
        this.department.map((value) => {
          option.push({ label: value.name, id: value.id })
        })
        this.optionValue.option = option
        this.eventOptionData(this.optionValue)
        // this.$emit('eventOptionData', this.optionValue)
      }
    },
    openDialog() {
      this.dialogFormVisible = true
      this.getTableData()
    },
    closeDialog() {
      this.selectDada = []
      this.clickIndex = -1
      this.selectTitle = ''
      this.dialogFormVisible = false
    },
    searchFn() {
      this.getTableData(1)
    },
    handleConfirm() {
      if (this.clickIndex < 0) {
        this.$message.error('选择为空')
      } else {
        if (this.remindButton) {
          var selectDada = this.selectDada
          this.$modalSure('确定后将替换原来的内容').then(() => {
            this.configData.edit = 1
            this.configData.data = selectDada
            // this.templateChange(this.configData)
            this.$emit('templateChange', this.configData)
          })
        } else {
          this.configData.edit = 1
          this.configData.data = this.selectDada
          // this.templateChange(this.configData)
          this.$emit('templateChange', this.configData)
        }
        this.closeDialog()
      }
    },
    // 获取列表数据
    getTableData(val) {
      if (val == 1) {
        this.where.page = 1
      }

      var data = {
        page: this.where.page,
        limit: this.where.limit,
        name: this.where.name,
        cate_id: this.cateId
      }
      assessTemplateListApi(data).then((res) => {
        this.tableData = res.data.list
        this.collects = res.data.collects ? res.data.collects : []
        this.total = res.data.count
        if (this.tableData.length > 0) {
          this.selectList(this.tableData[0], 0)
        }
      })
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    setIsCollect(item) {
      if (this.cateId === 'collect') {
        return 'active'
      } else {
        if (this.collects.length === 0) {
          return ''
        } else {
          if (this.collects.includes(item.id)) {
            return 'active'
          } else {
            return ''
          }
        }
      }
    },
    eventOptionData(data) {
      this.cateId = data.id
      this.where.page = 1
      this.clickIndex = -1
      this.selectDada = []
      this.selectTitle = ''
      this.getTableData()
    },
    handleSelectionChange(val) {
      this.multipleSelection = val
    },
    getRowKeys(row) {
      return row.id
    },
    showEnterItem(index) {
      this.hoverIndex = index
    },
    showLeaveItem(index) {
      this.hoverIndex = -1
    },
    setPreviewImage(item) {
      this.itemData = item
      this.$refs.preview.openDialog()
    },
    // 设置 删除 封面图
    handlePreview() {
      this.getTableData()
    },
    handleDelete(item) {
      this.$modalSure('考核模板删除后，将不可修改，你确定要删除吗').then(() => {
        templateDeleteApi(item.id).then((res) => {
          this.getTableData()
        })
      })
    },
    handleEdit(item) {
      assessTemplateEditApi(item.id).then((res) => {
        if (res.data === undefined) {
          this.$message.error('内容为空，不能编辑')
        } else {
          this.configData.edit = 2
          this.configData.data = res.data
          this.$emit('templateChange', this.configData)
          this.closeDialog()
        }
      })
    },
    statusStar(item) {
      assessTemplateCollectApi(item.id)
        .then((res) => {
          if (this.cateId === 'collect') {
            this.selectDada = []
            this.selectTitle = ''
          }
          this.getTableData()
        })
        .catch((error) => {
          item.collect = null
        })
    },
    selectList(row, index) {
      this.clickIndex = index
      this.selectTitle = this.tableData[index].name
      assessTemplateEditApi(row.id, { way: row.way }).then((res) => {
        this.selectDada = res.data ? res.data.info : []
      })
    }
  }
}
</script>
<style lang="scss" scoped>
::v-deep .el-dialog__body {
  padding: 10px !important;
}
.assess-left {
  min-height: 460px;
  max-height: 550px;
  margin-left: -10px;
  overflow: auto;
  border-right: 1px solid #f3f3f3;
  .assess-left-ul {
    list-style: none;
    margin: 0;
    padding: 0;
    //position: relative;
    li {
      font-size: 13px;
      padding: 14px 10px 14px 40px;
      color: #000000;
      cursor: pointer;
      .assess-left-icon {
        color: #aaaaaa;
        font-size: 13px;
      }
      .assess-left-more {
        color: #333333;
        text-align: right;
        position: absolute;
        right: 10px;
        transform: rotate(90deg);
      }
    }
    li.active {
      background-color: #f0fafe;
      border-right: 2px solid #1890ff;
      color: #1890ff;
      .assess-left-icon {
        color: #1890ff;
      }
    }
  }
}
.assess-left::-webkit-scrollbar {
  width: 5px;
  height: 1px;
}
.assess-left::-webkit-scrollbar-thumb {
  /*滚动条里面小方块*/
  border-radius: 5px;
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  background: #f5f5f5;
}
.assess-left::-webkit-scrollbar-track {
  /*滚动条里面轨道*/
  -webkit-box-shadow: inset 0 0 5px rgba(0, 0, 0, 0.2);
  border-radius: 5px;
  background: #f0f2f5;
}
.right-item {
  line-height: 25px;
  cursor: pointer;
  margin-top: 10px;
  &:first-child {
    margin-top: 0;
  }
}
.ml15 {
  margin-left: 15px;
}
.mr0 {
  margin-right: 0 !important;
}
.textover {
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
::v-deep.el-dialog {
  border-radius: 10px;
}
.dialog-title {
  font-size: 14px;
  display: flex;
  align-items: center;
  .dialog-title-title {
    width: 30%;
    font-size: 16px;
    font-family: PingFang SC;
    font-weight: 500;
  }
}
.content-temp {
  height: 550px;
  padding: 0 8px;
  .input-with-select {
    padding-right: 14px;
    margin-bottom: 10px;
  }
  .content-temp-ul {
    list-style: none;
    padding: 0;
    margin: 0;
    display: flex;
    flex-wrap: wrap;
    li {
      width: 132px;
      height: 160px;
      background-color: #f5f5f5;
      display: flex;
      flex-wrap: wrap;
      margin-right: 10px;
      margin-bottom: 10px;
      position: relative;
      border: 2px solid transparent;
      p {
        margin: 0;
        padding: 0;
      }
      img {
        width: 100%;
        height: 100%;
        position: absolute;
        left: 0;
        top: 0;
        z-index: 0;
      }
      .temp-text {
        padding: 20px 10px 10px 10px;
        position: absolute;
        width: 100%;
        height: 100%;
        left: 0;
        top: 0;
        z-index: 1;
        .title {
          font-size: 16px;
          width: 80%;
          margin-bottom: 10px;
        }
        .caption {
          font-size: 14px;
          font-family: PingFang SC;
          font-weight: 400;
        }
        .icon-star {
          width: 20px;
          position: absolute;
          border: 10px solid #fff;
          border-bottom: 8px solid transparent;
          height: 32px;
          text-align: center;

          // position: absolute;
          right: 17px;
          top: 0;
          i {
            position: absolute;
            top: -5px;
            left: -9px;
            color: #000000;
            font-size: 18px;

            cursor: pointer;
          }

          i.active {
            color: #f5c327;
          }
        }
      }
      .footer {
        position: absolute;
        left: 0;
        right: 0;
        bottom: 0;
        padding: 12px 6px;
        background-color: rgba(0, 0, 0, 0.6);
        display: flex;
        justify-content: space-between;
        color: #ffffff;
        font-size: 13px;
        z-index: 9999;
      }
    }
    li.active {
      border: 2px solid #1890ff;
    }
  }
}
.template-preview {
  height: 545px;
  p {
    margin: 0;
    padding: 0;
  }
  .title {
    padding: 14px 0;
    font-size: 16px;
    color: #333333;
    font-weight: 500;
  }
  .template-preview-list {
    margin: 0 10px 0 6px;
    .caption {
      margin: 10px 0 10px 10px;
      font-size: 14px;
      color: #333333;
      position: relative;
    }
    .caption:before {
      content: '';
      position: absolute;
      left: -14px;
      top: 4px;
      width: 5px;
      height: 5px;
      background-color: #333333;
      border-radius: 50%;
    }
    .template-preview-ul {
      border-left: 1px solid #cccccc;
      padding: 10px 0;
      margin: 0;
      list-style: none;
      li {
        font-size: 12px;
        margin: 0 0 14px 20px;
        position: relative;
      }
      li:before {
        content: '';
        position: absolute;
        left: -10px;
        top: 5px;
        width: 5px;
        height: 5px;
        background-color: #333333;
        border-radius: 50%;
      }
      li:last-of-type {
        margin-bottom: 0;
      }
    }
  }
}
.divBox .el-pagination {
  margin-top: 0;
  justify-content: center;
}
</style>
