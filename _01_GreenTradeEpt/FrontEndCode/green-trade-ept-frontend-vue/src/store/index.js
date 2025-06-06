import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    // 存储当前登录的用户（初始化时从localStorage读取数据）
    loginUsername: localStorage.getItem("thisUser") || null,
    // 存储当前token（初始化时从localStorage读取数据）
    authorization: localStorage.getItem("authorization") || "",
  },
  getters: {},
  mutations: {
    // 同步修改
    changeLoginUser(state, username) {
      state.loginUsername = username;
      // 同时存入localStorage
      if (username) {
        localStorage.setItem("thisUser", username);
      } else {
        localStorage.removeItem("thisUser");
      }
    },
    changeAuthorization(state, authorization) {
      state.authorization = authorization;
      // 同时存入localStorage
      if (authorization) {
        localStorage.setItem("authorization", authorization);
      } else {
        localStorage.removeItem("authorization");
      }
    },
  },
  actions: {
    // 用于清除登录状态（退出登录）
    logout({ commit }) {
      commit("changeLoginUser", null);
      commit("changeAuthorization", "");
    },
  },
  modules: {},
});
