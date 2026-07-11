
<template>
  <div>
    <el-dialog :visible.sync="open" :show-title="false" width="600px" append-to-body :before-close="cancel">
      <el-skeleton :loading="loding" :rows="10" animated>
        <template #default>
          <div class="dialog-header">
            <div class="schedule-title">
              <span>{{ formData.title}}</span>
            </div>
            <div class="schedule-time">
              <span>{{ dateDesc }}</span>
            </div>
          </div>
          <div class="body">
            <el-descriptions :column="1">
              <el-descriptions-item
                label="起止时间"
                label-class-name="item-label"
              >{{ parseTime(formData.startTime, '{h}:{i}') }} - {{ parseTime(formData.endTime, '{h}:{i}') }}</el-descriptions-item>
              <el-descriptions-item :label="formData.partType == '0' ? '创建人' : '组织人'" label-class-name="item-label">
                <div class="user-avatar">
                  <el-avatar :size="24" :src="avatar(formData.avatar)" />
                  <span>{{ formData.createBy }}</span>
                  <span v-if="formData.partType == '0'" class="creator">（我）</span>
                </div>
              </el-descriptions-item>
              <el-descriptions-item label="日程类型" label-class-name="item-label">
                <span class="creator">
                  <dict-tag :options="dict.type.schedule_type" :value="formData.partType" />
                </span>
              </el-descriptions-item>
              <el-descriptions-item v-if="formData.partType == '0'" label="日程分类" label-class-name="item-label">{{ formData.scheduleType.title || "" }}</el-descriptions-item>
              <el-descriptions-item label="提醒时间" label-class-name="item-label">
                <dict-tag :options="dict.type.schedule_remind_time" :value="formData.remindTime" />
              </el-descriptions-item>
              <el-descriptions-item label="提醒方式" label-class-name="item-label">
                <dict-tag :options="dict.type.schedule_remind_way" :value="formData.remindType" />
              </el-descriptions-item>
              <el-descriptions-item label="日程描述" label-class-name="item-label" content-class-name="item-content">
                <span v-html="formData.content"></span>
              </el-descriptions-item>
              <el-descriptions-item v-if="formData.partType == '1'" label="参与者" label-class-name="item-label">
                <span v-for="(user, index) in formData.joinUsers" :key="index" class="user-chip">
                  <el-avatar :size="24" :src="avatar(user.avatar)" />
                  <span class="ml2">{{ user.nickName }}</span>
                </span>
              </el-descriptions-item>
            </el-descriptions>
          </div>
          <div class="dialog-footer">
            <el-button v-if="isMyCreate" plain @click="edit" size="mini">编辑</el-button>
            <el-button plain @click="cancel" size="mini">关闭</el-button>
          </div>
        </template>
      </el-skeleton>
    </el-dialog>

    <ScheduleHandler :open="showEdit" :formData="formData" title="编辑日程" @submit="submit" @close="close" />
  </div>
</template>
  
<script>
import { getSchedule } from "@/api/schedule/schedule";
import ScheduleHandler from "../schedule/save.vue";
import { StrUtil } from "@/utils/StrUtil";

export default {
  components: { ScheduleHandler },
  dicts: ["schedule_remind_time", "schedule_type", "schedule_level", "schedule_remind_way", "schedule_repeat_way"],
  data() {
    return {
      loding: true,
      showEdit: false,
      formData: {},
      userId: this.$store.state.user.userInfo.userId,
      baseApi: process.env.VUE_APP_BASE_API,
    };
  },
  props: {
    id: {},
    open: {
      type: Boolean,
      default: false,
    },
    dateDesc: {},
  },
  watch: {
    id: function (newVal, oldVal) {
      if (!newVal) return;
      if (newVal != oldVal || !this.formData.id) {
        this.getScheduleInfo();
      }
    },
    immediate: true,
  },
  computed: {
    /** 是否我创建的，我创建的才可编辑 */
    isMyCreate() {
      return (createId) => {
        return this.userId == createId;
      };
    },
  },
  methods: {
    /** 头像处理 */
    avatar(val) {
      if (StrUtil.isBlank(val)) return;
      return this.baseApi + val;
    },
    /** 获取详情 */
    getScheduleInfo() {
      this.loding = true;
      getSchedule(this.id).then((res) => {
        if (res.code == 200) {
          this.formData = res.data;
        }
        this.loding = false;
      });
    },
    /** 打开编辑 */
    edit() {
      this.showEdit = true;
    },
    /** 取消 */
    cancel() {
      this.formData = {};
      this.$emit("close");
    },
    /** 提交 */
    submit() {
      this.showEdit = false;
      this.getScheduleInfo();
    },
    /** 关闭 */
    close() {
      this.showEdit = false;
    },
  },
};
</script>
  
<style scoped lang="scss">
::v-deep .el-dialog {
  border-radius: 8px !important;
}
::v-deep .el-dialog__header {
  display: none;
}
::v-deep .dialog-header {
  background: url("../../../../assets/images/schedule-header.jpg") center no-repeat content-box;
  background-size: auto;
  color: #fff;
  font-weight: bold;
  font-size: 16px;
  min-height: 90px;
  border-top-left-radius: 8px;
  border-top-right-radius: 8px;
}

::v-deep .el-dialog__title {
  color: #fff;
  font-weight: 600;
}
::v-deep .el-dialog__headerbtn {
  top: 12px !important;
  color: #fff;
}
::v-deep .el-dialog__close {
  color: #fff;
}

::v-deep .el-dialog__body {
  padding: 0;
}

.schedule-title {
  padding: 20px 30px 10px 30px;
}

.schedule-time {
  padding-left: 30px;
  font-size: 14px;
  padding-bottom: 10px;
}
.creator {
  color: #409eff;
}
.el-avatar {
  margin-right: 3px;
}

.dialog-footer {
  text-align: center;
  padding: 20px;
}

.body {
  padding: 20px 40px;
}

.user-avatar {
  display: flex;
  align-items: center;
}

::v-deep .item-label {
  padding-bottom: 12px;
  color: #909399;
  display: flex;
  align-items: center;
  width: 68px;
}
::v-deep .item-content {
  display: flex;
  overflow-x: hidden;
  max-height: 200px;
  max-width: 450px;
  color: #303133;

  p {
    margin: 0 !important;
    padding: 0 !important;
  }

  img {
    max-height: 250px;
  }

  a {
    font-style: italic;
    text-decoration: underline;
    color: #409eff;
  }
}

.user-chip {
  display: inline-flex;
  align-items: center;
  margin: 2px 5px;
  padding: 5px;
  background: #e9f2ff;
  border-radius: 4px;
  font-size: 12px;
  position: relative;
}
</style>