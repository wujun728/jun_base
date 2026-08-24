<style>
._fc-designer {
  height: 100%;
  min-height: 500px;
  overflow: hidden;
  cursor: default;
  position: relative;
}

._fc-designer > .el-main {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 0px;
}
._fc-designer .el-tabs__nav {
  float: none;
  text-align: center !important;
}
._fc-designer .el-tabs__item {
  width: 50% !important;
}

._fc-m .form-create ._fc-l-item {
  /*background: #2E73FF;*/
  width: 100%;
  height: 0px;
  overflow: hidden;
  transition: all 0.3s ease;
  margin-left: 0;
}

._fc-m .form-create .el-form-item__content {
  margin-left: 0 !important;
}

._fc-l,
._fc-m,
._fc-r {
  border-top: 1px solid #ececec;
  box-sizing: border-box;
}

._fc-l-group {
  padding: 0 12px;
}

._fc-l-title {
  font-weight: 600;
  font-size: 14px;
  margin: 6px 0px 5px;
  color: #000;
}

._fc-l-item {
  display: inline-block;
  background: #fff;
  color: #000;
  min-width: 70px;
  width: 50%;
  transition: all 0.2s ease;
  cursor: pointer;
}

._fc-l-item i {
  font-size: 16px;
  color: #999;
  display: inline-block;
}

._fc-l-item ._fc-l-name {
  font-size: 13px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #303133;
}

/*._fc-l-item:hover {*/
/*background: #2E73FF;*/
/*color: #fff;*/
/*}*/

._fc-m-tools {
  height: 40px;
  align-items: center;
  display: flex;
  justify-content: space-between;
  /* border: 1px solid #ececec; */
  border-top: 0 none;
  margin-top: 2px;
}
.edit {
  font-size: 14px;
  font-family: PingFang SC-中粗体, PingFang SC;
  font-weight: 600;
  color: rgba(0, 0, 0, 0.85);
}

._fc-m-tools button.el-button {
  padding: 5px 14px;
  display: inline-block;
  /* display: flex;
  align-items: center; */
}

._fc-m-tools .fc-icon {
  font-size: 13px;
  margin-right: 2px;
  color: #999999;
}

._fc-r .el-tabs__nav-wrap::after {
  height: 1px;
  background-color: #ececec;
}

._fc-r ._fc-r-tabs {
  display: flex;
  padding: 0;
  border-bottom: 1px solid #ececec;
}

._fc-r ._fc-r-tab {
  height: 40px;
  box-sizing: border-box;
  line-height: 40px;
  display: inline-block;
  list-style: none;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
  position: relative;
  flex: 1;
  text-align: center;
}
/*._fc-r ._fc-r-tab.active {*/
/*color: #409EFF;*/
/*border-bottom: 2px solid #409EFF;*/
/*}*/

.drag-box {
  min-height: 60px;
}

._fc-m-drag {
  overflow: auto;
  padding: 20px;
  padding-top: 0px;

  box-sizing: border-box;
}

._fc-m-drag,
.draggable-drag {
  background: #fff;
  height: 100%;
  position: relative;
}

