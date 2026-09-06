<template>
    <n-layout class="layout" content-style="display:flex; flex-direction: column;"
                  :style="{backgroundColor: darkTheme ? null : '#f5f6fa'}">
            <n-card title="查询条件" :size="size" v-if="hasQuerySlot('query')" style="margin-bottom: 15px">
                <slot name="query"></slot>
            </n-card>
            <n-layout-content>
                <n-card style="height: 100%; box-sizing: border-box">
                    <div style="display: flex; justify-content: space-between; margin-bottom: 5px">
                        <n-space>
                            <n-button :type="buttonConfig.addButton.colorType" :size="size" @click="onEvent(tableActionEnums.ADD)"
                                      v-if="buttonConfig.addButton.show">
                                <template #icon>
                                    <n-icon :component="AddCircleOutline"/>
                                </template>
                                {{ buttonConfig.addButton.title }}
                            </n-button>
                            <n-button :type="buttonConfig.batchDeleteButton.colorType"
                                      :size="size" v-if="hasSelect"
                                      :disabled="!hasSelect || checkedRowList.length === 0"
                                      v-show="buttonConfig.batchDeleteButton.show"
                                      @click="onEvent(tableActionEnums.BATCH_DELETE)">
                                <template #icon>
                                    <n-icon :component="TrashBinOutline"/>
                                </template>
                                {{ buttonConfig.batchDeleteButton.title }}
                            </n-button>
                        </n-space>
                        <div style="display: flex; align-items: center;"
                             :style="{ width: !buttonConfig.addButton.show ? '100%' : null, justifyContent: !buttonConfig.addButton.show ? 'flex-end' : null}">
                            <n-radio-group :size="size" v-model:value="tableSize">
                                <n-radio-button label="紧凑" value="small"/>
                                <n-radio-button label="默认" value="medium"/>
                                <n-radio-button label="宽松" value="large"/>
                            </n-radio-group>
                            <n-tooltip placement="top-start" trigger="hover" :show-arrow="false">
                                <template #trigger>
                                    <n-icon :component="Reload" :size="20" style="margin-left: 10px; cursor: pointer" @click="reloadData" />
                                </template>
                                刷新表格数据
                            </n-tooltip>
                            <n-space style="margin-left: 10px; cursor: pointer">
                                <n-popover :show-arrow="false" placement="bottom-end" trigger="click">
                                    <template #trigger>
                                        <n-icon style=" display: flex; align-items: center" :size="20">
                                            <SettingsOutline/>
                                        </n-icon>
                                    </template>
                                    <div style="width: 350px; height: 400px;">
                                        <n-divider title-placement="center">其他配置</n-divider>
                                        <div style="display: flex; justify-content: space-between">
                                            <div>
                                                <span style="margin-right: 10px">序号</span>
                                                <n-radio-group :size="size" v-model:value="hasIndex">
                                                    <n-radio-button label="开启" :value="true"/>
                                                    <n-radio-button label="隐藏" :value="false"/>
                                                </n-radio-group>
                                            </div>
                                            <div>
                                                <span style="margin-right: 10px">可选列</span>
                                                <n-radio-group :size="size" v-model:value="hasSelect">
                                                    <n-radio-button label="开启" :value="true"/>
                                                    <n-radio-button label="隐藏" :value="false"/>
                                                </n-radio-group>
                                            </div>
                                        </div>
                                        <n-divider title-placement="center">列的排序</n-divider>
                                        <n-scrollbar style="max-height: 240px">
                                            <div>
                                                <n-checkbox-group v-model:value="checkedList"
                                                                  @update:value="handleChecked">
                                                    <Draggable v-model="tableConfigColumns" group="people"
                                                               @start="drag = true" @end="onDragEnd"
                                                               item-key="id"
                                                               animation="300"
                                                               chosen-class="chosenClass">
                                                        <template #item="{element}">
                                                            <div class="item" :style="{borderColor: themeColor}">
                                                                <n-space item-style="display: flex;" align="center">
                                                                    <n-icon :component="Move"></n-icon>
                                                                    <n-checkbox :value="element[labelField]"
                                                                                :label="[undefined, null, ''].includes(element[labelField])
                                                            ? '可选列' : element[labelField]"/>
                                                                </n-space>
                                                            </div>
                                                        </template>
                                                    </Draggable>
                                                </n-checkbox-group>
                                            </div>
                                        </n-scrollbar>
                                    </div>
                                </n-popover>
                            </n-space>
                        </div>
                    </div>
                    <n-data-table :remote="true"
                                  :columns="tableColumns"
                                  :data="dataList"
                                  :size="tableSize"
                                  :pagination="pagination"
                                  :row-key="rowKey"
                                  :scroll-x="scrollX"
                                  :loading="loading"
                                  default-expand-all
                                  @update:page="handlePageChange"
                                  @update:page-size="handlePageSizeChange"
                                  @update:checked-row-keys="handleCheckRows"
                                  flex-height style="height: 95%;"/>
                </n-card>
            </n-layout-content>
        </n-layout>
