<template>
  <div>
    <el-dialog
      :title="title"
      :visible.sync="dialogFormVisible"
      :modal="true"
      width="698px"
      :before-close="closeDialog"
      :show-close="false"
    >
      <div slot="title" class="dialog-title">
        <div class="dialog-title-title">选择封面</div>
        <div class="dialog-title-upload">
          <span v-if="deleteButton" @click="deleteImage"><i class="el-icon-delete" />删除</span>
          <el-upload
            class="upload"
            :action="fileUrl"
            :on-success="handleSuccess"
            :headers="myHeaders"
            :show-file-list="false"
            :before-upload="handleUpload"
            :data="uploadData"
            multiple
          >
            <i class="el-icon-upload2" />上传封面图
          </el-upload>
        </div>
      </div>
      <el-col :span="24">
        <el-col :span="6">
          <div class="image-preview">
            <div class="image-preview-con">
              <img :src="data.cover === '' || isImage ? defaultImg : data.cover" alt="" />
              <div
                v-if="data.cover === '' || isImage"
                class="public-img"
                :class="colorBtn ? 'color-withe' : 'color-black'"
              >
                <p class="title">{{ data.name }}</p>
                <p class="content">{{ data.info }}</p>
              </div>
              <div v-else class="public-img" :class="data.color === '#FFFFFF' ? 'color-withe' : 'color-black'">
                <p class="title">{{ data.name }}</p>
                <p class="content">{{ data.info }}</p>
              </div>
            </div>
          </div>
        </el-col>
        <el-col :span="18">
          <div class="image-list">
            <ul class="image-list-ul">
              <li
                v-for="(item, index) in imageArray"
                :key="index"
                :class="(index + 1) % column == 0 ? 'mActive' : ''"
                @click="checkImage(index, item)"
              >
                <el-image :src="item.thumb_dir" lazy />

                <i
                  v-if="data.cover === '' || isImage"
                  v-show="index == imgIndex"
                  :class="colorBtn ? 'active' : ''"
                  class="el-icon-success image-success"
                />
                <i
                  v-else
                  v-show="index == getFindIndex(imageArray, 'thumb_dir', data.cover)"
                  :class="data.color == '#FFFFFF' ? 'active' : ''"
                  class="el-icon-success image-success"
                />
              </li>
            </ul>
          </div>
          <div class="text-left">
            <el-pagination
              :page-size="where.limit"
              :current-page="where.page"
              layout="prev, pager, next"
              :total="total"
              @size-change="handleSizeChange"
              @current-change="pageChange"
            />
          </div>
        </el-col>
      </el-col>
      <div slot="footer" class="dialog-footer">
        <el-button size="small" @click="closeDialog">{{ $t('public.cancel') }}</el-button>
        <el-button size="small" type="primary" @click="handleConfirm">{{ $t('public.ok') }}</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import RGBaster from '@/utils/rgbaster'
