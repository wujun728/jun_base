<template>
    <CommonModal v-model:show="modalShow"
                 :title="modalTitle"
                 @onSubmit="onSubmit" style="width: 800px">
        <n-form
            ref="modalFormRef"
            :model="modalForm"
            :inline="false"
            :rules="rules">
            <n-grid :cols="24" :x-gap="24">
                <n-form-item-gi :span="24" label="上级菜单" path="parentId">
                    <n-tree-select v-model:value="modalForm.parentId"
                                   label-field="menuName"
                                   key-field="menuId"
                                   :default-value="0"
                                   :options="menuDict"
                                   :show-path="true"
                                   placeholder="不选择时，默认顶级菜单"
                                   clearable
                                   default-expand-all/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="菜单名称" path="menuName">
                    <n-input v-model:value="modalForm.menuName" clearable placeholder="请输入菜单名称"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="显示顺序" path="sort">
                    <n-input-number v-model:value="modalForm.sort" clearable placeholder="请输入显示顺序" style="width: 100%"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="菜单图标" :path="['M', 'C'].includes(modalForm.menuType) ? 'icon' : ''">
                    <n-input v-model:value="modalForm.icon" clearable placeholder="请输入菜单图标"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="路由名称" :path="['M', 'C'].includes(modalForm.menuType) ? 'name' : ''">
                    <n-input v-model:value="modalForm.name" clearable placeholder="请输入路由名称"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="路由组件" :path="modalForm.menuType === 'C' ? 'component' : ''">
                    <n-input v-model:value="modalForm.component" clearable placeholder="请输入路由组件"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="权限字符" :path="modalForm.menuType === 'F' ? 'perms' : ''">
                    <n-input v-model:value="modalForm.perms" clearable placeholder="请输入权限字符"/>
                </n-form-item-gi>
                <n-form-item-gi :span="6" label="菜单状态" path="status">
                    <n-radio-group v-model:value="modalForm.status" name="radioGroup2" size="small">
                        <n-radio-button v-for="(item, index) in statusDict" :key="index" :value="item.value">
                            {{ item.label }}
                        </n-radio-button>
                    </n-radio-group>
                </n-form-item-gi>
                <n-form-item-gi :span="6" label="是否显示" path="visible">
                    <n-radio-group v-model:value="modalForm.visible" name="radioGroup2" size="small">
                        <n-radio-button v-for="(item, index) in whetherDict" :key="index" :value="item.value">
                            {{ item.label }}
                        </n-radio-button>
                    </n-radio-group>
                </n-form-item-gi>
                <n-form-item-gi :span="6" label="是否缓存" path="isCache">
                    <n-radio-group v-model:value="modalForm.isCache" name="radioGroup2" size="small">
                        <n-radio-button v-for="(item, index) in whetherDict" :key="index" :value="item.value">
                            {{ item.label }}
                        </n-radio-button>
                    </n-radio-group>
                </n-form-item-gi>
                <n-form-item-gi :span="6" label="菜单类型" path="menuType">
                    <n-radio-group v-model:value="modalForm.menuType" name="radioGroup2" size="small">
                        <n-radio-button v-for="(item, index) in menuTypeDict" :key="index" :value="item.value">
                            {{ item.label }}
                        </n-radio-button>
                    </n-radio-group>
                </n-form-item-gi>
                <n-form-item-gi :span="24" label="备注" path="remark">
                    <n-input v-model:value="modalForm.remark" clearable type="textarea" placeholder="请输入备注"/>
                </n-form-item-gi>
            </n-grid>
        </n-form>
    </CommonModal>
</template>

<script lang="ts">
import {defineComponent, watch} from "vue";
import CommonModal from "@/components/commonModal/index.vue";
import {useCommonModal} from "@/components/commonModal/useCommonModal";
import {menuController} from "@/api";
import {useStaticDict} from "@/dictionary/useStaticDict";
import {useDynamicDict} from "@/dictionary/useDynamicDict";
import {useRemoveEmptyChildrenField} from "@/hooks/useTree";

export default defineComponent({
    name: "deptEditForm",
    components: {
        CommonModal
    },
    setup() {
        const {
            modalShow,
            modalTitle,
            modalForm,
            modalFormRef,
            modalLoading,
            onAdd,
            onEdit,
            onSubmit,
            onDeleteById,
            onDeleteByIds
        } = useCommonModal("菜单", {
                status: "0",
                visible: "0",
                isCache: "0",
                menuType: "M"
            },
            menuController.add,
            menuController.edit,
            menuController.deleteById,
            menuController.deleteByIds,
            menuController.getMenuDetailsById
        )
        const {statusDict, whetherDict, menuTypeDict} = useStaticDict()
        const {menuDict} = useDynamicDict()


        const rules = {
            menuName: {type: "string", required: true, message: "请输入菜单名称", trigger: ["input", "blur"]},
            sort: {type: "number", required: true, message: "请输入排序", trigger: ["input", "blur"]},
            icon: {type: "string", required: true, message: "请输入菜单图标", trigger: ["input", "blur"]},
            component: {type: "string", required: true, message: "请输入路由组件", trigger: ["input", "blur"]},
            name: {type: "string", required: true, message: "请输入路由名称", trigger: ["input", "blur"]},
            perms: {type: "string", required: true, message: "请输入权限字符", trigger: ["input", "blur"]},
            status: {type: "string", required: true, message: "请选择状态", trigger: ["blur", "change"]},
            isCache: {type: "string", required: true, message: "请选择是否缓存", trigger: ["blur", "change"]},
            visible: {type: "string", required: true, message: "请选择是否显示", trigger: ["blur", "change"]},
            menuType: {type: "string", required: true, message: "请选择菜单类型", trigger: ["blur", "change"]},
        }

        const addChildMenu = (value: any) => {
            onAdd()
            modalForm.value.parentId = value.menuId
        }

        watch(() => modalForm.value, () => modalForm.value.path = modalForm.value.name, {deep: true})

        return {
            modalShow,
            modalTitle,
            modalForm,
            modalFormRef,
            modalLoading,
            rules,
            onAdd,
            onEdit,
            onSubmit,
            onDeleteById,
            onDeleteByIds,
            addChildMenu,
            menuDict,
            statusDict,
            whetherDict,
            menuTypeDict
        }
    }
})
</script>

<style scoped>

</style>