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

// 获取用户信息
export async function userInfo(username) {
  return (
    await newAxios.get("/user/userInfo", {
      params: { username }, // 请求参数
    })
  ).data;
}

// 修改细节信息
export async function updateDetail(user) {
  return (await newAxios.post("/user/updateDetail", user)).data;
}

// 更新用户密码（使用PATCH方法）
export async function updatePassword(pwdMap) {
  return (
    await newAxios.patch("/user/updatePassword", pwdMap, {
      headers: {
        "Content-Type": "application/json", // 明确指定JSON格式
      },
    })
  ).data;
}
