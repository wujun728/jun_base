<template>
  <div class="station">
    <!-- 文件属性详情侧滑 -->
    <el-drawer
      :before-close="handleClose"
      :direction="direction"
      :modal="true"
      :size="formData.width"
      :title="formData.title"
      :visible.sync="drawer"
      :wrapper-closable="true"
    >
      <div v-if="fileData.id !== undefined" class="content">
        <div class="logo">
          <i :class="getFileType(fileData.type, fileData.file_url)" class="icon iconfont" />
          <p>{{ $t('file.detailedinformation') }}</p>
        </div>
        <el-row :gutter="20" class="content-list mt15">
          <el-col class="content-left"><i class="icon iconfont iconkuaizhuangyangshi" /></el-col>
          <el-col class="content-right">
            <div>{{ $t('file.filetype') }}</div>
            <div v-if="fileData.type !== 1">{{ fileData.file_ext }}</div>
            <div v-else>{{ $t('file.folder') }}</div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="content-list mt15" v-if="fileData.type !== 1">
          <el-col class="content-left"><i class="icon iconfont iconkuaizhuangyangshi" /></el-col>
          <el-col class="content-right">
            <div>文件大小</div>
            <div>{{ formatBytesFn(fileData.file_size) }}</div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="content-list">
          <el-col class="content-left"><i class="icon iconfont iconwenjianweizhi" /></el-col>
          <el-col class="content-right">
            <div>{{ $t('file.filelocation') }}</div>
            <div v-if="fileData.path.length > 0">
              {{ $t('file.allpositions') }}
              <span>/ {{ fileData.path }}</span>
            </div>
            <div v-else>
              {{ $t('file.allpositions') }}
            </div>
          </el-col>
        </el-row>

        <el-row :gutter="20" class="content-list">
          <el-col class="content-left"><i class="icon iconfont icongongxiangzu" /></el-col>
          <el-col class="content-right">
            <div>{{ $t('hr.founder') }}</div>
            <div>{{ fileData.user.name || '--' }}</div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="content-list">
          <el-col class="content-left"><i class="icon iconfont iconchuangjianshijian" /></el-col>
          <el-col class="content-right">
            <div>{{ $t('file.creationtime') }}</div>
            <div>{{ fileData.created_at }}</div>
          </el-col>
        </el-row>
        <el-row :gutter="20" class="content-list">
          <el-col class="content-left"><i class="icon iconfont iconxiugaishijian" /></el-col>
          <el-col class="content-right">
            <div>{{ $t('file.updatetime') }}</div>
            <div>{{ fileData.updated_at }}</div>
          </el-col>
        </el-row>
      </div>
    </el-drawer>
  </div>
</template>

<script>
import { formatBytes } from '@/libs/public'
import { folderDetailApi, folderSpaceEntDetailApi } from '@/api/cloud'
import file from '@/utils/file'
Vue.use(file)
import Vue from 'vue'
export default {
  name: 'FileAttribute',
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
      direction: 'rtl',
      fileData: {}
    }
  },
  watch: {
    formData: {
      handler(nVal) {
        if (this.formData.is_file) {
          this.getView()
        } else {
          this.getSpace()
        }
      },
      deep: true
    }
  },
  methods: {
    handleClose() {
      this.drawer = false
      this.fileData = {}
    },
    openBox() {
      this.drawer = true
    },
    formatBytesFn(size) {
      if (size) {
        size = Number(size)
        return formatBytes(size)
      } else {
        return '--'
      }
    },

    // 我的文件-文件详情
    async getView() {
      const result = await folderDetailApi(this.formData.id)
      this.fileData = result.data
    },
    // 企业文件-文件详情
    async getSpace() {
      const result = await folderSpaceEntDetailApi(this.formData.fid, this.formData.id)
      this.fileData = result.data
    }
  }
}
</script>

<style lang="scss" scoped>
.content {
  padding: 20px 25px 0 25px;
  .logo {
    width: 100%;
    text-align: center;
    border-bottom: 1px solid rgba(216, 216, 216, 0.3);
    i {
      font-size: 80px;
    }
    p {
      margin: 10px 0;
      color: rgba(0, 0, 0, 0.5);
      text-align: left;
    }
  }
  .content-list {
    padding-bottom: 10px;
    margin-bottom: 10px;
    color: #999999;
    .content-left {
      width: 40px;
      i {
        font-size: 20px;
      }
    }
    .content-right {
      width: calc(100% - 40px);
      div:first-of-type {
        color: #000000;
        font-size: 14px;
        margin-bottom: 10px;
      }
    }
  }
  .content-list:last-of-type {
    margin-bottom: 0;
  }
}
</style>
