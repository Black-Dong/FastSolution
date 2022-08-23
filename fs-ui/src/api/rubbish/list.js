import request from '@/utils/request'

// 查询垃圾管理列表
export function listList(query) {
  return request({
    url: '/rubbish/list/list',
    method: 'get',
    params: query
  })
}

// 查询垃圾管理详细
export function getList(listId) {
  return request({
    url: '/rubbish/list/' + listId,
    method: 'get'
  })
}

// 新增垃圾管理
export function addList(data) {
  return request({
    url: '/rubbish/list',
    method: 'post',
    data: data
  })
}

// 修改垃圾管理
export function updateList(data) {
  return request({
    url: '/rubbish/list',
    method: 'put',
    data: data
  })
}

// 删除垃圾管理
export function delList(listId) {
  return request({
    url: '/rubbish/list/' + listId,
    method: 'delete'
  })
}
