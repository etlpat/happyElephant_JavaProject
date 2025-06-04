import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    // 存储当前登录的用户
    loginUsername: null,
    // 存储当前token
    authorization: "",
  },
  getters: {},
  mutations: {
    // 同步修改
    changeLoginUser(state, username) {
      state.loginUsername = username;
    },
    changeAuthorization(state, authorization) {
      state.authorization = authorization;
    },
  },
  actions: {}, // 异步操作
  modules: {},
});
