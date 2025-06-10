// 二次封装--针对业务逻辑的axios封装
import newAxios from "../util/AxiosUtils";

// 分页查询的请求
export async function getPage(pageNum, pageSize, keyword) {
  return (
    await newAxios.get("/order/getPage/" + pageNum, {
      params: { pageSize, keyword }, // 请求参数
    })
  ).data;
}

// 根据关键词和类型，获取分页数据
export async function getPageByType(pageNum, pageSize, keyword, type) {
  return (
    await newAxios.get("/order/getPageByType/" + pageNum, {
      params: { pageSize, keyword, type }, // 请求参数
    })
  ).data;
}

// 获取全部评论
export async function getContent() {
  return (await newAxios.get("/order/getContent")).data;
}
