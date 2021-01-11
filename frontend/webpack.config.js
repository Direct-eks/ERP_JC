const { resolve } = require('path')
// const webpack = require('webpack')
const VueLoaderPlugin = require('vue-loader/lib/plugin')
const VuetifyLoaderPlugin = require('vuetify-loader/lib/plugin')
const HtmlWebpackPlugin = require('html-webpack-plugin')
const url = require('url')
const MiniCssExtractPlugin = require('mini-css-extract-plugin')

const publicPath = ''

const bundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin

module.exports = (options = {}) => ({
    entry: {
        index: './src/main.js'
    },
    output: {
        path: resolve(__dirname, 'dist'),
        filename: options.dev ? '[name].js' : 'bundle.js',
        chunkFilename: "[name].bundle.js",
        publicPath: options.dev ? '/assets/' : publicPath
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                use: ['vue-loader']
            },
            {
                test: /\.js$/,
                exclude: /node_modules/,
                use: [
                    {
                        loader: 'babel-loader',
                        options: {
                            'babelrc': false,
                            presets: ["@babel/preset-env"],
                            plugins: ["@babel/plugin-syntax-dynamic-import"],
                        }
                    }
                ]
            },
            {
                test: /\.(sa|sc|c)ss$/,
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
            {
                test: /\.(png|jpg|jpeg|gif|eot|ttf|woff|woff2|svg|svgz)(\?.+)?$/,
                use: [{
                    loader: 'url-loader',
                    options: {
                        limit: 10000
                    }
                }]
            }
        ]
    },
    resolve: {
        alias: {
            '~': resolve(__dirname, 'src')
        },
        extensions: ['.js', '.vue', '.json', '.css']
    },
    devtool: options.dev ? 'eval-source-map' : 'source-map',
    devServer: {
        host: '127.0.0.1',
        port: 8080,
        proxy: {
            '/api': {
                target: 'http://localhost:8088',
                changeOrigin: true,
                pathRewrite: { '/api': '' }
            }
        },
        historyApiFallback: {
            index: url.parse(options.dev ? '/assets/' : publicPath).pathname
        },
        hot: true
    },
    plugins: [
        new VueLoaderPlugin(),
        new VuetifyLoaderPlugin(),
        new HtmlWebpackPlugin({
            template: 'src/assets/index.html',
            favicon: 'src/assets/favicon.png'
        }),
        // eslint-disable-next-line new-cap
        new bundleAnalyzerPlugin(),
        new MiniCssExtractPlugin({
            filename: '[name].[hash].css',
            chunkFilename: '[id].[hash].css',
            ignoreOrder: true,
        })
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
