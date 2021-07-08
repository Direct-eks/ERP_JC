const { spawn } = require('child_process')
const http = require('http')

const path = require('path')

let springBoot

process.on('message', (message) => {
    if (message.msg === 'ip') {
        springBoot = spawn(path.resolve('C:\\ERP_JC\\fulljre11\\bin\\java'),
            [
                '-Dlog4j.skipJansi=false',
                '-XX:TieredStopAtLevel=1',
                '-noverify',
                '-Dspring.jmx.enabled=false',
                '-jar',
                path.resolve('C:\\ERP_JC\\backend\\backend-0.0.1-SNAPSHOT.jar'),
                '--server.address=' + message.ipAddress
            ]);

        let applicationStarted = false

        springBoot.stdout.on('data', (data) => {
            const line = `${data}`.trimEnd()
            console.log(line)
            if (line.includes('Application started!')) {
                applicationStarted = true
                process.send({ msg: 'launched' })
            }
            else if (!applicationStarted) {
                process.send({ msg: `${data}` })
            }
        })

        springBoot.on('exit', (code) => {
            process.send({ msg: 'exited' })
        })
    }
    else if (message.msg === 'shutdown') {
        const options = {
            hostname: 'localhost',
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

