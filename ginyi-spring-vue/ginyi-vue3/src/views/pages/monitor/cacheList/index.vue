<template>
    <div style="height: 100%">
        <n-grid x-gap="12" :cols="5" style="height: 100%">
            <n-grid-item span="2">
                <n-card title="缓存列表" style="height: 100%">
                    <template #header-extra>
                        <n-tooltip placement="top-start" trigger="hover" :show-arrow="false">
                            <template #trigger>
                                <n-icon size="24">
                                    <ReloadOutline @click="reloadData" class="load-icon"/>
                                </n-icon>
                            </template>
                            重新获取缓存数据
                        </n-tooltip>
                    </template>
                    <n-data-table
                        :columns="columns"
                        :data="tableDataList"
                        :size="'small'"
                        flex-height style="height: 100%"
                        :bordered="false"
                    />
                </n-card>
            </n-grid-item>
            <n-grid-item span="3">
                <n-card title="数据详情" style="height: 100%" :content-style="{height: '100%'}">
                    <n-empty description="什么都没有哦~" v-if="cache.key === undefined"/>
                    <div style="height: 100%; display: flex; flex-direction: column" v-else>
                        <n-form label-placement="left">
                            <n-form-item label="键名">
                                <n-input v-model:value="cache.key" readonly></n-input>
                            </n-form-item>
                            <n-form-item label="TTL">
                                <n-input v-model:value="cache.expire" readonly></n-input>
                            </n-form-item>
                        </n-form>
                        <MonacoEditor v-model="rowData"
                                      :language="'json'"
                                      height="100%"
                                      @editorMounted="editorMounted"/>
                    </div>
                </n-card>
            </n-grid-item>
        </n-grid>
    </div>

</template>

<script lang="ts">
import {defineComponent, onMounted, ref} from "vue";
import {columns} from "@/views/pages/monitor/cacheList/columns";
import {useCommonTable} from "@/components/commonTable/useCommonTable";
import {monitorController} from "@/api";
import CommonTable from "@/components/commonTable/index.vue";
import * as monaco from "monaco-editor";
import MonacoEditor from "@/components/monacoEditor/index.vue";
import {eventBus} from "@/config/eventBus";
import {ReloadOutline} from "@vicons/ionicons5";

export default defineComponent({
    components: {
        CommonTable,
        MonacoEditor,
        ReloadOutline
    },
    setup() {
        const {
            tableDataList,
            getDataList,
        } = useCommonTable(monitorController.getCacheList)
        const rowData = ref<any>(undefined)
        const cache = ref<any>({
            key: undefined,
            expire: undefined
        })
        const editorMounted = (editor: monaco.editor.IStandaloneCodeEditor) => {
        }

        const reloadData = () => {
            getDataList()
            window.$message.success("操作成功")
        }

        onMounted(() => {
            getDataList()
            eventBus.on("handleClickDetails", (row: any) => {
                monitorController.getCacheValue(row).then(res => {
                    cache.value.key = res.data.key
                    cache.value.expire = res.data.expire
                    rowData.value = JSON.stringify(res.data.value, null, "\t")
                }).catch(e => {
                    getDataList()
                })
            })
            eventBus.on("handleClickDelete", (row: any) => {
                window.$dialog.error({
                    title: "温馨提醒",
                    content: "删除操作不可逆，是否继续？",
                    positiveText: "确定",
                    negativeText: "取消",
                    onPositiveClick: () => {
                        monitorController.removeCache(row.key).then(res => {
                            window.$message.success(res.msg)
                            getDataList()
                        })
                    }
                })
            })
        })

        return {
            columns,
            tableDataList,
            getDataList,
            rowData,
            editorMounted,
            cache,
            ReloadOutline,
            reloadData,
        }
    }
})
</script>

<style scoped lang="less">
.load-icon:hover {
    cursor: pointer;
}
</style>
