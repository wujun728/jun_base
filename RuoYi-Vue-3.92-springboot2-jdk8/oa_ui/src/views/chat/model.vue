<template>
  <div class="divBox">
    <el-card class="normal-page" body-style="padding: 0">
      <el-row>
        <el-col v-bind="gridl">
          <div class="title-16 mt20 ml20">供应商</div>
          <div class="leftBox">
            <div class="all-text" :class="activeVal === '' ? 'activeText' : ''" @click="handleAll">
              <span class="iconfont iconshanghu"></span> 全部模型
            </div>
            <div
              v-for="(item, index) in optionList"
              :key="index"
              class="option-item"
              :class="activeVal === item.value ? 'active' : ''"
              @click="handleTypes(item)"
            >
              <img :src="require(`../../assets/chat/${item.pic}`)" alt="" class="logo" /> {{ item.label }}
            </div>
          </div>
        </el-col>
        <el-col v-bind="gridr" class="boder-left">
          <div class="p20" v-loading="loading">
            <oaFromBox
              :title="title"
              :total="total"
              :btnText="`添加模型`"
              :search="search"
              :isViewSearch="false"
              :sortSearch="false"
              @confirmData="confirmData"
              @addDataFn="addDataFn"
            ></oaFromBox>
            <div class="mt20" id="list">
              <default-page v-if="listData.length == 0" :index="14" :min-height="520" />
              <div class="list" ref="container">
                <div class="item" v-for="(item, index) in listData" :key="index">
                  <div class="operate">
                    <span @click="handleInfo(item)">设置</span> <el-divider direction="vertical"></el-divider
                    ><span @click="handleDel(item.id)">删除</span>
                  </div>
                  <div class="title">
                    <img
                      v-if="item.json && item.json.pic"
                      :src="require(`../../assets/chat/${item.json.pic}`)"
                      alt=""
                      class="img"
                    />
                    <img v-else src="../../assets/chat/deepseek.png" alt="" class="img" />
                    <div style="width: 70%" class="over-text">{{ item.name }}</div>
                  </div>
                  <div class="flex mt14">
                    <div class="left-text">模型类型</div>
                    <div class="right-text">{{ item.models_type }}</div>
                  </div>
                  <div class="flex">
                    <div class="left-text">基础模型</div>
                    <div class="right-text">{{ item.is_model }}</div>
                  </div>
                  <div class="flex flex-between">
                    <div class="flex">
                      <div class="left-text">创建者</div>
                      <div class="right-text">{{ item.user ? item.user.name : '--' }}</div>
                    </div>
                    <div class="time">{{ item.created_at }}</div>
                  </div>
                </div>
              </div>
            </div>
            <el-pagination
              :current-page="where.page"
              :page-size="where.limit"
              :total="total"
              class="page-fixed"
              layout="total, prev, pager, next, jumper"
              @current-change="pageChange"
            />
          </div>
        </el-col>
      </el-row>
    </el-card>
    <!-- 添加供应商 -->
    <supplierDialog ref="supplierDialog" :list="optionList" @openModelDialog="openModelDialog" />
    <modelDialog
      :optionList="optionList"
      :supplierVal="supplierVal"
      ref="modelDialog"
      @isOk="getList"
      @openSupplierDialog="addDataFn"
    />
  </div>
