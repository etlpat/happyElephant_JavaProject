// 前端的全局配置文件
const { defineConfig } = require("@vue/cli-service");
module.exports = defineConfig({
  transpileDependencies: true,

  // 配置代理服务器
  devServer: {
    proxy: {
      // 代理服务器
      "/api": {
        // 请求前缀
        target: "http://localhost:8888", // 请求提交的地址
        pathRewrite: {
          "^/api": "http://localhost:8888", // 将前缀转为对应地址
        },
      },
    },
  },
});
