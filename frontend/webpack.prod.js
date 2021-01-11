const { merge } = require('webpack-merge')
const common = require('./webpack.common')

const { resolve } = require('path')
const publicPath = ''
const bundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin

module.exports = merge(common, {
    mode: 'production',
    output: {
        path: resolve(__dirname, 'dist'),
        filename: 'bundle.js',
        chunkFilename: "[name].bundle.js",
        publicPath: publicPath
    },
    plugins: [
        // eslint-disable-next-line new-cap
        new bundleAnalyzerPlugin(),
    ],
    optimization: {
        usedExports: true,
        runtimeChunk: "single",
        splitChunks: {
            chunks: 'all',
            cacheGroups: {
                vendor: {
                    test: /[\\/]node_modules[\\/]/,
                    name: 'vendors',
                    chunks: "all",
                    priority: 10
                },
                vuetify: {
                    name: 'vuetifyModules',
                    test: /[\\/]node_modules[\\/]vuetify[\\/]/,
                    priority: 20
                },
                commons: {
                    name: 'commonModules',
                    test: resolve('src/components'),
                    minChunks: 2,
                    priority: 5,
                    reuseExistingChunk: true
                },
                styles: {
                    name: 'styles',
                    test: /\.css$/,
                    chunks: "all",
                    enforce: true
                }
            }
        }
    }
})
