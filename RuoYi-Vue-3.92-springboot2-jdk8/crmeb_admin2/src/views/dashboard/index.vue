<template>
  <div>
    <!--头部-->
    <base-info ref="baseInfo" v-if="checkPermi(['admin:statistics:home:index'])" />
    <!--小方块-->
    <grid-menu class="mb14" />
    <!-- 用户概览 -->
    <user-overview></user-overview>
    <!--订单统计-->
    <visit-chart ref="visitChart" />
    <!--用户-->
    <user-chart ref="userChart" class="mb20" v-if="checkPermi(['admin:statistics:home:chart:user'])" />
  </div>
</template>

<script>
import baseInfo from './components/baseInfo';
import gridMenu from './components/gridMenu';
import visitChart from './components/visitChart';
import userChart from './components/userChart';
import userOverview from './components/userOverview';
import { checkPermi } from '@/utils/permission'; // 权限判断函数
import Cache from '@/plugins/cache';
export default {
  name: 'Dashboard',
  components: { baseInfo, gridMenu, visitChart, userChart, userOverview },
  data() {
    return {};
  },
  mounted() {},
  methods: {
    checkPermi,
  },
};
</script>
