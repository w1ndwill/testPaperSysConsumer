import request from '../utils/request';

/**
 * [模糊]查询用户列表,分页
 * @param {} params 
 */
export function query( params ) {
    return request({
        url: 'user/queryListByPage',
        method: 'get',
        params: params
    });
};
/**
 * 添加用户
 * @param {*} user 
 */
export function add ( user ) {
    return request({
        url: 'user/add',
        method: 'post',
        data: user
    });
};
/**
 * 删除用户
 * @param {} userId 
 */
export function del( userId ) {
    return request({
        url: 'user/del',
        method: 'post',
        params: userId
    });
};
/**
 * 编辑用户
 * @param {} user 
 */
export function edit( user ) {
    return request({
        url: 'user/edit',
        method: 'post',
        data: user
    });
};

/**
 * 发生短信验证码
 * @param {*} params 
 */
export function sendSms( params ) {
    return request({
        url: 'user/sendSms',
        method: 'post',
        data: params
    });
};
/**
 * 检查验证码是否正确
 * @param {*} params 
 */
export function checkValidate( params ) {
    return request({
        url: 'user/checkValidate',
        method: 'post',
        data: params
    });
};
/**
 * 重置密码
 */
export function reSetPsw( params ) {
    return request({
        url: 'user/reSetPassword',
        method: 'post',
        data: params
    });
};