<!-- @FileDescription: 绩效考核用到的静态数据、函数、变量...-->
<script>
const periodOptions = [
  { value: 1, label: '周考核' },
  { value: 2, label: '月考核' },
  { value: 5, label: '季度考核' },
  { value: 4, label: '半年考核' },
  { value: 3, label: '年考核' }
]
const periodOption = [
  { value: 1, label: '周考核' },
  { value: 2, label: '月考核' },
  { value: 5, label: '季度考核' },
  { value: 4, label: '半年考核' },
  { value: 3, label: '年考核' }
]
const statusOptions = [
  { name: '目标制定', value: '0' },
  { name: '执行期', value: 1 },
  { name: '上级评价', value: 2 },
  { name: '绩效审核', value: 3 },
  { name: '结束', value: 4 },
  { name: '未开始', value: 5 }
]
function getStatusText(id) {
  if (id == 0) {
    return '目标制定'
  } else if (id == 1) {
    return '执行期'
  } else if (id == 2) {
    return '上级评价'
  } else if (id == 3) {
    return '绩效审核'
  } else if (id == 4) {
    return '结束'
  } else if (id == 5) {
    return '未开始'
  } else {
    return '结束'
  }
}
function getStatusTag(status) {
  if (status == 0) {
    return {
      type: '',
      text: '目标制定'
    }
  } else if (status == 1) {
    return {
      type: '',
      text: '执行期'
    }
  } else if (status == 2) {
    return {
      type: 'success',
      text: '上级评价'
    }
  } else if (status == 3) {
    return {
      type: '',
      text: '绩效审核'
    }
  } else if (status == 4) {
    return {
      type: 'info',
      text: '结束'
    }
  } else if (status == 5) {
    return {
      type: 'info',
      text: '未开始'
    }
  } else {
    return {
      type: 'info',
      text: '结束'
    }
  }
}
function getPeriodText(id) {
  if (id == 1) {
    return '周考核'
  } else if (id == 2) {
    return '月考核'
  } else if (id == 3) {
    return '年考核'
  } else if (id == 5) {
    return '季度考核'
  } else if (id == 4) {
    return '半年考核'
  } else {
    return '月考核'
  }
}
export default {
  periodOptions,
  getStatusText,
  getStatusTag,
  getPeriodText,
  statusOptions,
  periodOption
}
</script>
