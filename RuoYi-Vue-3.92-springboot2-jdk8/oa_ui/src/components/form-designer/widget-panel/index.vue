<template>
  <el-scrollbar class="side-scroll-bar" :style="{ height: scrollerHeight }">
    <div class="panel-container">
      <el-tabs v-model="firstTab" class="no-bottom-margin indent-left-margin">
        <el-tab-pane name="componentLib">
          <span slot="label">{{ i18nt('designer.componentLib') }}</span>

          <el-collapse v-model="activeNames" class="widget-collapse">
            <el-collapse-item name="1" :title="i18nt('designer.containerTitle')">
              <draggable
                tag="ul"
                :list="containers"
                chosenClass="chosen"
                :group="{ name: 'dragGroup', pull: 'clone', put: false }"
                :clone="handleContainerWidgetClone"
                ghost-class="ghost"
                :sort="false"
                :move="checkContainerMove"
                @end="onContainerDragEnd"
                animation="100"
              >
                <li
                  v-for="(ctn, index) in containers"
                  :key="index"
                  class="container-widget-item"
                  :title="ctn.name"
                  @dblclick="addContainerByDbClick(ctn)"
                >
                  <span class="line1">{{ ctn.name }}</span>
                  <!-- <span>T</span> -->
                </li>
              </draggable>
            </el-collapse-item>
            <el-collapse-item v-for="(item, index) in crudList" :name="(index + 2).toString()" :title="item.table_name">
              <draggable
                tag="ul"
                :list="item.field"
                :group="{ name: 'dragGroup', pull: 'clone', put: false }"
                :move="checkFieldMove"
                :clone="handleFieldWidgetClone"
                ghost-class="ghost"
                :sort="false"
                animation="100"
              >
                <li
                  v-for="(fld, index) in item.field"
                  :key="index"
                  class="field-widget-item line1"
                  :title="fld.name"
                  @dblclick="addFieldByDbClick(fld)"
                  v-show="!designer.selectNames.includes(fld.options.name)"
                >
                  <span class="line1">{{ fld.name }}</span>
                </li>
              </draggable>
            </el-collapse-item>
          </el-collapse>
        </el-tab-pane>
      </el-tabs>
    </div>
  </el-scrollbar>
</template>

<script>
import Draggable from 'vuedraggable'
import { containers, basicFields, advancedFields, customFields } from './widgetsConfig'
import { addWindowResizeHandler } from '@/utils/util'
import i18n from '@/utils/i18n'
import axios from 'axios'
// import SvgIcon from '@/components/svg-icon'
import { formCrudList } from '@/api/form'
import index from '@/components/uploadPicture/index.vue'
import { mapGetters } from 'vuex'
import { set } from 'nprogress'
import { init } from 'echarts/lib/echarts'
import { isArray } from 'xe-utils'

