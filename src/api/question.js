import request from '../utils/request';


export function query( params ) {
    return request({
        url: 'question/queryListByPage',
        method: 'get',
        params: params
    });
};
export function add ( question ) {
    return request({
        url: 'question/add',
        method: 'post',
        data: question
    });
};
export function del( queId ) {
    return request({
        url: 'question/del',
        method: 'post',
        params: queId
    });
};
export function edit( question ) {
    return request({
        url: 'question/edit',
        method: 'post',
        data: question
    });
};


export function getCourse( ) {
    return request({
        url: 'question/getCourse',
        method: 'get',
    });
};
export function getQueBank(params ) {
    return request({
        url: 'question/getQueBank',
        method: 'get',
        params: params
    });
};

export function getKnowledge(params ) {
    return request({
        url: 'question/getKnowledge',
        method: 'get',
        params: params
    });
};