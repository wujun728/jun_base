<template>
  <!-- 新增或修改日程-->
  <div>
    <el-dialog :title="title" :visible.sync="open" width="1000px" append-to-body :before-close="cancel">
      <el-divider />
      <el-form ref="form" :model="form" :rules="rules" label-width="92px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" maxlength="50" show-word-limit />
        </el-form-item>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="类型" prop="partType">
              <el-select v-model="form.partType" placeholder="请选择">
                <el-option v-for="dict in dict.type.schedule_type" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-form-item v-if="form.partType == '0'" label="日程分类" prop="typeId">
          <el-select v-model="form.typeId">
            <el-option v-for="item in typeList" :key="item.id" :value="item.id" :label="item.title" />
          </el-select>
          <el-tooltip content="请点击左侧日程菜单，进入“我的日程”页面，在我的分类处，新增个人日程分类。" placement="top">
            <i class="el-icon-question ml5"></i>
          </el-tooltip>
        </el-form-item>
        <el-form-item v-if="form.partType == '1'" label="参与人员" prop="joinUserNames">
          <div class="user-select-trigger" @click="openUserSelector">
            <div v-if="selectedUsers.length > 0" class="selected-users">
              <span v-for="(user, index) in selectedUsers" :key="index" class="user-chip">
                <el-avatar :size="24" :src="avatar(user.avatar)" />
                <span class="ml5">{{ user.nickName }}</span>
                <span @click.stop="removeUser(index)">
                  <el-icon name="close" class="remove-icon" />
                </span>
              </span>
            </div>
            <span v-else class="placeholder">
              <i class="el-icon-user mr5"></i>请选择人员
            </span>
          </div>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker clearable v-model="beginDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择开始时间" @change="beginDateChange" />
          <el-time-picker
            v-if="anyBeginTime"
            v-model="beginTimeDate"
            format="HH:mm"
            placeholder="任意时间点"
            class="ml10"
            :clearable="false"
            @change="beginTimeChange"
          />
          <el-time-select
            v-else
            v-model="beginTimeStr"
            :picker-options="{start: '08:00',step: '00:15', end: '20:00'}"
            :clearable="false"
            placeholder="选择时间"
            class="ml10"
            @change="beginTimeChange"
          />
          <el-checkbox v-model="anyBeginTime" @click="anyBeginTimeChange" class="ml10">任意时间</el-checkbox>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker clearable v-model="endDate" type="date" value-format="yyyy-MM-dd" placeholder="请选择结束时间" @change="endDateChange" />
          <el-time-picker
            v-if="anyEndTime"
            v-model="endTimeDate"
            format="HH:mm"
            placeholder="任意时间点"
            class="ml10"
            :clearable="false"
            @change="endTimeChange"
          />
          <el-time-select
            v-else
            v-model="endTimeStr"
            :picker-options="{start: '08:00',step: '00:15', end: '20:00'}"
            :clearable="false"
            placeholder="选择时间"
            class="ml10"
            @change="endTimeChange"
          />
          <el-checkbox v-model="anyEndTime" @click="anyEndTimeChange" class="ml10">任意时间</el-checkbox>
        </el-form-item>

        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="提醒时间" prop="remindTime">
              <el-select v-model="form.remindTime" placeholder="请选择">
                <el-option v-for="dict in dict.type.schedule_remind_time" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="提醒方式" prop="remindType">
              <el-select v-model="form.remindType" placeholder="请选择">
                <el-option v-for="dict in dict.type.schedule_remind_way" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="是否重复" prop="repeatFlag">
              <el-select v-model="form.repeatFlag" placeholder="请选择">
                <el-option v-for="dict in dict.type.sys_yes_or_no" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="重复方式" prop="repeatType" v-if="form.repeatFlag === '1'">
              <el-select v-model="form.repeatType" placeholder="请选择">
                <el-option v-for="dict in dict.type.schedule_repeat_way" :key="dict.value" :label="dict.label" :value="dict.value" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>
        <el-row :gutter="24">
          <el-col :span="12"></el-col>
          <el-col :span="12"></el-col>
        </el-row>
        <el-form-item label="日程内容">
          <span class="content-tip">( 注意：添加访问链接时，必须要带上：http://或者https:// )</span>
          <editor v-model="form.content" :min-height="192" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <UserAllSelect :open.sync="openSelect" type="multiple" :selectedUsers="selectedUsers" @confimUser="confimUser" />
  </div>
</template>

<script>
import { saveSchedule } from "@/api/schedule/schedule";
import { listAll } from "@/api/schedule/type";
import UserAllSelect from "@/components/org/UserAllSelect/index.vue";
import { parseTime } from "@/utils/ruoyi";
import { StrUtil } from "@/utils/StrUtil";

export default {
  dicts: ["sys_yes_or_no", "schedule_type", "schedule_remind_time", "schedule_level", "schedule_remind_way", "schedule_repeat_way"],
  components: { UserAllSelect },
  data() {
    return {
      checkList: ["0"],
      openSelect: false,
      typeId: null,
      // 个人分类配置
      typeList: [],
      // 个人分类配置下拉选项
      typeOptions: [],
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        title: [{ required: true, message: "标题不能为空", trigger: "blur" }],
        partType: [{ required: true, message: "请选则类型", trigger: "blur" }],
        typeId: [{ required: true, message: "请选则分类", trigger: "blur" }],
        startTime: [{ required: true, message: "请选则开始时间", trigger: "blur" }],
        endTime: [{ required: true, message: "请选则结束时间", trigger: "blur" }],
        joinUserNames: [{ required: true, message: "请选则参与人员", trigger: "change" }],
      },
      // 开始日期
      beginDate: null,
      // 开始时间
      beginTime: null,
      // 开始时间（日期型）
      beginTimeDate: null,
      // 开始时间（字符串型）
      beginTimeStr: "",
      // 是否任意开始时间
      anyBeginTime: false,
      // 结束日期
      endDate: null,
      // 结束时间
      endTime: null,
      // 结束时间（日期型）
      endTimeDate: null,
      // 结束时间（字符串型）
      endTimeStr: "",
      // 是否任意结束时间
      anyEndTime: false,
      selectedUsers: [],
      baseApi: process.env.VUE_APP_BASE_API,
    };
  },
  props: {
    title: {
      type: String,
      default: null,
    },
    open: {
      type: Boolean,
      default: false,
    },
    formData: {
      type: Object,
      default: () => {},
    },
  },
  computed: {
    joinUserIds() {
      return this.selectedUsers.map((u) => u.userId);
    },
    joinUserNamesStr() {
      return this.selectedUsers.map((u) => u.nickName).join("、");
    },
  },
  watch: {
    // 监听 selectedUsers 的变化，自动更新 form 字段
    joinUserIds(newVal) {
      this.$set(this.form, "joinUsers", newVal);
    },
    joinUserNamesStr(newVal) {
      this.$set(this.form, "joinUserNames", newVal);
    },
    formData: {
      handler(obj) {
        if ("id" in obj) {
          this.form = JSON.parse(JSON.stringify(obj));
          this.beginDate = new Date(obj.startTime);
          this.beginTimeDate = parseTime(obj.startTime, "{h}:{i}");
          this.endDate = new Date(obj.endTime);
          this.endTimeDate = parseTime(obj.endTime, "{h}:{i}");
          this.selectedUsers = obj.joinUsers ? [...obj.joinUsers] : [];
        }
      },
      deep: true,
      immediate: true,
    },
  },
  mounted() {
    this.reset();
    this.getTypeList();
  },
  methods: {
    /** 头像处理 */
    avatar(val) {
      if (StrUtil.isBlank(val)) return;
      return this.baseApi + val;
    },
    // 取消按钮
    cancel() {
      this.reset();
      this.$emit("close");
    },
    // 表单重置
    reset() {
      this.form = {
        id: null,
        title: null,
        typeId: null,
        partType: "0",
        startTime: null,
        endTime: null,
        repeatFlag: "0",
        repeatType: "1",
        remindType: "1",
        remindTime: "1",
        level: "0",
        joinUserNames: null,
        joinUsers: null,
      };
      this.beginDate = new Date();
      this.beginTime = null;
      this.beginTimeDate = null;
      this.beginTimeStr = "";
      this.anyBeginTime = false;
      this.endDate = new Date();
      this.endTime = null;
      this.endTimeDate = null;
      this.endTimeStr = "";
      this.anyEndTime = false;
      this.resetForm("form");
      this.selectedUsers = [];
    },
    // 获取个人分类所有数据
    getTypeList() {
      listAll().then((res) => {
        if (res.code == 200) {
          this.typeList = res.data || [];
        }
      });
    },
    /** 开始日期变更 */
    beginDateChange(date) {
      this.endDate = date;
      if (!this.beginTime) return;
      this.form.startTime = new Date(parseTime(date, "{y}-{m}-{d}") + " " + this.beginTime + ":00");
    },
    /** 开始时间变更 */
    beginTimeChange(time) {
      if (!this.beginDate) return;
      if (time instanceof Date) {
        time = parseTime(time, "{h}:{i}");
      }
      this.beginTime = time;
      this.form.startTime = new Date(parseTime(this.beginDate, "{y}-{m}-{d}") + " " + time + ":00");
    },
    /** 开始时间方式切换 */
    anyBeginTimeChange(val) {
      this.anyBeginTime = val;
      if (!val) {
        this.beginTimeDate = null;
      } else {
        this.beginTimeStr = "";
      }
    },
    /** 结束日期变更 */
    endDateChange(date) {
      if (!this.endTime) return;
      this.form.endDate = new Date(parseTime(date, "{y}-{m}-{d}") + " " + this.endTime + ":00");
    },
    /**结束时间变更 */
    endTimeChange(time) {
      if (!this.endDate) return;
      if (time instanceof Date) {
        time = parseTime(time, "{h}:{i}");
      }
      this.endTime = time;
      this.form.endTime = new Date(parseTime(this.endDate, "{y}-{m}-{d}") + " " + time + ":00");
    },
    anyEndTimeChange(val) {
      this.anyEndTime = val;
      if (!val) {
        this.endTimeDate = null;
      } else {
        this.endTimeStr = "";
      }
    },
    /** 选人 */
    openUserSelector() {
      this.openSelect = true;
    },
    /** 选人确认 */
    confimUser(selection) {
      this.selectedUsers = selection || [];
      this.openSelect = false;
    },
    /** 移除已选用户 */
    removeUser(index) {
      this.selectedUsers.splice(index, 1);
      this.$emit("update:selectedUsers", this.selectedUsers);
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate((valid) => {
        if (valid) {
          saveSchedule(this.form).then((response) => {
            this.$modal.msgSuccess("操作成功");
            this.$emit("submit");
            this.reset();
          });
        }
      });
    },
  },
};
</script>

<style scoped lang="scss">
.user-select-trigger {
  padding: 5px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  min-height: 32px;
  cursor: pointer;
  display: flex;
  align-items: center;
  flex-wrap: wrap;
}

.user-select-trigger:hover {
  border-color: #409eff;
}

.selected-users .user-chip {
  display: inline-flex;
  align-items: center;
  margin: 2px 5px;
  padding: 2px 6px;
  background: #e9f2ff;
  border-radius: 4px;
  font-size: 12px;
  position: relative;
}

.remove-icon {
  margin-left: 6px;
  cursor: pointer;
  color: #999;
  font-size: 12px;
}

.remove-icon:hover {
  color: #f56c6c;
}

.placeholder {
  color: #c0c4cc;
  padding-left: 5px;
}

.content-tip {
  color: #cc6b04;
}
</style>