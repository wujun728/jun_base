<!-- 左边菜单栏 -->
<template>
  <div>
    <div class="cloudfile-left v-height-flag">
      <div class="mr15 ml15">
        <div v-if="searchFlag" class="search-association">
          <template v-if="searchList.length > 0">
            <div>文件</div>
            <div
              v-for="(item, index) in searchList"
              :key="index"
              :class="isAcitive === index ? 'isAcitive' : ''"
              class="search-item"
              @mouseover="mouseOver(index)"
            >
              <span>1</span>
              <div class="keyword" v-html="item.name"></div>
            </div>
          </template>
          <el-empty v-if="searchList.length <= 0" description="暂无数据"></el-empty>
        </div>
      </div>

      <div class="tab-name mr20 ml20">
        <span class="name">企业空间</span>

        <span class="number p0">
          <el-tooltip content="创建企业空间" effect="dark" placement="top">
            <span class="iconfont icontianjia p0" @click="addName" />
          </el-tooltip>
        </span>
      </div>

      <div
        :class="entIndex == 'recently' ? 'activeText' : ''"
        class="name over-text m20"
        @click="entClick('recently', 0, 'recently')"
      >
        <i class="icon iconfont iconzuijin" /> <span>最近打开</span>
      </div>
      <div class="box">
        <el-scrollbar style="height: 100%">
          <ul v-show="showEntBtn" class="cloudfile-left-list">
            <li
              v-for="(item, index) in entArray"
              :key="index"
              :class="entIndex === index ? 'active' : ''"
              @click="entClick(index, item.id, item)"
            >
              <div class="name over-text">
                <i class="icon iconfont iconrenshi-huibaoguanli-cebian" />
                <span>{{ item.name }}</span>
              </div>
              <div class="number">
                <el-popover
                  v-if="entIndex === index"
                  :ref="`pop-${item.id}`"
                  :offset="10"
                  placement="bottom-end"
                  trigger="click"
                  @hide="handleHide"
                  @after-enter="handleShow(item.id)"
                >
                  <div class="right-item-list">
                    <div class="right-item" @click.stop="addEdit(item)">
                      {{ $store.state.user.userInfo.uid === item.uid ? $t('public.edit') : $t('public.check') }}
                    </div>
                    <div
                      v-if="$store.state.user.userInfo.uid === item.uid"
                      class="right-item"
                      @click.stop="handleDelete(item)"
                    >
                      {{ $t('public.delete') }}
                    </div>
                  </div>
                  <div slot="reference" class="icon iconfont icongengduo right-icon" />
                </el-popover>
              </div>
            </li>
          </ul>
        </el-scrollbar>
      </div>
      <!-- </div> -->

      <!--回收站文件-->

      <div class="border-bottom" />
      <div class="recovery" @click="recoveryFn()"><i class="iconfont iconshanchu1"></i>回收站</div>
    </div>
    <space-dialog ref="spaceDialog" :from-data="fromData" @isOk="getEntList" />
  </div>
</template>

