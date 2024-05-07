import request from '../utils/request';

export function getAdminList() {
    return request({
        url: 'paperWd/getAdmin',
        method: 'get',
    });
}

export function uploadto_server(formData) {
    return request({
        url: '/paperWd/upload',
        method: 'post',
        data: formData,
        headers: {
            'Content-Type': 'multipart/form-data'
        },
    });
}

export function save(formData) {
    return request({
        url: '/paperWd/save',
        method: 'post',
        data: formData,
    });
}

export function getCourse( ) {
    return request({
        url: 'question/getCourse',
        method: 'get',
    });
};

export function query( params ) {
    return request({
        url: '/paperWd/queryPaperWdByPage',
        method: 'get',
        params: params
    });
}

export function check( params ) {
    return request({
        url: '/paperWd/check',
        method: 'post',
        data: params
    });
}