._fc-m-drag > form,
._fc-m-drag > form > .el-row {
  height: 100%;
}
.aside-right::-webkit-scrollbar-thumb {
  -webkit-box-shadow: inset 0 0 6px #ccc;
}
.aside-right::-webkit-scrollbar {
  width: 4px !important; /*对垂直流动条有效*/
}
.drag-center .el-form {
  height: calc(100% - 45px);
}
.dialog .el-form-item--mini .el-form-item__content {
  margin-left: 0px !important;
}
.el-tabs__nav-wrap::after {
  height: 1px;
}
._fc-l-group .el-tabs__item {
  font-size: 14px;
  font-family: PingFang SC-Semibold, PingFang SC;
  font-weight: 600;
}
/* ._fc-l-group .el-tabs__content {
  margin-top: 20px;
} */
.row {
  width: 125px;
  height: 36px;
  background: #f9f9f9;
  border-radius: 4px 4px 4px 4px;
  line-height: 36px;
  padding: 0 10px;
  display: flex;
  justify-content: space-between;
}
._fc-title {
  font-size: 13px;
  font-family: PingFang SC-Medium, PingFang SC;
  font-weight: 600;
  color: #303133;
  margin-bottom: 10px;
}
.handle-r {
  display: flex;
  font-size: 12px;
  font-family: PingFang SC-Regular, PingFang SC;
  font-weight: 400;
  color: #1890ff;
  align-items: center;
  cursor: pointer;
}
.handle-r .views {
  display: flex;
  align-items: center;
}
.handle-r .views-c {
  color: #ed4014;
}
._fc-l-group .el-tabs__item {
  line-height: 38px;
}
.handle-r .iconshanchu1 {
  font-size: 12px;
}
.handle-r .icondakai {
  font-size: 12px;
}
/* .el-dialog__headerbtn {
  position: absolute;
  top: 10px;
  right: 20px;
} */
</style>

<style lang="scss" scoped>
::v-deep .el-dialog__header {
  padding: 24px 20px;
}
</style>
<template>
  <ElContainer class="_fc-designer" :style="'height:' + height_">
    <ElMain>
      <ElContainer style="height: 100%">
        <el-aside class="_fc-l" width="280px">
          <template v-for="(item, index) in menuList">
            <div class="_fc-l-group" :key="index">
              <el-tabs v-model="activeName">
                <el-tab-pane label="控件" name="first" class="mb20 mt20">
                  <draggable
                    :group="{ name: 'default', pull: 'clone', put: false }"
                    :sort="false"
                    :list="item.list"
                    class="pl10"
                  >
                    <div class="_fc-l-item mb20" v-for="(data, index) in item.list" :key="index">
                      <div class="acea-row row-middle row">
                        <span class="_fc-l-name">{{ data.label }}</span>
                        <div class="_fc-l-icon mr5">
                          <i class="fc-icon" :class="data.icon || 'icon-input'"></i>
                        </div>
                      </div>
                    </div>
                  </draggable>
                </el-tab-pane>
                <el-tab-pane label="控件组" class="mt10" name="second">
                  <div class="pl10 _fc-title">人事管理</div>
                  <draggable
                    :group="{ name: 'default', pull: 'clone', put: false }"
                    :sort="false"
                    :list="item.group"
                    class="pl10"
                  >
                    <div class="_fc-l-item mb20" v-for="(data, index) in item.group" :key="index">
                      <div class="acea-row row-middle row">
                        <span class="_fc-l-name">{{ data.label }}</span>
                        <div class="_fc-l-icon mr5">
                          <i class="fc-icon" :class="data.icon || 'icon-input'"></i>
                        </div>
                      </div>
                    </div>
                  </draggable>
                  <div class="pl10 _fc-title">客户管理</div>
                  <draggable
                    :group="{ name: 'default', pull: 'clone', put: false }"
                    :sort="false"
                    :list="item.contract"
                    class="pl10"
                  >
                    <div class="_fc-l-item mb20" v-for="(data, index) in item.contract" :key="index">
                      <div class="acea-row row-middle row">
                        <span class="_fc-l-name">{{ data.label }}</span>
                        <div class="_fc-l-icon mr5">
                          <i class="fc-icon" :class="data.icon || 'icon-input'"></i>
                        </div>
                      </div>
                    </div>
                  </draggable>
                </el-tab-pane>
              </el-tabs>
            </div>
          </template>
        </el-aside>
        <ElContainer class="_fc-m">
          <ElMain style="padding: 0">
            <div class="_fc-m-drag drag-center">
              <div class="_fc-m-tools" height="45">
                <slot name="handle"></slot>
                <div class="edit">编辑字段</div>
                <div class="handle-r">
                  <div @click="previewFc" class="mr20 views"><span class="iconfont icondakai"></span> 预览</div>

                  <div class="views views-c" @click="clearDragRule">
                    <span class="iconfont iconshanchu1"></span> 清空
                  </div>
                </div>
              </div>
              <FormCreate :rule="dragForm.rule" :option="form.value" v-model="dragForm.api"></FormCreate>
            </div>
          </ElMain>
        </ElContainer>
        <ElAside class="_fc-r" width="320px">
          <ElContainer style="height: 100%">
            <ElMain
              class="aside-right"
              v-show="activeTab === 'props'"
              style="padding: 0 20px"
              :key="activeRule ? activeRule._id : ''"
            >
              <div>
                <ElDivider v-if="showBaseRule">表单内容说明</ElDivider>
                <FormCreate
                  v-if="basicForm.rule"
                  :rule="basicForm.rule"
                  :option="basicForm.options"
                  @change="basicChange"
                  @removeField="basicRemoveField"
                ></FormCreate>
                <FormCreate
                  v-else
                  v-model="baseForm.api"
                  :rule="baseForm.rule"
                  :option="baseForm.options"
                  @change="baseChange"
                ></FormCreate>
                <ElDivider v-if="propsForm.rule.length > 0">表单内容配置</ElDivider>
                <FormCreate
                  v-model="propsForm.api"
                  :rule="propsForm.rule"
                  :option="propsForm.options"
                  @change="propChange"
                  @removeField="propRemoveField"
                ></FormCreate>
              </div>
            </ElMain>
          </ElContainer>
        </ElAside>
        <ElDialog :visible.sync="preview.state" width="800px" append-to-body class="dialog">
          <viewForm :rule="preview.rule" :option="preview.option" v-if="preview.state"></viewForm>
        </ElDialog>
      </ElContainer>
    </ElMain>
  </ElContainer>
