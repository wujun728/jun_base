<!--审批设置-表单配置 -->
<template>
  <div class="formData">
    <el-card class="box-card">
      <form-create-designer ref="designer" :condition="$store.state.business.conditionsFields"></form-create-designer>
    </el-card>
  </div>
</template>
<script>
import formCreateDesigner from './components/form-create-designer/src/index'
export default {
  props: {
    tabName: {
      type: String,
      default: ''
    },
    conf: {
      type: Object,
      default: () => {
        return null
      }
    }
  },
  data() {
    return {
      formData: {}
    }
  },
  components: {
    formCreateDesigner
  },
  mounted() {
    if (typeof this.conf === 'object' && this.conf !== null) {
      this.$refs.designer.setRule(this.conf.props)
    }
  },
  methods: {
    /**
     * 供父组件使用 获取表单JSON
     */
    getData() {
      return new Promise((resolve, reject) => {
        const FcDesignerRule = this.$refs.designer.getJson()
        if (JSON.parse(FcDesignerRule).length === 0) {
          reject({ msg: '您的行容器中没有组件,请添加组件', target: this.tabName })
          return
        }

        resolve({
          formData: {
            form: {},
            props: JSON.parse(FcDesignerRule)
          }
        })
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.formData {
  height: calc(100vh - 130px);
  ::v-deep.el-aside,
  ::v-deep._fc-l-group {
    padding: 0;
  }
  ::v-deep._fc-l,
  ::v-deep._fc-m,
  ::v-deep._fc-r {
    border-top: none;
    background: #fff;
  }
  ::v-deep.el-aside::-webkit-scrollbar {
    display: none;
  }
  ::v-deep._fc-designer {
    min-height: calc(100vh - 142px);
  }
  ::v-deep ._fc-m-drag::-webkit-scrollbar-thumb {
    -webkit-box-shadow: inset 0 0 6px #ccc;
  }
  ::v-deep ._fc-m-drag::-webkit-scrollbar {
    width: 4px !important; /*对垂直流动条有效*/
  }
  ::v-deep ._fc-m {
    border-left: 1px solid #eeeeee;
    border-right: 1px solid #eeeeee;
  }
}
.box-card {
  height: 100%;
  ::v-deep.el-card__body {
    padding-top: 0;
  }
}
</style>
