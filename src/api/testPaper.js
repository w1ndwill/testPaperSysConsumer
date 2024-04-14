import request from '../utils/request';


export function query( params ) {
    return request({
        url: 'testPaper/queryListByPage',
        method: 'get',
        params: params
    });
};
export function add ( course ) {
    return request({
        url: 'testPaper/add',
        method: 'post',
        data: course
    });
};
export function del( courseId ) {
    return request({
        url: 'testPaper/del',
        method: 'post',
        params: courseId
    });
};
export function edit( course ) {
    return request({
        url: 'testPaper/edit',
        method: 'post',
        data: course
    });
};
/**
 * 获取课程列表
 */
export function getCourse( ) {
    return request({
        url: 'testPaper/getCourse',
        method: 'get'
    });
};
/**
 * 通过 queIds 获取题目列表,返回map[bool,choice,simple]
 */
export function getQueByIds(ids){
    return request({
        url: 'testPaper/getQueByIds',
        method: 'post',
        data:ids
    });
};