<!-- 
  @FileDescription: 客户/合同新增的动态表单组件
  功能：提供动态表单渲染，支持多种表单控件类型
-->
<template>
  <div>
    <!-- 表单主体 -->
    <el-form ref="form" :model="ruleForm" :rules="rule" label-width="auto">
      <!-- 遍历表单分组 -->
      <div v-for="(item, itemIndex) in formInfo" :key="itemIndex" class="p20">
        <!-- 分组标题 -->
        <div v-if="item.status == 1" class="from-item-title">
          <span class="title">{{ item.title }}</span>
        </div>

        <!-- 表单字段区域 -->
        <div class="form-box">
          <!-- 遍历表单字段 -->
          <div v-for="(val, index) in item.data" :key="index" class="form-item">
            <el-form-item :prop="val.key">
              <span slot="label" class="label">{{ val.key_name }}：</span>

              <!-- 文本输入框 -->
              <el-input
                v-if="val.input_type === 'input' && val.type === 'text'"
                v-model="ruleForm[val.key]"
                :maxlength="val.max"
                :min="val.min"
                :placeholder="val.placeholder"
                clearable
                size="small"
              />

              <!-- 数字输入框 -->
              <el-input-number
                v-if="val.type === 'number'"
                v-model="ruleForm[val.key]"
                :controls="false"
                :max="val.max"
                :min="val.min"
                :placeholder="val.placeholder"
                :precision="val.decimal_place"
                size="small"
                style="width: 100%"
              />

              <!-- 文本域 -->
              <el-input
                v-if="val.type === 'textarea'"
                v-model="ruleForm[val.key]"
                :autosize="autosize"
                :maxlength="val.max"
                :placeholder="val.placeholder"
                clearable
                show-word-limit
                size="small"
                type="textarea"
              />

              <!-- 级联选择器 -->
              <el-cascader
                v-if="val.input_type === 'select' && val.options_level > 1"
                v-model="ruleForm[val.key]"
                :options="val.options"
                :placeholder="val.placeholder"
                :props="{
                  checkStrictly: false,
                  label: 'label',
                  value: 'value',
                  multiple: val.type !== 'single' && val.type !== 'cascader'
                }"
                clearable
                collapse-tags
                filterable
                size="small"
                style="width: 100%"
              />

              <!-- 下拉选择器 -->
              <el-select
                v-if="val.input_type === 'select' && val.options_level == 1"
                v-model="ruleForm[val.key]"
                :multiple="val.type !== 'single'"
                :placeholder="val.placeholder"
                clearable
                filterable
                size="small"
                style="width: 100%"
              >
                <el-option
                  v-for="el in val.options"
                  :key="el.value"
                  :disabled="el.disabled"
                  :label="el.label"
                  :value="el.value"
                />
              </el-select>

              <!-- 客户标签选择器 -->
              <select-label
                ref="selectLabel"
                v-show="val.key === 'customer_label'"
                :list="treeData"
                :placeholder="val.placeholder"
                :props="{ children: 'children', label: 'name' }"
                :value="Array.isArray(val.value) ? val.value : defaultLabelList"
                style="width: 100%"
                @handleLabelConf="handleLabelConf($event, val)"
              />

              <!-- 单选按钮组 -->
              <el-radio-group v-if="val.type === 'radio'" v-model="ruleForm[val.key]">
                <el-radio 
                  v-for="(el, index) in val.options" 
                  :key="index" 
                  :label="el.value"
                >
                  {{ el.label }}
                </el-radio>
              </el-radio-group>

              <!-- 多选按钮组 -->
              <el-checkbox-group
                v-if="val.type === 'checked'"
                v-model="ruleForm[val.key]"
                :max="val.max"
                :min="val.min"
              >
                <el-checkbox 
                  v-for="(check, checkIndex) in val.options" 
                  :key="checkIndex" 
                  :label="check.value"
                >
                  {{ check.label }}
                </el-checkbox>
              </el-checkbox-group>

              <!-- 日期选择器 -->
              <el-date-picker
                v-if="val.type === 'date'"
                v-model="ruleForm[val.key]"
                :format="'yyyy-MM-dd'"
                :placeholder="val.placeholder"
                :value-format="'yyyy-MM-dd'"
                clearable
                size="small"
                type="date"
              />

              <!-- 文件上传 -->
              <upload-file
                v-if="val.type === 'file'"
                @getVal="getVal($event, val)"
                :maxLength="val.max"
                :only-image="false"
                :value="val.files"
              />

              <!-- 图片上传 -->
              <upload-file
                v-if="val.type === 'images'"
                @getVal="getVal($event, val)"
                :maxLength="val.max"
                :only-image="true"
                :value="val.files"
              />

              <!-- 富文本编辑器 -->
              <ueditor-from
                v-if="val.type === 'oaWangeditor'"
                ref="ueditorFrom"
                :border="true"
                :content="ruleForm[val.key]"
                :height="`400px`"
                @input="ueditorEdit"
              />
            </el-form-item>
          </div>
        </div>
      </div>
    </el-form>

    <!-- 表单操作按钮 -->
    <div class="button from-foot-btn fix btn-shadow">
      <el-button class="el-btn" size="small" @click="resetForm">
        取消
      </el-button>
      
      <el-button 
        v-if="btnShow && types !== 3" 
        :loading="addContractLoading" 
        size="small" 
        @click="addContract"
      >
        {{ type == 'contract' ? '保存并添加回款' : '保存并添加合同' }}
      </el-button>
      
      <el-button 
        :loading="saveLoading" 
        size="small" 
        type="primary" 
        @click="handleConfirm('ruleForm')"
      >
        保存
      </el-button>
    </div>
  </div>
</template>

