import request from '@/utils/request'

// 查询最近使用模板列表
export function getReceTemplateList() {
  return request({
    url: '/workflow/receTemplate/list',
    method: 'get'
  })
}

// 新增最近使用模板
export function addReceTemplate(data) {
  return request({
    url: '/workflow/receTemplate',
    method: 'post',
    data: data
  })
}