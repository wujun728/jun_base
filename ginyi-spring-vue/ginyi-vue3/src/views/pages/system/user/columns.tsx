import {DataTableColumns, NImage, NSpace, NSwitch, NTag} from "naive-ui";
import {useColumns} from "@/views/pages/system/user/useColumns";
import {setting} from "@/config/setting";
import {h} from "vue";
import {eventBus} from "@/config/eventBus";

const {logo} = setting

const {
    useRenderSexById,
    useRenderDeptNameById,
    useRenderRoleNameByIds,
    useRenderPostNameByIds
} = useColumns()

/**
 * 状态的点击事件
 * @param row
 */
const handleSwitchClick = (row: any) => {
    eventBus.emit("handleUserStatusSwitchClick", row)
}

export const columns: DataTableColumns<any> = [
    {
        title: "用户昵称",
        key: "nickName"
    },
    {
        title: "用户账号",
        key: "userName"
    },
    {
        title: "头像",
        key: "avatar",
        width: 80,
        render: (row) => (
            <NImage width="30" style="border-radius: 50%;" src={logo}/>
        )
    },
    {
        title: "性别",
        key: "sex",
        width: 80,
        render: (row) => (
            <span>{useRenderSexById(row.sex)}</span>
        )
    },
    {
        title: "状态",
        key: "status",
        width: 150,
        render: (row) => (
            h(NSwitch, {
                value: row.status === "0",
                onClick: () => handleSwitchClick(row)
            }, {
                checked: () => "正常",
                unchecked: () => "禁用"
            })
        )
    },
    {
        title: "部门",
        key: "deptId",
        width: 300,
        ellipsis: {
            tooltip: true
        },
        render: (row) => (
            <span>{useRenderDeptNameById(row.deptId)}</span>
        )
    },
    {
        title: "岗位",
        key: "postIds",
        width: 200,
        render: (row) => (
            <NSpace>
                {useRenderPostNameByIds(row.postIds)?.map(post => <NTag key={post}>{post}</NTag>)}
            </NSpace>
        )
    },
    {
        title: "角色",
        key: "roleIds",
        width: 200,
        ellipsis: {
            tooltip: true
        },
        render: (row) => (
            <NSpace>
                {useRenderRoleNameByIds(row.roleIds)?.map(role => <NTag key={role}>{role}</NTag>)}
            </NSpace>
        )
    },
    {
        title: "手机号码",
        key: "phoneNumber"
    },
    {
        title: "创建时间",
        key: "createTime",
        width: 200
    },
    {
        title: "更新时间",
        key: "updateTime",
        width: 200
    }
]