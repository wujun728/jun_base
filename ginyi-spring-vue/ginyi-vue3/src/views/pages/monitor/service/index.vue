<template>
    <n-spin :show="loading">
        <n-grid :x-gap="12" :y-gap="12" :cols="2">
            <n-grid-item>
                <n-card title="CPU" hoverable>
                    <template #header-extra>
                        <n-tooltip placement="top-start" trigger="hover" :show-arrow="false">
                            <template #trigger>
                                <n-icon size="24">
                                    <ReloadOutline @click="reloadData" class="load-icon"/>
                                </n-icon>
                            </template>
                            重新获取数据
                        </n-tooltip>
                    </template>
                    <n-table :bordered="false">
                        <thead>
                        <tr>
                            <th>属性</th>
                            <th>值</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>核心数</td>
                            <td>
                                <n-tag>{{ serverInfo?.cpu?.cpuNum }}</n-tag>
                            </td>
                        </tr>
                        <tr>
                            <td>用户使用率</td>
                            <td>
                                <n-tag type="warning">{{ serverInfo?.cpu?.used }}%</n-tag>
                            </td>
                        </tr>
                        <tr>
                            <td>系统使用率</td>
                            <td>
                                <n-tag type="info">{{ serverInfo?.cpu?.sys }}%</n-tag>
                            </td>
                        </tr>
                        <tr>
                            <td>当前空闲率</td>
                            <td>
                                <n-tag type="success">{{ serverInfo?.cpu?.free }}%</n-tag>
                            </td>
                        </tr>
                        </tbody>
                    </n-table>
                </n-card>
            </n-grid-item>
            <n-grid-item>
                <n-card title="内存" hoverable>
                    <template #header-extra>
                        <n-tooltip placement="top-start" trigger="hover" :show-arrow="false">
                            <template #trigger>
                                <n-icon size="24">
                                    <ReloadOutline @click="reloadData" class="load-icon"/>
                                </n-icon>
                            </template>
                            重新获取数据
                        </n-tooltip>
                    </template>
                    <n-table :bordered="false">
                        <thead>
                        <tr>
                            <th>属性</th>
                            <th>内存</th>
                            <th>JVM</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>总内存</td>
                            <td>
                                <n-tag>{{ serverInfo?.memory?.total }}G</n-tag>
                            </td>
                            <td>
                                <n-tag>{{ serverInfo?.jvm?.total }}M</n-tag>
                            </td>
                        </tr>
                        <tr>
                            <td>已用内存</td>
                            <td>
                                <n-tag type="warning">{{ serverInfo?.memory?.used }}G</n-tag>
                            </td>
                            <td>
                                <n-tag type="warning">{{ serverInfo?.jvm?.used }}M</n-tag>
                            </td>
                        </tr>
                        <tr>
                            <td>剩余内存</td>
                            <td>
                                <n-tag type="info">{{ serverInfo?.memory?.free }}G</n-tag>
                            </td>
                            <td>
                                <n-tag type="info">{{ serverInfo?.jvm?.free }}M</n-tag>
                            </td>
                        </tr>
                        <tr>
                            <td>使用率</td>
                            <td>
                                <n-tag type="success">{{ serverInfo?.memory?.free }}%</n-tag>
                            </td>
                            <td>
                                <n-tag type="success">{{ serverInfo?.jvm?.usage }}%</n-tag>
                            </td>
                        </tr>
                        </tbody>
                    </n-table>
                </n-card>
            </n-grid-item>
            <n-grid-item span="2">
                <n-card title="服务器信息" hoverable>
                    <template #header-extra>
                        <n-tooltip placement="top-start" trigger="hover" :show-arrow="false">
                            <template #trigger>
                                <n-icon size="24">
                                    <ReloadOutline @click="reloadData" class="load-icon"/>
                                </n-icon>
                            </template>
                            重新获取数据
                        </n-tooltip>
                    </template>
                    <n-table :bordered="false">
                        <thead>
                        <tr>
                            <th>服务器名称</th>
                            <th>服务器IP</th>
                            <th>操作系统</th>
                            <th>系统架构</th>
                        </tr>
                        </thead>
                        <tbody>
                        <tr>
                            <td>{{ serverInfo?.sys?.computerName }}</td>
                            <td>{{ serverInfo?.sys?.computerIp }}</td>
                            <td>{{ serverInfo?.sys?.osName }}</td>
                            <td>{{ serverInfo?.sys?.osArch }}</td>
                        </tr>
                        </tbody>
                    </n-table>
                </n-card>
            </n-grid-item>
            <n-grid-item span="2">
                <n-card title="Java虚拟机信息" hoverable>
                    <template #header-extra>
                        <n-tooltip placement="top-start" trigger="hover" :show-arrow="false">
                            <template #trigger>
                                <n-icon size="24">
                                    <ReloadOutline @click="reloadData" class="load-icon"/>
                                </n-icon>
                            </template>
                            重新获取数据
                        </n-tooltip>
                    </template>
                    <n-descriptions label-placement="left" title="描述" bordered :column="2"
                                    :label-style="{width: '150px'}">
                        <n-descriptions-item label="Java名称">
                            {{ serverInfo?.jvm?.name }}
                        </n-descriptions-item>
                        <n-descriptions-item label="Java版本">
                            {{ serverInfo?.jvm?.version }}
                        </n-descriptions-item>
                        <n-descriptions-item label="启动时间">
                            {{ serverInfo?.jvm?.startTime }}
                        </n-descriptions-item>
                        <n-descriptions-item label="运行时间">
                            {{ serverInfo?.jvm?.runTime }}
                        </n-descriptions-item>
                        <n-descriptions-item label="安装路径">
                            {{ serverInfo?.jvm?.home }}
                        </n-descriptions-item>
                        <n-descriptions-item label="项目路径">
                            {{ serverInfo?.sys?.userDir }}
                        </n-descriptions-item>
                        <n-descriptions-item label="运行参数">
                            {{ serverInfo?.jvm?.inputArgs }}
                        </n-descriptions-item>
                    </n-descriptions>
                </n-card>
            </n-grid-item>
            <n-grid-item span="2">
                <n-card title="磁盘状态" hoverable>
                    <template #header-extra>
                        <n-tooltip placement="top-start" trigger="hover" :show-arrow="false">
                            <template #trigger>
                                <n-icon size="24">
                                    <ReloadOutline @click="reloadData" class="load-icon"/>
                                </n-icon>
                            </template>
                            重新获取数据
                        </n-tooltip>
                    </template>
                    <n-data-table
                        :columns="columns"
                        :data="serverInfo?.file"
                        :bordered="false"
                    />
                </n-card>
            </n-grid-item>
        </n-grid>
    </n-spin>
</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {columns} from "@/views/pages/monitor/service/columns";
import {monitorController} from "@/api";
import {ReloadOutline} from "@vicons/ionicons5";


export default defineComponent({
    components: {
        ReloadOutline
    },
    setup() {
        const serverInfo = ref<any>(undefined)
        const loading = ref<boolean>(true)

        const reloadData = () => {
            loading.value = true
            getServerInfo()
        }
        const getServerInfo = () => {
            monitorController.getServerInfo().then(res => {
                serverInfo.value = res.data
                loading.value = false
            })
        }

        onMounted(() => {
            getServerInfo()
        })
        return {
            columns,
            serverInfo,
            loading,
            ReloadOutline,
            reloadData
        }
    }
})
</script>

<style scoped lang="less">
.load-icon:hover{
    cursor: pointer;
}
</style>