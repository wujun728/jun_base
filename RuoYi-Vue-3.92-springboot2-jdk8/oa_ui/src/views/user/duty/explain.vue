<template>
  <div class="divBox">
    <el-card class="employees-card-bottom">
      <!-- 头部 -->
      <el-form :inline="true" class="from-s" @submit.native.prevent>
        <div class="flex-row flex-col">
          <div>
            <el-tabs v-model="activeVal" class="tabs" @tab-click="activeClick">
              <el-tab-pane label="我的岗位职责" name="1" />
              <el-tab-pane label="下级岗位职责" name="2" />
            </el-tabs>
          </div>
        </div>
      </el-form>
      <div class="splitLine mb20"></div>
      <!-- 我的岗位职责 -->
      <div v-if="activeVal == 1" class="main">
        <div v-if="detailData.duty" class="detail-box">
          <div class="title">{{ detailData.name }}岗位职责</div>
          <div class="content" v-html="detailData.duty" @click="replayImgShow($event)" />
        </div>
        <div v-else>
          <default-page v-height :index="12" :min-height="580" />
        </div>
      </div>
      <!-- 下级岗位职责 -->
      <div v-if="activeVal == 2" class="table-box">
        <div class="inTotal flex">
          共 {{ totalSubmit }} 条
          <el-input
            v-model="where.name"
            class="search-input"
            clearable
            placeholder="请输入姓名搜索"
            prefix-icon="el-icon-search"
            size="small"
            @change="getList(1)"
            @keyup.native.stop.prevent.enter="getList(1)"
          ></el-input>
        </div>

        <oa-table
          :tableOptions="tableOptions"
          :height="tableHeight"
          :tableData="tableData"
          :total="totalSubmit"
          :loading="false"
          @handleSizeChange="handleSizeChange"
          @handleCurrentChange="handleCurrentChange"
        >
          <template #frames="{ row }">
            <div v-for="(item, index) in row.frames" :key="index" class="frame-name over-text">
              <span class="icon-h">
                {{ item.name }}<span v-show="item.is_mastart === 1 && row.frames.length > 1" title="主部门">(主)</span>
                <span v-show="item.is_admin == 1" class="guan" title="主管">(管)</span>
              </span>
            </div>
          </template>

          <template #options="{ row }">
            <el-button size="small" type="text" @click="onCheck(row)">查看</el-button>
            <el-button v-if="row.operate" size="small" type="text" @click="onEdit(row)">编辑</el-button>
          </template>
        </oa-table>
      </div>
    </el-card>
    <!-- 查看 -->
    <el-drawer :before-close="handleClose" :visible.sync="checkDrawer" direction="rtl" size="60%" title="岗位职责表">
      <div v-if="detailDataInfo" class="check-box">
        <div class="content mt14" v-html="detailDataInfo" @click="replayImgShow($event)" />
      </div>
      <div v-else>
        <default-page v-height :index="14" :min-height="580" />
      </div>
    </el-drawer>
    <!-- 编辑 -->
    <el-drawer
      :before-close="handleClose"
      :visible.sync="editDrawer"
      :wrapperClosable="false"
      direction="rtl"
      size="60%"
      title="编辑岗位职责表"
    >
      <div class="check-box">
        <div class="user-name">
          <span>人员姓名：</span>
          <span class="text">{{ userName }}</span>
        </div>
        <div class="boder">
          <component
            :is="loadEdit"
            ref="ueditorFrom"
            :border="true"
            :content="content"
            :height="height"
            @input="ueditorEdit"
          />
        </div>
      </div>
      <div class="button from-foot-btn fix btn-shadow">
        <el-button size="small" @click="editDrawer = false">{{ $t('public.cancel') }}</el-button>
        <el-button :loading="loading" size="small" type="primary" @click="handleConfirm">保存</el-button>
      </div>
    </el-drawer>
    <image-viewer ref="imageViewer" :srcList="srcList"></image-viewer>
  </div>
</template>

<script>
import ueditorFrom from '@/components/form-common/oa-wangeditor'
import oaTable from '@/components/form-common/oa-table'
import defaultPage from '@/components/common/defaultPage'
import imageViewer from '@/components/common/imageViewer'
import { endJobInfoApi } from '@/api/enterprise'
import { subordinateApi, mineAnalysis, subordinateInfoApi, putSubordinateApi } from '@/api/user'

