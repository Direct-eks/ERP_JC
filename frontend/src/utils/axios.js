import axios from 'axios'
import main from '~/main'

/*------------- axios --------------*/
axios.defaults.withCredentials = false

axios.interceptors.request.use((config) => {
    const token = sessionStorage.getItem('userToken')
    config.headers.Authorization = !!token ? token : ''
    // console.log('modified header', config.headers)
    return config
})

axios.interceptors.response.use((response) => {
    if (response.status && response.status === 200) {
        // main.$store.commit('setSnackbar', {
        //     message: '请求成功', color: 'success'
        // })
    }
    return Promise.resolve(response)
}, (error) => {
    console.log("error status: " + error.response.status)
    console.log("error message: " + error.response.data.message)
    switch (error.response.status) {
        case 400: // Bad Request
            break
        case 401: // Unauthorized
            main.$store.commit('setSnackbar', {
                message: '登录失效，请重新登录', color: 'error'
            })
            break
        case 403: // Forbidden
            main.$store.commit('setSnackbar', {
                message: '权限错误', color: 'error'
            })
            break
        case 404: // Not Found
            main.$store.commit('setSnackbar', {
                message: '资源请求错误', color: 'error'
            })
            break
        case 500: // Internal Server Error
            main.$store.commit('setSnackbar', {
                message: '服务器错误', color: 'error'
            })
            break
        case 503: // Service Unavailable
            main.$store.commit('setSnackbar', {
                message: '服务器不可用', color: 'error'
            })
            break
        case 504: // Gateway Timeout
            main.$store.commit('setSnackbar', {
                message: '无法链接服务器', color: 'error'
            })
            break
        default:
            main.$store.commit('setSnackbar', {
                message: '未知错误' + error.status, color: 'error'
            })
    }
    return Promise.reject(error)
})

let base
if (process.env.NODE_ENV === 'development') {
    base = '/api'
}
else {
    base = 'http://127.0.0.1:8088'
}

export const getRequest = (url, params) => {
    return axios({
        method: 'get',
        url: `${base}${url}`,
        params: params
    })
}
export const postRequest = (url, body) => {
    return axios({
        method: 'post',
        url: `${base}${url}`,
        data: body
    })
}
export const putRequest = (url, body) => {
    return axios({
        method: 'put',
        url: `${base}${url}`,
        data: body
    })
}
export const patchRequest = (url, body) => {
    return axios({
        method: 'patch',
        url: `${base}${url}`,
        data: body
    })
}
export const deleteRequest = (url, params) => {
    return axios({
        method: 'delete',
        url: `${base}${url}`,
        params: params
    })
}
