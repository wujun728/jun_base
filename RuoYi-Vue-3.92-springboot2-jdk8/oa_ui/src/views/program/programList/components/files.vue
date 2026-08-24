<template>
  <div class="box">
    <div class="header">
      <span>资料列表</span>
      <div class="btn-box">
        <el-upload
          :headers="myHeaders"
          :http-request="uploadServerLog"
          :show-file-list="false"
          action="##"
          class="mr10 upload-real"
        >
          <el-button v-if="!percentShow" class="mb14" size="small" type="primary">
            <i class="el-icon-plus"></i>
            新增
          </el-button>
          <div v-else class="addText">
            <img alt="" class="l_gif" src="../../../../assets/images/loading.gif" />
          </div>
        </el-upload>
      </div>
    </div>
    <div class="flex-10">
      <div class="inTotal">共{{ total }}项</div>
      <el-input
        v-model="where.name"
        clearable
        placeholder="请输入文件名称"
        prefix-icon="el-icon-search"
        size="small"
        style="width: 250px"
        @change="getList(1)"
      ></el-input>
      <el-date-picker
        v-model="timeVal"
        end-placeholder="上传时间"
        format=" yyyy/MM/dd"
        range-separator="至"
        size="small"
        start-placeholder="上传时间"
        style="width: 250px"
        type="daterange"
        value-format="yyyy/MM/dd"
        @change="timeChange"
      >
      </el-date-picker>
      <el-tooltip content="重置搜索条件" effect="dark" placement="top">
        <div class="reset" @click="reset"><i class="iconfont iconqingchu"></i></div>
      </el-tooltip>
    </div>
    <!-- 表格 -->
    <div class="table-box mt20">
      <el-table :data="fileData">
        <el-table-column label="文件封面" prop="name" width="120">
          <template slot-scope="scope">
            <div class="file" v-if="toSrcIcon(scope.row.name) !== 'img'">{{ getFileTypeFn(scope.row.name) }}</div>
            <img v-else :src="scope.row.att_dir" alt="" class="img" @click="filePreview(scope.row)" />
          </template>
        </el-table-column>
        <el-table-column label="文件名称" min-width="150" prop="real_name"> </el-table-column>
        <el-table-column label="大小" min-width="180" prop="att_size">
          <template slot-scope="scope"> {{ formatBytesFn(scope.row.att_size) }} </template>
        </el-table-column>
        <el-table-column label="上传用户" min-width="100" prop="card.name" />
        <el-table-column label="上传时间" min-width="130" prop="created_at"> </el-table-column>
        <el-table-column :label="$t('public.operation')" fixed="right" prop="address" width="180">
          <template slot-scope="scope">
            <el-button type="text" @click="filePreview(scope.row)"> 预览 </el-button>
            <el-button type="text" @click="handleLabel(scope.row)"> 重命名 </el-button>
            <el-button type="text" @click="handleFileDelete(scope.row, scope.$index)"> 删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div class="paginationClass mb30">
      <el-pagination
        :current-page="where.page"
        :page-size="where.limit"
        :total="total"
        layout="total, prev, pager, next, jumper"
        @size-change="handleSizeChange"
        @current-change="pageChange"
      />
    </div>

    <!-- 重命名弹窗 -->
    <el-dialog :append-to-body="true" :visible.sync="dialogFormVisible" title="修改文件名" width="30%">
      <el-form>
        <el-form-item label="重命名：" label-width="80px">
          <el-input v-model="real_name" autocomplete="off" size="small"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="dialogFormVisible = false">取 消</el-button>
        <el-button size="small" type="primary" @click="renameFn">确 定</el-button>
      </div>
    </el-dialog>
    <!-- 打开文件 -->
    <fileDialog ref="viewFile"></fileDialog>
  </div>
