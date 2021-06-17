const {app, ipcMain, BrowserWindow, dialog, Menu} = require('electron')

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

springBootLauncher.on('message', (message) => {
    if (message.msg === 'launched') {
        launchWin.webContents.send('async-msg', "launched")
        launchWin.setClosable(true)
    } else if (message.msg === 'exited') {
        app.quit()
    } else {
        launchWin.webContents.send('async-msg', "")
    }
})

let launchWin
Menu.setApplicationMenu(null)

app.whenReady().then(() => {
    launchWin = new BrowserWindow({
        width: 500,
        height: 300,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
        },
        useContentSize: false,
        resizable: false,
        maximizable: false,
        closable: false,
        show: false
    })
    launchWin.loadFile(__dirname + '/launch_page/index.html')
        .then(() => launchWin.show())
    launchWin.on('close', (e) => {
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
    ipcMain.once('ip', (ipAddress) => {
        springBootLauncher.send({ msg: 'ip', ipAddress: `${ipAddress}` })
    })
})

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        springBootLauncher.send('shutdown')
    }
})

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
        // createWindow()
    }
})
