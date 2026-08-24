<template>
  <div>
    <div class="title-16 mb20">考核流程</div>
    <div class="flex pt-20">
      <div class="process-left">
        <div class="process-left-con">
          <div v-for="(item, index) in tableData" :key="index" class="process-left-list">
            <el-button type="primary" :class="index == tabIndex ? '' : 'bgc-f7'" round @click="clickBtn(index)">
              {{ item.name }}
            </el-button>
            <div v-if="index != tableData.length - 1" class="process-icon" :class="item.id === 4 ? 'mb40' : ''">
              <i class="el-icon-d-arrow-left" />
            </div>
          </div>
        </div>
      </div>

      <div class="process-right">
        <el-col>
          <div
            v-for="(item, index) in tableData"
            v-show="item.id < 6"
            :key="index"
            class="process-right-list"
            :class="index == tabIndex ? 'active' : ''"
          >
            <p class="title">{{ item.name }}</p>
            <div v-if="item.id == 5" class="process-right-con">
              <div class="left">{{ $t('access.reviewer') }}：</div>
              <div class="right">
                <div class="right-con">
                  <!-- <el-checkbox :checked="true" :disabled="true" @change="changeSuperior($event)"
                    >上上级领导</el-checkbox
                  > -->
                  <el-button type="primary" circle>1</el-button>
                  <span>上上级领导</span>
                </div>
              </div>
            </div>

            <div class="process-right-con">
              <div class="left">{{ item.title }}：</div>
              <div class="right">
                <div v-for="(items, i) in item.children" :key="i" class="right-con">
                  <el-button type="primary" circle>{{ i + 1 }}</el-button>
                  <span>{{ items.label }}</span>
                </div>
              </div>
            </div>
            <div v-if="item.id === 4" class="process-right-con">
              <div class="left">审核人：</div>
              <div class="right">
                <div class="right-con">
                  <el-button type="primary" circle>1</el-button>
                  <span>上上级领导</span>
                </div>
              </div>
            </div>

            <!-- <div v-if="item.id == 5" class="process-right-con">
              <div class="left" />
              <div class="right color444">{{ $t('access.placeholder20') }}</div>
            </div> -->
          </div>
        </el-col>
      </div>
    </div>
  </div>
</template>

