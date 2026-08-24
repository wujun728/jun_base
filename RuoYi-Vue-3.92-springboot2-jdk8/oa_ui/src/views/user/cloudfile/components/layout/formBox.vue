<template>
  <!-- 云盘-文件顶部操作 -->
  <div class="header" draggable="false" ref="showboxRef">
    <div>
      <div v-if="switchIndex == 2" class="header-left">
        <el-button size="small" @click="allMove">移动至</el-button>
        <el-button size="small" @click="allDelete(1)">删除</el-button>
      </div>
      <div v-if="switchIndex == 3" class="header-left">
        <el-tabs v-model="type" class="tab-share" @tab-click="handleClick">
          <el-tab-pane label="共享给我" name="1" />
          <el-tab-pane label="我的共享" name="2" />
        </el-tabs>
      </div>
      <div v-if="switchIndex == 4 || switchIndex == 5" class="header-left">
        <el-dropdown v-if="switchIndex == 4" trigger="click" @command="handleScreen">
          <el-button type="primary" icon="el-icon-plus" size="small">{{ $t('file.newlybuild') }}</el-button>
          <el-dropdown-menu slot="dropdown">
            <el-dropdown-item
              v-for="item in newlyBuildData"
              :key="item.id"
              :divided="item.divided"
              style="width: 160px"
              placement="top-end"
              :command="item.id"
            >
              <i class="icon iconfont" :class="item.icon" />
              <span>{{ item.name }}</span>
            </el-dropdown-item>
          </el-dropdown-menu>
        </el-dropdown>
        <el-upload
          v-if="switchIndex == 4"
          class="upload"
          action="##"
          :on-success="handleSuccess"
          :on-change="onChenge"
          :http-request="uploadServerLog"
          :show-file-list="false"
          :before-upload="handleUpload"
          multiple
        >
          <el-button plain size="small" class="ml10 mr10">{{ $t('file.upload') }}</el-button>
        </el-upload>
        <el-button plain size="small" @click="allMove">{{ $t('file.moveto') }}</el-button>
        <el-button plain size="small" @click="allDelete(1)">{{ $t('public.delete') }}</el-button>
      </div>

      <div v-if="switchIndex == 6 || switchIndex == 1" class="header-16 mt20">
        <div class="title-16">
          <div v-if="breadcrumbArray.length == 0">最近打开</div>
          <el-breadcrumb separator-class="el-icon-arrow-right" v-else>
            <el-breadcrumb-item
              v-for="(item, indexJ) in breadcrumbArray"
              :key="indexJ"
              @click.native="getBreadcrumb(item.pid, indexJ)"
            >
              <span class="breadcrumb-item" :style="{ color: indexJ == breadcrumbArray.length - 1 ? '#303133' : '' }">
                {{ item ? item.name : '--' }}
              </span>
            </el-breadcrumb-item>
          </el-breadcrumb>
        </div>

        <div class="flex" v-if="spaceId && breadcrumbArray.length != 0">
          <!-- 新建 -->
          <el-dropdown trigger="click" @command="handleScreen" class="mr10 ml14">
            <el-button type="primary" icon="el-icon-plus" size="small">{{ $t('file.newlybuild') }}</el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item
                v-for="item in newlyBuildData"
                :key="item.id"
                :divided="item.divided"
                style="width: 160px"
                placement="top-end"
                :command="item.id"
              >
                <template v-if="item.id !== 6">
                  <i class="icon iconfont" :class="item.icon" />
                  <span>{{ item.name }}</span>
                </template>
                <el-upload
                  ref="upload"
                  class="upload-list"
                  v-if="item.id == 6"
                  action="##"
                  :on-success="handleSuccess"
                  :on-change="onChenge"
                  :http-request="uploadServerLog"
                  :show-file-list="false"
                  :before-upload="handleUpload"
                  multiple
                >
                  <div>
                    <i class="icon iconfont" :class="item.icon" />
                    {{ item.name }}
                  </div>
                </el-upload>
              </el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
          <!-- 更多操作 -->
          <el-dropdown trigger="hover">
            <span class="el-dropdown-link">
              <i class="iconfont icongengduo2 pointer"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="allMove">移动至</el-dropdown-item>
              <!-- <el-dropdown-item @click.native="recoveryBtn">回收站</el-dropdown-item> -->
              <el-dropdown-item @click.native="allDelete(3)">删除</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </div>
      </div>

      <div v-if="switchIndex == 7" class="header-left mt20">
        <el-button plain size="small" @click="allDestroy">{{ $t('file.restorefile') }}</el-button>
        <el-button plain size="small" @click="allDelete(2)">{{ $t('file.completelydelete') }}</el-button>
        <el-button v-if="entButton" plain size="small" @click="returnSpace">
          {{ $t('file.returnspace') }}
        </el-button>
      </div>
    </div>
    <div>
      <div class="header-16">
        <div :class="breadcrumbArray.length > 0 ? 'mt8' : 'mt14'">
          <span class="total-16">共{{ total }}项</span>
          <el-input
            placeholder="搜索文件名称"
            style="width: 250px"
            prefix-icon="el-icon-search"
            size="small"
            clearable
            v-model="fileStyle.keyword"
            @change="getStyle()"
          >
          </el-input>
        </div>

        <div class="flex" :class="breadcrumbArray.length > 0 ? 'mt8' : 'mt14'">
          <!-- 文件时间筛选 -->
          <div class="mr10 icon-name" v-if="switchIndex !== 1">
            <div class="el-dropdown sort-dropdown display-align">
              <span
                @click.stop="handleSortShow"
                ref="showPanel"
                class="el-dropdown-link pointer el-dropdown-selfdefine text-16"
              >
                {{ sortName }} <i class="icon iconfont iconpaixu"></i
              ></span>
              <div class="sort-content" v-show="isSortShow">
                <ul class="el-dropdown-menu el-popper el-dropdown-menu--medium">
                  <li
                    v-for="(item, index) in fileSortData"
                    :key="item.id"
                    @click="handleCommand(item.id)"
                    v-show="(index !== 4 && switchIndex !== 7) || switchIndex === 7"
                    class="el-dropdown-menu__item"
                  >
                    <i
                      :style="{ paddingRight: sortIndex - 1 === index ? '0' : '15px' }"
                      :class="sortIndex - 1 === index ? 'el-icon-check' : ''"
                    />
                    <span>{{ item.name }}</span>
                  </li>
                </ul>
                <ul class="el-dropdown-menu el-popper el-dropdown-menu--medium sort-ul">
                  <li
                    v-for="(item, index) in fileSort"
                    :key="'sort' + item.id"
                    @click="handleSort(index, item.id)"
                    class="el-dropdown-menu__item"
                  >
                    <i
                      :style="{ paddingRight: sortValue === index ? '0' : '15px' }"
                      :class="sortValue === index ? 'el-icon-check' : ''"
                    />
                    <span>{{ item.name }}</span>
                  </li>
                </ul>
              </div>
            </div>
          </div>

          <!-- 文件类型筛选 -->
          <div class="mr10 icon-name">
            <el-dropdown class="el-dropdown sort-dropdown display-align" trigger="click" @command="handleType">
              <span class="el-dropdown-link pointer el-dropdown-selfdefine text-16">
                类型
                <i class="icon iconfont iconshaixuan" />
              </span>
              <el-dropdown-menu slot="dropdown">
                <el-dropdown-item
                  v-for="(item, index) in fileScreenData"
                  :key="item.id"
                  style="width: 120px"
                  :command="item.id"
                >
                  <i
                    :style="{ paddingRight: typeValue === item.id ? '0' : '15px' }"
                    :class="typeValue === item.id ? 'el-icon-check' : ''"
                  />
                  <span>{{ item.name }}</span>
                </el-dropdown-item>
              </el-dropdown-menu>
            </el-dropdown>
          </div>

          <!-- 文件展示样式 -->
          <div class="el-dropdown-link pointer text">
            <i @click="handleFileStyle(2)" v-if="fileStyle.style == 1" class="icon iconfont iconkuaizhuangyangshi" />
            <i @click="handleFileStyle(1)" v-if="fileStyle.style == 2" class="icon iconfont iconliebiaoyangshi" />
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import helper from '@/libs/helper'
import SettingMer from '@/libs/settingMer'
import { getToken } from '@/utils/auth'
export default {
  name: '',
  components: {},
  props: {
    switchIndex: {
      type: Number,
      default: 1
    },
    pid: {
      type: Number,
      default: 0
    },
    spaceId: {
      type: Number,
      default: 0
    },
    total: {
      type: Number,
      default: 0
    },
    breadcrumbArray: {
      type: Array,
      default: []
    }
  },
  data() {
    return {
      myHeaders: {
        authorization: 'Bearer ' + getToken()
      },
      entButton: false,
      fileUrl: '',
      uploadSize: 50,
      uploadData: {},
      type: '1',
      isSortShow: false,
      fileStyle: {
        style: 1,
        sort: 3,
        type: 1,
        keyword: '',
        sortBy: 6
      },
      newlyBuildData: [
        { name: '使用模版', id: 7, icon: 'iconshiyongmoban color-doc' },
        { name: '上传文件', id: 6, icon: ' iconwenjianshangchuan color-file', divided: true },
        { name: this.$t('file.newdocument'), id: 1, icon: 'iconwendang1 color-doc', divided: false },
        { name: this.$t('file.newtable'), id: 2, icon: 'iconbiaoge color-excel', divided: false },
        { name: this.$t('file.newslide'), id: 3, icon: 'iconppt color-ppt', divided: false },
        { name: this.$t('file.newfolder'), id: 5, icon: 'iconwenjianjia color-file', divided: true }
      ],
      fileSortData: [
        { name: this.$t('file.filename'), id: 1 },
        { name: this.$t('file.creationtime'), id: 2 },
        { name: this.$t('file.updatetime'), id: 3 },
        { name: this.$t('file.filesize'), id: 4 },
        { name: this.$t('file.deletetime'), id: 5 }
      ],
      fileSort: [
        { name: this.$t('file.order'), id: 6 },
        { name: this.$t('file.desc'), id: 7 }
      ],
      fileScreenData: [
        { name: this.$t('file.type'), id: 1 },
        { name: this.$t('file.document'), id: 2 },
        { name: this.$t('file.ppt'), id: 3 },
        { name: this.$t('file.picture'), id: 4 },
        { name: this.$t('file.table'), id: 5 }
      ],
      newlyBuildData: [
        { name: '使用模版', id: 7, icon: 'iconshiyongmoban color-doc' },
        { name: '上传文件', id: 6, icon: ' iconwenjianshangchuan color-file', divided: true },
        { name: this.$t('file.newdocument'), id: 1, icon: 'iconwendang1 color-doc', divided: false },
        { name: this.$t('file.newtable'), id: 2, icon: 'iconbiaoge color-excel', divided: false },
        { name: this.$t('file.newmindmap'), id: 9, icon: 'iconxmindgeshi color-mindmap', divided: false },
        // { name: this.$t('file.newslide'), id: 3, icon: 'iconppt color-ppt', divided: false },
        { name: this.$t('file.newfolder'), id: 5, icon: 'iconwenjianjia color-file', divided: true }
      ],
      screenIndex: 1,
      screenName: '',
      typeValue: 1,
      sortIndex: 3,
      sortValue: 1,
      sortName: '',
      typeName: ''
    }
  },
  watch: {
    spaceId: {
      handler(nVal, oVal) {
        if (this.switchIndex == 6 || this.switchIndex == 1) {
          this.typeValue = 1
          // this.$emit('formBoxClick', 'handleType', 1)
          this.fileUrl = SettingMer.https + `/cloud/file/${this.spaceId}/upload`
        } else {
          this.fileUrl = SettingMer.https + `/folder/upload`
        }
      },
      immediate: true,
      deep: true
    }
  },
  mounted() {
    this.sortName = this.fileSortData[this.sortIndex - 1].name

    document.addEventListener('click', this.handleGlobalClick)
  },
  destroyed() {
    document.removeEventListener('click', this.handleGlobalClick)
  },
  methods: {
    handleGlobalClick(e) {
      if (this.$refs.showboxRef) {
        let isSelf = this.$refs.showPanel?.contains(e.target)
        if (!isSelf) {
          this.isSortShow = false
        }
      }
    },
    // 上传
    uploadServerLog(params) {
      this.$emit('uploadServerLog', params)
    },
    allMove() {
      this.$emit('formBoxClick', 'allMove')
    },
    allDelete(val) {
      this.$emit('formBoxClick', 'allDelete', val)
    },
    handleClick(e) {
      this.$emit('formBoxClick', 'handleClick', e)
    },
    handleScreen(e) {
      if (e === 6) return false
      this.$emit('formBoxClick', 'handleScreen', e)
    },
    getBreadcrumb(pid, index) {
      this.$emit('formBoxClick', 'getBreadcrumb', pid, index)
    },
    getStyle() {
      this.$emit('formBoxClick', 'styleChage', this.fileStyle)
    },
    handleType(command) {
      this.typeValue = command
      this.$emit('formBoxClick', 'handleType', command)
    },

    // 排序
    handleSort(index, id) {
      this.sortValue = index
      this.fileStyle.sortBy = id
      this.isSortShow = false
      this.$emit('handleSort', index, id)
    },
    handleCommand(command) {
      this.isSortShow = false
      this.sortName = this.fileSortData[command - 1].name
      this.sortIndex = command
      this.fileStyle.sort = command

      this.$emit('handleCommand', command)
    },
    handleSortShow() {
      this.isSortShow = !this.isSortShow
    },
    handleFileStyle(e) {
      this.fileStyle.style = e
      this.$emit('formBoxClick', 'styleChage', this.fileStyle, 'unChange')
    },

    // 回收站
    recoveryBtn() {
      this.entButton = true
      this.$emit('formBoxClick', 'recoveryBtn')
    },

    // 返回企业空间
    returnSpace() {
      this.$emit('formBoxClick', 'returnSpace')
    },

    // 上传成功
    handleSuccess(response, file, fileList) {},

    // 上传文件变化
    onChenge(file, fileList) {},

    // 上传前
    handleUpload(file, item) {
      this.$emit('uploadFile', file)
    }
  }
}
</script>
<style scoped lang="scss">
.cloudfile-right {
  padding: 0 20px;

  .header-16 {
    display: flex;
    justify-content: space-between;
    align-items: center;
    .breadcrumb-item {
      cursor: pointer;
      font-family: PingFang SC, PingFang SC;
      font-weight: 400;
      font-size: 15px;
      color: #909399;
      margin-top: 26px;
    }
  }
  .el-dropdown-link {
    height: 32px;
    display: flex;
    align-items: center;
  }
  .el-dropdown-link:hover {
    background-color: rgba(247, 247, 247, 1);
  }
  .check {
    display: inline-block;
    width: 15px;
  }
  ::v-deep.el-dropdown-menu__item {
    display: flex;
    align-items: center;
  }
  .mt8 {
    margin-top: 7px;
  }
  .text {
    width: 25px;
    color: #909399;
    height: 32px;
    display: flex;
    justify-content: center;
    font-family: PingFang SC, PingFang SC;
    font-weight: 400;
    font-size: 13px;
    border-radius: 4px;
  }

  .header {
    .header-right {
      display: flex;
      align-items: center;
      justify-content: flex-end;
      // text-align: right;
      flex-wrap: wrap;

      ::v-deep .el-dropdown-selfdefine {
        display: flex;
        align-items: center;
        i {
          padding-left: 6px;
        }
      }
      .icon-name {
        font-size: 13px;
        span {
          padding-right: 3px;
        }
        i {
          font-size: 13px;
        }
      }
    }
  }
  .sort-dropdown {
    .sort-content {
      position: absolute;
      left: -20px;
      top: 30px;
      z-index: 222;
      ul {
        width: 120px;
        list-style: none;
        margin-bottom: 0;
      }
      ::v-deep .el-dropdown-menu {
        position: static;
        margin: 0;
      }
      .sort-ul {
        margin-top: -3px !important;
        border-radius: 0 0 4px 4px !important;
        li {
          padding-right: 40px !important;
        }
      }
    }
  }
}
.icongengduo2 {
  font-size: 32px !important;
}
::v-deep.el-pagination {
  padding-bottom: 0;
}
.iconshaixuan {
  margin-left: 4px;
  font-size: 13px;
  color: #999999;
}
.iconpaixu {
  margin-left: 4px;
  font-size: 13px;
  color: #999999;
}

.color-mindmap {
  font-size: 14px;
}
</style>
