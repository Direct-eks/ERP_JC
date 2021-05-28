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
let launchWin

app.whenReady().then(() => {
    launchWin = new BrowserWindow({
        width: 500,
        height: 300,
        frame: false,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
        },
        useContentSize: true,
        show: false
    })
    launchWin.on('ready-to-show',()=>{
        launchWin.show();
    })
    launchWin.loadFile(__dirname + '/launch_page/index.html')
})

springBootLauncher.on('message', (message) => {
    if (`${message}` === 'launched') {
        win = new BrowserWindow({
            webPreferences: {
                nodeIntegration: false,
            },
            useContentSize: true,
            show: false
        })
        win.on('ready-to-show',()=>{
            win.show();
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
        win.loadFile(__dirname + '/webpages/index.html', {hash: '#/Login'})
        win.maximize()
        launchWin.close()
        launchWin.destroy()
        launchWin = null
    } else if (`${message}` === 'exited') {
        app.quit()
    } else {
        launchWin.webContents.send('async-msg')
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









