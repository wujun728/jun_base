<template>
  <div class="table-box">
    <el-timeline v-if="tabsName === 'personnelChange'">
      <div class="default" v-if="list.length == 0">
        <img src="../../../../assets/images/emptyState.png" alt="" class="img" />
        <div class="text">暂无人事异动数据~</div>
      </div>
      <el-timeline-item v-for="(item, index) in list" :key="index">
        <!-- 离职 -->
        <div class="quit" v-if="item.types === 3">
          <div class="time">
            {{ item.date ? item.date : item.created_at }} <el-tag type="warning" size="mini" class="ml15">离职</el-tag>
          </div>
          <div>离职时部门： {{ item.o_frame ? item.o_frame.name : '' }}</div>
          <div>离职时职位：{{ item.o_position ? item.o_position.name : '' }}</div>
          <div>离职原因： {{ item.info }}</div>
          <div>离职备注： {{ item.mark }}</div>
        </div>
        <!-- 转正 -->
        <div class="quit" v-if="item.types === 1">
          <div class="time">
            {{ item.date ? item.date : item.created_at }}
            <el-tag type="warning" size="mini" class="ml15">转正</el-tag>
          </div>
          <div>转正备注： {{ item.mark || '--' }}</div>
        </div>

        <!-- 调岗 -->
        <div class="post-transfer quit" v-if="item.types === 2">
          <div class="time">{{ item.created_at }} <el-tag type="success" size="mini" class="ml15">调岗</el-tag></div>
          <div>新部门职位： 产品研发部</div>
          <div>原部门职位： 研发工程师；p8，职等3</div>
          <div>调岗备注： 由于特殊原因进行调岗操作，已经得到上级领导批准</div>
        </div>
        <!-- 入职 -->
        <div class="post-transfer quit" v-if="item.types === 0">
          <div class="time">
            {{ item.date ? item.date : item.created_at }} <el-tag size="mini" class="ml15">入职</el-tag>
          </div>

          <div>员工类型： {{ item.is_part || '--' }}</div>
          <div>入职时部门：{{ item.n_frame ? item.n_frame.name : '--' }}</div>
          <div>职位： {{ item.n_position ? item.n_position.name : '--' }}</div>
        </div>
      </el-timeline-item>
    </el-timeline>
    <el-timeline v-if="tabsName == 'salaryAdjustmentRecord'">
      <div class="default" v-if="list.length == 0">
        <img src="../../../../assets/images/emptyState.png" alt="" class="img" />
        <div class="text">暂无调薪数据~</div>
      </div>
      <el-timeline-item
        v-for="(activity, index) in list"
        :key="index"
        :icon="activity.icon"
        :type="activity.type"
        :color="activity.color"
        :size="activity.size"
      >
        <!-- 调薪记录 -->
        <div class="quit">
          <div class="time">
            {{ activity.take_date }}
            <el-tag type="success" size="mini" class="ml15" v-if="index + 1 !== list.length">调薪</el-tag>
            <el-tag size="mini" class="ml15" v-else>定薪</el-tag>
          </div>
          <div class="icon"></div>
          <div>薪资合计(元)： {{ activity.total }}</div>
          <div class="salary">
            <span v-for="(item, id) in activity.content" :key="id"
              >{{ item.label }}&nbsp;&nbsp;&nbsp;{{ item.value }}</span
            >
          </div>

          <div>操作日期： {{ activity.created_at }}</div>
        </div>
      </el-timeline-item>
    </el-timeline>
  </div>
</template>
<script>
export default {
  name: 'dfd',

  props: {
    tabsName: {
      type: String,
      default: ''
    },
    list: {
      type: Array,
      default: []
    }
  },
  data() {
    return {}
  },

  methods: {}
}
</script>
<style scoped lang="scss">
.table-box {
  width: 100%;
  margin-top: 30px;
  margin-bottom: 200px;
  height: calc(100vh - 140px);

  overflow-y: auto;
}
.iconfont {
  color: #1890ff;
}
.iconbianji1 {
  margin-right: 10px;
}

.table-box::-webkit-scrollbar {
  height: 0;
  width: 0;
}
.salary {
  width: 100%;
  display: flex;
  justify-content: space-between;
}

.quit {
  font-size: 11px;
  color: #666;
  position: relative;
  margin-top: 3px;
  > div {
    margin-bottom: 15px;
    &:last-of-type {
      margin-bottom: 0;
    }
  }
}
.icon {
  position: absolute;
  top: 2px;
  right: 20px;
  display: flex;
  font-size: 18px;
}
.time {
  font-size: 13px;
  font-weight: 700;
  color: #666;
  display: flex;
  align-items: center;
}
.ml15 {
  margin-left: 15px;
}
.default {
  position: absolute;
  left: 50%;
  top: 50%;
  display: flex;
  transform: translate(-50%, -50%);
  flex-direction: column;
  justify-content: center;
  align-items: center;

  .img {
    width: 100px;
    height: 90px;
  }
  .text {
    color: #666;
  }
}
</style>