</template>
<script>
import oaFromBox from '@/components/common/oaFromBox'
import supplierDialog from './components/supplierDialog'
import defaultPage from '@/components/common/defaultPage'
import modelDialog from './components/modelDialog'
import { getModelsOptionsApi, getModelsListApi, delModelsApi } from '@/api/chatAi'
export default {
  name: '',
  components: { oaFromBox, modelDialog, supplierDialog, defaultPage },
  props: {},
  data() {
    return {
      total: 0,
      title: '全部',
      loading: false,
      optionList: [],
      activeVal: '',
      where: {
        page: 1,
        limit: 15,
        name: '',
        uids: '',
        provider: ''
      },
      listData: [],
      search: [
        {
          field_name: '模型名称',
          field_name_en: 'name',
          form_value: 'input'
        },
        {
          field_name: '创建人员',
          field_name_en: 'user_id',
          form_value: 'user_id',
          onlyOne: false,
          data_dict: []
        }
      ],
      supplierVal: {},
      gridl: {
        xl: 3,
        lg: 4,
        md: 5,
        sm: 6,
        xs: 24
      },
      gridr: {
        xl: 21,
        lg: 20,
        md: 19,
        sm: 18,
        xs: 24
      }
    }
  },
  created() {
    this.getOptions()
  },

  mounted() {
    setTimeout(() => {
      this.getLimit()
    }, 300)
    window.addEventListener('resize', this.getLimit)
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.getLimit)
  },
  methods: {
    getList(val) {
      if (val) {
        this.where.page = val
      }
      this.loading = true
      getModelsListApi(this.where).then((res) => {
        this.listData = res.data.list
        this.total = res.data.count
        this.loading = false
      })
    },
    handleAll() {
      this.title = '全部'
      this.activeVal = ''
      this.where.provider = ''
      this.getList(1)
    },
    handleTypes(item) {
      this.title = item.label
      this.activeVal = item.value
      this.where.provider = item.value
      this.getList(1)
    },
    confirmData(val) {
      if (val === 'reset') {
        this.where.uids = []
        this.where.name = ''
      } else {
        this.where.uids = val.user_id
        this.where.name = val.name
      }
      this.getList(1)
    },

    addDataFn(val, type) {
      if (this.activeVal !== '' && !type) {
        let obj = {
          label: this.title,
          value: this.activeVal
        }
        this.$refs.modelDialog.openBox(obj)
      } else {
        this.$refs.supplierDialog.openBox(val)
      }
    },
    getLimit() {
      const windowHeight = document.documentElement.clientHeight - 304
      const winWidth = document.getElementById('list').offsetWidth
      const col = Math.floor(winWidth / 480)
      const row = Math.floor(windowHeight / 150)
      this.where.limit = col * row
      this.getList()
    },
    openModelDialog(item) {
      this.supplierVal = item
      this.$refs.modelDialog.openBox(item)
    },
    handleInfo(item) {
      this.$refs.modelDialog.openBox(item)
      this.supplierVal = item.json
    },
    handleDel(id) {
      this.$modalSure('确认删除此模型').then(() => {
        delModelsApi(id).then((res) => {
          if (res.status == '200') {
            let totalPage = Math.ceil((this.total - 1) / this.where.limit)
            let currentPage = this.where.page > totalPage ? totalPage : this.where.page
            this.where.page = currentPage < 1 ? 1 : currentPage
            this.getList()
          }
        })
      })
    },
    getOptions() {
      getModelsOptionsApi().then((res) => {
        this.optionList = res.data
      })
    },
    pageChange(val) {
      this.where.page = val
      this.getList()
    }
  }
}
</script>
<style scoped lang="scss">
.option-item {
  width: 100%;
  height: 40px;
  font-weight: 400;
  font-size: 14px;
  color: #303133;
  line-height: 40px;
  padding-left: 49px;
  cursor: pointer;
}
.time {
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 14px;
  color: #909399;
}
.logo {
  width: 20px;
  height: 20px;
  margin-right: 8px;
}
.active {
  color: #1890ff;
  background: #f1f9ff;
}
.operate {
  position: absolute;
  top: 14px;
  right: 14px;
  font-weight: 400;
  font-size: 13px;
  color: #1890ff;
  cursor: pointer;
  display: flex;
}
.list {
  box-sizing: border-box; /* 防止padding影响高度 */
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(482px, 0.5fr));
  grid-auto-rows: minmax(150px, auto); /* 行高自适应内容，最小150px */
  gap: 15px; /* 卡片间距 */
}

.item {
  border: 1px solid #dcdfe6;
  border-radius: 10px;
  padding: 14px;
  font-family: PingFang SC, PingFang SC;
  position: relative;
  .title {
    display: flex;
    align-items: center;

    font-weight: 500;
    font-size: 14px;
    color: #303133;
    .img {
      display: block;
      width: 32px;
      height: 32px;
      border-radius: 4px;
      margin-right: 8px;
    }
  }
  .left-text {
    width: 56px;
    text-align: right;
    font-weight: 400;
    font-size: 14px;
    color: #909399;
    font-family: PingFang SC, PingFang SC;
  }
  .right-text {
    margin-bottom: 8px;
    font-weight: 400;
    font-size: 14px;
    color: #303133;
    margin-left: 12px;
    font-family: PingFang SC, PingFang SC;
  }
}
.boder-left {
  min-height: calc(100vh - 77px);
  border-left: 1px solid #eeeeee;
}
.p20 {
  padding: 14px 20px;
}
.activeText {
  color: #1890ff;
  background: #f1f9ff;
}
.all-text {
  width: 100%;
  height: 40px;
  margin-top: 12px;
  font-family: PingFang SC, PingFang SC;

  font-weight: 500;
  font-size: 14px;

  padding-left: 22px;
  line-height: 40px;
  cursor: pointer;
  .iconshanghu {
    margin-right: 10px;
  }
}
</style>
