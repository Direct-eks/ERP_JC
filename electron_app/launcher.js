const { spawn } = require('child_process')
const http = require('http')


const springBoot = spawn("java", ['-jar', './target/backend-0.0.1-SNAPSHOT.jar']);

springBoot.stdout.on('data', (data) => {
    const line = `${data}`
    console.log(line.trimEnd())
    if (line.includes('Application started!')) {
        process.send('launched')
    }
})

springBoot.on('exit', (code) => {
    process.send(`${code}`)
})

// springBoot.on('close', (code) => {
//     process.send(`${code}`)
// })

process.on('message', (message) => {
    console.log('child process receive message', `${message}`)
    if (`${message}` === 'shutdown') {
        const options = {
            hostname: 'localhost',
            port: 8088,
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

