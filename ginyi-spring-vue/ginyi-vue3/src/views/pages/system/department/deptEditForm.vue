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
                <n-form-item-gi :span="24" label="上级部门" path="parentId">
                    <n-tree-select v-model:value="modalForm.parentId"
                                   label-field="deptName"
                                   key-field="deptId"
                                   :default-value="0"
                                   :options="deptDict"
                                   :show-path="true"
                                   placeholder="不选择时，默认顶级部门"
                                   clearable
                                   default-expand-all/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="部门名称" path="deptName">
                    <n-input v-model:value="modalForm.deptName" clearable placeholder="请输入部门名称"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="显示顺序" path="sort">
                    <n-input-number v-model:value="modalForm.sort" clearable placeholder="请输入显示顺序" style="width: 100%"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="部门负责人" path="deptName">
                    <n-input v-model:value="modalForm.leader" clearable placeholder="请输入部门负责人"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="负责人邮箱" path="email">
                    <n-input v-model:value="modalForm.email" clearable placeholder="请输入负责人邮箱"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="负责人联系电话" path="phone">
                    <n-input v-model:value="modalForm.phone" clearable placeholder="请输入负责人联系电话"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="状态" path="status">
                    <n-radio-group v-model:value="modalForm.status" name="radioGroup2" size="small">
                        <n-radio-button v-for="(item, index) in statusDict" :key="index" :value="item.value">
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
import {defineComponent, ref, watch} from "vue";
import CommonModal from "@/components/commonModal/index.vue";
import {useCommonModal} from "@/components/commonModal/useCommonModal";
import {deptController} from "@/api";
import {useStaticDict} from "@/dictionary/useStaticDict";
import {useDynamicDict} from "@/dictionary/useDynamicDict";

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
        } = useCommonModal("部门", {status: "0"},
            deptController.add,
            deptController.edit,
            deptController.deleteById,
            deptController.deleteByIds,
            deptController.getDeptDetailsById
        )
        const {statusDict} = useStaticDict()
        const {deptDict} = useDynamicDict()


        const rules = {
            deptName: {type: "string", required: true, message: "请输入部门名称", trigger: ["input", "blur"]},
            sort: {type: "number", required: true, message: "请输入排序", trigger: ["input", "blur"]},
            status: {type: "string", required: true, message: "请选择状态", trigger: ["blur", "change"]},
            email: {type: "string", required: true, message: "请输入部门负责人邮箱", trigger: ["input", "blur"]},
            phone: {type: "string", required: true, message: "请输入部门负责人联系电话", trigger: ["input", "blur"]},
        }

        const addChildDept = (value: any) => {
            onAdd()
            modalForm.value.parentId = value.deptId
        }

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
            deptDict,
            addChildDept,
        }
    }
})
</script>

<style scoped>

</style>