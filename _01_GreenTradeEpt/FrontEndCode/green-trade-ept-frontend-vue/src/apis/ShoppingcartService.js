// 二次封装--针对业务逻辑的axios封装
import newAxios from "../util/AxiosUtils";

// 分页查询购物车项（关联对应的订单对象）
export async function getPage(pageNum, pageSize, ownName) {
  return (
    await newAxios.get("/shoppingcart/getPage/" + pageNum, {
      params: { pageSize, ownName }, // 请求参数
    })
  ).data;
}

// 添加购物车项
export async function save(shoppingcart) {
  return (await newAxios.post("/shoppingcart/save", shoppingcart)).data;
}

// 删除购物车项
export async function remove(id) {
  return (
    await newAxios.get("/shoppingcart/remove", {
      params: { id }, // 请求参数
    })
  ).data;
}

// 更新商品数量（使得原本的count值加上changeCount）
export async function updateCount(changeCount, id) {
  return (
    await newAxios.get("/shoppingcart/updateCount", {
      params: { changeCount, id }, // 请求参数
    })
  ).data;
}
