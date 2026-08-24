<template>
  <!-- 查看账目详情侧滑弹窗 -->
  <div>
    <el-drawer :before-close="handleClose" :visible.sync="drawer" direction="rtl" size="450px" title="查看账目详情">
      <el-tabs v-model="tabIndex" tab-position="top" type="border-card" @tab-click="handleClick">
        <el-tab-pane label="账目信息" name="1" />
        <el-tab-pane label="操作记录" name="2" />

        <!-- 账目信息 -->
        <div class="contract-body">
          <div v-if="tabIndex == 1">
            <el-form label-width="auto">
              <div class="form-box">
                <div class="form-item">
                  <el-form-item>
                    <span slot="label">收支时间：</span>
                    <p>{{ delData.edit_time }}</p>
                  </el-form-item>
                  <el-form-item>
                    <span slot="label">账目类型：</span>
                    <p>{{ delData.types == 0 ? '支出' : '收入' }}</p>
                  </el-form-item>

                  <el-form-item>
                    <span slot="label">收支金额(元)：</span>
                    <p>{{ delData.num }}</p>
                  </el-form-item>
                  <el-form-item>
                    <span slot="label">收支方式：</span>
                    <p>{{ delData.pay_type }}</p>
                  </el-form-item>
                  <el-form-item>
                    <span slot="label">账目分类：</span>
                    <p>{{ delData.cate ? delData.cate.name : '--' }}</p>
                  </el-form-item>
                  <el-form-item>
                    <span slot="label">备注：</span>
                    <p class="remarks-wrap">
                      {{ delData.client_bill ? delData.client_bill.mark : delData.mark }}
                    </p>
                  </el-form-item>
                  <el-form-item>
                    <span slot="label">数据来源：</span>
                    <p>{{ delData.link_id > 0 ? '合同账目' : '手动添加' }}</p>
                  </el-form-item>
                  <el-form-item>
                    <span slot="label">客户名称：</span>
                    <p>{{ delData.client ? delData.client.customer_name : '--' }}</p>
                  </el-form-item>
                  <el-form-item>
                    <span slot="label">合同名称：</span>
                    <p>{{ delData.contract ? delData.contract.title : '--' }}</p>
                  </el-form-item>

                  <el-form-item>
                    <span slot="label">付款单号：</span>
                    <p>{{ delData.client_bill ? delData.client_bill.bill_no : '--' }}</p>
                  </el-form-item>
                  <el-form-item>
                    <span slot="label">业务员：</span>
                    <p>
                      {{
                        delData.user
                          ? delData.user.name
                          : delData.client && delData.client.card
                          ? delData.client.card.name
                          : '--'
                      }}
                    </p>
                  </el-form-item>
                  <el-form-item>
                    <span slot="label">付款凭证：</span>

                    <upload-file
                      v-if="delData.attachs && delData.attachs.length > 0"
                      :only-image="false"
                      :onlyRead="true"
                      :value="delData.attachs"
                    ></upload-file>
                    <!-- <div v-if="imageUrl">
                      <img :src="imageUrl" alt="" class="attachsImg" @click="handlePictureCardPreview(imageUrl)" />
                    </div> -->
                    <div v-else>--</div>
                  </el-form-item>
                  <el-form-item>
                    <span slot="label">创建时间：</span>
                    <p>{{ delData.created_at }}</p>
                  </el-form-item>
                </div>
              </div>
            </el-form>
          </div>

          <!-- 操作记录 -->
          <div v-if="tabIndex == 2" class="invoice-body1">
            <div v-if="recordList.length == 0" class="default">
              <img alt="" class="img" src="../../../../../assets/images/defd.png" />
              <div class="text">暂无操作记录</div>
            </div>
            <el-steps v-if="recordList.length !== 0" :active="1" class="set" direction="vertical" space="130px">
              <el-step v-for="(item, index) in recordList" :key="index">
                <div slot="icon">
                  <span class="iconfont iconfapiaoxiangqing-caozuojilu"></span>
                </div>
                <div slot="description">
                  <div :class="item.operation_name == '申请开票' ? 'removeBorderLine' : ''" class="operationBox">
                    <div class="header">
                      <div class="left">{{ item.operation_name }}</div>
                      <div class="right">
                        {{ item.card.name }}
                        <el-divider direction="vertical" />
                        {{ item.created_at }}
                      </div>
                    </div>

                    <div v-if="item.operation_name !== '申请开票'" class="footer">
                      <el-form :row-style="{ height: '32px' }" class="description" label-width="100px">
                        <el-form-item v-for="(details, index) in item.operation" :key="index" :label="details.name">
                          <template v-if="details.val.after">
                            <img
                              v-if="details.val.before"
                              :src="details.val.before"
                              alt=""
                              class="item-img"
                              @click="handlePictureCardPreview(details.val.before)"
                            />
                            <span v-else>--</span>
                            <span class="ml10 mr10"> 改为</span>
                            <img
                              v-if="details.val.after"
                              :src="details.val.after"
                              alt=""
                              @click="handlePictureCardPreview(details.val.after)"
                              class="item-img"
                            />
                            <span v-else>--</span>
                          </template>

                          <span v-else class="content">{{ details.val }}</span>
                        </el-form-item>
                      </el-form>
                    </div>
                  </div>
                </div>
              </el-step>
            </el-steps>
          </div>
        </div>
      </el-tabs>
    </el-drawer>
    <el-image-viewer v-if="isImage" :on-close="closeImageViewer" :url-list="srcList" />
  </div>
