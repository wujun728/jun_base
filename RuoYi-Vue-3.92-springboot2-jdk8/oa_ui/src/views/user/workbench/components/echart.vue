<template>
  <div class="assess">
    <el-scrollbar style="height: 100%; width: 100%;">
      <ul class="assess-ul">
        <li v-for="(item, index) in tableData" :key="index">
          <el-row class="assess-item-top">
            <el-col :span="22" class="top-left">
              <div class="name over-text1">
                {{ item.name }}
              </div>
              <div class="ratio" :title="assessInfo.types == 1 ? '总分' + item.ratio : '权重' + item.ratio + '%'">
                ({{ item.ratio }}{{ assessInfo.types == 1 ? '分' : '%' }})
              </div>
            </el-col>
            <el-col :span="2" class="text-right">
              <i
                class="assess-icon"
                :class="index === showIndex && showBtn ? 'el-icon-arrow-down' : 'el-icon-arrow-right'"
                @click="showItem(index)"
              ></i>
            </el-col>
          </el-row>
          <div class="assess-item-body" v-show="index === showIndex && showBtn">
            <el-row
              class="assess-list-item"
              v-for="(items, index) in item.target"
              :key="'assess' + index"
              @click.native="targetItem(item, items)"
            >
              <el-col :span="22" class="top-left">
                <div class="name over-text1">{{ items.name }}</div>
                <div class="ratio" :title="assessInfo.types == 1 ? '总分' + items.ratio : '权重' + items.ratio + '%'">
                  ({{ items.ratio }}{{ assessInfo.types == 1 ? '分' : '%' }})
                </div>
              </el-col>
              <el-col :span="2" class="text-right">
                <el-progress
                  class="pointer"
                  type="circle"
                  :width="12"
                  :stroke-width="2"
                  :show-text="false"
                  :percentage="changeProgress(items)"
                ></el-progress>
              </el-col>
            </el-row>
          </div>
        </li>
      </ul>
    </el-scrollbar>
    <assess-target ref="assessTarget" :config="configData"></assess-target>
  </div>
</template>
<script>
import { userAssessInfo } from '@/api/user'
export default {
  name: 'AssessInfo',
  props: {
    id: {
      type: Number,
      default: 0
    }
  },
  components: {
    assessTarget: () => import('./assessTarget')
  },
  data() {
    return {
      tableData: {},
      assessInfo: {},
      showIndex: 0,
      showBtn: true,
      configData: {}
    }
  },
  computed: {},
  mounted() {
    this.getTableData()
  },
  methods: {
    async getTableData() {
      const result = await userAssessInfo(this.id)
      this.tableData = result.data ? result.data.info : []
      this.assessInfo = result.data ? result.data.assessInfo : {}
    },
    showItem(index) {
      if (this.showIndex === index) {
        this.showBtn = !this.showBtn
      } else {
        this.showIndex = index
        this.showBtn = true
      }
    },
    targetItem(item, tItem) {
      this.configData = {
        title: '自评',
        width: '600px',
        assessId: this.id,
        spaceId: item.id,
        targetId: tItem.id,
        data: tItem,
        types: this.assessInfo.types
      }
      this.$refs.assessTarget.handleOpen()
    },
    changeProgress(item) {
      let num = 0
      if (this.assessInfo.types == 1) {
        num = Math.floor((item.finish_ratio / item.ratio) * 100)
      } else {
        num = Number(item.finish_ratio)
      }
      return num
    }
  }
}
</script>

<style lang="scss" scoped>
.assess {
  width: 100%;
  height: 406px;
  overflow: hidden;
  padding-bottom: 14px;
  .assess-ul {
    padding: 6px 20px 0 20px;
    margin: 0;
    list-style: none;
    li {
      margin-bottom: 20px;
      &:last-of-type {
        margin-bottom: 0;
      }
      .assess-item-top {
        .top-left {
          position: relative;
          display: flex;
          align-items: center;
          padding-left: 14px;
          font-size: 14px;
          &:after {
            content: '';
            width: 4px;
            height: 4px;
            background-color: #1890ff;
            border-radius: 50%;
            position: absolute;
            left: 0;
            top: 6px;
          }
          .name {
            color: #303133;
            max-width: 80%;
            font-weight: 600;
          }
          .ratio {
            color: #909399;
            font-size: 13px;
            padding-left: 6px;
          }
        }
        .assess-icon {
          cursor: pointer;
        }
      }

      .assess-item-body {
        margin: 15px 0;
        padding: 20px 15px;
        background: #f2f5fc;
        border-radius: 4px;
        .assess-list-item {
          margin-bottom: 20px;
          cursor: pointer;
          &:last-of-type {
            margin-bottom: 0;
          }
          .top-left {
            display: flex;
            align-items: center;
            font-size: 13px;
            .name {
              color: #303133;
              max-width: 80%;
            }
            .ratio {
              color: #909399;
              padding-left: 6px;
            }
          }
        }
      }
    }
  }
}
</style>
