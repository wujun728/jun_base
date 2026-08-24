<!--审批设置-基础配置 -->
<template>
  <div class="basicSetting">
    <el-card class="box-card">
      <div class="setting-container mt14">
        <el-row>
          <el-col v-bind="grid1">&nbsp;</el-col>
          <el-col v-bind="grid2">
            <el-form ref="elForm" :model="examineFrom" :rules="rules" size="medium" label-width="150px">
              <el-form-item :label="$t('business.businessType1')" prop="name">
                <el-input
                  v-model="examineFrom.name"
                  :maxlength="16"
                  show-word-limit
                  :placeholder="$t('business.message1')"
                  clearable
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
              <el-form-item :label="$t('business.businessPic1')" prop="icon">
                <div
                  v-if="examineFrom.icon"
                  class="selIcon mr15"
                  @click.stop="handleIcon"
                  :style="{ backgroundColor: examineFrom.color }"
                >
                  <i class="icon iconfont" :class="examineFrom.icon" style="color: #fff"></i>
                </div>
                <el-popover ref="iconPopover" placement="bottom" width="400" trigger="click">
                  <div v-for="(i, index) in iconList" :key="index" class="icon-item" @click="itemChose(i)">
                    <i class="icon iconfont" :class="i.icon" :style="{ color: i.color }"></i>
                  </div>
                  <el-button slot="reference">{{ $t('business.changeIcon') }}</el-button>
                </el-popover>
              </el-form-item>
              <el-form-item :label="$t('business.businessTypeIn')" prop="flowRemark">
                <el-input
                  v-model="examineFrom.info"
                  type="textarea"
                  :placeholder="$t('business.message3')"
                  :maxlength="100"
                  show-word-limit
                  :autosize="{ minRows: 4, maxRows: 4 }"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
              <el-form-item label="排序：" prop="flowRemark">
                <el-input
                  v-model="examineFrom.sort"
                  type="number"
                  placeholder="请输入排序"
                  :style="{ width: '100%' }"
                ></el-input>
              </el-form-item>
            </el-form>
          </el-col>
          <el-col v-bind="grid1"></el-col>
        </el-row>
      </div>
    </el-card>
  </div>
</template>

<script>
import iconfontList from './iconfontList.js'
export default {
  name: 'BasicSetting',
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
      grid1: {
        xl: 8,
        lg: 6,
        md: 2,
        sm: 24,
        xs: 24
      },
      grid2: {
        xl: 8,
        lg: 12,
        md: 20,
        sm: 24,
        xs: 24
      },
      visible: false,
      selectedIcon: '',
      selectedColor: '',
      examineFrom: {
        name: '',
        icon: '',
        info: '',
        sort: 0,
        color: ''
      },
      rules: {
        name: [
          {
            required: true,
            message: this.$t('business.message1'),
            trigger: 'blur'
          }
        ],
        icon: [
          {
            required: true,
            message: this.$t('business.message2'),
            trigger: 'change'
          }
        ]
      },
      iconList: iconfontList
    }
  },
  computed: {},
  created() {
    if (typeof this.conf === 'object' && this.conf !== null) {
      Object.assign(this.examineFrom, this.conf)
    }
  },
  methods: {
    // 给父级页面提供得获取本页数据得方法
    getData() {
      return new Promise((resolve, reject) => {
        this.$refs['elForm'].validate((valid) => {
          if (!valid) {
            reject({ target: this.tabName })
            return
          }
          resolve({ examineFrom: this.examineFrom, target: this.tabName })
        })
      })
    },
    itemChose(row) {
      this.examineFrom.icon = row.icon
      this.examineFrom.color = row.color
      this.resetForm('elForm', 'icon')
      this.$refs.iconPopover.doClose()
    },
    handleIcon() {
      this.$refs.iconPopover.doShow()
    },
    resetForm(formName, props) {
      this.$refs[formName].validateField(props)
    }
  }
}
</script>

<style scoped lang="scss">
@media only screen and (max-width: 500px) {
  .gridmenu {
    width: 100%;
  }

  .gridmain {
    width: 100%;
  }

  .gridright {
    width: 100%;
  }
}
.basicSetting {
  height: calc(100vh - 130px);
  ::v-deep .el-card {
    height: 100%;
  }
}
.selIcon {
  width: 36px !important;
  height: 36px !important;
  line-height: 36px !important;
}
.icon-item,
.selIcon {
  display: inline-block;
  width: 46px;
  height: 46px;
  text-align: center;
  line-height: 46px;
  cursor: pointer;
  border-radius: 3px;
}
.iconfont {
  font-size: 20px;
}
.icon-item:hover {
  background: #edf7ff;
}
.page {
  width: 100vw;
  height: 100vh;
  //padding-top: $header-height;
  box-sizing: border-box;

  .page__header {
    width: 100%;
    height: 54px;
    /*flex-center()*/
    justify-content: space-between;
    box-sizing: border-box;
    color: white;
    background: #3296fa;
    font-size: 14px;
    position: fixed;
    top: 0;

    .page-actions {
      height: 100%;
      text-align: center;
      line-height: 54px;

      > div {
        height: 100%;
        padding-left: 20px;
        padding-right: 20px;
        display: inline-block;
      }

      i {
        font-size: 22px;
        vertical-align: middle;
      }
    }

    .step-tab {
      display: flex;
      justify-content: center;
      height: 100%;
      position: relative;

      > .step {
        width: 140px;
        line-height: 54px;
        padding-left: 30px;
        padding-right: 30px;
        cursor: pointer;
        position: relative;

        &.ghost-step {
          position: absolute;
          height: 54px;
          left: 0;
          z-index: -1;
          background: #4483f2;
          transition: transform 0.5s;

          &::after {
            content: '';
            border-width: 6px 6px 6px;
            border-style: solid;
            border-color: transparent transparent white;
            position: absolute;
            bottom: 0;
            left: 50%;
            margin-left: -6px;
          }
        }

        &.active > .step-index {
          background: white;
          color: #4483f2;
        }

        > .step-index {
          display: inline-block;
          width: 18px;
          height: 18px;
          border: 1px solid #fff;
          border-radius: 8px;
          line-height: 18px;
          text-align: center;
          box-sizing: border-box;
        }
      }
    }
  }

  .page__content {
    width: 100%;
    height: 100%;
    overflow: hidden;
    box-sizing: border-box;
    background: #f5f5f7;
  }
}

.publish-btn {
  margin-right: 20px;
  color: #3296fa;
  padding: 7px 20px;
  border-radius: 4px;
  font-size: 14px;
}

.github {
  position: fixed;
  bottom: 10px;
  left: 20px;
}
</style>
