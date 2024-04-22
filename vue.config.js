module.exports = {
    // baseUrl: './',
    publicPath:'./',
    assetsDir: 'static',
    productionSourceMap: false,
    // devServer: {
    //     proxy: {
    //         '/api':{
    //             target:'http://jsonplaceholder.typicode.com',
    //             changeOrigin:true,
    //             pathRewrite:{
    //                 '/api':''
    //             }
    //         }
    //     }
    // }
    devServer: {
        disableHostCheck: true,
        open: true,  // 自动打开浏览器
        hot: true, // 实时打包编译
        host: '0.0.0.0',
        inline: true,  // 表示实时刷新浏览器
        port: '80',   // 指定打开浏览器的端口号
    }
}
