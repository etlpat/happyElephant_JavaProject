// 二次封装--针对业务逻辑的axios封装
import newAxios from "../util/AxiosUtils";

// 分页查询的请求
export async function getPageAll(pageNum, pageSize) {
  return (
    await newAxios.get("/expert/getPageAll/" + pageNum, {
      params: { pageSize }, // 请求参数
    })
  ).data;
}
