<template>
  <div class="assess-left">
    <div class="el-card__header">
      <span class="pull-left">晋升表</span>
      <el-tooltip content="添加晋升表类型" effect="dark" placement="top">
        <span class="iconfont icontianjia pull-right" @click="addType()"></span>
      </el-tooltip>
    </div>
    <div class="v-height-flag">
      <div v-height>
        <el-scrollbar style="height: 100%">
          <ul class="assess-left-ul">
            <li
              v-for="(item, index) in department"
              :key="index"
              :class="index == tabIndex ? 'active' : ''"
              @click="clickDepart(index, item)"
            >
              <div>
                <el-tooltip content="显示/隐藏" effect="dark" placement="top">
                  <span v-if="item.status == 1" class="iconfont icondakai" @click.stop="putStatus(item)" />
                  <span v-if="item.status == 0" class="iconfont iconyincang" @click.stop="putStatus(item)" />
                </el-tooltip>

                <span>{{ item.name }}</span>
              </div>

              <el-popover :ref="`pop-${item.id}`" :offset="10" placement="bottom-end" trigger="click">
                <div class="right-item-list">
                  <div class="right-item" @click.stop="addDivsion(item)">{{ $t('public.edit') }}</div>
                  <div class="right-item" @click.stop="handleDelete(item.id)">{{ $t('public.delete') }}</div>
                </div>
                <i slot="reference" class="icon iconfont icongengduo pointer assess-left-more"></i>
              </el-popover>
            </li>
          </ul>
        </el-scrollbar>
      </div>
    </div>
    <!-- 新增晋升表 -->
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
import {
  promotionListApi,
  subPromotionApi,
  putPromotionApi,
  deletePromotionApi,
  promotionShowApi
} from '@/api/config.js'
export default {
  name: 'CrmebOaEntLeft',
  components: { oaDialog: () => import('@/components/form-common/dialog-form') },

  data() {
    return {
      tabIndex: 0,
      department: [],
      fromData: {
        with: '600px',
        title: '新增晋升表',
        btnText: '保存',
        labelWidth: '90px',
        type: ''
      },
      formConfig: [
        {
          type: 'input',
          label: '名称：',
          placeholder: '请输入晋升表名称',
          key: 'name'
        },
        {
          type: 'inputNumber',
          label: '排序：',
          placeholder: '请输入排序',
          key: 'sort'
        }
      ],
      formDataInit: {
        name: '',
        sort: 0
      },
      id: '',
      formRules: {
        name: { required: true, message: '请输入晋升表名称', trigger: 'blur' }
      }
    }
  },

  mounted() {},
  created() {
    this.getList(1)
  },

  methods: {
    async getList(val) {
      const result = await promotionListApi()
      this.department = result.data.list
      if (val == 1) {
        await this.$emit('clickType', this.department[0], val)
      }
    },
    addType() {
      this.formDataInit.name = ''
      this.formDataInit.sort = 1
      this.fromData.title = '新增晋升表'
      this.fromData.type = ''
      this.$refs.oaDialog.openBox()
    },
    clickDepart(index, item) {
      this.tabIndex = index
      this.$emit('clickType', item)
    },
    // 新增
    async submit(data) {
      if (this.fromData.type == 'edit') {
        await putPromotionApi(this.id, data)
        await this.$refs.oaDialog.handleClose()
        this.getList()
      } else {
        await subPromotionApi(data)
        await this.$refs.oaDialog.handleClose()
        this.getList()
        this.tabIndex = 0
        this.$emit('clickType', this.department[0])
      }
    },
    // 编辑
    addDivsion(item) {
      this.formDataInit.name = item.name
      this.formDataInit.sort = item.sort
      this.id = item.id
      this.fromData.title = '编辑晋升表'
      this.fromData.type = 'edit'
      this.$refs.oaDialog.openBox()
    },
    // 删除
    handleDelete(id) {
      this.$modalSure('您确定要删除此条数据吗').then(() => {
        deletePromotionApi(id).then((res) => {
          this.getList(1)
        })
      })
    },
    // 修改状态
    async putStatus(item) {
      let id = item.id
      let status = item.status == 1 ? 0 : 1
      await promotionShowApi(id, { status: status })
      this.getList()
    }
  }
}
</script>

<style lang="scss" scoped>
.pull-left {
  margin-top: 4px;
  font-family: PingFang SC, PingFang SC;
  font-weight: 600;
  font-size: 14px;
  color: #303133;
}
.icontianjia {
  cursor: pointer;
  font-size: 14px;
  color: #1890ff;
}
.assess-left {
  height: 100%;
  margin: -15px 0 -20px -20px;
  padding: 14px 0;
  border-right: 1px solid #eeeeee;
  ::v-deep .el-card__header {
    border-bottom: none;
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding-top: 0;
    padding-bottom: 15px;
    button {
      justify-content: flex-end;
    }
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  overflow: auto;
  .assess-left-ul {
    list-style: none;
    margin: 0;
    padding: 0;
    li {
      height: 40px;
      line-height: 38px;
      font-size: 14px;
      font-family: PingFangSC-Regular, PingFang SC;
      font-weight: 400;
      padding-left: 20px;
      padding-right: 15px;
      color: #303133;
      cursor: pointer;
      display: flex;
      justify-content: space-between;
      .assess-left-more {
        font-size: 14px;
      }
      &.active {
        background-color: rgba(24, 144, 255, 0.08);
        border-right: 2px solid #1890ff;
        color: #1890ff;
        font-weight: 600;
        .assess-left-more {
          color: #1890ff;
        }
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
</style>