<script>
import { folderSpaceDeleteApi, folderSpaceListApi, folderTotalApi, folderMatchtListApi } from '@/api/cloud'
import debounce from '@form-create/utils/lib/debounce'
import countTo from 'vue-count-to'
export default {
  name: 'CloudfileLeft',
  components: {
    spaceDialog: () => import('../spaceDialog'),
    countTo
  },
  data() {
    return {
      showComBtn: true,
      showEntBtn: true,
      fromData: {},
      tableFrom: {
        id: '',
        selectIndex: 6,
        keyword: '',
        type: 1
      },
      isAcitive: false,
      searchFlag: false,
      searchList: [],
      shareArray: [
        { name: '最近浏览', type: 1, icon: 'iconzuijin' } //最近访问
        // { name: this.$t('file.collection'), type: 2, icon: 'iconshoucang1', number: 0 }, //收藏
        // { name: this.$t('file.share'), type: 3, icon: 'iconchangyongwenjianjia', number: 0 } //分享
      ],
      commonlyArray: [
        { name: this.$t('file.myfiles'), type: 4, icon: 'iconquanbuwendang' }, //我的文件
        { name: this.$t('file.commonfolders'), type: 5, icon: 'iconchangyongwenjianjia' } //常用文件夹
      ],
      recoveryArray: [{ name: this.$t('file.recyclebin'), type: 7, icon: 'iconhuishouzhan' }],
      fileSortData: [
        { name: this.$t('file.filename'), id: 1 }, //文件名
        { name: this.$t('file.creationtime'), id: 2 }, //创建时间
        { name: this.$t('file.updatetime'), id: 3 }, //图片
        { name: this.$t('file.filesize'), id: 4 }, //大小
        { name: this.$t('file.deletetime'), id: 5 }, //删除时间
        { name: this.$t('file.order'), id: 6 }, //升序
        { name: this.$t('file.desc'), id: 7 } //降序
      ],
      shareIndex: 0,
      entIndex: 'recently',
      comIndex: null,
      recIndex: null,
      entArray: [],
      activeValue: '',
      spaceId: 0
    }
  },
  computed: {
    lang: function () {
      return this.$store.getters.lang
    }
  },
  watch: {
    lang() {
      this.setOptions()
    }
  },
  mounted() {
    this.getEntList()
    this.confirmData('recently')
  },
  methods: {
    setOptions() {
      this.shareArray = [
        { name: this.$t('file.lately'), type: 1, icon: 'iconzuijin' }, //最近访问
        { name: this.$t('file.collection'), type: 2, icon: 'iconshoucang1', number: 0 }, //收藏
        { name: this.$t('file.share'), type: 3, icon: 'iconchangyongwenjianjia', number: 0 } //分享
      ]
      this.commonlyArray = [
        { name: this.$t('file.myfiles'), type: 4, icon: 'iconquanbuwendang' }, //我的文件
        { name: this.$t('file.commonfolders'), type: 5, icon: 'iconchangyongwenjianjia' } //常用文件夹
      ]
      this.recoveryArray = [{ name: this.$t('file.recyclebin'), type: 7, icon: 'iconhuishouzhan' }] //回收站文件
      this.getFolderTotal()
    },
    // 展示常用文件夹和我的文件
    showCommonly() {
      this.showComBtn === false ? (this.showComBtn = true) : (this.showComBtn = false)
    },
    // 展开企业空间
    showEnt() {
      this.showEntBtn === false ? (this.showEntBtn = true) : (this.showEntBtn = false)
    },
    //确定是否提交
    confirmData(data) {
      this.$emit('confirmData', this.tableFrom, data)
    },
    recoveryFn() {
      this.tableFrom.selectIndex = 7
      this.confirmData('recovery')
    },
    // 搜索
    handleSearch() {
      this.tableFrom.type = 2
      this.confirmData()
    },
    // 点击企业空间
    entClick(index, id, item) {
      this.shareIndex = null
      this.comIndex = null
      this.recIndex = null
      this.tableFrom.selectIndex = 6
      this.tableFrom.type = 1
      this.tableFrom.id = id
      this.confirmData(item)
      this.entIndex = index
    },

    getEntList() {
      folderSpaceListApi().then((res) => {
        this.entArray = res.data
        if (this.entArray.length > 0) {
          this.entClick('recently', 0, 'recently')
        } else {
          this.tableFrom.id = ''
          this.confirmData('recently')
        }
      })
    },
    //获取文件夹总数
    getFolderTotal() {
      folderTotalApi().then((res) => {
        this.shareArray[1].number = res.data.collect
        this.shareArray[2].number = res.data.share
      })
    },
    // 展示回收站文件
    clickRecovery(index, type) {
      this.shareIndex = null
      this.entIndex = null
      this.comIndex = null
      this.tableFrom.selectIndex = type
      this.tableFrom.type = 1
      this.tableFrom.id = 0
      this.confirmData()
      this.recIndex = index
    },
    // 鼠标悬浮
    mouseOver(index) {
      this.isAcitive = index
    },

    // 防抖input
    inputChange: debounce(function () {
      // this.searchFlag = true;
      // this.folderMatchtListApi();
    }, 1500),

    folderMatchtListApi() {
      let data = {
        keyword: this.tableFrom.keyword
      }

      folderMatchtListApi(this.tableFrom.id, data).then((res) => {
        this.searchList = res.data
        this.searchList.map((item) => {
          item.name = this.discolorationKeyword(item.name)
        })
      })
    },

    // 关键字变色
    discolorationKeyword(val) {
      const keyword = this.tableFrom.keyword
      if (val.indexOf(keyword) > -1) {
        return val.replace(keyword, `<span style="color:#1890ff;">${keyword}</span>`)
      } else {
        return val
      }
    },

    addName() {
      this.fromData = {
        title: this.$t('file.spacesettings'),
        name: this.$t('setting.edit.selectmembers1'),
        edit: 1
      }
      this.$refs.spaceDialog.dialogVisible = true
    },
    // 编辑窗口显示
    handleShow(value) {
      this.activeValue = value
    },
    // 编辑窗口隐藏
    handleHide() {
      this.activeValue = ''
    },
    // 修改分类
    addEdit(item) {
      this.fromData = {
        title: `空间${this.$store.state.user.userInfo.uid === item.uid ? '编辑' : '查看'}`,
        name: this.$t('setting.edit.selectmembers1'),
        edit: 2,
        data: item
      }
      this.$refs.spaceDialog.dialogVisible = true
    },
    // 删除
    async handleDelete(item) {
      await this.$modalSure(this.$t('file.placeholder02'))
      await folderSpaceDeleteApi(item.id)
      this.getEntList()
    }
  }
}
</script>

