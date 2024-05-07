import request from '../utils/request';

export function query(keyOwner) {
    return request({
        url: 'api/keys/queryKey',
        method: 'get',
        params: { keyOwner: keyOwner }
    });
}

export function generate( keyOwner , overwrite) { // key is a json object
    return request({
        url: 'api/keys/generate',
        method: 'post',
        data: {
            keyOwner: keyOwner,
            overwrite: overwrite
        }
    });
};

export function getAllKeys(params) {
    return request({
        url: 'api/keys/getAllKeys',
        method: 'get',
        params: params
    });
}


