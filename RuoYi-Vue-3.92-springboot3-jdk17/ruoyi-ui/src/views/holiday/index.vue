<template>
  <div class="app-container">
    <el-tabs v-model="activeName" @tab-click="handleClick">
      <el-tab-pane label="假日设置" name="1">
        <holiday-setting />
      </el-tab-pane>
      <el-tab-pane label="补班设置" name="2">
        <work-setting />
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import HolidaySetting from "./holiday-setting";
import WorkSetting from "./work-setting";
export default {
  name: "Holiday",
  components: { HolidaySetting, WorkSetting },
  data() {
    return {
      activeName: "1",
    };
  },
  methods: {
    handleClick() {},
  },
};
</script>
