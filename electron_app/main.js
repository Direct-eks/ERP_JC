const {app, ipcMain, BrowserWindow, dialog, Menu} = require('electron')

/* ---------------------- */

const fs = require('fs')
const path = require('path')
const os = require('os');
const http = require('http')

let backendFilesExist = false
let jreFilesExist = false
let databaseFilesExist = false

const jarName = 'backend-0.0.1-SNAPSHOT.jar'

const files = fs.readdirSync(path.resolve('C:\\ERP_JC'))
for (const file of files) {
    if (file === 'db') databaseFilesExist = true
    if (file === 'fulljre11') jreFilesExist = true
    if (file === 'backend') backendFilesExist = true
}

if (!backendFilesExist || !jreFilesExist || !databaseFilesExist) {
    if (!backendFilesExist) dialog.showErrorBox('错误', '服务端文件未找到')
    if (!jreFilesExist) dialog.showErrorBox('错误', '运行环境文件未找到')
    if (!databaseFilesExist) dialog.showErrorBox('错误', '数据库文件未找到')
    app.quit()
    process.exit(1)
}

// get ipv4 address of this machine
function getIPAddress() {
    const interfaces = os.networkInterfaces();
    for (const devName in interfaces) {
        const tempInterface = interfaces[devName];
        for (let i = 0; i < tempInterface.length; i++) {
            const alias = tempInterface[i];
            if (alias.family === 'IPv4' && alias.address !== '127.0.0.1' && !alias.internal) {
                return alias.address;
            }
        }
    }
}
const myIPAddress = getIPAddress();
console.log(myIPAddress)


const child = require('child_process')
const springBootLauncher = child.fork(__dirname + '/launcher.js')

springBootLauncher.on('message', (message) => {
    if (message.msg === 'launched') {
        mainWin = new BrowserWindow({
            width: 600,
            height: 400,
            webPreferences: {
                nodeIntegration: true,
                contextIsolation: false,
            },
            useContentSize: true
        })
        mainWin.loadFile(__dirname + '/webpages/index.html', {hash: '#/Login'})
            .then(() => {
                launchWin.destroy()
                launchWin = null
                mainWin.maximize()
            })
        mainWin.on('close', (e) => {
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
    } else if (message.msg === 'exited') {
        app.quit()
    } else {
        launchWin.webContents.send('async-msg', "")
    }
})

let launchWin, mainWin
Menu.setApplicationMenu(null)

app.whenReady().then(() => {
    springBootLauncher.send({ msg: 'start', jarName: jarName})
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
})

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        const options = {
            hostname: myIPAddress,
            port: 80,
            path: '/actuator/shutdown',
            method: 'POST',
            headers: {}
        }
        const req = http.request(options, res => {
            console.log(`${res.statusCode}`)
        })
        req.write('')
        req.end()
    }
})

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
        // createWindow()
    }
})
