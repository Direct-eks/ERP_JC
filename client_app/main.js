const { app, BrowserWindow, dialog } = require('electron')

/* ---------------------- */
let win

app.whenReady().then(() => {
    win = new BrowserWindow({
        width: 600,
        height: 400,
        webPreferences: {
            nodeIntegration: true,
            contextIsolation: false,
        },
        useContentSize: true
    })
    win.loadFile(__dirname + '/webpages/index.html', {hash: '#/Login'})
        .then(() => win.maximize())
    win.on('close', (e) => {
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

app.on('window-all-closed', () => {
    if (process.platform !== 'darwin') {
        app.quit()
    }
})

app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) {
        // createWindow()
    }
})


/* ---------------------- */