export default {
  name: 'Duty',
  components: { defaultPage, ueditorFrom, imageViewer, oaTable },
  data() {
    return {
      activeVal: '1',
      loading: false,
      userName: '',
      where: {
        name: '',
        page: 1,
        limit: 15
      },
      detailData: {},

      loadEdit: null,
      content: '',
      totalSubmit: 0,
      checkDrawer: false,
      editDrawer: false,
      tableData: [],
      srcList: [],
      mineContent: '',
      detailDataInfo: {},
      height: 'calc(100vh - 200px)',
      tableOptions: [
        {
          label: '序号',
          type: 'index'
        },
        {
          label: '人员姓名',
          prop: 'name'
        },
        {
          label: '职位',
          render: (row) => {
            return <span>{row.job ? row.job.name : '--'}</span>
          }
        },
        {
          label: '部门',
          type: 'slot',
          name: 'frames'
        },
        {
          label: '更新时间',
          prop: 'updated_at'
        },
        {
          label: '操作',
          slot: 'options',
          fixed: 'right',
          width: '130'
        }
      ]
    }
  },
  mounted() {
    this.userEntInfo()
  },
  methods: {
    async userEntInfo() {
      const jobId = this.$store.state.user.userInfo.job.id

      if (jobId) {
        this.handleDetail(jobId)
      }
    },
    // 查看详情
    async handleDetail(id) {
      const result = await endJobInfoApi(id)
      this.detailData = result.data
    },

    // 富文本查看图片
    replayImgShow(e) {
      if (e.target.tagName === 'IMG') {
        this.srcList = [e.target.currentSrc]
        this.$refs.imageViewer.openImageViewer(e.target.currentSrc)
      }
    },

    activeClick() {
      if (this.activeVal == '1') {
        this.getMineAnalysis()
      } else {
        this.getList()
      }
    },

    // 获取列表
    async getList(val) {
      if (val == 1) {
        this.where.page = 1
      }
      const result = await subordinateApi(this.where)
      this.tableData = result.data.list
      this.totalSubmit = result.data.count
    },

    // 获取我的岗位职责
    async getMineAnalysis() {
      const result = await mineAnalysis()
      this.mineContent = result.data.data
    },

    handleSizeChange(val) {
      this.where.page = 1
      this.where.limit = val
      this.getList()
    },
    handleCurrentChange(page) {
      this.where.page = page
      this.getList()
    },

    // 查看下级岗位职责
    onCheck(data) {
      this.getCheck(data.id)
      this.checkDrawer = true
    },

    // 岗位职责详情
    getCheck(uid) {
      subordinateInfoApi(uid)
        .then((res) => {
          this.detailDataInfo = res.data.duty
          this.loadEdit = ueditorFrom
        })
        .catch((err) => {
          this.detailDataInfo = ''
          this.loadEdit = ueditorFrom
        })
    },

    // 编辑下级岗位职责
    onEdit(data) {
      this.userName = data.name
      this.uid = data.id
      this.editDrawer = true
      this.getCheck(data.id)
      setTimeout(() => {
        this.$refs.ueditorFrom.tabButton = true
        this.content = this.detailDataInfo
      }, 300)
    },

    // 保存编辑内容
    async handleConfirm() {
      this.loading = true
      let data = {
        duty: this.content
      }
      const result = await putSubordinateApi(this.uid, data)
      if (result.status == 200) {
        this.editDrawer = false
        await this.getList()
      }
      this.loading = false
    },

    ueditorEdit(e) {
      this.content = e
    },

    handleClose() {
      this.editDrawer = false
      this.checkDrawer = false
    }
  }
}
</script>

<style lang="scss" scoped>
.main {
  width: 800px;
  margin: 0 auto;
}
.detail-box {
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  color: #666666;
  padding: 25px 34px 5px 34px;
  min-height: 580px;
  .title {
    font-size: 24px;
    font-weight: 700;
    text-align: center;
    margin-bottom: 20px;
  }
  .content {
    margin-top: 30px;
    ::v-deep p {
      text-indent: 2em;
      font-size: 14px;
      line-height: 1.5;
    }
    ::v-deep img {
      cursor: pointer;
    }

    ::v-deep table {
      border: 1px solid #ccc;
    }

    ::v-deep table th {
      border: 1px solid #ccc;
    }
    ::v-deep table td {
      padding: 10px 5px;
      border: 1px solid #ccc;
    }
  }
}
.inTotal {
  margin: 10px 0;
}
.icon-h {
  position: relative;
  & > span {
    color: #1890ff;
  }
  .guan {
    color: #ff9900;
  }
}
.check-box {
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  color: #666666;
  padding: 0px 20px 0 24px;

  .content {
    ::v-deep p {
      text-indent: 2em;
      font-size: 14px;
      line-height: 1.5;
    }
    ::v-deep table {
      border: 1px solid #ccc;
    }

    ::v-deep table th {
      border: 1px solid #ccc;
    }
    ::v-deep table td {
      padding: 10px 5px;
      border: 1px solid #ccc;
    }
  }
}
.user-name {
  margin-top: 30px;
  margin-bottom: 20px;
}
.user-name > span {
  font-size: 13px;
  color: #909399;
}
.user-name .text {
  display: inline-block;
  font-size: 13px;
  color: #303133;
}
::v-deep .el-form-item {
  padding-bottom: 0;
}

::v-deep .el-tabs__nav-wrap::after {
  content: '';
  position: absolute;
  left: 0;
  bottom: 0;
  width: 100%;
  height: 0;
}
::v-deep.el-select {
  width: 100%;
}
.flex {
  display: flex;
  align-items: center;
}
.search-input {
  width: 200px;
  margin-left: 10px;
}
</style>
