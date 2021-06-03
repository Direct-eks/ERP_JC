const { spawn } = require('child_process')
const http = require('http')

const path = require('path')
const springBoot = spawn(path.resolve('C:\\ERP_JC\\fulljre11\\bin\\java'),
    ['-Dlog4j.skipJansi=false', '-XX:TieredStopAtLevel=1', '-noverify',
        '-Dspring.jmx.enabled=false', '-jar',
        path.resolve('C:\\ERP_JC\\backend-0.0.1-SNAPSHOT.jar')]);

let applicationStarted = false

springBoot.stdout.on('data', (data) => {
    const line = `${data}`.trimEnd()
    console.log(line)
    if (line.includes('Application started!')) {
        applicationStarted = true
        process.send('launched')
    }
    else if (!applicationStarted) {
        process.send(`${data}`)
    }
})

springBoot.on('exit', (code) => {
    process.send('exited')
})

process.on('message', (message) => {
    console.log('child process receive message', `${message}`)
    if (`${message}` === 'shutdown') {
        const options = {
            hostname: 'localhost',
            port: 58080,
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

