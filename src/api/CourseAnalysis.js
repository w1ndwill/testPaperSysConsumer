import request from '../utils/request';

export function query() {
    return request({
        url: 'CourseAnalysis/query',
        method: 'get'
    });
};

export function getQueBankList() {
    return request({
        url: 'CourseAnalysis/getQueBankList',
        method: 'get'
    });
};