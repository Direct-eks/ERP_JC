const { spawn } = require('child_process')

const path = require('path')

let springBoot

process.on('message', (message) => {
    if (message.msg === 'start') {
        const jarName = message.jarName
        springBoot = spawn(path.resolve('C:\\ERP_JC\\fulljre11\\bin\\java'),
            [
                '-Dlog4j.skipJansi=false',
                '-XX:TieredStopAtLevel=1',
                '-noverify',
                '-Dspring.jmx.enabled=false',
                '-jar',
                path.resolve('C:\\ERP_JC\\backend\\', jarName),
                '--server.address=0.0.0.0'
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
})