import { attachCoverDeleteApi, attachCoverListApi, attachCoverSetApi } from '@/api/enterprise'
import SettingMer from '@/libs/settingMer'
import { getToken } from '@/utils/auth'
export default {
  name: 'Preview',

  props: {
    title: {
      type: String,
      default: ''
    },
    column: {
      type: Number,
      default: 5
    },
    data: {
      type: Object,
      default: () => {
        return {}
      }
    }
  },
  data() {
    return {
      dialogFormVisible: false,
      imageArray: [],
      imgIndex: null,
      id: 0,
      where: {
        page: 1,
        limit: 15
      },
      total: 0,
      defaultImg: require('@/assets/images/moren.jpg'),
      defaultMore: require('@/assets/images/moren.jpg'),
      colorBtn: false,
      isImage: false,
      myHeaders: {
        authorization: 'Bearer ' + getToken()
      },
      uploadData: {},
      uploadSize: 10,
      deleteButton: false
    }
  },
  computed: {
    fileUrl() {
      return SettingMer.https + `/system/attach/cover`
    }
  },
  mounted() {},
  methods: {
    openDialog() {
      this.dialogFormVisible = true
      this.getTableData()
    },
    getTableData() {
      const data = {
        page: this.where.page,
        limit: this.where.limit
      }
      attachCoverListApi(data).then((res) => {
        this.imageArray = res.data.list
        if (this.data.cover != '') {
          this.imgIndex = this.getFindIndex(this.imageArray, 'thumb_dir', this.data.cover)
          this.defaultImg = this.data.cover
          this.isImage = false
        } else {
          this.imgIndex = null
          this.defaultImg = this.defaultMore
        }
        this.total = res.data.count
      })
    },
    pageChange(page) {
      this.where.page = page
      this.getTableData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTableData()
    },
    checkImage(index, item) {
      this.isImage = true
      this.defaultImg = item.thumb_dir
      var _this = this
      this.deleteButton = this.$store.state.user.userInfo.uid === item.uid && item.thumb_dir !== this.data.cover
      window.RGBaster.colors(this.defaultImg, {
        paletteSize: 30,
        // 颜色排除
        exclude: ['rgb(255,255,255)', 'rgb(0,0,0)'],
        success: function (payload) {
          const RgbValue = payload.dominant.replace('rgb(', '').replace(')', '')
          const RgbValueArray = RgbValue.split(',')
          const $grayLevel =
            Number(RgbValueArray[0]) * 0.299 + Number(RgbValueArray[1]) * 0.587 + Number(RgbValueArray[2]) * 0.114
          if ($grayLevel >= 192) {
            _this.colorBtn = false
          } else {
            _this.colorBtn = true
          }
        }
      })
      this.imgIndex = index
      this.id = item.id
    },
    closeDialog() {
      this.dialogFormVisible = false
      this.isImage = false
      this.id = 0
      this.imgIndex = null
      this.deleteButton = false
    },
    handleConfirm() {
      if (this.id == 0) {
        this.$message.error('请选择封面图')
      } else {
        this.setImagePreview()
      }
    },
    async deleteImage() {
      await attachCoverDeleteApi({ ids: [this.id] })
      this.getTableData()
    },
    async setImagePreview() {
      const data = {
        cover: this.defaultImg,
        color: this.colorBtn ? '#FFFFFF' : '#000000'
      }
      await attachCoverSetApi(this.data.id, data)
      await this.$emit('handlePreview', 1)
      this.closeDialog()
    },
    getFindIndex(arr, name, number) {
      const index = arr.findIndex(function (item) {
        return item[name] == number
      })
      return index
    },
    // 上传成功
    handleSuccess(response) {
      if (response.status === 200) {
        this.$message.success('上传成功')
        this.where.page = 1
        this.getTableData()
      } else {
        this.$message.error(response.message)
      }
    },
    // 上传前
    handleUpload(file) {
      const types = ['jpeg', 'gif', 'bmp', 'png', 'jpg', 'doc', 'docx', 'xls', 'xlsx', 'xlsm', 'ppt', 'pptx', 'txt']
      this.uploadData.cid = 9
      const fileTypeName = file.name.substr(file.name.lastIndexOf('.') + 1)
      const isImage = types.includes(fileTypeName.toLowerCase())
      const isLtSize = file.size / 1024 / 1024 < this.uploadSize
      if (!isImage) {
        // this.$message.error('暂不支持 ' + fileTypeName + '该格式');
        this.$message.error('仅支持 ' + types.join(',') + ' 格式')
        return false
      }
      if (!isLtSize) {
        this.$message.error('上传图片大小不能超过 ' + this.uploadSize + ' MB!')
        return false
      }
      return true
    }
  }
}
</script>

<style lang="scss" scoped>
.color-black {
  color: #000000 !important;
}
.color-withe {
  color: #ffffff !important;
}
.text-left {
  .el-pagination {
    justify-content: flex-start;
  }
}
.dialog-title {
  font-size: 14px;
  display: flex;
  .dialog-title-title {
    width: 50%;
  }
  .dialog-title-upload {
    width: 50%;
    text-align: right;
    font-size: 13px;
    span {
      cursor: pointer;
      padding-right: 15px;
      i {
        padding-right: 6px;
      }
    }
    .upload {
      display: inline-block;
      span {
        padding-right: 0;
      }
    }
  }
}
.image-preview {
  width: 172px;
  height: auto;
  .image-preview-con {
    width: auto;
    height: 208px;
    position: relative;
    img {
      width: 100%;
      height: 100%;
    }
    .public-img {
      left: 10px;
      position: absolute;
      color: #000000;
      top: 15px;
      p {
        padding: 0;
        margin: 0;
      }
      .title {
        font-size: 15px;
        padding-bottom: 10px;
      }
      .content {
        font-size: 13px;
      }
    }
  }
}
.image-list {
  margin-left: 20px;
  .image-list-ul {
    margin: 0;
    padding: 0;
    list-style: none;
    overflow: hidden;
    min-height: 342px;
    li {
      width: 86px;
      height: 104px;
      float: left;
      margin: 0 30px 10px 0;
      position: relative;
      ::v-deep .el-image__inner {
        width: 86px;
        height: 104px;
      }
      .image-success {
        position: absolute;
        right: 10px;
        top: 10px;
        font-size: 14px;
        color: #1890ff;
      }
      .image-success.active {
        color: #ffffff;
      }
    }
    li.mActive {
      // margin-right: 0;
    }
  }
}
</style>