export default {
  name: 'FieldPanel',
  mixins: [i18n],
  components: {
    Draggable
    // SvgIcon
  },
  props: {
    designer: Object
  },
  inject: ['getBannedWidgets', 'getDesignerConfig'],
  data() {
    return {
      designerConfig: this.getDesignerConfig(),

      firstTab: 'componentLib',

      scrollerHeight: 0,

      activeNames: ['1', '2', '3', '4'],
      containers,
      basicFields,
      advancedFields,
      customFields,
      containersNames: containers.map((con) => con.type),
      nameArr: []
    }
  },
  watch: {
    'designer.widgetList': {
      handler(val) {
        // this.crudList.forEach((item) => {
        //   item.field.forEach((fld) => {
        //     val.forEach((v) => {
        //       console.log('v', v)
        //       let names = this.crudStatusFilter(v, fld, [])
        //       if (names && names.length) this.nameArr = Array.from(new Set([...this.nameArr, ...names]))
        //       console.log('nameArr', this.nameArr)
        //       if (this.nameArr.includes(fld.options.name)) {
        //         fld.hide = true
        //       } else {
        //         fld.hide = false
        //       }
        //     })
        //   })
        // })
      },
      deep: true
    },
    crudList: {
      handler(val) {},
      deep: true,
      immediate: true
    }
  },
  computed: {
    ...mapGetters(['crudList'])
  },
  created() {
    let id = this.$route.query.id

    this.getCrudList(id)
  },
  mounted() {
    this.loadWidgets()
    this.scrollerHeight = window.innerHeight - 250 + 'px'
    addWindowResizeHandler(() => {
      this.$nextTick(() => {
        this.scrollerHeight = window.innerHeight - 250 + 'px'
      })
    })
  },
  methods: {
    getCrudList(id) {
      formCrudList(id).then((res) => {
        for (let i = 0; i < res.data.length; i++) {
          for (let j = 0; j < res.data[i].field.length; j++) {
            res.data[i].field[j].i = j
            res.data[i].field[j].pidx = i
          }
        }
        setTimeout(() => {
          this.$store.dispatch('app/setFormList', res.data)
        }, 300)
      })
    },
    isBanned(wName) {
      return this.getBannedWidgets().indexOf(wName) > -1
    },

    loadWidgets() {
      this.containers = this.containers
        .map((con) => {
          console.log('con', con)
          return {
            ...con,
            displayName: con.name
          }
        })
        .filter((con) => {
          return !con.internal
        })
    },

    handleContainerWidgetClone(origin) {
      return this.designer.copyNewContainerWidget(origin)
    },

    handleFieldWidgetClone(origin) {
      console.log('handleFieldWidgetClone', origin)
      return this.designer.copyNewFieldWidget(origin)
    },

    checkContainerMove(evt) {
      return this.designer.checkWidgetMove(evt)
    },

    checkFieldMove(evt) {
      return this.designer.checkFieldMove(evt)
    },

    onContainerDragEnd(evt) {
      //console.log('Drag end of container: ')
      //console.log(evt)
    },
    addContainerByDbClick(container) {
      this.designer.addContainerByDbClick(container)
    },

    addFieldByDbClick(widget) {
      this.designer.addFieldByDbClick(widget)
    },

    loadFormTemplate(jsonUrl) {
      this.$confirm(this.i18nt('designer.hint.loadFormTemplateHint'), '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消'
      })
        .then(() => {
          axios
            .get(jsonUrl)
            .then((res) => {
              let modifiedFlag = false
              if (typeof res.data === 'string') {
                modifiedFlag = this.designer.loadFormJson(JSON.parse(res.data))
              } else if (res.data.constructor === Object) {
                modifiedFlag = this.designer.loadFormJson(res.data)
              }
              if (modifiedFlag) {
                this.designer.emitHistoryChange()
              }

              this.$message.success(this.i18nt('designer.hint.loadFormTemplateSuccess'))
            })
            .catch((error) => {
              this.$message.error(this.i18nt('designer.hint.loadFormTemplateFailed') + ':' + error)
            })
        })
        .catch((error) => {
          console.error(error)
        })
    }
  }
}
</script>

<style lang="scss" scoped>
.color-svg-icon {
  -webkit-font-smoothing: antialiased;
  color: #7c7d82;
}

.side-scroll-bar {
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
}
// 展示一行
.line1 {
  overflow: hidden;
  text-overflow: ellipsis; //文本溢出显示省略号
  white-space: nowrap; //文本不会换行
}
div.panel-container {
  padding-bottom: 10px;
}

.no-bottom-margin ::v-deep .el-tabs__header {
  margin-bottom: 0;
}

.indent-left-margin {
  ::v-deep .el-tabs__nav {
    margin-left: 20px;
  }
}

.el-collapse-item ::v-deep ul > li {
  list-style: none;
}

.widget-collapse {
  border-top-width: 0;

  ::v-deep .el-collapse-item__header {
    margin-left: 8px;
    font-weight: bold;
  }

  ::v-deep .el-collapse-item__content {
    padding-bottom: 6px;

    ul {
      padding-left: 10px; /* 重置IE11默认样式 */
      margin: 0; /* 重置IE11默认样式 */
      margin-block-start: 0;
      margin-block-end: 0.25em;
      padding-inline-start: 10px;

      &:after {
        content: '';
        display: block;
        clear: both;
      }

      .container-widget-item,
      .field-widget-item {
        display: flex;
        justify-content: space-between;
        height: 32px;
        line-height: 32px;
        width: 47%;
        float: left;
        margin: 2px 6px 6px 0;
        cursor: move;
        white-space: nowrap;
        text-overflow: ellipsis;
        overflow: hidden;
        color: #303133;
        background: #f9f9f9;
        // border: 1px solid #eee;
        border-radius: 4px;
        padding: 0 8px;
      }

      .container-widget-item:hover,
      .field-widget-item:hover {
        background: #f1f2f3;
        border-color: #1890ff;

        .color-svg-icon {
          color: #409eff;
        }
      }

      .drag-handler {
        position: absolute;
        top: 0;
        left: 160px;
        background-color: #dddddd;
        border-radius: 5px;
        padding-right: 5px;
        font-size: 11px;
        color: #666666;
      }
    }
  }
}

.el-card.ft-card {
  border: 1px solid #8896b3;
}

.ft-card {
  margin-bottom: 10px;

  .bottom {
    margin-top: 10px;
    line-height: 12px;
  }

  .ft-title {
    font-size: 13px;
    font-weight: bold;
  }

  .right-button {
    padding: 0;
    float: right;
  }

  .clear-fix:before,
  .clear-fix:after {
    display: table;
    content: '';
  }

  .clear-fix:after {
    clear: both;
  }
}
.chosen {
  background-color: #409eff !important;
  color: #fff;
}
</style>
@/utils/i18ns