</template>

<script>
import form from '../config/base/form'
import field from '../config/base/field'
import validate from '../config/base/validate'
import { deepCopy } from '@form-create/utils/lib/deepextend'
import is, { hasProperty } from '@form-create/utils/lib/type'
import { lower } from '@form-create/utils/lib/tocase'
import ruleList from '../config/rule'
import draggable from 'vuedraggable'
import createMenu from '../config/menu'
import { upper } from '../utils/index'
import { designerForm } from '../config/viewForm'
import formCreate from '../config/viewForm'
import { approveHolidayTypeSelectApi } from '@/api/business.js'

export default {
  name: 'FcDesigner',
  components: {
    draggable,
    FormCreate: designerForm.$form(),
    viewForm: formCreate.$form()
  },
  props: ['menu', 'height', 'config', 'condition'],
  computed: {
    height_() {
      const h = this.height
      if (!h) return '100%'
      return is.Number(h) ? `${h}px` : h
    }
  },
  mounted() {
    this.approveHolidayTypeSelect()
  },
  provide: (_) => ({
    fcx: {
      active: null
    }
  }),
  data() {
    const children = []
    return {
      activeName: 'first',
      holidayRule: [],
      cacheProps: {},
      basicCacheProps: {},
      moveRule: null,
      addRule: null,
      added: null,
      activeTab: 'form',
      activeRule: null,
      children,
      menuList: this.menu || createMenu(),
      showBaseRule: false,
      visible: {
        preview: false
      },
      preview: {
        state: false,
        rule: [],
        option: {}
      },
      dragForm: {
        rule: this.makeDragRule(children),
        api: {}
      },
      typeList: [
        'leaveFrom',
        'overtimeFrom',
        'outFrom',
        'refillFrom',
        'tripFrom',
        'contractPayment',
        'contractRenewal',
        'contractExpenditure',
        'issueInvoice',
        'voidedInvoice'
      ],
      gropList: [],
      // validateLonelyGroup: false,
      form: {
        rule: form(),
        option: {
          form: {
            labelPosition: 'top',
            size: 'mini'
          },
          submitBtn: false
        },
        value: {
          form: {
            inline: false,
            hideRequiredAsterisk: false,
            labelPosition: 'top',
            size: 'medium',
            labelWidth: '125px'
          },
          submitBtn: false
        }
      },
      baseForm: {
        rule: field(),
        api: {},
        options: {
          form: {
            labelPosition: 'top',
            size: 'mini'
          },
          submitBtn: false,
          mounted: (fapi) => {
            fapi.activeRule = this.activeRule
            fapi.setValue(fapi.options.formData || {})
          }
        }
      },
      validateForm: {
        rule: validate(),
        api: {},
        options: {
          form: {
            labelPosition: 'top',
            size: 'mini'
          },
          submitBtn: false,
          mounted: (fapi) => {
            fapi.activeRule = this.activeRule
            fapi.setValue(fapi.options.formData || {})
          }
        }
      },

      propsForm: {
        rule: [],
        api: {},
        options: {
          form: {
            labelPosition: 'top',
            size: 'mini'
          },
          submitBtn: false,
          mounted: (fapi) => {
            fapi.activeRule = this.activeRule
            fapi.setValue(fapi.options.formData || {})
          }
        }
      },
      basicForm: {
        rule: [],
        api: {},
        options: {
          form: {
            labelPosition: 'top',
            size: 'mini'
          },
          submitBtn: false,
          mounted: (fapi) => {
            fapi.activeRule = this.activeRule
            fapi.setValue(fapi.options.formData || {})
          }
        }
      }
    }
  },
  watch: {
    'preview.state': function (n) {
      if (!n) {
        this.$nextTick(() => {
          this.preview.rule = this.preview.option = null
        })
      }
    }
  },
  methods: {
    approveHolidayTypeSelect() {
      approveHolidayTypeSelectApi().then((res) => {
        this.holidayRule = res.data
      })
    },
    addMenu(config) {
      if (!config.name || !config.list) return
      let flag = true
      this.menuList.forEach((v, i) => {
        if (v.name === config.name) {
          this.$set(this.menuList, i, config)
          flag = false
        }
      })
      if (flag) {
        this.menuList.push(config)
      }
    },
    removeMenu(name) {
      ;[...this.menuList].forEach((v, i) => {
        if (v.name === name) {
          this.menuList.splice(i, 1)
        }
      })
    },
    setMenuItem(name, list) {
      this.menuList.forEach((v) => {
        if (v.name === name) {
          v.list = list
        }
      })
    },
    appendMenuItem(name, item) {
      this.menuList.forEach((v) => {
        if (v.name === name) {
          v.list.push(item)
        }
      })
    },
    removeMenuItem(item) {
      this.menuList.forEach((v) => {
        let idx
        if (is.String(item)) {
          ;[...v.list].forEach((menu, idx) => {
            if (menu.name === item) {
              v.list.splice(idx, 1)
            }
          })
        } else {
          if ((idx = v.list.indexOf(item)) > -1) {
            v.list.splice(idx, 1)
          }
        }
      })
    },
    addComponent(data) {
      if (Array.isArray(data)) {
        data.forEach((v) => {
          ruleList[v.name] = v
        })
      } else {
        ruleList[data.name] = data
      }
    },
    dragStart(children) {
      this.moveRule = children
      this.added = false
    },
    dragUnchoose(children, evt) {
      this.addRule = {
        children,
        oldIndex: evt.oldIndex
      }
    },
    getParent(rule) {
      let parent = rule.__fc__.parent.rule
      const config = parent.config
      if (config && config.config.inside) {
        rule = parent
        parent = parent.__fc__.parent.rule
      }
      return { root: parent, parent: rule }
    },
    makeDrag(group, tag, children, on, subRule) {
      return {
        type: 'DragBox',
        wrap: {
          show: false
        },
        col: {
          show: false
        },
        inject: true,
        props: {
          rule: {
            props: {
              tag: 'el-col'
            },
            attrs: {
              group: group === true ? 'default' : group,
              ghostClass: 'ghost',
              animation: 150,
              handle: '._fc-drag-btn',
              emptyInsertThreshold: 0,
              direction: 'vertical'
            }
          },
          subRule: subRule || {
            props: {
              name: 'fade',
              tag: 'div'
            }
          },
          tag
        },
        children,
        on
      }
    },
    clearDragRule() {
      this.gropList = []
      this.setRule([])
    },
    makeDragRule(children) {
      return [
        this.makeDrag(
          true,
          'draggable',
          children,
          {
            add: (inject, evt) => this.dragAdd(children, evt),
            end: (inject, evt) => this.dragEnd(children, evt),
            start: (inject, evt) => this.dragStart(children, evt),
            unchoose: (inject, evt) => this.dragUnchoose(children, evt)
          },
          {
            props: {
              name: 'fade',
              tag: 'div'
            }
          }
        )
      ]
    },
    previewFc() {
      this.preview.state = true
      this.preview.rule = this.getRule()
      this.preview.option = this.getOption()
    },
    getRule() {
      let data = this.parseRule(deepCopy(this.dragForm.api.rule[0].children))
      data.forEach((value) => {
        if (value.hasOwnProperty('children')) {
          value.children.forEach((item) => {
            if (item.title === '假期类型') {
              this.$set(item, 'options', this.holidayRule)
            }
          })
        }
      })
      return data
    },
    getJson() {
      return formCreate.toJson(this.getRule())
    },
    getOption() {
      const option = deepCopy(this.form.value)
      delete option.submitBtn
      return option
    },
    setRule(rules) {
      const children = this.loadRule(is.String(rules) ? formCreate.parseJson(rules) : rules)
      this.children = children
      this.clearActiveRule()
      this.dragForm.rule = this.makeDragRule(children)
    },
    clearActiveRule(type) {
      if (this.typeList.indexOf(type) >= 0) {
        this.gropList = []
      }

      this.activeRule = null
      this.activeTab = 'form'
    },
    setOption(option) {
      const _ = option
      _.submitBtn = false
      delete _.resetBtn
      this.form.value = _
    },
    loadRule(rules) {
      const loadRule = []
      rules.forEach((rule) => {
        if (is.String(rule)) {
          return loadRule.push(rule)
        }
        const config = ruleList[rule._fc_drag_tag] || ruleList[rule.type]
        const _children = rule.children
        rule.children = []
        if (rule.control) {
          rule._control = rule.control
          delete rule.control
        }
        if (config) {
          rule = this.makeRule(config, rule)
          if (_children) {
            let children = rule.children[0].children

            if (config.drag) {
              children = children[0].children
            }
            children.push(...(config.loadChildren === false ? _children : this.loadRule(_children)))
          }
        } else if (_children) {
          rule.children = this.loadRule(_children)
        }
        loadRule.push(rule)
      })
      return loadRule
    },
    parseRule(children) {
      return [...children].reduce((initial, rule) => {
        if (is.String(rule)) {
          initial.push(rule)
          return initial
        } else if (rule.type === 'DragBox') {
          initial.push(...this.parseRule(rule.children))
          return initial
        } else if (rule.type === 'DragTool') {
          rule = rule.children[0]
          if (rule.type === 'DragBox') {
            initial.push(...this.parseRule(rule.children))
            return initial
          }
        }
        if (!rule) return initial
        rule = { ...rule }
        if (rule.children.length) {
          rule.children = this.parseRule(rule.children)
        }

        delete rule._id
        delete rule.originChildren
        if (rule.config) {
          delete rule.config.config
        }
        if (rule.effect) {
          delete rule.effect._fc
          delete rule.effect._fc_tool
        }
        if (rule._control) {
          rule.control = rule._control
          delete rule._control
        }
        Object.keys(rule)
          .filter(
            (k) =>
              (Array.isArray(rule[k]) && rule[k].length === 0) ||
              (is.Object(rule[k]) && Object.keys(rule[k]).length === 0)
          )
          .forEach((k) => {
            delete rule[k]
          })
        initial.push(rule)
        return initial
      }, [])
    },
    baseChange(field, value, _, fapi) {
      if (this.activeRule && fapi[this.activeRule._id] === this.activeRule) {
        if (field == 'info') {
          this.activeRule.props.placeholder = value
        } else {
          this.$set(this.activeRule, field, value)
        }
      }
    },
    propRemoveField(field, _, fapi) {
      if (this.activeRule && fapi[this.activeRule._id] === this.activeRule) {
        this.dragForm.api.sync(this.activeRule)
        if (field.indexOf('formCreate') === 0) {
          field = field.replace('formCreate', '')
          if (!field) return
          field = lower(field)
          if (field.indexOf('effect') === 0 && field.indexOf('>') > -1) {
            this.$delete(this.activeRule.effect, field.split('>')[1])
          } else if (field.indexOf('props') === 0 && field.indexOf('>') > -1) {
            this.$delete(this.activeRule.props, field.split('>')[1])
          } else if (field === 'child') {
            this.$delete(this.activeRule.children, 0)
          } else if (field) {
            this.$set(this.activeRule, field, undefined)
          }
        } else {
          this.$delete(this.activeRule.props, field)
        }
      }
    },
    propChange(field, value, _, fapi) {
      if (this.activeRule.children && this.activeRule.children.length > 1) {
        this.activeRule.children.forEach((item) => {
          if (item.type == 'timeFrom') {
            this.$set(item.props, field, value)
          }
        })
      } else {
        if (this.activeRule && fapi[this.activeRule._id] === this.activeRule) {
          if (field.indexOf('formCreate') === 0) {
            field = field.replace('formCreate', '')
            if (!field) return
            field = lower(field)
            if (field.indexOf('effect') === 0 && field.indexOf('>') > -1) {
              this.$set(this.activeRule.effect, field.split('>')[1], value)
            } else if (field.indexOf('props') === 0 && field.indexOf('>') > -1) {
              this.$set(this.activeRule.props, field.split('>')[1], value)
            } else if (field === 'child') {
              this.$set(this.activeRule.children, 0, value)
            } else {
              this.$set(this.activeRule, field, value)
            }
          } else {
            this.$set(this.activeRule.props, field, value)
          }
        }
      }
    },
    basicChange(field, value, _, fapi) {
      if (this.activeRule.children && this.activeRule.children.length > 1) {
        this.activeRule.children.forEach((item) => {
          if (item.type == 'timeFrom') {
            this.$set(item.props, field, value)
          }
        })
      } else {
        if (this.activeRule && fapi[this.activeRule._id] === this.activeRule) {
          if (field.indexOf('formCreate') === 0) {
            field = field.replace('formCreate', '')
            if (!field) return
            field = lower(field)
            if (field.indexOf('effect') === 0 && field.indexOf('>') > -1) {
              this.$set(this.activeRule.effect, field.split('>')[1], value)
            } else if (field.indexOf('props') === 0 && field.indexOf('>') > -1) {
              this.$set(this.activeRule.props, field.split('>')[1], value)
            } else if (field === 'child') {
              this.$set(this.activeRule.children, 0, value)
            } else {
              this.$set(this.activeRule, field, value)
            }
          } else {
            this.$set(this.activeRule.props, field, value)
          }
        }
      }
    },
    basicRemoveField(field, _, fapi) {
      // 确保当前激活的规则存在，并且是当前表单API实例操作的目标
      if (this.activeRule && fapi[this.activeRule._id] === this.activeRule) {
        // 首先同步表单状态
        this.dragForm.api.sync(this.activeRule)

        // 基于字段名移除对应的属性
        if (field.indexOf('formCreate') === 0) {
          field = field.replace('formCreate', '')
          if (!field) return
          field = field.toLowerCase()

          if (field.indexOf('effect') === 0 && field.indexOf('>') > -1) {
            this.$delete(this.activeRule.effect, field.split('>')[1])
          } else if (field.indexOf('props') === 0 && field.indexOf('>') > -1) {
            this.$delete(this.activeRule.props, field.split('>')[1])
          } else if (field === 'child') {
            this.$delete(this.activeRule.children, 0)
          } else if (field) {
            this.$set(this.activeRule, field, undefined)
          }
        } else {
          // 如果字段不是以 'formCreate' 开头，则直接从 activeRule 中移除
          this.$delete(this.activeRule, field)
        }
      }
    },
    validateChange(formData) {
      if (!this.activeRule || this.validateForm.api[this.activeRule._id] !== this.activeRule) return
      this.activeRule.validate = formData.validate || []
      this.dragForm.api.refreshValidate()
      this.dragForm.api.nextTick(() => {
        this.dragForm.api.clearValidateState(this.activeRule.field)
      })
    },
    toolActive(rule) {
      if (this.activeRule) {
        delete this.propsForm.api[this.activeRule._id]
        delete this.baseForm.api[this.activeRule._id]
        delete this.validateForm.api[this.activeRule._id]
        delete this.basicForm.api[this.activeRule._id]
      }
      this.activeRule = rule

      this.$nextTick(() => {
        this.activeTab = 'props'
        this.$nextTick(() => {
          this.propsForm.api[this.activeRule._id] = this.activeRule
          this.basicForm.api[this.activeRule._id] = this.activeRule
          this.baseForm.api[this.activeRule._id] = this.activeRule
          this.validateForm.api[this.activeRule._id] = this.activeRule
        })
      })

      if (!this.cacheProps[rule._id]) {
        this.cacheProps[rule._id] = rule.config.config.props()
      }
      if (rule.config.config.basic()) {
        this.basicCacheProps[rule._id] = rule.config.config.basic()
      }
      this.propsForm.rule = this.cacheProps[rule._id]
      this.basicForm.rule = this.basicCacheProps[rule._id]

      const formData = { ...rule.props, formCreateChild: rule.children[0] }
      Object.keys(rule).forEach((k) => {
        if (['effect', 'config', 'payload', 'id', 'type'].indexOf(k) < 0) formData['formCreate' + upper(k)] = rule[k]
      })
      ;['props', 'effect'].forEach((name) => {
        rule[name] &&
          Object.keys(rule[name]).forEach((k) => {
            formData['formCreate' + upper(name) + '>' + k] = rule[name][k]
          })
      })
      this.propsForm.options.formData = formData
      this.basicForm.options.formData = formData

      this.showBaseRule = hasProperty(rule, 'field') && rule.input !== false

      if (this.showBaseRule) {
        this.baseForm.options.formData = {
          field: rule.field,
          title: rule.title || '',
          info: rule.info,
          _control: rule._control
        }

        this.validateForm.options.formData = { validate: rule.validate ? [...rule.validate] : [] }
      }
    },
    dragAdd(children, evt) {
      const newIndex = evt.newIndex
      const menu = evt.item._underlying_vm_
      if (!menu) {
        if (this.addRule) {
          const rule = this.addRule.children.splice(this.addRule.oldIndex, 1)
          children.splice(newIndex, 0, rule[0])
        }
      } else {
        const rule = this.makeRule(ruleList[menu.name])
        children.splice(newIndex, 0, rule)
      }
      this.added = true
    },
    dragEnd(children, { newIndex, oldIndex }) {
      if (!this.added && !(this.moveRule === children && newIndex === oldIndex)) {
        const rule = this.moveRule.splice(oldIndex, 1)
        children.splice(newIndex, 0, rule[0])
      }
      this.moveRule = null
      this.addRule = null
      this.added = false
    },

    makeRule(config, _rule) {
      // 在所有拖拽项目里面找该config是否属于控件组下的数据
      const dataSources = this.menuList[0]
      if (this.menuList && dataSources && dataSources.group && dataSources.group.length !== 0) {
        // 如果是控件组下的数据
        if (dataSources.group.some((item) => item === config) || dataSources.contract.some((item) => item === config)) {
          // 判断是否有其他控件组数据
          if (this.gropList.length === 0) {
            this.gropList.push(config)
          } else {
            this.$message.error('当前表单最多只能添加一个控件组')
            return
          }
        }
      }

      const rule = _rule || config.rule()
      rule.config = { config }
      if (!rule.effect) rule.effect = {}
      rule.effect._fc = true
      rule._fc_drag_tag = config.name

      let drag

      rule.originChildren = rule.children

      if (config.drag) {
        const children = []
        rule.children.push(
          (drag = this.makeDrag(config.drag, rule.type, children, {
            end: (inject, evt) => this.dragEnd(inject.self.children, evt),
            add: (inject, evt) => this.dragAdd(inject.self.children, evt),
            start: (inject, evt) => this.dragStart(inject.self.children, evt),
            unchoose: (inject, evt) => this.dragUnchoose(inject.self.children, evt)
          }))
        )
        rule.originChildren = children
      }

      if (config.children && !_rule) {
        const child = this.makeRule(ruleList[config.children])
        ;(drag || rule).children.push(child)
      }

      if (config.inside) {
        rule.children = [
          {
            type: 'DragTool',
            props: {
              dragBtn: config.dragBtn !== false,
              children: config.children,
              type: rule.type
            },
            effect: {
              _fc_tool: true
            },
            inject: true,
            on: {
              delete: ({ self }) => {
                const top = this.getParent(self)
                const field = top.parent.field
                if (this.condition && this.condition.indexOf(field) > -1) {
                  this.$message.error('该字段在流程中被使用')
                  return
                }

                this.getParent(self).parent.__fc__.rm()
                this.clearActiveRule(rule.type)
              },
              add: ({ self }) => {
                const top = this.getParent(self)
                top.root.children.splice(
                  top.root.children.indexOf(top.parent) + 1,
                  0,
                  this.makeRule(top.parent.config.config)
                )
              },
              addChild: ({ self }) => {
                const top = this.getParent(self)
                const config = top.parent.config.config
                const item = ruleList[config.children]
                if (!item) return
                ;(!config.drag ? top.parent : top.parent.children[0]).children[0].children.push(this.makeRule(item))
              },
              copy: ({ self }) => {
                const top = this.getParent(self)
                top.root.children.splice(top.root.children.indexOf(top.parent) + 1, 0, formCreate.copyRule(top.parent))
              },
              active: ({ self }) => {
                this.toolActive(this.getParent(self).parent)
              }
            },
            children: rule.children
          }
        ]
        return rule
      } else {
        return {
          type: 'DragTool',
          props: {
            dragBtn: config.dragBtn !== false,
            children: config.children,
            type: rule.type
          },
          effect: {
            _fc_tool: true
          },
          inject: true,
          on: {
            delete: ({ self }) => {
              const field = self.children[0].field
              if (this.condition && this.condition.indexOf(field) > -1) {
                this.$message.error('该字段在流程中被使用')
                return
              }

              self.__fc__.rm()
              this.clearActiveRule(rule.type)
            },
            add: ({ self }) => {
              const top = this.getParent(self)
              top.root.children.splice(
                top.root.children.indexOf(top.parent) + 1,
                0,
                this.makeRule(self.children[0].config.config)
              )
            },
            addChild: ({ self }) => {
              const config = self.children[0].config.config
              const item = ruleList[config.children]
              if (!item) return
              ;(!config.drag ? self : self.children[0]).children[0].children.push(this.makeRule(item))
            },
            copy: ({ self }) => {
              const top = this.getParent(self)
              top.root.children.splice(top.root.children.indexOf(top.parent) + 1, 0, formCreate.copyRule(top.parent))
            },
            active: ({ self }) => {
              this.toolActive(self.children[0])
            }
          },
          children: [rule]
        }
      }
    }
  },
  created() {
    document.body.ondrop = (e) => {
      e.preventDefault()
      e.stopPropagation()
    }
  }
}
</script>