</template>

<script lang="ts">
import {defineComponent, h, onMounted, reactive, ref, toRefs, watch} from "vue";
import {storeToRefs} from "pinia";
import {useSystemStore} from "@/store/modules/useSystemStore";
import {AddCircleOutline, Move, SettingsOutline, TrashBinOutline, Reload} from "@vicons/ionicons5";
import {DataTableRowKey, NButton, NSpace, PaginationInfo} from "naive-ui";
import Draggable from 'vuedraggable'
import {useDeepClone} from "@/hooks/useObject";
import {definedProps} from "@/components/commonTable/props";
import {Size} from "naive-ui/es/button/src/interface";
import {IButtonConfig} from "@/interface/modules/system";
import {tableActionEnums} from "@/enums/tableActionEnums";

export default defineComponent({
    name: "CommonTable",
    emits: ["onPageChange", "onPageSizeChange", "onEvent"],
    props: {
        ...definedProps
    },
    components: {
        SettingsOutline, Draggable, Move
    },
    setup(props, context) {
        // 可选列
        const selectCol = ref<Array<any>>([{
            type: 'selection'
        }])
        // 操作列
        const actionCol = ref<Array<IButtonConfig>>([
            props.buttonConfig.editButton,
            props.buttonConfig.deleteButton,
        ])
        // draggable 配置项
        const dragData = reactive({
            drag: false,
        })
        const {
            layoutHeaderHeight,
            clientHeight,
            layoutFooterHeight,
            themeColor,
            darkTheme
        } = storeToRefs((useSystemStore()));
        // 表格的尺寸
        const tableSize = ref<string>("medium")
        // 是否开启序号列
        const hasIndex = ref<boolean>(false)
        // 是否开启可选列
        const hasSelect = ref<boolean>(false)
        // 表格的列
        const tableColumns = ref<Array<any>>([])
        // 更多设置中的表格的列
        const tableConfigColumns = ref<Array<any>>([])
        // 更多设置中的单选框选中的
        const checkedList = ref<Array<string>>([])
        // 表格中启用可选列后选中的
        const checkedRowList = ref<Array<any>>([])

        watch(() => hasSelect.value, () => {
            concatActionCol()
        })

        // 拼接操作列
        const concatActionCol = () => {
            if (hasSelect.value) {
                tableColumns.value = selectCol.value.concat(
                    tableConfigColumns.value.filter(column => {
                        return checkedList.value.includes(column[props.labelField as string])
                    })
                )
            } else if (checkedList.value.length < tableConfigColumns.value.length) {
                tableColumns.value = tableConfigColumns.value.filter(column => {
                    return checkedList.value.includes(column[props.labelField as string])
                })
            } else {
                tableColumns.value = props.columns as Array<any>
            }
            tableColumns.value = [...tableColumns.value, ...renderActionCol()]
        }


        // 渲染操作列
        const renderActionCol = () => {
            return props.showActionCol ? [{
                title: "操作",
                key: "action",
                fixed: "right",
                width: props.actionWidth,
                render: (row: any) => {
                    return h(NSpace, null, () => [
                            [...actionCol.value.filter(action => action.show), ...props.actionColData].map((action) => {
                                return h(NButton, {
                                        size: props.size as Size,
                                        type: action.colorType,
                                        onClick: () => onEvent(action.actionType, row)
                                    },
                                    {default: () => action.title}
                                )
                            })
                        ]
                    )
                }
            }] : []
        }

        // 检查插槽是否有内容
        const hasQuerySlot = (name: string) => !!context.slots[name];
        // 勾选的行
        const handleCheckRows = (rowKeys: DataTableRowKey[]) => {
            checkedRowList.value = rowKeys
        }
        // 更多设置里边的复选框选中事件
        const handleChecked = (values: Array<any>) => {
            // 返回勾选的列，重新赋值给 tableColumns (回显)
            tableColumns.value = tableConfigColumns.value.filter(column => {
                return values.includes(column[props.labelField as string])
            })
            tableColumns.value = [...tableColumns.value, ...renderActionCol()]
            // 添加操作列
            if (hasSelect.value) {
                tableColumns.value.unshift(...selectCol.value)
            }
        }

        const onDragEnd = () => {
            dragData.drag = false
            // 将排序后的 columns 重新赋值给 tableColumns (回显)
            tableColumns.value = [...tableConfigColumns.value.filter(column => {
                return checkedList.value.includes(column[props.labelField as string])
            }), ...renderActionCol()]
            if (hasSelect.value) {
                tableColumns.value.unshift(...selectCol.value)
            }
        }

        // 页码改变事件
        const handlePageChange = (page: number) => {
            context.emit("onPageChange", page)
        }
        // 每页数据大小改变事件
        const handlePageSizeChange = (pageSize: number) => {
            context.emit("onPageSizeChange", pageSize)
        }

        const onEvent = (type: any, row?: any) => {
            if ([tableActionEnums.ADD, tableActionEnums.BATCH_DELETE].includes(type)) {
                context.emit("onEvent", {type: type, data: type === tableActionEnums.BATCH_DELETE ? checkedRowList.value : undefined})
            } else {
                context.emit("onEvent", {type: type, row: row})
            }
        }

        const reloadData = () => {
            const pagination = props.pagination as PaginationInfo
            handlePageChange(pagination.page)
        }

        onMounted(() => {
            concatActionCol()
            checkedList.value = tableColumns.value.map(item => item[props.labelField as string])
            tableConfigColumns.value = useDeepClone(tableColumns.value.filter(column => {
                return column.key !== "action"
            }))
            setTimeout(() => hasSelect.value = true, 0)
        })

        return {
            tableColumns,
            SettingsOutline,
            Move, AddCircleOutline, TrashBinOutline, Reload,
            hasQuerySlot,
            tableSize,
            hasIndex,
            hasSelect,
            handleCheckRows,
            ...toRefs(dragData),
            handleChecked,
            checkedList,
            tableConfigColumns,
            checkedRowList,
            handlePageChange,
            handlePageSizeChange,
            layoutHeaderHeight, clientHeight, layoutFooterHeight,
            onDragEnd,
            themeColor,
            darkTheme,
            onEvent,
            tableActionEnums,
            reloadData
        }
    }
})
</script>

<style scoped lang="less">
.layout {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    padding: 20px;
}

.chosenClass {
    opacity: 1;
}

.item {
    height: 40px;
    line-height: 40px;
    border-radius: 4px;
    margin: 5px 0;
    padding: 0 5px;
    border: 1px solid;
    display: flex;
    align-items: center;
}

.item:hover {
    cursor: move;
}
</style>