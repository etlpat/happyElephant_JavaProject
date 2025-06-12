// 二次封装--针对业务逻辑的axios封装
import newAxios from "../util/AxiosUtils";

// 获取银行列表
export async function getBankList() {
  return (await newAxios.get("/bank/getBankList")).data;
}

// 获取银行数据
export async function getBank(id) {
  return (
    await newAxios.get("/bank/getBank", {
      params: { id }, // 请求参数
    })
  ).data;
}
