// 二次封装--针对业务逻辑的axios封装
import newAxios from "../util/AxiosUtils";

// 分页查询的请求
export async function getPage(pageNum, pageSize, keyword) {
  return (
    await newAxios.get("/knowledge/getPage/" + pageNum, {
      params: { pageSize, keyword }, // 请求参数
    })
  ).data;
}
