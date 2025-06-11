import Vue from "vue";
import VueRouter from "vue-router";
import store from "@/store";
// import HomeView from "../views/HomeView.vue";
// import { component } from "vue/types/umd";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    // 重定向到menu菜单
    redirect: "/menu",
  },
  {
    // 一级路由
    // 菜单menu，一切的入口
    path: "/menu",
    name: "menu",
    component: () => import("../components/Menu.vue"),
    children: [
      // 二级路由
      // 用于打开menu菜单页面的二级菜单main的内容（8个二级路由对应8个模块视图的内容）
      // 注意：二级路由的路径path，应该改为相对路径（去掉开头的 /）
      {
        path: "home",
        name: "home",
        component: () => import("../views/_1_HomeView.vue"),
      },
      {
        path: "goodInfo",
        name: "goodInfo",
        component: () => import("../views/_1.1_GoodInfo.vue"),
      },
      {
        path: "commoditySource",
        name: "commoditySource",
        component: () => import("../views/_2_CommoditySourceView.vue"),
      },
      {
        path: "commodityInfo",
        name: "commodityInfo",
        component: () => import("../views/_2.1_CommodityInfo.vue"),
      },
      {
        path: "purchaseDemand",
        name: "purchaseDemand",
        component: () => import("../views/_3_PurchaseDemandView.vue"),
      },
      {
        path: "agriculturalKnowledge",
        name: "agriculturalKnowledge",
        component: () => import("../views/_4_AgriculturalKnowledgeView.vue"),
      },
      {
        path: "knowledgeInfo",
        name: "knowledgeInfo",
        component: () => import("../views/_4.1_KnowledgeInfo.vue"),
      },
      {
        path: "expertGuidance",
        name: "expertGuidance",
        component: () => import("../views/_5_ExpertGuidanceView.vue"),
      },
      {
        path: "moreExpert",
        name: "moreExpert",
        component: () => import("../views/_5.1_MoreExpert.vue"),
      },
      {
        path: "askAQuestion",
        name: "askAQuestion",
        component: () => import("../views/_5.2_AskAQuestion.vue"),
      },
      {
        path: "reserve",
        name: "reserve",
        component: () => import("../views/_5.3_Reserve.vue"),
      },
      {
        path: "shoppingCart",
        name: "shoppingCart",
        component: () => import("../views/_6_ShoppingCartView.vue"),
        // 路由守卫
        beforeEnter: (to, from, next) => {
          // 判断用户是否登录，若登录才能打开购物车模块
          if (!store.state.authorization) {
            alert("只有登录用户才能查看购物车，请先登录！");
          } else {
            next(); // 放行
          }
        },
      },
      {
        path: "financingApplication",
        name: "financingApplication",
        component: () => import("../views/_7_FinancingApplicationView.vue"),
      },
      {
        path: "settings",
        name: "settings",
        component: () => import("../views/_8_SettingsView.vue"),
      },
    ],
  },
  {
    path: "/login",
    name: "login",
    component: () => import("../views/Login.vue"),
  },
  {
    path: "/register",
    name: "register",
    component: () => import("../views/Register.vue"),
  },
];

const router = new VueRouter({
  routes,
});

// 重写push方法避免重复导航错误
const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => {
    if (err.name !== "NavigationDuplicated") throw err;
  });
};

export default router;
