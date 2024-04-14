import request from '../utils/request';


export function query( params ) {
    return request({
        url: 'questionBank/queryListByPage',
        method: 'get',
        params: params
    });
};
export function add ( queBank ) {
    return request({
        url: 'questionBank/add',
        method: 'post',
        data: queBank
    });
};
export function del( queBankId ) {
    return request({
        url: 'questionBank/del',
        method: 'post',
        params: queBankId
    });
};
export function edit( queBank ) {
    return request({
        url: 'questionBank/edit',
        method: 'post',
        data: queBank
    });
};


export function getCourse( ) {
    return request({
        url: 'questionBank/getCourse',
        method: 'get'
    });
};

export function UntieQuestion( params) {
    return request({
        url: 'questionBank/UntieQuestion',
        method: 'get',
        params:params
    });
};