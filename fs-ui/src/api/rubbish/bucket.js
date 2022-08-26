import request from '@/utils/request'

// 查询垃圾站管理列表
export function listBucket(query) {
  return request({
    url: '/rubbish/bucket/list',
    method: 'get',
    params: query
  })
}

// 查询垃圾站管理详细
export function getBucket(id) {
  return request({
    url: '/rubbish/bucket/' + id,
    method: 'get'
  })
}

// 新增垃圾站管理
export function addBucket(data) {
  return request({
    url: '/rubbish/bucket',
    method: 'post',
    data: data
  })
}

// 修改垃圾站管理
export function updateBucket(data) {
  return request({
    url: '/rubbish/bucket',
    method: 'put',
    data: data
  })
}

// 删除垃圾站管理
export function delBucket(id) {
  return request({
    url: '/rubbish/bucket/' + id,
    method: 'delete'
  })
}