</template>
<script>
import { billRecordApi } from '@/api/enterprise'
import ElImageViewer from 'element-ui/packages/image/src/image-viewer'
import uploadFile from '@/components/form-common/oa-upload'

export default {
  name: 'viewDetails',
  components: { ElImageViewer, uploadFile },
  props: {
    formData: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      drawer: false,
      tabIndex: '1',
      imageUrl: '',
      delData: {},
      recordList: [],
      isImage: false,
      srcList: []
    }
  },
  computed: {},
  watch: {
    formData: {
      handler(nVal, oVal) {
        this.delData = nVal.data

        if (this.delData.attachs && this.delData.attachs.length > 0) {
          this.delData.attachs.forEach((item) => {
            item.name = item.real_name
          })
        }
      },
      deep: true
    }
  },

  methods: {
    handelOpen() {
      this.drawer = true
    },

    handlePictureCardPreview(val) {
      this.srcList.push(val)
      this.isImage = true
    },
    closeImageViewer() {
      this.isImage = false
      this.srcList = []
    },
    // 获取操作记录
    operationRecord() {
      billRecordApi(this.delData.id).then((res) => {
        this.recordList = res.data.list
      })
    },
    handleClose() {
      this.drawer = false
      this.tabIndex = '1'
      this.imageUrl = ''
    },
    handleClick(e) {
      if (this.tabIndex == '2') {
        this.operationRecord()
      }
    }
  }
}
</script>
<style lang="scss" scoped>
.attachsImg {
  width: 50px;
  height: 50px;
}
.item-img {
  width: 48px;
  height: 48px;
  border-radius: 4px;
  cursor: pointer;
}
.default {
  width: 500px;
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 200px;
  .img {
    width: 200px;
    height: 150px;
  }
  .text {
    font-size: 14px;
    font-family: PingFangSC-Regular, PingFang SC;
    font-weight: 400;
    color: #c0c4cc;
  }
}
.contract-body {
  padding: 20px;
  height: calc(100vh - 124px);
  overflow-y: auto;
}

.description ::v-deep .el-form-item {
  margin-bottom: 10px;
}

::v-deep .el-form-item__label {
  font-size: 13px !important;
  font-weight: 400;
  color: #909399 !important;
}
.set {
  ::v-deep .el-step__icon.is-text {
    border: none;
  }
  .iconfapiaoxiangqing-caozuojilu {
    font-size: 13px;
    color: #1890ff;
  }
  ::v-deep .el-step__line {
    width: 1px;
    background-color: #ebeef4;
  }
  ::v-deep .el-step__icon {
    margin-top: 20px;
    height: 12px;
  }
  ::v-deep .el-step.is-vertical .el-step__line {
    top: 20px;
    bottom: -18px;
  }
}
.invoice-body1 {
  padding: 0 30px 0px 15px !important;
}

.operationBox {
  margin-bottom: 35px;
  width: 380px !important;
  border-radius: 4px 4px 4px 4px;
  border: 1px solid #eaf4ff;
  .header {
    padding: 13px 20px;
    height: 46px;
    background: #f7fbff;
    display: flex;
    justify-content: space-between;

    .left {
      font-size: 14px;

      font-family: PingFang SC-中黑体, PingFang SC;
      font-weight: 600;
      color: #303133;
    }
    .right {
      font-size: 13px;
      font-family: PingFang SC-常规体, PingFang SC;
      font-weight: normal;
      color: #909399;
    }
  }
  .footer {
    padding: 20px 20px 20px 0px;
    ::v-deep .el-form-item {
      // margin-bottom: 12px;
    }
    ::v-deep .el-form-item__label {
      line-height: 18px;
    }
    ::v-deep.el-form-item__content {
      line-height: 18px;
    }
  }
}

.removeBorderLine {
  border: none !important;
}
.name {
  width: 100%;
  font-size: 13px;
  margin-left: 12px;
  font-weight: 400;
  color: #909399;
}
.content {
  display: inline-block;
  width: 250px;
  font-size: 13px;
  color: #303133;

  word-wrap: break-word;
}
.remarks-wrap {
  display: inline-block;
  width: 300px;
  word-wrap: break-word;
}

.img {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  margin-top: 10px;
}
.form-box {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  .form-item {
    width: 49%;
    ::v-deep .el-form-item__content {
      width: calc(100% - 110px);
      font-size: 13px !important;
    }
    ::v-deep .el-select--medium {
      width: 100%;
    }
    ::v-deep .el-form-item {
      margin-bottom: 0;
    }
    ::v-deep .el-textarea__inner {
      resize: none;
    }

    p {
      margin: 0;
      padding: 0;
      font-weight: 400 !important;
      color: #303133;
      font-size: 13px !important;
    }
  }
}
::v-deep .el-tabs__item.is-active {
  border-right-color: transparent !important;
  border-left-color: transparent !important;
  &::after {
    content: '';
    height: 2px;
    width: 100%;
    background-color: #1890ff;
    position: absolute;
    left: 0;
    top: 0;
  }
}
::v-deep .el-tabs__header {
  background-color: #f7fbff;
  border-bottom: none;
}
::v-deep .el-tabs__content {
  padding: 0;
}
::v-deep .el-tabs__nav-wrap::after {
  height: 0;
}
::v-deep .el-tabs__active-bar {
  top: 0;
}
.el-tabs--border-card {
  height: 39px;
  position: fixed;
  width: 100%;
  z-index: 4;
  background-color: transparent;
  border: none;
  box-shadow: none;
}
::v-deep.el-drawer__header {
  border-bottom: 0;
}
::v-deep.el-tabs__header .el-tabs__item {
  line-height: 40px;
}
</style>
