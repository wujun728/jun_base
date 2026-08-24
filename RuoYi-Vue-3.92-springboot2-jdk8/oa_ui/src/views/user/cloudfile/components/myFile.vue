<template>
  <div class="content">
    <div class="header-caption">
      <el-row class="mb10">
        <el-col :span="21">
          <el-breadcrumb separator-class="el-icon-arrow-right">
            <el-breadcrumb-item
              v-for="(item, index) in breadcrumbArray"
              :key="index"
              @click.native="getBreadcrumb(item.pid, index)"
            >
              {{ item.name }}
            </el-breadcrumb-item>
          </el-breadcrumb>
        </el-col>
        <el-col :span="3" class="text-right">
          <span>{{ $t('file.total') }} {{ total }} {{ $t('file.individual') }}</span>
        </el-col>
      </el-row>
    </div>
    <div v-if="fileData.length > 0">
      <div class="first-header">
        <p>
          <el-checkbox v-model="menuIds" :disabled="fileData.length <= 0" :label="0" @change="checkChange(1)">
            {{ checked ? $t('file.reverse') : $t('file.all') }}
          </el-checkbox>
        </p>
        <p v-if="fileStyle.style == 1 ? true : false">{{ $t('file.updatetime') }}</p>
        <p v-if="fileStyle.style == 1 ? true : false">{{ $t('file.collection') }}</p>
      </div>
      <ul :class="fileStyle.style == 1 ? 'content-ul' : 'infeed-ul'" class="public-ul">
        <li v-for="(item, index) in fileData" :key="index">
          <div class="content-left pointer">
            <el-checkbox :key="item.id" v-model="menuIds" :label="item.id" @change="checkChange(2)" />
            <p
              v-if="renameIndex !== index"
              class="public-title"
              @click="getItemFile(item)"
              @contextmenu.prevent.stop="rightClick($event, item, index)"
            >
              <i :class="getFileType(item.type, item.file_url)" class="icon iconfont" />
              <span class="over-text">{{ item.name }}</span>
            </p>
            <p v-else class="public-title">
              <i :class="getFileType(item.type, item.file_url)" class="icon iconfont" @click="getItemFile(item)" />
              <input
                :id="'input' + index"
                v-model="item.name"
                :placeholder="$t('file.placeholder10')"
                autofocus="autofocus"
                class="rename-input"
                maxlength="20"
                @blur="renameBlur(item)"
              />
            </p>
          </div>
          <div v-if="fileStyle.style == 1 ? true : false">{{ item.updated_at }}</div>
          <div v-if="fileStyle.style === 1 ? true : false" class="icon-star">
            <i
              :class="item.is_collect === 0 ? 'el-icon-star-off' : 'el-icon-star-on color-collect'"
              class="collect"
              @click="getCollect(item)"
            />
            <div class="icon-star-right">
              <i
                v-if="item.type !== 1 && fileIsImage(item.file_type)"
                class="icon iconfont iconfenxiang"
                @click="setShare(item, 2)"
              />
              <el-popover
                :ref="`pop-${index}`"
                :offset="30"
                :width="40"
                placement="bottom-start"
                popper-class="area_popper"
                trigger="click"
                @after-enter="handleShow(index)"
              >
                <div class="right-item-list">
                  <div v-if="item.type == 1" class="right-item" @click="setShortcutItem(item)">
                    {{ item.is_shortcut === 0 ? $t('file.setcommon') : $t('file.cancelcommon') }}
                  </div>
                  <div v-if="item.type != 1" class="right-item" @click="fileDownLoad(item)">
                    {{ $t('public.download') }}
                  </div>

                  <div
                    v-if="item.type != 1 && fileIsImage(item.file_type)"
                    class="right-item"
                    @click="setShare(item, 1)"
                  >
                    {{ item.is_share === 0 ? $t('file.share') : $t('file.cancelsharing') }}
                  </div>
                  <div class="right-item" @click="getStartCollect(item)">
                    {{ item.is_collect === 0 ? $t('file.collection') : $t('file.cancelcollection') }}
                  </div>

                  <div class="right-item" @click="getMoveDialog(item, 1)">{{ $t('file.moveto') }}</div>
                  <div v-if="item.type !== 1" class="right-item" @click="getMoveDialog(item, 2)">
                    {{ $t('file.copyto') }}
                  </div>
                  <div class="right-item" @click="setRenameItem(item, index)">{{ $t('file.rename') }}</div>

                  <div class="right-item" @click="getFileAttribute(item)">{{ $t('file.attribute') }}</div>
                  <div class="right-item" @click="getFolderDelete(item.id)">{{ $t('public.delete') }}</div>
                </div>
                <i slot="reference" class="icon iconfont icongengduo1" />
              </el-popover>
            </div>
          </div>
        </li>
      </ul>
      <div class="text-right">
        <el-pagination
          :current-page="where.page"
          :page-size="where.limit"
          :page-sizes="[10, 15, 20]"
          :total="total"
          layout="total, prev, pager, next, jumper"
          @size-change="handleSizeChange"
          @current-change="pageChange"
        />
      </div>
    </div>
    <default-page v-else :index="7" :min-height="510" />
    <el-image-viewer v-if="isImage" :on-close="closeImageViewer" :url-list="srcList" />
    <share-dialog ref="shareDialog" :from-data="fromData" @isOk="isOk" />
    <move-dialog ref="moveDialog" :move-data="moveData" @handlerMove="handlerMove" />
    <file-attribute ref="fileAttribute" :form-data="formBoxConfig" />
    <right-click ref="rightClick" :config-data="rightData" @handleRightClick="handleRightClick" />
  </div>
