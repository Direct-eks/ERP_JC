const { app, BrowserWindow, dialog } = require('electron')

const child = require('child_process')


const springBootLauncher = child.fork('./launcher.js')


springBootLauncher.on('message', (message) => {
    if (`${message}` === 'launched') {
        app.whenReady().then(createWindow)
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
        createWindow()
    }
})

function createWindow () {
    const win = new BrowserWindow({
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

    win.loadFile('./dist/index.html')
}




