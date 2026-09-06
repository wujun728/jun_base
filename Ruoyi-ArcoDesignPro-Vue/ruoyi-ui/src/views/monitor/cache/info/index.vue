<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统监控', '缓存监控']"></Breadcrumb>
    <a-card
        class="general-card r-nav-card"
        :body-style="{ padding: '15px 0' }"
        :header-style="{ padding: '15px 0' }"
    >
      <a-spin dot :loading="loading" style="width: 100%;">
        <a-row>
          <a-col :span="24" class="card-box arco-table arco-table-size-medium">
            <a-card>
              <template #title>
                <span><icon-computer/>基本信息</span>
              </template>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table class="arco-table-element" style="width: 100%">
                  <tbody>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">Redis版本</span></td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.cache.info.redis_version }}</span>
                    </td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">运行模式</span></td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{
                        state.cache.info.redis_mode === "standalone" ? "单机" : "集群"
                      }}</span></td>
                    <td class="arco-table-td is-leaf"><span class="arco-table-cell arco-table-cell-align-left">端口</span>
                    </td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.cache.info.tcp_port }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">客户端数</span></td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{
                        state.cache.info.connected_clients
                      }}</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">运行时间(天)</span></td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.cache.info.uptime_in_days }}</span>
                    </td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">使用内存</span></td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{
                        state.cache.info.used_memory_human
                      }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">使用CPU</span></td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{
                        parseFloat(state.cache.info.used_cpu_user_children).toFixed(2)
                      }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">内存配置</span></td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.cache.info.maxmemory_human }}</span>
                    </td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">AOF是否开启</span></td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{
                        state.cache.info.aof_enabled === "0" ? "否" : "是"
                      }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">RDB是否成功</span></td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{
                        state.cache.info.rdb_last_bgsave_status
                      }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">Key数量</span></td>
                    <td v-if="state.cache.dbSize" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.cache.dbSize }} </span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">网络入口/出口</span></td>
                    <td v-if="state.cache.info" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{
                        state.cache.info.instantaneous_input_kbps
                      }}kps/{{ state.cache.info.instantaneous_output_kbps }}kps</span></td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </a-card>
          </a-col>
          <a-col :span="12" class="card-box arco-table arco-table-size-medium">
            <a-card>
              <template #title>
                <span><icon-code-square/>命令统计</span>
              </template>
              <Chart style="height: 420px" :option="commandStatsOption" autoresize/>
            </a-card>
          </a-col>

          <a-col :span="12" class="card-box arco-table arco-table-size-medium">
            <a-card>
              <template #title>
                <span><icon-dashboard/>内存信息</span>
              </template>
              <Chart style="height: 420px" :option="usedMemoryOption" autoresize/>
            </a-card>
          </a-col>

        </a-row>
      </a-spin>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import useLoading from "@/hooks/loading";
import {getCache, CacheData} from "@/api/monitor/cache";
import {reactive} from "vue";
import useChartOption from "@/hooks/chart-option";

const state = reactive({
  cache: {} as CacheData,
  commandStatsOption: {},
  usedMemoryOption: {},
});

const {loading, setLoading} = useLoading(true);

setLoading(true);
// 获取缓存信息
const fetchCacheData = async () => {
  const {data} = await getCache();
  state.cache = data;
  state.commandStatsOption = {
    tooltip: {
      trigger: "item",
      formatter: "{a} <br/>{b} : {c} ({d}%)",
    },
    series: [
      {
        name: "命令",
        type: "pie",
        roseType: "radius",
        radius: [15, 95],
        center: ["50%", "38%"],
        data: data.commandStats,
        animationEasing: "cubicInOut",
        animationDuration: 1000,
      }
    ]
  };
  state.usedMemoryOption = {
    tooltip: {
      formatter: `{b} <br/>{a} : ${state.cache.info.used_memory_human}`,
    },
    series: [
      {
        name: "峰值",
        type: "gauge",
        min: 0,
        max: 1000,
        detail: {
          formatter: state.cache.info.used_memory_human,
        },
        data: [
          {
            value: parseFloat(state.cache.info.used_memory_human),
            name: "内存消耗",
          }
        ]
      }
    ]
  };
  setLoading(false);
};
fetchCacheData();

const {chartOption: commandStatsOption} = useChartOption((isDark) => {
  return state.commandStatsOption;
});
const {chartOption: usedMemoryOption} = useChartOption((isDark) => {
  return state.usedMemoryOption;
});

</script>

<style scoped lang="less">
.container {
  padding: 0 20px 20px 20px;
}

.card-box {
  padding-right: 15px;
  padding-left: 15px;
  margin-bottom: 20px;
}
</style>
