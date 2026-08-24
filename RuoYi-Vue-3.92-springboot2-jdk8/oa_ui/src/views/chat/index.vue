<template>
  <div class="divBox">
    <el-card class="normal-page">
      <formBox
        :title="`应用列表`"
        :total="total"
        :search="search"
        :btnText="`创建应用`"
        :isViewSearch="false"
        :sortSearch="false"
        @confirmData="confirmData"
        @addDataFn="addDataFn"
      ></formBox>
      <div id="content-box " class="mt20" v-loading="loading">
        <default-page v-if="listData.length == 0" :index="14" :min-height="520" />
        <div class="list" id="listBox" ref="container">
          <div class="item" id="item" v-for="(item, index) in listData" :key="index">
            <div class="flex">
              <img :src="item.pic" alt="" class="img" />
              <div class="flex-column" style="width: 100%">
                <div class="title over-text">{{ item.name }}</div>
                <span class="name">创建者：{{ item.user ? item.user.name : '--' }} </span>
              </div>
              <div class="status" v-if="item.status == 1">发布</div>
            </div>
            <div class="over-text2 content">
              {{ item.info }}
            </div>
            <div class="operate flex flex-center">
              <span @click="handleOpenChatApp(item.id)"> 使用</span>
              <template v-if="item.auth">
                <el-divider direction="vertical"></el-divider>
                <span @click="handleEdit(item)"> 设置</span>
                <el-divider direction="vertical"></el-divider>
                <span @click="deleteFn(item.id)"> 删除</span>
              </template>
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
    </el-card>
    <oa-dialog
      ref="oaDialog"
      @submit="addSubmit"
      :fromData="fromData"
      :formConfig="formConfig"
      :formRules="formRules"
      :formDataInit="formDataInit"
    />
  </div>
</template>
<script>
import { roterPre } from '@/settings'
import formBox from '@/components/common/oaFromBox'
import oaDialog from '@/components/form-common/dialog-form'
import defaultPage from '@/components/common/defaultPage'
import { getApplicationsListApi, chatSaveApplicationsApi, delApplicationsApi } from '@/api/chatAi'
import { AiEmbeddedManage } from '@/libs/ai-embedded'

