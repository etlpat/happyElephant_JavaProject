// 二次封装--针对业务逻辑的axios封装
import newAxios from "../util/AxiosUtils";

// 根据knowledgeId获取评论列表
export async function getAllByKnowledgeId(knowledgeId) {
  return (
    await newAxios.get("/discuss/getAllByKnowledgeId", {
      params: { knowledgeId }, // 请求参数
    })
  ).data;
}

// 插入评论对象
export async function save(discuss) {
  return (await newAxios.post("/discuss/save", discuss)).data;
}