</template>

<script>
import {
  folderCollectApi,
  folderDeleteApi,
  folderListApi,
  folderRenameApi,
  folderShortcutApi,
  folderUnCollectApi,
  folderUnShareApi,
  folderUnShortcutApi,
  folderViewApi
} from '@/api/cloud'

import file from '@/utils/file'
import Vue from 'vue'
Vue.use(file)
import SettingMer from '@/libs/settingMer'
export default {
  name: 'MyFile',
  components: {
    shareDialog: () => import('./shareDialog'),
    moveDialog: () => import('./moveDialog'),
    fileAttribute: () => import('./fileAttribute'),
    ElImageViewer: () => import('element-ui/packages/image/src/image-viewer'),
    rightClick: () => import('./rightClick'),
    defaultPage: () => import('@/components/common/defaultPage')
  },
  props: {
    fileStyle: {
      type: Object,
      default: () => {
        return {}
      }
    },
    switch: {
      type: Number,
      default: 4
    },
    wps_type: {
      type: String | Number,
      default: '0'
    }
  },
  data() {
    return {
      checked: false,
      fileData: [],
      activeValue: '',
      renameIndex: null,
      renameName: '',
      where: {
        page: 1,
        limit: 15,
        pid: '',
        is_shortcut: this.switch === 4 ? '' : 1
      },
      total: 0,
      breadcrumbArray: [{ name: this.$t('file.allfiles'), pid: '' }],
      id: 0,
      isImage: false,
      srcList: [],
      formBoxConfig: {},
      menuIds: [],
      handleData: {
        id: 0,
        ids: [],
        type: 1
      },
      fromData: {
        id: 0,
        type: 1
      },
      moveData: {
        id: 0,
        type: 1
      },
      rightData: {
        type: 4,
        data: {}
      },
      rightClickIndex: -1
    }
  },
  watch: {
    switch: {
      handler(nVal) {
        if (nVal === 4) {
          // 我的文件
          this.where.page = 1
          this.where.pid = ''
          this.where.is_shortcut = ''
          this.menuIds = []
          this.checked = false
          this.getTreeData()
        } else {
          this.where.page = 1
          this.where.pid = ''
          this.where.is_shortcut = 1
          this.menuIds = []
          this.checked = false
          this.getTreeData()
        }
        this.breadcrumbArray = [{ name: this.$t('file.allfiles'), pid: '' }]
      },
      deep: true
    },
    lang() {
      this.setOptions()
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },
  mounted() {
    this.getTreeData()
  },
  methods: {
    setOptions() {
      this.breadcrumbArray[0].name = this.$t('file.allfiles')
    },
    pageChange(page) {
      this.where.page = page
      this.getTreeData()
    },
    handleSizeChange(val) {
      this.where.limit = val
      this.getTreeData()
    },
    getTreeData(val) {
      var data = {
        page: this.where.page,
        limit: this.where.limit,
        pid: this.where.pid,
        sort_type: this.fileSortName(this.fileStyle.sort),
        file_type: this.fileTypeName(this.fileStyle.type),
        keyword: this.fileStyle.keyword,
        is_shortcut: this.where.is_shortcut
      }
      folderListApi(data).then((res) => {
        this.fileData = res.data.list
        this.total = res.data.count
        if (this.fileData.length > 0) {
          this.fileData.map((value) => {
            if (!this.fileIsImage(value.file_type)) {
              this.srcList.push(SettingMer.httpUrl + value.file_url)
            }
          })
        }
      })
    },
    // 点击item项
    getItemFile(item) {
      if (item.type == 1) {
        this.where.pid = item.id
        this.where.is_shortcut = 0
        if (this.breadcrumbArray.findIndex((n) => n.pid === item.id) < 0) {
          this.breadcrumbArray.push({ name: item.name, pid: item.id })
          this.getTreeData()
          this.handleData.type = 1
          this.handlerEmit()
        }
      } else {
        // 浏览文件
        const fileType = item.file_url.substr(item.file_url.lastIndexOf('.') + 1)
        const types = ['jpeg', 'gif', 'bmp', 'png', 'jpg']
        const isImage = types.includes(fileType)
        if (!isImage) {
          if (this.wps_type == '1' && ['docx', 'doc', 'xlsx'].includes(item.file_ext)) {
            let name = ['docx', 'doc'].includes(item.file_ext) ? 'fileEdit' : 'excelEdit'
            const routeData = this.$router.resolve({
              name: name,
              query: {
                word_url: item.file_url,
                file_name: item.name,
                id: item.id,
                pid: item.pid
              }
            })
            folderViewApi(item.id)
              .then((res) => {})
              .finally(() => {
                window.open(routeData.href, '_blank')
              })
          } else {
            this.handleSeeFil(item)
          }
        } else {
          this.isImage = true
          const url = SettingMer.httpUrl + item.file_url
          if (this.srcList.length > 0) {
            this.srcList.map((value, index) => {
              if (value === url) {
                this.srcList.splice(index, 1)
              }
            })
            this.srcList.unshift(url)
          }
        }
      }
    },
    closeImageViewer() {
      this.isImage = false
    },
    // 点击面包屑
    getBreadcrumb(id, index) {
      if (id != this.where.pid) {
        if (this.switch === 5 && id == '') {
          this.where.is_shortcut = 1
        }
        this.where.pid = id
        this.breadcrumbArray.splice(index + 1)
        this.getTreeData()
        this.handleData.type = 1
        this.handlerEmit()
      }
    },
    handlerEmit() {
      this.handleData.id = this.where.pid === '' ? 0 : this.where.pid
      this.handleData.ids = this.menuIds
      this.$emit('handlerMyFile', this.handleData)
    },
    // 移动回调
    handlerMove(data) {
      if (data.type === 1) {
        this.getTreeData()
      }
    },
    handleShow(index) {
      this.activeValue = index
    },
    checkChange(type) {
      if (type === 1) {
        if (this.fileData.length > 0) {
          this.fileData.map((item) => {
            if (this.menuIds.findIndex((n) => n === item.id) < 0) {
              this.checked = true
              this.menuIds.push(item.id)
            } else {
              this.checked = false
              if (this.menuIds.findIndex((n) => n == item.id) > -1) {
                this.menuIds.splice(
                  this.menuIds.findIndex((n) => n == item.id),
                  1
                )
              }
            }
          })
        }
      } else {
        if (this.menuIds.length === 1 && this.menuIds[0] === 0) {
          this.checked = false
          this.menuIds = []
        }
      }
      this.handleData.type = 1
      this.handlerEmit()
    },
    getCollect(item) {
      if (item.is_collect == 0) {
        this.setFolderCollect(item)
      } else {
        this.setFolderUnCollect(item)
      }
    },
    // 设置收藏
    async setFolderCollect(item) {
      await folderCollectApi(item.id)
      await this.getTreeData()
      this.handleData.type = 2
      await this.handlerEmit()
    },
    // 取消收藏
    async setFolderUnCollect(item) {
      await folderUnCollectApi(item.id)
      await this.getTreeData()
      this.handleData.type = 2
      await this.handlerEmit()
    },
    // 浏览文件
    async handleSeeFil(item) {
      await folderViewApi(item.id).then((res) => {
        window.open(res.data.url, '_blank')
      })
    },
    getStartCollect(item) {
      this.getCollect(item)
      this.closePopover()
    },
    // 删除
    getFolderDelete(id) {
      folderDeleteApi(id).then((res) => {
        if (this.where.page > 1 && this.fileData.length <= 1) {
          this.where.page--
        }
        this.getTreeData()
        this.closePopover()
      })
    },
    // 重命名
    setRenameItem(item, index) {
      this.renameIndex = index
      this.renameName = item.name
      this.closePopover()
      this.$nextTick(() => {
        document.getElementById('input' + index).focus()
      })
    },
    // 失去焦点
    renameBlur(item) {
      this.renameIndex = null
      if (item.name === '') {
        item.name = this.renameName
        return false
      }
      if (this.renameName !== this.trim(item.name)) {
        this.getFolderRename(item.id, { name: item.name })
      }
    },
    getFileAttribute(item) {
      this.formBoxConfig = {
        title: this.$t('file.fileattribute'),
        is_file: true,
        id: item.id,
        width: '450px'
      }
      this.$refs.fileAttribute.openBox()
    },
    async getFolderRename(id, data) {
      await folderRenameApi(id, data)
      await this.getTreeData()
    },
    setShortcutItem(item) {
      if (item.is_shortcut == 0) {
        this.getFolderShortcut(item.id)
      } else {
        this.getFolderUnShortcut(item.id)
      }
      this.closePopover()
    },
    // 设为常用文件夹
    async getFolderShortcut(id) {
      await folderShortcutApi(id)
      await this.getTreeData()
    },
    // 取消常用文件夹
    async getFolderUnShortcut(id) {
      await folderUnShortcutApi(id)
      await this.getTreeData()
    },
    // 共享
    setShare(item, type) {
      this.id = item.id
      this.fromData.id = item.id
      this.fromData.title = this.$t('file.filesharing')
      this.fromData.name = this.$t('file.selectsharer')
      if (type === 1) {
        this.closePopover()
        this.fromData.type = 1
        if (item.is_share === 0) {
          this.$refs.shareDialog.handleOpen()
        } else {
          this.getFolderUnShare(item)
        }
      } else {
        if (item.is_share === 0) {
          this.fromData.type = 1
        } else {
          this.fromData.type = 2
        }
        this.$refs.shareDialog.handleOpen()
        this.$refs.shareDialog.getRule()
      }
    },
    // 取消共享
    async getFolderUnShare(item) {
      await this.$modalSure(this.$t('file.placeholder09'))
      await folderUnShareApi(item.id)
      await this.getTreeData()
      this.handleData.type = 2
      this.handlerEmit()
    },
    // 移动或复制
    getMoveDialog(item, type) {
      if (type === 1) {
        this.moveData.type = 1
        this.moveData.id = item.id
      } else {
        this.moveData.type = 5
        this.moveData.id = item.id
      }
      this.closePopover()
      this.$refs.moveDialog.dialogVisible = true
      this.$refs.moveDialog.getFolderSpaceEntDir()
    },
    closePopover() {
      if (this.rightClickIndex > -1) {
        this.rightClickIndex = -1
      } else {
        this.$refs[`pop-${this.activeValue}`][0].doClose()
      }
    },
    rightClick(event, item, index) {
      this.rightData.type = this.switch
      this.rightData.data = item
      this.rightClickIndex = index
      this.$refs.rightClick.menuVisible = true
      this.$refs.rightClick.rightClick(event)
    },
    handleRightClick(data) {
      if (data.index === 1) {
        // 收藏
        this.setShortcutItem(data.row)
      } else if (data.index === 2) {
        // 移动
        this.getMoveDialog(data.row, 1)
      } else if (data.index === 3) {
        // 复制
        this.getMoveDialog(data.row, 2)
      } else if (data.index === 4) {
        // 重命名
        this.setRenameItem(data.row, this.rightClickIndex)
      } else if (data.index === 5) {
        // 属性
        this.getFileAttribute(data.row)
      } else if (data.index === 6) {
        // 删除
        this.getFolderDelete(data.row.id)
      } else if (data.index === 7) {
        // 共享
        this.setShare(data.row, 1)
      } else if (data.index === 8) {
        // 收藏
        this.getStartCollect(data.row)
      }
    },
    isOk() {
      this.getTreeData()
      this.handleData.type = 2
      this.handlerEmit()
    }
  }
}
</script>

<style lang="scss" scoped>
.content {
  padding-right: 15px;
  .header-caption {
    ::v-deep .el-breadcrumb__inner {
      cursor: pointer;
    }
  }
  .rename-input {
    width: 100%;
    border: none;
    outline: none;
  }
  .rename-input:focus {
    border: transparent;
  }
  .first-header {
    background-color: #fafafa;
    border-bottom: none;
    margin-bottom: 0;
    display: flex;
    align-items: center;
    padding: 10px 0;
    p {
      margin: 0;
      padding: 0;
    }
    p:nth-of-type(1) {
      width: 55%;
      display: flex;
      align-items: center;
      i {
        padding-right: 15px;
      }
      ::v-deep .el-checkbox {
        margin-right: 15px;
      }
    }
    p:nth-of-type(2) {
      width: 25%;
    }
    p:nth-of-type(3) {
      width: 20%;
    }
  }
  .collect {
    cursor: pointer;
  }
  .public-ul {
    padding: 0;
    margin: 0;
    list-style: none;
    ::v-deep .el-checkbox__label {
      display: none;
    }
  }
  .content-ul {
    li {
      display: flex;
      justify-content: center;
      padding: 15px 0;
      margin: 0 0 10px 0;
      border-bottom: 1px solid #eeeeee;
      font-size: 13px;
      p {
        margin: 0;
        padding: 0;
        i {
          font-size: 20px;
        }
      }
      .content-left {
        width: 55%;
        display: flex;
        align-items: center;
        i {
          margin-right: 15px;
        }
        ::v-deep .el-checkbox {
          margin-right: 15px;
        }
      }
      div:nth-of-type(2) {
        width: 25%;
      }
      .icon-star {
        width: 20%;
        padding-right: 15px;
        .collect {
          font-size: 20px;
        }
      }
      .icon-star-right {
        float: right;
        i {
          margin-right: 15px;
        }
      }
      .public-title {
        display: flex;
        align-items: center;
        width: calc(100% - 150px);
      }
    }
  }
  .infeed-ul {
    display: flex;
    flex-wrap: wrap;
    li {
      display: flex;
      justify-content: center;
      position: relative;
      width: 120px;
      height: 120px;
      margin-right: 20px;
      flex-wrap: wrap;
      span,
      i {
        display: block;
      }
      p {
        margin: 0;
      }
      .content-left {
        width: 100%;
        text-align: center;
        ::v-deep .el-checkbox {
          position: absolute;
          left: 0;
          top: 10px;
        }
        i {
          padding-top: 20px;
          text-align: center;
          font-size: 60px;
        }
        span {
          padding-top: 10px;
        }
      }
      .icon-star {
        position: absolute;
        left: 0;
        bottom: 20px;
      }
    }
  }
}
.right-item-list {
  margin: -12px;
  ::v-deep .el-divider--horizontal {
    margin: 6px 0;
  }
  .right-item {
    padding: 6px 0 0 12px;
    line-height: 25px;
    cursor: pointer;
    &:first-child {
      margin-top: 0;
    }
  }
  .right-item:hover {
    background-color: #f5f7fa;
  }
}
</style>
