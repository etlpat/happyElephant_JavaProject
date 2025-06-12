import axios from "axios";
import qs from "qs";
import store from "@/store"; // 引入Vuex store

// 封装newAxios对象(一次封装，针对axion对象本身封装)
let newAxios = axios.create({
  baseURL: "/api", // 每个请求前缀添加"/api"
});

newAxios.defaults.headers.post["Content-Type"] =
  "application/x-www-form-urlencoded";

// 添加请求拦截器
newAxios.interceptors.request.use(
  // 拦截方法
  function (config) {
    // 从Vuex获取authorization
    const token = store.state.authorization;

    // (1)如果token存在，添加到请求头
    if (token) {
      config.headers.Authorization = token;
    }

    // (2)若为post请求，将数据序列化再发送到后端
    if (config.method == "post") {
      config.data = qs.stringify(config.data); // 将数据序列化
    }

    return config;
  },

  function (error) {
    return Promise.reject(error);
  }
);

// 添加响应拦截器
newAxios.interceptors.response.use(
  function (response) {
    return response;
  },
  function (error) {
    // 拦截401异常状态
    if (error.response.status === 401) {
      // 清除本地存储的 token 和用户信息
      localStorage.removeItem("authorization");
      localStorage.removeItem("thisUser");

      // 提示并跳转到登录页
      alert("登录已过期，请重新登录");
      window.location.href = "/"; // 或使用 Vue Router 跳转
    }
    return Promise.reject(error);
  }
);

// 默认暴露newAxios对象
export default newAxios;
