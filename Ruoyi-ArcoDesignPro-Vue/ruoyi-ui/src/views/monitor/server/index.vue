<template>
  <div class="container">
    <!--面包屑-->
    <Breadcrumb :items="['系统监控', '服务监控']"></Breadcrumb>
    <a-card
        class="general-card r-nav-card"
        :body-style="{ padding: '15px 0' }"
        :header-style="{ padding: '15px 0' }"
    >
      <a-spin dot :loading="loading">
        <a-row>
          <a-col :span="12" class="card-box arco-table arco-table-size-medium">
            <a-card>
              <template #title>
                <span><icon-dashboard/> CPU</span>
              </template>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table class="arco-table-element" style="width: 100%;">
                  <thead>
                  <tr class="arco-table-tr">
                    <th class="arco-table-td is-leaf"><span class="arco-table-cell arco-table-cell-align-left">属性</span>
                    </th>
                    <th class="arco-table-td is-leaf"><span class="arco-table-cell arco-table-cell-align-left">值</span>
                    </th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">核心数</span>
                    </td>
                    <td v-if="state.server.cpu" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.cpu.cpuNum }}</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">用户使用率</span></td>
                    <td v-if="state.server.cpu" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.cpu.used }}%</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">系统使用率</span></td>
                    <td v-if="state.server.cpu" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.cpu.sys }}%</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">当前空闲率</span></td>
                    <td v-if="state.server.cpu" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.cpu.free }}%</span></td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </a-card>
          </a-col>

          <a-col :span="12" class="card-box arco-table arco-table-size-medium">
            <a-card>
              <template #title>
                <span><icon-file/> 内存</span>
              </template>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table class="arco-table-element" style="width: 100%;">
                  <thead>
                  <tr class="arco-table-tr">
                    <th class="arco-table-td is-leaf"><span class="arco-table-cell arco-table-cell-align-left">属性</span>
                    </th>
                    <th class="arco-table-td is-leaf"><span class="arco-table-cell arco-table-cell-align-left">内存</span>
                    </th>
                    <th class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">JVM</span>
                    </th>
                  </tr>
                  </thead>
                  <tbody>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">总内存</span>
                    </td>
                    <td v-if="state.server.mem" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.mem.total }}G</span></td>
                    <td v-if="state.server.jvm" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.jvm.total }}M</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">已用内存</span>
                    </td>
                    <td v-if="state.server.mem" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.mem.used }}G</span></td>
                    <td v-if="state.server.jvm" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.jvm.used }}M</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">剩余内存</span>
                    </td>
                    <td v-if="state.server.mem" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.mem.free }}G</span></td>
                    <td v-if="state.server.jvm" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.jvm.free }}M</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">使用率</span>
                    </td>
                    <td v-if="state.server.mem" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left"
                        :class="{'text-danger': state.server.mem.usage > 80}">{{ state.server.mem.usage }}%</span></td>
                    <td v-if="state.server.jvm" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left"
                        :class="{'text-danger': state.server.jvm.usage > 80}">{{ state.server.jvm.usage }}%</span></td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </a-card>
          </a-col>

          <a-col :span="24" class="card-box arco-table arco-table-size-medium">
            <a-card>
              <template #title>
                <span><icon-computer/> 服务器信息</span>
              </template>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table class="arco-table-element" style="width: 100%;">
                  <tbody>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">服务器名称</span></td>
                    <td v-if="state.server.sys" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.sys.computerName }}</span>
                    </td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">操作系统</span>
                    </td>
                    <td v-if="state.server.sys" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.sys.osName }}</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">服务器IP</span></td>
                    <td v-if="state.server.sys" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.sys.computerIp }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">系统架构</span>
                    </td>
                    <td v-if="state.server.sys" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.sys.osArch }}</span></td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </a-card>
          </a-col>

          <a-col :span="24" class="card-box arco-table arco-table-size-medium">
            <a-card>
              <template #title>
                <span><icon-storage/> Java虚拟机信息</span>
              </template>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table class="arco-table-element" style="width: 100%;table-layout:fixed;">
                  <tbody>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">Java名称</span></td>
                    <td v-if="state.server.jvm" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.jvm.name }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">Java版本</span></td>
                    <td v-if="state.server.jvm" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.jvm.version }}</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">启动时间</span>
                    </td>
                    <td v-if="state.server.jvm" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.jvm.startTime }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">运行时长</span>
                    </td>
                    <td v-if="state.server.jvm" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.jvm.runTime }}</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td colspan="1" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">安装路径</span></td>
                    <td v-if="state.server.jvm" colspan="3" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.jvm.home }}</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td colspan="1" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">项目路径</span></td>
                    <td v-if="state.server.sys" colspan="3" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.sys.userDir }}</span></td>
                  </tr>
                  <tr class="arco-table-tr">
                    <td colspan="1" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">运行参数</span></td>
                    <td v-if="state.server.jvm" colspan="3" class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ state.server.jvm.inputArgs }}</span></td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </a-card>
          </a-col>
          <a-col :span="24" class="card-box arco-table arco-table-size-medium">
            <a-card>
              <template #title>
                <span><icon-common/> 磁盘状态</span>
              </template>
              <div class="el-table el-table--enable-row-hover el-table--medium">
                <table class="arco-table-element" style="width: 100%;">
                  <thead>
                  <tr class="arco-table-tr">
                    <th class="arco-table-td arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">盘符路径</span></th>
                    <th class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">文件系统</span>
                    </th>
                    <th class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">盘符类型</span>
                    </th>
                    <th class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">总大小</span>
                    </th>
                    <th class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">可用大小</span>
                    </th>
                    <th class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">已用大小</span>
                    </th>
                    <th class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">已用百分比</span></th>
                  </tr>
                  </thead>
                  <tbody v-if="state.server.sysFiles">
                  <tr v-for="(sysFile, index) in state.server.sysFiles" :key="index">
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ sysFile.dirName }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ sysFile.sysTypeName }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ sysFile.typeName }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ sysFile.total }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ sysFile.free }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left">{{ sysFile.used }}</span></td>
                    <td class="arco-table-td is-leaf"><span
                        class="arco-table-cell arco-table-cell-align-left"
                        :class="{'text-danger': sysFile.usage > 80}">{{ sysFile.usage }}%</span>
                    </td>
                  </tr>
                  </tbody>
                </table>
              </div>
            </a-card>
          </a-col>
        </a-row>
      </a-spin>
    </a-card>
  </div>
</template>

<script lang="ts" setup>

import {getServer, ServerRecord} from "@/api/monitor/server";
import useLoading from "@/hooks/loading";
import {reactive} from "vue";

const state = reactive({
  server: {} as ServerRecord
});

const {loading, setLoading} = useLoading(true);

setLoading(true);
// 查询服务器信息
const fetchServerData = async () => {
  const {data} = await getServer();
  state.server = data;
  setLoading(false);
};
fetchServerData();

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

.text-danger {
  color: #ed5565;
}
</style>
