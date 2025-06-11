// 二次封装--针对业务逻辑的axios封装
import newAxios from "../util/AxiosUtils";

// 插入对象
export async function save(reserve) {
  return (await newAxios.post("/reserve/save", reserve)).data;
}
