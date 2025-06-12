import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";
import DateUtils from "./util/DateUtils";
import newAxios from "./util/AxiosUtils"; // 引入封装的axios实例
import ElementUI from "element-ui"; // 引入ElementUI的组件库和全部样式
import "element-ui/lib/theme-chalk/index.css";

// 将axios实例挂载到Vue原型上，方便组件内使用
Vue.prototype.$http = newAxios;

// 注册到Vue原型
Vue.prototype.$dateUtils = DateUtils;

Vue.config.productionTip = false;

// 使用ElementUI插件
Vue.use(ElementUI);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
