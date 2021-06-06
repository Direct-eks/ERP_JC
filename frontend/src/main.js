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


if (process.env.NODE_ENV === "development") {

}

if (process.env.NODE_ENV === "production") {
    Vue.config.productionTip = false
}

function validateFloat(value) {
    let val = value.toString()
    val = val.replace(/[^\d.]/g, "") // 清除“数字”和“.”以外的字符
    val = val.replace(/\.{2,}/g, ".") // 只保留第一个. 清除多余的
    val = val.replace(".", "$#$").replace(/\./g, "").replace("$#$", ".")
    val = val.replace(/^(-)*(\d+)\.(\d\d).*$/, '$1$2.$3') // 只能输入两个小数
    if (val.indexOf(".") < 0 && val !== "") { // 如果没有小数点，首位不能为0
        val = parseFloat(val)
    }
    console.log('float', val)
    return val
}
Vue.prototype.$validateFloat = validateFloat

function validateNumber(value) {
    const val = value.toString().replace(/[^\d]/g, "")
    console.log('number', val)
    return val
}
Vue.prototype.$validateNumber = validateNumber

function createTree(data, modelSearch) {
    function createTreeHelper (tree, lastLevelIndex, data, prefix) {
        let count = 0;
        for (let item of data) {
            if (item.treeLevel.startsWith(prefix + '-') && // no children
                item.treeLevel.lastIndexOf('-') ===
                    item.treeLevel.indexOf('-', prefix.length)) {
                tree[lastLevelIndex].children.push(modelSearch ? {
                    label: item.code,
                    categoryID: item.modelCategoryID,
                    treeLevel: item.treeLevel,
                    children: []
                } : {
                    label: item.name,
                    areaID: item.areaID,
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
            tree.push(modelSearch ? {
                label: item.code,
                categoryID: item.modelCategoryID,
                treeLevel: item.treeLevel,
                children: []
            } : {
                label: item.name,
                areaID: item.areaID,
                treeLevel: item.treeLevel,
                children: []
            })
        }
    }
    for (let index in tree) {
        createTreeHelper(tree, index, data, tree[index].treeLevel)
    }
    console.log('tree: ', tree)
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

import Big from "big.js"
Vue.prototype.$Big = Big

export default new Vue({
    el: '#app',
    store,
    router,
    vuetify,
    render: h => h(App),
    components: { App },
})