<script>
import { contractCategorySelectApi, clientConfigLabelApi } from '@/api/enterprise'

export default {
  name: 'OaForm',
  components: {
    uploadFile: () => import('@/components/form-common/oa-upload'),
    ueditorFrom: () => import('@/components/form-common/oa-wangeditor'),
    selectLabel: () => import('@/components/form-common/select-label')
  },
  
  props: {
    // 表单配置信息
    formInfo: {
      type: Array,
      default: () => []
    },
    
    // 表单类型
    type: {
      type: String,
      default: ''
    },
    
    // 表单子类型
    types: {
      type: Number,
      default: 1
    },
    
    // 是否显示附加按钮
    btnShow: {
      type: Boolean,
      default: true
    }
  },

  data() {
    return {
      defaultLabelList: [],    // 默认标签列表
      drawer: true,           // 抽屉状态
      saveLoading: false,      // 保存加载状态
      ruleForm: {},            // 表单数据
      rule: {},                // 表单验证规则
      autosize: {              // 文本域自适应配置
        minRows: 6
      },
      treeData: [],            // 树形数据
      imageList: [],           // 图片列表
      labelAllList: [],        // 全部客户标签
      addContractLoading: false, // 添加合同加载状态
      heightInputRole: 32,      // 输入框高度
      attachList: [],           // 附件列表
      labelList: []             // 选中客户标签
    }
  },

  watch: {
    // 监听表单配置变化
    formInfo: {
      handler(nVal) {
        if (nVal.length == 0) return false
        
        nVal.forEach((item) => {
          item.data.forEach((val) => {
            // 处理客户标签
            if (val.key === 'customer_label') {
              if (this.$refs.selectLabel && val.options.length > 0) return
            }

            // 处理合同客户和分类
            if (val.key === 'contract_customer') {
              val.options_level = 1
            }

            // 处理文件类型
            if (val.type === 'file' && val.files && val.files.length > 0) {
              this.attachList = val.files
            }

            // 处理图片类型
            if (val.type === 'images' && val.files && val.files.length > 0) {
              this.imageList = val.files
            }

            // 处理单选类型
            if (val.type === 'radio') {
              val.value = val.value + ''
            }

            // 设置必填验证规则
            if (val.required == 1) {
              this.rule[val.key] = [{
                required: true,
                message: '请输入' + val.key_name,
                trigger: 'blur'
              }]
            }

            // 设置表单值
            this.$set(this.ruleForm, val.key, val.value)

            // 处理多选类型
            if (val.type === 'checked' && val.key !== 'customer_label') {
              if (val.value === '') {
                this.ruleForm[val.key] = []
              }
            }
          })
        })
      },
      immediate: true,
      deep: true
    },

    // 监听附件列表变化
    attachList: {
      handler(nVal) {
        if (nVal.length > 0) {
          const filekey = this.getKey('file')
          let ids = nVal.map(item => item.id)
          this.ruleForm[filekey] = ids
        }
      }
    },

    // 监听图片列表变化
    imageList: {
      handler(nVal) {
        if (nVal.length > 0) {
          const imgkey = this.getKey('images')
          let ids = nVal.map(item => item.id)
          this.ruleForm[imgkey] = ids
        }
      }
    }
  },

  mounted() {
    this.getTreeData()
  },

  methods: {
    /**
     * 获取上传文件的值
     * @param {Array} val - 文件数组
     * @param {Object} item - 当前字段配置
     */
    getVal(val, item) {
      let arr = val.map(el => el.id)
      this.ruleForm[item.key] = [...this.ruleForm[item.key], ...arr]
    },

    /**
     * 获取合同分类数据
     * @param {Number} index - 表单分组索引
     * @param {Number} index1 - 字段索引
     */
    async getCategory(index, index1) {
      const result = await contractCategorySelectApi()
      this.formInfo[index].data[index1].options = result.data
    },

    /**
     * 处理客户标签选择
     * @param {Object} res - 选择结果
     * @param {Object} val - 当前字段配置
     */
    handleLabelConf(res, val) {
      this.labelList = res.list
      this.ruleForm[val.key] = res.ids
    },

    /**
     * 获取标签树形数据
     */
    getTreeData() {
      clientConfigLabelApi({ page: 0, limit: 0 }).then(res => {
        this.treeData = res.data.list
      })
    },

    /**
     * 富文本编辑回调
     * @param {String} val - 富文本内容
     */
    ueditorEdit(val) {
      let key = this.getKey('oaWangeditor')
      this.ruleForm[key] = val
    },

    /**
     * 获取指定类型的字段key
     * @param {String} row - 字段类型或key
     * @param {String} key - 是否按key查找
     * @returns {String} 字段key
     */
    getKey(row, key) {
      let formKey = ''
      this.formInfo.forEach(item => {
        item.data.forEach(val => {
          if (key && val.key === row) {
            formKey = val.key
          }
          if (val.type === row) {
            formKey = val.key
          }
        })
      })
      return formKey
    },

    /**
     * 重置表单
     */
    resetForm() {
      this.$refs.form.resetFields()
      this.attachList = []
      this.labelList = []
      this.imageList = []
      this.saveLoading = false
      this.addContractLoading = false
      this.$emit('handleClose')
    },

    /**
     * 提交表单
     */
    handleConfirm() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.saveLoading = true
          this.$emit('submitOk', this.ruleForm)
        }
      })
    },

    /**
     * 添加合同
     */
    addContract() {
      this.$refs.form.validate(valid => {
        if (valid) {
          this.addContractLoading = true
          this.$emit('addContinueOk', this.ruleForm)
        }
      })
    }
  }
}
</script>

<style lang="scss" scoped>
/* 表单样式保持不变 */
</style>
