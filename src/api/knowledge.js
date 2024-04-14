import request from '../utils/request';

export function knowledgeQuery(params) {
  return request({
    url: 'knowledge/queryListByPage',
    method: 'get',
    params: params
  });
};

export function add(knowledge) {
  return request({
    url: 'knowledge/add',
    method: 'post',
    data: knowledge
  });
};

export function del(knowledgeId) {
  return request({
    url: 'knowledge/del',
    method: 'post',
    params: knowledgeId
  });
};

export function edit(knowledge) {
  return request({
    url: 'knowledge/edit',
    method: 'post',
    data: knowledge
  });
};