<style lang="scss" scoped>
.isAcitive {
  background-color: #f5f5f5;
}
::v-deep.el-input--small .el-input__inner {
  cursor: pointer;
}
.box {
  height: calc(100vh - 220px);
}
.recovery {
  cursor: pointer;
  margin-left: 23px;
  margin-top: 14px;
  font-family: PingFang SC, PingFang SC;
  font-weight: 400;
  font-size: 14px;
  color: #303133;
  .iconshanchu1 {
    color: #909399;
    margin-right: 8px;
  }
}

.m20 {
  height: 40px;
  line-height: 40px;
  padding-left: 20px;
  cursor: pointer;
}

::v-deep .el-empty__image svg {
  width: 50%;
}
.icontianjia {
  color: #1890ff;
  cursor: pointer;
}

.cloudfile-left {
  font-family: PingFang SC, PingFang SC;
  margin-right: -20px;
  .p0 {
    padding: 0 !important;
  }
  ::v-deep .el-scrollbar__wrap {
    overflow-x: hidden;
  }
  border-right: 1px solid #f5f5f5;
  padding-top: 20px;
  .cloudfile-left-list {
    list-style: none;
    padding: 0;
    li {
      cursor: pointer;
      display: flex;
      align-items: center;
      padding: 10px 15px;
      padding-left: 20px;
      font-weight: 400;
      font-size: 14px;
      color: #303133;
      border-right: 2px solid #ffffff;
      i {
        font-size: 20px;
        padding-right: 3px;
        color: #909399;
      }
      .iconzuijin {
        font-size: 18px;
      }
      .name {
        width: 70%;
        font-size: 13px;
      }
      .number {
        width: 30%;
        display: flex;
        justify-content: flex-end;
      }
    }

    li.border {
      border-bottom: 1px solid #f5f5f5;
    }
    li.active {
      background: rgba(24, 144, 255, 0.08);
      color: #1890ff;
      font-family: PingFang SC, PingFang SC;
      font-weight: 500;
      font-size: 14px;
      border-right: 2px solid #1890ff;
    }
  }
  .border-bottom {
    border-bottom: 1px solid #eeeeee;
  }
  .activeText {
    background: rgba(24, 144, 255, 0.08);
    color: #1890ff;
    font-family: PingFang SC, PingFang SC;
    font-weight: 500;
    font-size: 14px;
    border-right: 2px solid #1890ff;
  }

  .tab-name {
    cursor: auto;
    font-size: 13px;
    display: flex;
    align-items: center;
    padding: 0px 0 12px 0;
    .name {
      width: 70%;
      font-weight: 600;
      font-size: 14px;
      color: #303133;
    }
    .number {
      width: 30%;
      text-align: right;
    }
    span {
      padding-right: 10px;
    }
    i {
      cursor: pointer;
      font-size: 13px;
    }
  }
}
.search-association {
  position: absolute;
  top: 60px;
  width: 213px;
  height: 253px;
  background: #ffffff;
  box-shadow: 0px 0px 14px 0px rgba(0, 0, 0, 0.08);
  border-radius: 4px;
  z-index: 500;
  padding-top: 12px 0;
  font-size: 13px;
  font-weight: 400;
  color: #909399;
  overflow-y: auto;

  .search-item {
    height: 40px;
    line-height: 40px;
    padding: 0 12px;
    cursor: pointer;

    .keyword {
      font-size: 13px;
      font-weight: 400;
      color: #303133;
    }
  }
}
.right-item-list {
  margin: -12px;
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
::v-deep.el-scrollbar__view {
  height: 100%;
  overflow-y: scroll;
  scrollbar-width: none; /* firefox */
  -ms-overflow-style: none; /* IE 10+ */
}
</style>
