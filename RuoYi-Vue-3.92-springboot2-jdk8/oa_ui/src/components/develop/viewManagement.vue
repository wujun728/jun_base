<!-- @FileDescription: 低代码-新建编辑视图管理-->
<template>
  <div class="oa-dialog">
    <el-dialog
      top="10%"
      :visible.sync="show"
      width="700px"
      append-to-body
      :show-close="false"
      :close-on-click-modal="false"
    >
      <div slot="title" class="header flex-between">
        <span class="title">视图管理</span>
        <span class="el-icon-close" @click="handleClose"></span>
      </div>
      <div class="flex mb20 flex-between">
        <div>
          <el-input
            v-model="where.title"
            prefix-icon="el-icon-search"
            size="small"
            placeholder="请输入视图名称(10个字以内)"
            clearable
            style="width: 300px"
            class="input"
            @change="changeList"
          ></el-input>
        </div>
        <el-button type="primary" size="small" @click="addViewFn">新建视图</el-button>
      </div>

      <!-- 内容 -->
      <ul class="content-title" v-if="list.length > 0">
        <li>
          <p>顺序</p>
          <p>视图名称</p>
          <p>类型</p>
          <p>操作</p>
        </li>
        <ul class="content-body">
          <draggable
            v-model="list"
            chosen-class="chosen"
            force-fallback="true"
            group="people"
            animation="1000"
            @start="onStart"
            @end="onEnd"
          >
            <transition-group>
              <li v-for="(item, index) in list" :key="index">
                <p><i class="icon iconfont icontuodong item-drag"></i></p>
                <p class="text">{{ item.senior_title }}</p>
                <p class="text">{{ item.senior_type == 0 ? '个人' : '公共' }}</p>
                <p>
                  <span class="iconfont iconbianji1 mr10" @click.stop="editFn(item)"></span>
                  <span class="el-icon-delete" @click.stop="delFn(item, index)"></span>
                </p>
              </li>
            </transition-group>
          </draggable>
        </ul>
      </ul>
      <div v-if="list.length == 0">
        <default-page :index="19" :height="`200px`" :imgWidth="`117px`"></default-page>
      </div>
    </el-dialog>
    <!-- 新建视图 -->
    <oa-dialog
      ref="oaDialog"
      :fromData="fromData"
      :formConfig="formConfig"
      :formRules="formRules"
      :formDataInit="formDataInit"
      @submit="submit"
    ></oa-dialog>
  </div>
</template>
<script>
import defaultPage from '@/components/common/defaultPage'
import draggable from 'vuedraggable'
import oaDialog from '@/components/form-common/dialog-form'
import { crudSeniorSaveApi, crudSeniorDelApi, crudSeniorSortApi } from '@/api/develop'
export default {
  name: '',
  components: {
    oaDialog,
    draggable,
    defaultPage
  },
  props: {
    keyName: {
      type: String,
      default: ''
    },
    view_search_boolean: {
      type: Number,
      default: 1
    },
    senior_search: {
      type: Array,
      default: []
    },
    list: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      show: false,
      fromData: {
        width: '500px',
        title: '新建视图',
        btnText: '确定',
        labelWidth: '100px',
        type: ''
      },
      formDataInit: {
        senior_title: '',
        senior_type: '0'
      },
      formConfig: [
        {
          type: 'input',
          label: '视图名称：',
          placeholder: '请输入视图名称(10个字以内)',
          key: 'senior_title'
        },
        {
          type: 'radio',
          label: '视图类型：',
          placeholder: '请选择视图类型',
          key: 'senior_type',
          options: [
            {
              value: '个人',
              label: '0'
            },
            {
              value: '公共',
              label: '1'
            }
          ]
        }
      ],
      formRules: {
        senior_title: [
          {
            required: true,
            message: '请输入视图名称',
            trigger: 'blur'
          },
          { min: 0, max: 10, message: '最多输入10个字', trigger: 'blur' }
        ],
        senior_type: [
          {
            required: true,
            message: '请选择视图类型',
            trigger: 'change'
          }
        ]
      },

      where: {
        title: ''
      }
    }
  },

  mounted() {},
  methods: {
    handleClose() {
      this.show = false
    },
    addViewFn() {
      this.formDataInit = {
        senior_title: '',
        senior_type: '0'
      }
      this.fromData.title = '新建视图'
      this.$refs.oaDialog.openBox()
    },
    delFn(item) {
      this.$modalSure('确定删除此数据').then(() => {
        crudSeniorDelApi(this.keyName, item.id).then((res) => {
          if (res.status == 200) {
            this.$emit('getViewList')
          }
        })
      })
    },
    editFn(item) {
      this.formDataInit = {
        senior_title: item.senior_title,
        senior_type: item.senior_type + '',
        id: item.id,
        search_boolean: item.view_search_boolean,
        senior_search: item.senior_search
      }
      this.fromData.title = '编辑视图'
      this.$refs.oaDialog.openBox()
    },
    onStart() {},
    onEnd() {
      let obj = {
        id: []
      }
      this.list.map((item) => {
        obj.id.push(item.id)
      })
      crudSeniorSortApi(this.keyName, obj).then((res) => {
        this.$emit('getViewList')
      })
    },
    changeList() {
      this.$emit('getViewList', this.where.title)
    },
    submit(data) {
      data.sort = 0
      if (!data.id) {
        data.search_boolean = this.view_search_boolean
        data.senior_search = this.senior_search
      }
      data.search_boolean = this.view_search_boolean
      data.senior_search = this.senior_search
      if (data.senior_type == 2) {
        data.senior_type = 0
      }
      crudSeniorSaveApi(this.keyName, data).then((res) => {
        if (res.status == 200) {
          this.$emit('getViewList')
          this.$refs.oaDialog.handleClose()
        }
      })
    },
    openBox() {
      this.show = true
    }
  }
}
</script>
<style scoped lang="scss">
.oa-dialog {
  .header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    border-bottom: 1px solid #000;

    .el-icon-close {
      color: #c0c4cc;
      font-weight: 500;
      font-size: 14px;
    }
  }

  .content {
    max-height: calc(100vh - 420px);
    overflow-y: auto;
  }
  .content::-webkit-scrollbar {
    height: 0;
    width: 0;
  }
  .content:first-child {
    padding: 0 20px;
  }

  .vertical {
    display: flex;
    flex-direction: column;
  }
  .add-type {
    display: flex;
    justify-content: flex-start;
  }

  .dialog-footer {
    display: flex;
    justify-content: flex-end;
  }
  ::v-deep.el-dialog {
    border-radius: 6px;
  }

  ::v-deep .el-button--medium {
    padding: 10px 15px;
  }
}

ul {
  list-style: none;
  padding: 0;

  li {
    padding: 10px 0;
    display: flex;
    justify-content: space-between;
    align-items: center;

    p {
      width: calc(100% / 4);
      padding-left: 20px;

      &:first-of-type {
        padding-left: 0;
      }
      &:nth-of-type(2) {
        width: calc(100% / 2);
      }
      &:last-of-type {
        width: calc(100% / 8);
      }
    }
  }
}
.iconbianji1 {
  font-size: 14px;
}
.el-icon-delete {
  font-size: 14px;
}
.content-title {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #9e9e9e;
}
.el-icon-delete {
  margin-left: 6px;
  cursor: pointer;
}
.iconfont {
  cursor: pointer;
}
.text {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 13px;
  color: #303133;
}
</style>
