<template>
    <CommonModal v-model:show="modalShow" :title="modalTitle" @onSubmit="onSubmit" style="width: 800px">
        <n-form
            ref="modalFormRef"
            :model="modalForm"
            :inline="false"
            :rules="rules">
            <n-grid :cols="24" :x-gap="24">
                <n-form-item-gi :span="12" label="角色名称" path="roleName">
                    <n-input v-model:value="modalForm.roleName" clearable placeholder="请输入角色名称"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="角色权限字符串" path="roleKey">
                    <n-input v-model:value="modalForm.roleKey" clearable placeholder="请输入角色权限字符串"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="角色顺序" path="sort">
                    <n-input-number v-model:value="modalForm.sort" clearable placeholder="请输入角色顺序" style="width: 100%"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="状态" path="status">
                    <n-radio-group v-model:value="modalForm.status" name="radioGroup2" size="small">
                        <n-radio-button v-for="(item, index) in statusDict" :key="index" :value="item.value">
                            {{ item.label }}
                        </n-radio-button>
                    </n-radio-group>
                </n-form-item-gi>
                <n-form-item-gi :span="24" label="角色权限" path="permissions">
                    <n-transfer ref="transfer"
                                v-model:value="modalForm.permissions"
                                :render-source-list="renderSourceList"
                                :source-filterable="true"
                                :target-filterable="true"
                                :show-selected="true"
                                :options="options"/>
                </n-form-item-gi>
                <n-form-item-gi :span="24" label="备注" path="remark">
                    <n-input v-model:value="modalForm.remark" clearable type="textarea" placeholder="请输入备注"/>
                </n-form-item-gi>
            </n-grid>
        </n-form>
    </CommonModal>
</template>

<script lang="ts">
import {defineComponent, h, onMounted, ref} from "vue";
import CommonModal from "@/components/commonModal/index.vue";
import {useCommonModal} from "@/components/commonModal/useCommonModal";
import {menuController, roleController} from "@/api";
import {useStaticDict} from "@/dictionary/useStaticDict";
import {NTree, TransferRenderSourceList} from "naive-ui";
import {useFieldClone, useTreeToArray} from "@/hooks/useTree";

export default defineComponent({
    name: "UserEditForm",
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
        } = useCommonModal("角色", {status: "0"},
            roleController.add,
            roleController.edit,
            roleController.deleteById,
            roleController.deleteByIds,
            roleController.getRoleDetailsById
        )

        const menuList = ref<Array<any>>([])
        const {statusDict} = useStaticDict()

        const rules = {
            roleName: {type: "string", required: true, message: "请输入角色名称", trigger: ["input", "blur"]},
            roleKey: {type: "string", required: true, message: "请输入角色权限字符串", trigger: ["input", "blur"]},
            sort: {type: "number", required: true, message: "请输入排序", trigger: ["input", "blur"]},
            status: {type: "string", required: true, message: "请选择状态", trigger: ["blur", "change"]},
            permissions: {type: "array", required: true, message: "请选择权限", trigger: ["blur", "change"]},
        }

        const options = ref<Array<any>>([])
        const renderSourceList: TransferRenderSourceList = ({onCheck, pattern}) => {
            return h(NTree, {
                style: "margin: 0 4px;",
                keyField: "menuId",
                labelField: "menuName",
                checkable: true,
                cascade: false,
                selectable: false,
                blockLine: true,
                checkOnClick: true,
                defaultExpandAll: false,
                data: menuList.value,
                checkedKeys: modalForm.value.permissions,
                pattern,
                onUpdateCheckedKeys: (checkedKeys: Array<string | number>) => {
                    onCheck(checkedKeys)
                }
            })
        }

        const getMenuList = () => {
            menuController.list({}).then(res => {
                const temp = useFieldClone(res.data.list, "menuId", "value")
                menuList.value = useFieldClone(temp, "menuName", "label")
                options.value = useTreeToArray(menuList.value, [])
            })
        }

        onMounted(() => {
            getMenuList()
        })


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
            statusDict,
            menuList,
            renderSourceList,
            options
        }
    }
})
</script>

<style scoped>

</style>