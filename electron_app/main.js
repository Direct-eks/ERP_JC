const { app, BrowserWindow, dialog } = require('electron')

/* ---------------------- */

let fs = require('fs')
filenames = fs.readdirSync(app.getAppPath())
let backend = false
for (let file of filenames) {
    console.log(file)
    if (file === 'springboot') backend = true
}

if (!backend) {
    app.quit()
}
else {
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
            }
            else {
                return null
            }
        })

    })

    springBootLauncher.on('message', (message) => {
        if (`${message}` === 'launched') {
            win.setProgressBar(-1)
            win.loadFile(__dirname + '/webpages/index.html', {hash: '#/Login'})
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
}

/* ---------------------- */









