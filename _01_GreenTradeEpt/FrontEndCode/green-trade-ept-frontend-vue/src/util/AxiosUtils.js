import axios from "axios";
import qs from "qs";

// 封装newAxios对象(一次封装，针对axion对象本身封装)
let newAxios = axios.create({
  baseURL: "/api", // 每个请求前缀添加"/api"
});

// newAxios.defaults.headers.common["Authorization"] = AUTH_TOKEN;
newAxios.defaults.headers.post["Content-Type"] =
  "application/x-www-form-urlencoded";

// 添加请求拦截器
newAxios.interceptors.request.use(
  function (config) {
    // 在发送请求之前做些什么
    if (config.method == "post") config.data = qs.stringify(config.data); // 将数据序列化
    return config;
  },
  function (error) {
    // 对请求错误做些什么
    return Promise.reject(error);
  }
);

// 默认暴露newAxios对象
export default newAxios;
