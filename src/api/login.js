import request from '../utils/request';


export function login (params) {
    return request({
        url:  'user/login',
        method: 'post',
        data: params
    });
};

export function logOut (userId) {
    return request({
        url:  'user/loginOut',
        method: 'post',
        data: userId
    });
};

export function changePsw (data) {
    return request({
        url:  'user/changePassword',
        method: 'post',
        data: data
    });
};
export function register (data) {
    return request({
        url:  'user/register',
        method: 'post',
        data: data
    });
};