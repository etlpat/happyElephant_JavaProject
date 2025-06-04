import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import store from "./store";

// 引入ElementUI的组件库和全部样式
import ElementUI from "element-ui";
import "element-ui/lib/theme-chalk/index.css";

Vue.config.productionTip = false;

// 使用ElementUI插件
Vue.use(ElementUI);

new Vue({
  router,
  store,
  render: (h) => h(App),
}).$mount("#app");
