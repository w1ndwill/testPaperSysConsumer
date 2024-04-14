import request from '../utils/request';

export function query( params ) {
    return request({
        url: 'course/queryListByPage',
        method: 'get',
        params: params
    });
};
export function add ( course ) {
    return request({
        url: 'course/add',
        method: 'post',
        data: course
    });
};
export function del( courseId ) {
    return request({
        url: 'course/del',
        method: 'post',
        params: courseId
    });
};
export function edit( course ) {
    return request({
        url: 'course/edit',
        method: 'post',
        data: course
    });
};