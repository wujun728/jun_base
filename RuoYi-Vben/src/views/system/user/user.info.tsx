import { DescItem } from '@/components/Description';
import { DictEnum } from '@/enums/dictEnum';
import { useRender } from '@/hooks/component/useRender';
import { Tag } from 'ant-design-vue';
import { dateUtil } from '@/utils/dateUtil';

function renderTags(list: string[]) {
  return (
    <div class="flex flex-row flex-wrap gap-0.5">
      {list.map((item) => (
        <Tag key={item}>{item}</Tag>
      ))}
    </div>
  );
}

const { renderDict } = useRender();
export const descSchema: DescItem[] = [
  {
    label: '用户ID',
    field: 'userId',
  },
  {
    label: '用户状态',
    field: 'status',
    render(value) {
      return renderDict(value, DictEnum.NORMAL_DISABLE);
    },
  },
  {
    label: '用户信息',
    field: 'nickName',
    render(_, data) {
      const { userName, nickName, deptName = '暂无部门信息' } = data;
      // 为了兼容新版本和旧版本
      let currentDept = deptName;
      if (data.dept && data.dept.deptName) {
        currentDept = data.dept.deptName;
      }
      return `${userName} / ${nickName} / ${currentDept}`;
    },
  },
  {
    label: '手机号',
    field: 'phonenumber',
    render(value) {
      return value || '未设置手机号码';
    },
  },
  {
    label: '邮箱',
    field: 'email',
    render(value) {
      return value || '未设置邮箱地址';
    },
  },
  {
    label: '岗位',
    field: 'postNames',
    render(value) {
      if (Array.isArray(value) && value.length === 0) {
        return '暂无信息';
      }
      return renderTags(value);
    },
  },
  {
    label: '权限',
    field: 'roleNames',
    render(value) {
      if (Array.isArray(value) && value.length === 0) {
        return '暂无信息';
      }
      return renderTags(value);
    },
  },
  {
    label: '创建时间',
    field: 'createTime',
  },
  {
    label: '上次登录IP',
    field: 'loginIp',
    render(value) {
      return value || <span class="text-orange-500">从未登录过</span>;
    },
  },
  {
    label: '上次登录时间',
    field: 'loginDate',
    render(value) {
      if (!value) {
        return <span class="text-orange-500">从未登录过</span>;
      }
      // 默认en显示
      dateUtil.locale('zh-cn');
      // 计算相差秒数
      const diffSeconds = dateUtil(new Date()).diff(dateUtil(value), 'second');
      /**
       * 转为时间显示(x月 x天)
       * https://dayjs.fenxianglu.cn/category/duration.html#%E4%BA%BA%E6%80%A7%E5%8C%96
       *
       * */
      const diffText = dateUtil.duration(diffSeconds, 'seconds').humanize();
      return (
        <div class="flex gap-2">
          {value}
          <Tag bordered={false} color="cyan">
            {diffText}前
          </Tag>
        </div>
      );
    },
  },
  {
    label: '备注',
    field: 'remark',
    render(value) {
      return value || '无';
    },
  },
];
