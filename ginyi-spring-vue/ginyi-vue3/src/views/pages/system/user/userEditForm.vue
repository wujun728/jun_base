<template>
    <CommonModal v-model:show="modalShow" :title="modalTitle" @onSubmit="onSubmit" style="width: 800px">
        <n-form
            ref="modalFormRef"
            :model="modalForm"
            :inline="false"
            :rules="rules">
            <n-grid :cols="24" :x-gap="24">
                <n-form-item-gi :span="12" label="用户账号" path="userName">
                    <n-input v-model:value="modalForm.userName" clearable placeholder="请输入用户账号"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="用户昵称" path="nickName">
                    <n-input v-model:value="modalForm.nickName" clearable placeholder="请输入用户昵称"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="手机号码" path="phoneNumber">
                    <n-input v-model:value="modalForm.phoneNumber" clearable placeholder="请输入手机号码"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="头像" path="avatar">
                    <n-input v-model:value="modalForm.avatar" clearable placeholder="请上传头像"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="邮箱" path="email">
                    <n-input v-model:value="modalForm.email" clearable placeholder="请输入邮箱"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="岗位" path="postIds">
                    <n-select v-model:value="modalForm.postIds"
                              label-field="postName"
                              value-field="postId"
                              clearable
                              filterable
                              :multiple="true"
                              :options="postDict"/>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="归属部门" path="deptId">
                    <n-cascader value-field="deptId"
                                label-field="deptName"
                                v-model:value="modalForm.deptId"
                                :expand-trigger="'click'"
                                :options="deptDict"
                                :check-strategy="'all'"
                                :show-path="true"
                                :filterable="true"
                    />
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="性别" path="sex">
                    <n-radio-group v-model:value="modalForm.sex" size="small" name="radioGroup1">
                        <n-radio-button v-for="(item, index) in sexDict" :key="index" :value="item.value">
                            {{ item.label }}
                        </n-radio-button>
                    </n-radio-group>
                </n-form-item-gi>
                <n-form-item-gi :span="12" label="用户角色" path="roleIds">
                    <n-select v-model:value="modalForm.roleIds"
                              label-field="roleName"
                              value-field="roleId"
                              clearable
                              filterable
                              :multiple="true"
                              :options="roleDict"/>
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
import {defineComponent} from "vue";
import CommonModal from "@/components/commonModal/index.vue";
import {useCommonModal} from "@/components/commonModal/useCommonModal";
import {userController} from "@/api";
import {useDynamicDict} from "@/dictionary/useDynamicDict";
import {useStaticDict} from "@/dictionary/useStaticDict";

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
        } = useCommonModal("用户", {
                sex: "0",
                status: "0"
            },
            userController.add,
            userController.edit,
            userController.deleteById,
            userController.deleteByIds
        )

        const {roleDict, deptDict, postDict} = useDynamicDict()
        const {statusDict, sexDict} = useStaticDict()

        const rules = {
            deptId: {type: "number", required: true, message: "请选择归属部门", trigger: ['blur', 'change']},
            status: {type: "string", required: true, message: "请选择状态", trigger: ["blur", "change"]},
            postIds: {type: "array", required: true, message: "请选择岗位", trigger: ["blur", "change"]},
            roleIds: {type: "array", required: true, message: "请选择角色", trigger: ["blur", "change"]},
            userName: {type: "string", required: true, message: "请输入用户名", trigger: ["input", "blur"]},
            nickName: {type: "string", required: true, message: "请输入用户昵称", trigger: ["input", "blur"]},
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
            sexDict,
            roleDict,
            deptDict,
            postDict
        }
    }
})
</script>

<style scoped>

</style>