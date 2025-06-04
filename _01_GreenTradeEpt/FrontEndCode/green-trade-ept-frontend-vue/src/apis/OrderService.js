// 二次封装--针对业务逻辑的axios封装
import newAxios from "../util/AxiosUtils";

// 分页查询的请求
export async function getPage(pageNum, pageSize, keyword) {
  const token = localStorage.getItem("Authorization"); // 获取token

  return (
    await newAxios.get("/order/getPage/" + pageNum, {
      params: { pageSize, keyword }, // 请求参数
      headers: {
        Authorization: token, // 请求头
      },
    })
  ).data;
}

// 获取全部评论
export async function getContent() {
  const token = localStorage.getItem("Authorization"); // 获取token
  return (
    await newAxios.get("/order/getContent", {
      headers: {
        Authorization: token, // 请求头
      },
    })
  ).data;
}
