const { merge } = require('webpack-merge')
const common = require('./webpack.base.config')

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
    module: {
        rules: [
            {
                test: /\.(sa|sc)ss$/,
                use: [
                    'style-loader',
                    {
                        loader: 'css-loader',
                        options: {
                            esModule: false
                        }
                    },
                    {
                        loader: 'sass-loader',
                        // Requires sass-loader@^8.0.0
                        options: {
                            // eslint-disable-next-line global-require
                            implementation: require('sass'),
                            sassOptions: {
                                // eslint-disable-next-line global-require
                                fiber: require('fibers'),
                                indentedSyntax: true // optional
                            },
                        },
                    },
                ],
            },
        ]
    },
    devtool: 'eval-source-map',
    target: 'web',
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
