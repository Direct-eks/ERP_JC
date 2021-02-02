const { merge } = require('webpack-merge')
const common = require('./webpack.base.config')

const { resolve } = require('path')
const publicPath = ''
const bundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin

const MiniCssExtractPlugin = require('mini-css-extract-plugin')
// const PurgeCSSPlugin = require('purgecss-webpack-plugin')
// const glob = require('glob-all')

module.exports = merge(common, {
    mode: 'production',
    output: {
        path: resolve(__dirname, 'dist'),
        filename: '[name].js',
        chunkFilename: "[name].bundle.js",
        publicPath: publicPath
    },
    module: {
        rules: [
            {
                test: /\.(sa|sc)ss$/,
                use: [
                    MiniCssExtractPlugin.loader,
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
    plugins: [
        new MiniCssExtractPlugin({
            filename: '[name].[fullhash].css',
            chunkFilename: '[id].[fullhash].css',
            ignoreOrder: true,
        }),
        // new PurgeCSSPlugin({
        //     paths: glob.sync([
        //         `${resolve(__dirname, 'src')}/**/*`,
        //         `${resolve(__dirname, 'node_modules/vuetify/src')}/**/*.js`,
        //     ])
        // }),
        // eslint-disable-next-line new-cap
        new bundleAnalyzerPlugin({
            analyzerPort: 58081
        }),
    ],
    optimization: {
        usedExports: true,
        runtimeChunk: "single",
        splitChunks: {
            chunks: 'all',
            cacheGroups: {
                defaultVendors: {
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
