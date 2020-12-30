const { app, BrowserWindow, dialog } = require('electron')

const child = require('child_process')


const springBootLauncher = child.fork('./launcher.js')

let win
let progress = 0.001

app.whenReady().then(() => {
    win = new BrowserWindow({
        width: 800,
        height: 600,
        webPreferences: {
            nodeIntegration: true,
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
        }
        else {
            return null
        }
    })

})

springBootLauncher.on('message', (message) => {
    if (`${message}` === 'launched') {
        win.setProgressBar(-1)
        win.loadFile('./dist/index.html', {hash: '#/Login'})
        win.maximize()
    }
    else if (`${message}` === 'one line') {
        try {
            win.setProgressBar(progress)
            progress += 0.004
        } catch (error) {
            //ignore
        }
    }
    else {
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







