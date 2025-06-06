// 二次封装--针对业务逻辑的axios封装
import newAxios from "../util/AxiosUtils";

// 完成一次登录的请求-响应
export async function login(username, password) {
  // 向服务器发送axios的post请求，并接收登录成功后返回的result(token)信息
  let result = (await newAxios.post("/user/login", { username, password }))
    .data;
  return result;
}

// 注册
export async function register(username, password1, password2) {
  return (
    await newAxios.post("/user/register", { username, password1, password2 })
  ).data;
}
