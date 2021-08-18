import Vue from 'vue'
import router from '~/utils/router'
import store from '~/store/index'

import vuetify from '~/plugins/vuetify' // path to vuetify export

import App from './App.vue'

import { getRequest } from './utils/axios'
import { postRequest } from './utils/axios'
import { deleteRequest } from './utils/axios'
import { putRequest } from './utils/axios'
import { patchRequest } from './utils/axios'
import { getFileRequest } from "./utils/axios";
import api from './utils/api'

Vue.prototype.$getRequest = getRequest
Vue.prototype.$postRequest = postRequest
Vue.prototype.$deleteRequest = deleteRequest
Vue.prototype.$putRequest = putRequest
Vue.prototype.$patchRequest = patchRequest
Vue.prototype.$getFileRequest = getFileRequest
Vue.prototype.$api = api


if (process.env.NODE_ENV === "production" || process.env.BUILD_ENV === 'webOnly') {
    Vue.config.productionTip = false
}

Date.prototype.format = function (fmt) {
    const o = {
        "M+": this.getMonth() + 1, // 月份
        "d+": this.getDate(), // 日
        "h+": this.getHours(), // 小时
        "m+": this.getMinutes(), // 分
        "s+": this.getSeconds(), // 秒
        "q+": Math.floor((this.getMonth() + 3) / 3), // 季度
        S: this.getMilliseconds() // 毫秒
    };
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (`${this.getFullYear()}`).substr(4 - RegExp.$1.length));
    }
    for (const k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? (o[k]) : ((`00${o[k]}`).substr((`${o[k]}`).length)));
        }
    }
    return fmt;
}

import Big from "big.js"
Vue.prototype.$Big = Big
Vue.prototype.$Big.DP = 8
Vue.prototype.$Big.RM = Big.roundHalfUp

function validateFloat(value, useTwoDecimal) {
    let val = value.toString()
    val = val.replace(/[^\d.]/g, "") // 清除“数字”和“.”以外的字符
    val = val.replace(/\.{2,}/g, ".") // 只保留第一个. 清除多余的
    val = val.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".")
    if (useTwoDecimal) {
        val = val.replace(/^(-)*(\d+)\.(\d{1,2}).*$/, '$1$2.$3') // 只能输入2个小数
    }
    else {
        val = val.replace(/^(-)*(\d+)\.(\d{1,8}).*$/, '$1$2.$3') // 只能输入8个小数
    }
    if (val.indexOf(".") < 0 && val !== "") { // 如果没有小数点，首位不能为0
        val = parseFloat(val)
    }
    // console.log('float', val)
    return val
}
Vue.prototype.$validateFloat = validateFloat

function validateNumber(value) {
    let val = value.toString()
    if (val.startsWith('-')) {
        val = '-' + val.replace(/[^\d]/g, "")
    }
    else {
        val = val.replace(/[^\d]/g, "")
    }
    // console.log('number', val)
    return val
}
Vue.prototype.$validateNumber = validateNumber

function createTree(data, mode) {
    let id, code
    switch (mode) {
        case 'company':
            id = 'areaID'
            code = 'name'
            break
        case 'model':
            id = 'modelCategoryID'
            code = 'code'
            break
        case 'fee':
            id = 'feeCategoryID'
            code = 'name'
            break
        default: return
    }

    function createTreeHelper (tree, lastLevelIndex, data, prefix) {
        let count = 0;
        for (let item of data) {
            if (item.treeLevel.startsWith(prefix + '-') && // no children
                item.treeLevel.lastIndexOf('-') ===
                    item.treeLevel.indexOf('-', prefix.length)) {
                tree[lastLevelIndex].children.push({
                    label: item[code],
                    categoryID: item[id],
                    treeLevel: item.treeLevel,
                    children: []
                })
                count++
            }
        }
        if (count === 0) return

        const children = tree[lastLevelIndex].children
        for (let index = 0; index < children.length; ++index) {
            createTreeHelper(children, index, data, children[index].treeLevel)
        }
    }

    const tree = [];
    for (const item of data) {
        if (item.treeLevel.indexOf('-') === -1) { // first level object
            tree.push({
                label: item[code],
                categoryID: item[id],
                treeLevel: item.treeLevel,
                children: []
            })
        }
    }
    for (let index in tree) {
        createTreeHelper(tree, index, data, tree[index].treeLevel)
    }
    return tree
}
Vue.prototype.$createTree = createTree

function createOneLevelTree(data) {
    const tree = [];
    data.forEach(item => {
        tree.push({label: item.name, children: [], categoryID: item.categoryID})
    })
    return tree
}
Vue.prototype.$createOneLevelTree = createOneLevelTree

export default new Vue({
    el: '#app',
    store,
    router,
    vuetify,
    render: h => h(App),
    components: { App },
})
