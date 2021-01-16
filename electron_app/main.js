const {app, BrowserWindow, dialog} = require('electron')

/* ---------------------- */

const fs = require('fs')
const path = require('path')

let backendFilesExist = false
let jreFilesExist = false
let databaseFilesExist = false

const files = fs.readdirSync(path.resolve('C:\\ERP_JC'))
for (const file of files) {
    // console.log(file)
    if (file === 'db') databaseFilesExist = true
    if (file === 'fulljre11') jreFilesExist = true
    if (file === 'backend-0.0.1-SNAPSHOT.jar') backendFilesExist = true
}

if (!backendFilesExist || !jreFilesExist || !databaseFilesExist) {
    if (!backendFilesExist) dialog.showErrorBox('错误', '服务端文件未找到')
    if (!jreFilesExist) dialog.showErrorBox('错误', '运行环境文件未找到')
    if (!databaseFilesExist) dialog.showErrorBox('错误', '数据库文件未找到')
    app.quit()
    process.exit(1)
}


const child = require('child_process')
const springBootLauncher = child.fork(__dirname + '/launcher.js')

let win
let progress = 0.001

app.whenReady().then(() => {
    win = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: false,
        },
        useContentSize: true
    })

    win.on('close', (e) => {
        // e.preventDefault();
        let result = dialog.showMessageBoxSync({
            type: "warning",
            title: '退出确认',
            message: '确认退出？',
            buttons: ['取消', '确认']
        })
        if (result === 0) {
            e.preventDefault()
        } else {
            return null
        }
    })

})

springBootLauncher.on('message', (message) => {
    if (`${message}` === 'launched') {
        win.setProgressBar(-1)
        win.loadFile(__dirname + '/webpages/index.html', {hash: '#/Login'})
        win.maximize()
    } else if (`${message}` === 'one line') {
        try {
            win.setProgressBar(progress)
            progress += 0.004
        } catch (error) {
            //ignore
        }
    } else {
        app.quit()
    }
})

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        console.log('window closed')
        // app.quit()
        springBootLauncher.send('shutdown')
        console.log('message passed')
    }
})

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
        // createWindow()
    }
})


/* ---------------------- */









