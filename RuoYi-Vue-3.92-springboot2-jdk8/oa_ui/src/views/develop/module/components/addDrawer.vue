<template>
  <div>
    <el-drawer
      :title="this.id > 0 ? '编辑' : '新增'"
      :visible.sync="drawer"
      direction="rtl"
      :show-close="true"
      :wrapper-closable="true"
      :append-to-body="true"
      :wrapperClosable="false"
      :before-close="handleClose"
      :size="`55%`"
    >
      <div class="mt14" v-if="importTemplate.widgetList.length">
        <VFormRender
          v-if="drawer"
          ref="preForm"
          :form-json="importTemplate"
          :form-data="testFormData"
          :preview-state="true"
          :option-data="testOptionData"
          :global-dsv="designerDsv"
        >
        </VFormRender>

        <div class="button from-foot-btn fix btn-shadow">
          <el-button size="small" class="el-btn" @click="handleClose">取消</el-button>
          <el-button size="small" type="primary" :loading="loading" @click="saveFn">保存</el-button>
        </div>
      </div>
      <div v-else>
        <default-page :index="16" :min-height="420" />
      </div>
    </el-drawer>
  </div>
</template>
<script>
import VFormRender from '@/components/form-render/index'
import defaultPage from '@/components/common/defaultPage'
import { crudModuleCreateApi, crudModuleSaveDataApi, crudModuleUpdateApi } from '@/api/develop'

export default {
  name: '',
  components: { VFormRender, defaultPage },
  props: {
    keyName: {
      type: String,
      default: ''
    },
    crud: {
      type: Object,
      default: () => {}
    }
  },
  data() {
    return {
      drawer: false,
      importTemplate: {
        formConfig: {},
        widgetList: []
      },
      title: '新增',
      id: 0, // 当前列表的id值
      designer: {},
      testFormData: {},
      loading: false,
      testOptionData: {
        select62173: [
          { label: '01', value: 1 },
          { label: '22', value: 2 },
          { label: '333', value: 3 }
        ],

        select001: [
          { label: '辣椒', value: 1 },
          { label: '菠萝', value: 2 },
          { label: '丑橘子', value: 3 }
        ]
      }
    }
  },
  computed: {
    designerDsv() {
      return this.globalDsv
    }
  },

  methods: {
    async openBox(id, data) {
      if (id) {
        this.id = id
      } else {
        this.$set(this, 'testFormData', {})
      }
      let obj = {}
      if (this.crud) {
        obj.crud_id = this.crud.crud_id
        obj.crud_value = this.crud.crud_value
      } else {
        obj = {}
      }
      obj.id = this.id

      const res = await crudModuleCreateApi(this.keyName, obj)
      if (res.data.form_info && res.data.form_info.global_options) {
        this.$set(this.importTemplate, 'formConfig', res.data.form_info.global_options)
      }
      this.$set(this.importTemplate, 'widgetList', res.data.form_info.options)
      if (data) {
        this.$set(this, 'testFormData', data.module_info)
      }
      this.drawer = true
    },

    saveFn() {
      this.$refs['preForm']
        .getFormData()
        .then((formData) => {
          if (this.id > 0) {
            this.upladData(this.id, formData)
          } else {
            this.savaData(formData)
          }
        })
        .catch((error) => {
          this.$message.error(error)
        })
    },

    savaData(data) {
      this.loading = true
      if (this.crud) {
        data.crud_id = this.crud.crud_id
        data.crud_value = this.crud.crud_value
      }
      crudModuleSaveDataApi(this.keyName, data)
        .then((res) => {
          if (res.status == 200) {
            this.handleClose()
            this.$emit('getList')
          }
          this.loading = false
        })
        .catch((err) => {
          this.loading = false
        })
    },

    upladData(id, data) {
      this.loading = true
      if (this.crud) {
        data.crud_id = this.crud.crud_id
        data.crud_value = this.crud.crud_value
      }
      crudModuleUpdateApi(this.keyName, id, data)
        .then((res) => {
          if (res.status == 200) {
            this.handleClose()
            this.$emit('getList')
          }
          this.loading = false
        })
        .catch((err) => {
          this.loading = false
        })
    },

    handleClose() {
      // this.$refs['preForm'].resetForm()
      this.drawer = false
      this.id = 0
    }
  }
}
</script>
<style scoped lang="scss">
::v-deep.el-drawer__body {
  padding: 10px 20px 50px 20px;
}
.p10 {
  width: 100%;
  padding: 0 10px;
}
</style>