export default {
  name: 'chat',
  components: { formBox, oaDialog, defaultPage },
  props: {},
  data() {
    return {
      loading: false,
      search: [
        {
          form_value: 'input',
          field_name: '应用名称',
          field_name_en: 'name'
        }
      ],
      fromData: {
        title: '创建应用',
        width: '650px',
        type: 'add',
        btnText: '创建'
      },
      formConfig: [
        {
          type: 'input',
          label: '应用名称：',
          placeholder: '请输入应用名称',
          key: 'name',
          maxlength: 20,
          showWordLimit: true
        },
        {
          type: 'textarea',
          label: '应用简介：',
          maxlength: 100,
          showWordLimit: true,
          placeholder:
            '你是一个企业管理小助手，你能通过企业管理数据，进行挑战与现状分析、核心数据统计和优化建议；同时，保持积极的语气，展示出解决方案的可行性，这样更有助于激励团队采取行动。',
          key: 'info',
          height: '120px'
        }
      ],
      formDataInit: {
        name: '',
        info: ''
      },
      formRules: {
        name: [{ required: true, message: '请输入应用名称', trigger: 'blur' }]
      },
      total: 0,
      where: {
        page: 1,
        limit: 15
      },
      listData: []
    }
  },
  created() {
    this.initAiEmbedded()
  },
  mounted() {
    this.getLimit()
    window.addEventListener('resize', this.getLimit)
  },

  beforeDestroy() {
    window.removeEventListener('resize', this.getLimit)
    if (this.chatInstance) {
      this.chatInstance.destroy()
    }
  },
  methods: {
    initAiEmbedded() {
      this.chatInstance = new AiEmbeddedManage()
      this.chatInstance.init(this.$store.getters.token, {
        scene: 'app-preview-use',
        appId: 0,
        defaultShow: false
      })
    },
    handleOpenChatApp(appId) {
      this.chatInstance.instance.openApp(appId)
    },
    addSubmit(data) {
      // 应用默认值
      let user = JSON.parse(localStorage.getItem('userInfo'))
      let sitedata = JSON.parse(localStorage.getItem('sitedata'))
      data.pic = sitedata.site_logo
      data.data_arrange_text =
        '根据用户提出的内容，整理数据！如果超过1条数据，请用表格展示！如果为一条数据请分析数据意思用语意化输出内容'
      data.edit = [user.id]
      data.auth_ids = [user.id]
      data.json = [
        {
          name: 'temperature',
          filed: '采样温度',
          value: '0.95',
          message: '介于 0 和 2 之间。更高的值，如 0.8，会使输出更随机，而更低的值，如 0.2，会使其更加集中和确定'
        },
        {
          name: 'max_tokens',
          filed: '最大tokens',
          value: '2048',
          message:
            '限制一次请求中模型生成 completion 的最大 token 数。输入 token 和输出 token 的总长度受模型的上下文长度的限制'
        }
      ]
      chatSaveApplicationsApi(data).then((res) => {
        if (res.status == '200') {
          this.$router.push(`${roterPre}/chat/setting?id=${res.data.id}`)
          this.$refs.oaDialog.handleClose()
        }
      })
    },

    handleEdit(item) {
      this.$router.push(`${roterPre}/chat/setting?id=${item.id}`)
    },
    getList(val) {
      if (val) {
        this.where.page = 1
      }
      this.loading = true
      getApplicationsListApi(this.where).then((res) => {
        this.listData = res.data.list
        this.total = res.data.count
        this.loading = false
      })
    },
    confirmData(val) {
      if (val === 'reset') {
        this.where.name = ''
      } else {
        this.where.name = val.name
      }
      this.getList(1)
    },
    pageChange(val) {
      this.where.page = val
      this.getList()
    },
    addDataFn() {
      this.$refs.oaDialog.openBox()
    },
    // 删除
    deleteFn(id) {
      this.$modalSure('删除后该应用将不再提供服务，请谨慎操作').then(async () => {
        await delApplicationsApi(id)
        let totalPage = Math.ceil((this.total - 1) / this.where.limit)
        let currentPage = this.where.page > totalPage ? totalPage : this.where.page
        this.where.page = currentPage < 1 ? 1 : currentPage
        await this.getList()
      })
    },
    getLimit() {
      const windowHeight = document.documentElement.clientHeight - 287
      const winWidth = document.getElementById('listBox').offsetWidth
      const col = Math.floor((winWidth + 15) / (367 + 15))
      const row = Math.floor((windowHeight + 15) / (153 + 15))
      this.where.limit = col * row
      this.getList()
    }
  }
}
</script>
<style scoped lang="scss">
.list {
  box-sizing: border-box; /* 防止padding影响高度 */
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(367px, 0.33fr));
  grid-auto-rows: minmax(153px, auto); /* 行高自适应内容，最小150px */
  gap: 15px; /* 卡片间距 */
}
.item {
  border: 1px solid #dcdfe6;
  border-radius: 10px 10px 10px 10px;
  padding: 14px;
  font-family: PingFang SC, PingFang SC;
  position: relative;
  .status {
    position: absolute;
    right: 14px;
    width: 48px;
    height: 20px;
    background: rgba(24, 144, 255, 0.05);
    border-radius: 3px 3px 3px 3px;
    border: 1px solid #1890ff;
    display: flex;
    align-items: center;

    justify-content: center;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 12px;
    color: #1890ff;
  }
  .content {
    margin-top: 10px;
    font-weight: 400;
    font-size: 14px;
    color: #606266;
    line-height: 20px;
  }
  .operate {
    position: absolute;
    bottom: 14px;
    font-weight: 400;
    font-size: 13px;
    color: #1890ff;
    cursor: pointer;
  }
  .img {
    display: block;
    width: 32px;
    height: 32px;
    border-radius: 4px;
    margin-right: 8px;
  }
  .title {
    width: 80%;
    font-weight: 500;
    font-size: 14px;
    color: #303133;
  }
  .name {
    margin-top: 3px;
    font-weight: 400;
    font-size: 12px;
    color: #606266;
  }
}
</style>
