const { merge } = require('webpack-merge')
const common = require('./webpack.common')

const { resolve } = require('path')
const url = require('url')

module.exports = merge(common, {
    mode: 'development',
    output: {
        path: resolve(__dirname, 'dist'),
        filename: '[name].js',
        chunkFilename: "[name].bundle.js",
        publicPath: '/assets/'
    },
    devtool: 'inline-source-map',
    devServer: {
        host: '127.0.0.1',
        port: 58888,
        proxy: {
            '/api': {
                target: 'http://localhost:58080',
                changeOrigin: true,
                pathRewrite: { '/api': '' }
            }
        },
        historyApiFallback: {
            index: url.parse('/assets/').pathname
        },
        hot: true,
        inline: true,
    },
})
