import request from '../utils/request';

export const fetchData = params => {
    return request({
        url: './table.json',
        method: 'get',
        params: params
    });
};
/**
 * 获取试卷数量
 * @param {*} params 
 * @returns 
 */
export const getTpNo = params => {
    return request({
        url: 'testPaper/getTpNo',
        method: 'get',
        params: params
    });
};
/**
 * 获取课程数量
 * @param {*} params 
 * @returns 
 */
export const getCourseNo = params => {
    return request({
        url: 'course/getCourseNo',
        method: 'get',
        params: params
    });
};
/**
 * 获取题目数量
 * @param {*} params 
 * @returns 
 */
export const getQueNo = params => {
    return request({
        url: 'question/getQueNo',
        method: 'get',
        params: params
    });
};

export const getPaperNo = params => {
    return request({
        url: 'paperWd/getPapNo',
        method: 'get',
        params: params
    });
}

