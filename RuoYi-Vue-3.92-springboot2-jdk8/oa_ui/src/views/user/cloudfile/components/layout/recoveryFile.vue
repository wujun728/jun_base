<template>
  <div class="v-height-flag box">
    <oaFromBox
      :search="search"
      :total="total"
      :isBack="true"
      :title="`回收站`"
      :btnText="`彻底删除`"
      :btnIcon="false"
      :isViewSearch="false"
      :sortSearch="false"
      :dropdownList="dropdownList"
      @backFn="backFn"
      @addDataFn="batchDeletFn"
      @dropdownFn="dropdownFn"
      @confirmData="confirmData"
    ></oaFromBox>
    <!-- <div class="content mt10 v-height-flag"> -->
    <el-table
      class="mt10"
      :data="fileData"
      style="width: 100%"
      :height="tableHeight"
      @selection-change="handleSelectionChange"
    >
      <el-table-column type="selection"> </el-table-column>
      <el-table-column prop="name" label="名称" min-width="250">
        <template slot-scope="scope">
          {{ scope.row.name }}
          <span v-if="scope.row.type == 1">
            <i class="icon iconfont" :class="getFileType(scope.row.type, scope.row.file_url)" />
          </span>
          <span v-else>.{{ scope.row.file_ext }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="path" label="位置" min-width="150"> </el-table-column>
      <el-table-column prop="deleted_at" label="删除时间"> </el-table-column>
      <el-table-column prop="date" label="操作人">
        <template slot-scope="scope">
          <div v-if="scope.row.del_user">
            <img :src="scope.row.del_user.avatar" alt="" class="img" />{{ scope.row.del_user.name }}
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="date" label="操作" width="100">
        <template slot-scope="scope">
          <el-dropdown>
            <span class="el-dropdown-link el-button--text el-button more">
              <i class="iconfont icongengduo1" />
            </span>
            <el-dropdown-menu style="text-align: center">
              <el-dropdown-item v-if="userInfo.id == scope.row.master_uid" @click.native="deleteFn(scope.row.id)">
                彻底删除
              </el-dropdown-item>
              <el-dropdown-item
                v-if="
                  scope.row.del_user && (userInfo.id == scope.row.master_uid || userInfo.id == scope.row.del_user.id)
                "
                @click.native="recoveryFn(scope.row.id)"
              >
                恢复文件
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <!-- </div> -->
    <div class="page-fixed">
      <el-pagination
        :current-page="where.page"
        :page-size="where.limit"
        :page-sizes="[15, 20, 30]"
        :total="total"
        layout="total,sizes, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="pageChange"
      />
    </div>
    <!-- <default-page v-else :index="7" :min-height="510" /> -->
  </div>
</template>

<script>
import Vue from 'vue'
import file from '@/utils/file'
Vue.use(file)
import {
  folderForceDeleteApi,
  folderForceDeletesApi,
  folderSpaceEntRecoverApi,
  folderSpaceEntRecycleApi,
  folderSpaceEntAllRecoverApi
} from '@/api/cloud'
import defaultPage from '@/components/common/defaultPage'
import oaFromBox from '@/components/common/oaFromBox'
export default {
  name: 'RecoveryFile',
  components: {
    defaultPage,
    oaFromBox
  },
  props: {},
  data() {
    return {
      fileData: [],
      where: {
        page: 1,
        limit: 15
      },
      userInfo: JSON.parse(localStorage.getItem('userInfo')),

      dropdownList: [{ label: '恢复文件', value: '1' }],
      search: [
        {
          field_name: '搜索文件名称',
          field_name_en: 'keyword',
          form_value: 'input'
        },
        {
          field_name: '删除时间',
          field_name_en: 'time',
          form_value: 'date_picker'
        }
      ],
      loading: false,
      total: 0,
      ids: []
    }
  },

  mounted() {
    this.getTreeData()
  },
  methods: {
    pageChange(page) {
      this.where.page = page
      this.getTreeData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTreeData()
    },
    handleSelectionChange(val) {
      this.ids = []
      val.map((item) => {
        this.ids.push(item.id)
      })
    },
    confirmData(data) {
      if (data == 'reset') {
        this.where = {
          limit: this.where.limit
        }
      } else {
        this.where = { ...this.where, ...data }
      }
      this.where.page = 1
      this.getTreeData()
    },
    dropdownFn() {
      if (this.ids.length == 0) {
        this.$message.error('至少选中一个数据')
        return false
      }
      let data = {
        id: this.ids
      }
      this.$modalSure('确定要恢复此文件吗').then(() => {
        folderSpaceEntAllRecoverApi(data).then((res) => {
          if (res.status == 200) {
            this.getTreeData()
          }
        })
      })
    },
    getTreeData() {
      this.loading = true
      folderSpaceEntRecycleApi(this.where).then((res) => {
        this.loading = false
        this.fileData = res.data.list
        this.total = res.data.count
      })
    },

    // 删除文件
    deleteFn(id) {
      this.$modalSure('删除后将无法恢复，确定删除吗').then(() => {
        folderForceDeleteApi(id).then((res) => {
          if (res.status == 200) {
            let totalPage = Math.ceil((this.total - 1) / this.where.limit)
            let currentPage = this.where.page > totalPage ? totalPage : this.where.page
            this.where.page = currentPage < 1 ? 1 : currentPage
            this.getTreeData()
          }
        })
      })
    },
    backFn() {
      this.$emit('backFn')
    },

    // 批量删除
    batchDeletFn() {
      if (this.ids.length == 0) {
        this.$message.error('至少选中一个数据')
        return false
      }
      let data = {
        id: this.ids
      }
      this.$modalSure('删除后将无法恢复，确定删除吗').then(() => {
        folderForceDeletesApi(data).then((res) => {
          if (res.status == 200) {
            let totalPage = Math.ceil((this.total - this.ids.length) / this.where.limit)
            let currentPage = this.where.page > totalPage ? totalPage : this.where.page
            this.where.page = currentPage < 1 ? 1 : currentPage
            this.getTreeData()
          }
        })
      })
    },
    recoveryFn(id) {
      this.$modalSure('确定恢复此数据').then(() => {
        folderSpaceEntRecoverApi(id).then((res) => {
          if (res.status == 200) {
            this.getTreeData()
          }
        })
      })
    }
  }
}
</script>

<style lang="scss" scoped>
.box {
  margin: 20px;
}
.icongengduo1 {
  color: #909399;
}
.img {
  width: 22px;
  height: 22px;
  border-radius: 50%;
  margin-right: 6px;
  vertical-align: middle;
}
</style>