</template>
<script>
import { programFileListApi, programrealNameApi, programFileDelApi } from '@/api/program'
import { uploader } from '@/utils/uploadCloud'
import { roterPre } from '@/settings'
import { fileLinkDownLoad, formatBytes, getFileType, getFileExtension } from '@/libs/public'
export default {
  name: '',
  components: {
    fileDialog: () => import('@/components/openFile/previewDialog ') // 图片、MP3，MP4弹窗
  },
  props: { programId: { type: Number, default: 0 } },
  data() {
    return {
      myHeaders: {
        authorization: 'Bearer ' + localStorage.getItem('token')
      },
      dialogFormVisible: false,
      percentShow: false,
      real_name: '',
      from: {
        name: '',
        time: ''
      },
      timeVal: [],
      where: {
        limit: 15,
        page: 1,
        program_id: this.programId
      },
      total: 0,
      fileData: [],
      srcList: [],
      rowData: {}
    }
  },

  mounted() {
    this.getList()
  },
  methods: {
    getList(val) {
      this.srcList = []
      if (val == 1) {
        this.where.page = val
      }
      programFileListApi(this.where).then((res) => {
        this.fileData = res.data.list
        this.total = res.data.count
      })
    },
    getFileTypeFn(name) {
      return getFileExtension(name)
    },
    timeChange(e) {
      this.where.time = e[0] + '-' + e[1]
      this.getList(1)
    },
    // 上传文件方法
    uploadServerLog(params) {
      this.percentShow = true
      const file = params.file
      let options = {
        way: 2,
        relation_type: 'program',
        relation_id: this.programId,
        eid: 0
      }
      uploader(file, 0, options)
        .then((res) => {
          // 获取上传文件渲染页面
          if (res.data) {
            this.percentShow = false
            setTimeout(() => {
              this.getList()
            }, 500)
          }
        })
        .catch((err) => {
          this.percentShow = false
        })
    },
    pageChange(page) {
      this.where.page = page
      this.getList()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getList()
    },
    formatBytesFn(size) {
      if (size) {
        return formatBytes(Number(size))
      } else {
        return '--'
      }
    },

    // 删除附件
    handleFileDelete(row, index) {
      this.$modalSure('确定要删除此数据').then(() => {
        programFileDelApi(row.id).then((res) => {
          this.fileData.splice(index, 1)
          if (this.where.page > 1 && this.fileData.length <= 0) {
            this.where.page--
          }
          this.getList()
        })
      })
    },

    renameFn() {
      let data = {
        real_name: this.real_name
      }
      programrealNameApi(this.rowData.id, data).then((res) => {
        this.getList()
        this.dialogFormVisible = false
      })
    },

    // 重命名
    handleLabel(row) {
      this.rowData = row
      this.dialogFormVisible = true
      this.real_name = row.real_name
    },

    toSrcIcon(name) {
      return getFileType(name)
    },

    reset() {
      this.where.time = ''
      this.where.page = 1
      this.where.limit = 10
      this.where.name = ''
      this.timeVal = []
      this.getList()
    }
  }
}
</script>
<style lang="scss" scoped>
.box {
  padding: 0 20px;
}
.file {
  display: flex;
  width: 30px;
  height: 38px;
  background: url('../../../../assets/images/cloud/file-box.png') no-repeat;
  background-size: 30px 38px;
  color: #fff !important;
  justify-content: center;
  line-height: 38px;
  font-size: 12px;
  margin-right: 10px;
}

.header {
  margin-top: 25px;
  display: flex;
  justify-content: space-between;
  span {
    font-size: 18px;
    line-height: 32px;
    color: #303133;
    font-weight: 500;
  }
  .fz30 {
    font-size: 30px;
    margin-left: 14px;
    color: #909399;
    font-weight: 400;
  }
}
.flex-10 {
  height: 32px;
  display: flex;
  gap: 10px;
  align-items: center;
}
.icon-cover {
  font-size: 28px;
}
.img {
  display: inline-block;
  width: 30px;
  height: 30px;
}
</style>