<script>
import { assessScoreUpdateApi, assessVerifyApi } from '@/api/enterprise'
export default {
  name: 'AssessProcess',
  data() {
    return {
      value: '',
      tableData: [
        {
          id: 1,
          name: this.$t('access.goalsetting'),
          title: this.$t('access.participants'),
          children: [{ label: this.$t('toptable.assessor') }]
        },
        {
          id: 2,
          name: this.$t('access.executionphase'),
          title: this.$t('access.participants'),
          children: [{ label: this.$t('toptable.assessor') }, { label: this.$t('access.examinee') }]
        },
        {
          id: 3,
          name: this.$t('access.higherevaluation'),
          title: this.$t('access.required'),
          children: [{ label: this.$t('access.assessmentscore') }, { label: this.$t('access.superiorcomments') }]
        },
        {
          id: 4,
          name: this.$t('access.performancegrievance'),
          title: this.$t('access.applicant'),
          children: [{ label: this.$t('access.examinee') }]
        },
        {
          id: 5,
          name: this.$t('access.performancereview'),
          title: this.$t('access.jurisdiction'),
          children: [
            { label: this.$t('access.adjustscore') },
            { label: this.$t('access.superiorcomments') },
            { label: this.$t('access.toscore') }
          ]
        },
        { id: 6, name: this.$t('access.end') }
      ],
      tabIndex: 0,
      staff: []
    }
  },
  mounted() {
    this.getVerify()
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
  methods: {
    setOptions() {
      this.tableData = [
        {
          id: 1,
          name: this.$t('access.goalsetting'),
          title: this.$t('access.participants'),
          children: [{ label: this.$t('toptable.assessor') }]
        },
        {
          id: 2,
          name: this.$t('access.executionphase'),
          title: this.$t('access.participants'),
          children: [{ label: this.$t('toptable.assessor') }, { label: this.$t('access.examinee') }]
        },
        {
          id: 3,
          name: this.$t('access.higherevaluation'),
          title: this.$t('access.required'),
          children: [{ label: this.$t('access.assessmentscore') }, { label: this.$t('access.superiorcomments') }]
        },
        {
          id: 4,
          name: this.$t('access.performancegrievance'),
          title: this.$t('access.applicant'),
          children: [{ label: this.$t('access.examinee') }]
        },
        {
          id: 5,
          name: this.$t('access.performancereview'),
          title: this.$t('access.jurisdiction'),
          children: [
            { label: this.$t('access.adjustscore') },
            { label: this.$t('access.superiorcomments') },
            { label: this.$t('access.toscore') }
          ]
        },
        { id: 6, name: this.$t('access.end') }
      ]
    },
    async getVerify() {
      const result = await assessVerifyApi()
      result.data.is_superior == 1 ? (this.isSuperior = true) : (this.isSuperior = false)
      result.data.is_appoint == 1 ? (this.isAppoint = true) : (this.isAppoint = false)
    },
    clickBtn(index) {
      this.tabIndex = index
    },
    async handlePreserve() {
      if (this.isAppoint && this.departmentObj.userList.length == 0) {
        this.$message.error(this.$t('access.placeholder21'))
      } else {
        const is_superior = this.isSuperior == true ? 1 : 0
        const is_appoint = this.isAppoint == true ? 1 : 0
        const data = {
          is_superior: is_superior,
          is_appoint: is_appoint,
          staff: this.staff
        }
        await assessScoreUpdateApi(data)
      }
    },

    changeSuperior(event) {
      // event ? this.isSuperior = 1 : this.isSuperior = 0
    },
    changeAppoint(event) {
      // event ? this.isAppoint = 1 : this.isAppoint = 0
    }
  }
}
</script>

<style lang="scss" scoped>
.color189 {
  color: #1890ff;
}
.bgc-f7 {
  background-color: #f7f7f7;
  border-color: transparent;
  color: #000000;
}
.color444 {
  font-size: 13px;
  color: #444444;
}

.process-left {
  display: flex;
  // flex-direction: row-reverse;
  .process-left-con {
    .process-left-list {
      .el-button--medium {
        padding: 8px 0;
        width: 100px;
        font-size: 13px;
      }
      .process-icon {
        margin: 19px 0;
        text-align: center;
        i {
          font-size: 26px;
          transform: rotate(-90deg);
          color: #999999;
          background: linear-gradient(-180deg, #dedede, #999999);
          background: -webkit-linear-gradient(-180deg, #dedede, #999999);
          -webkit-background-clip: text;
          -webkit-text-fill-color: transparent;
        }
      }
    }
  }
}
.process-right {
  padding-left: 30px;
  .process-right-list {
    width: 100%;
    padding: 16px 10px;
    border: 1px solid #f7f7f7;
    border-radius: 5px;
    margin-bottom: 15px;
    .title {
      padding: 0;
      margin: 0;
      font-weight: bold;
      font-size: 14px;
      color: #000000;
    }
    .process-right-con {
      margin-top: 14px;
      display: flex;
      align-items: center;
      .left {
        width: 60px;
        font-size: 13px;
        color: #444444;
      }
      .right {
        width: 90%;
        .right-con {
          display: inline-block;
          margin-right: 20px;
          button {
            width: 16px;
            height: 16px;
            font-size: 13px;
            padding: 0;
          }
        }
        .el-input__inner {
          width: 100%;
          height: 36px;
          line-height: 36px;
        }
        ::v-deep .disable {
          pointer-events: none;
          color: #eeeeee;
          border-color: #eeeeee;
        }
      }
    }
  }
  .process-right-list.active {
    border-color: #1890ff;
  }
}
.flex-box {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  height: 100%;
  .item {
    margin-right: 10px;
  }
}
.explain-footer {
  border-top: 4px solid #fefefe;
  margin-top: 15px;
  padding: 15px 0;
  text-align: center;
}
.mb40 {
  margin: 34px 0 !important;
}
</style>
