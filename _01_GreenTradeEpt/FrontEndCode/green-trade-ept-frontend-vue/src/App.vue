<!-- 根组件App -->
<template>
  <div id="app">
    <!-- 只显示组件 -->
    <router-view></router-view>
  </div>
</template>

<script>
export default {
  mounted() {
    // 监听页面关闭/刷新
    window.addEventListener('beforeunload', () => {
      localStorage.removeItem('Authorization');// 每次关闭页面，删除token令牌（暂时替代退登功能）
    });
  },
  // 可选：移除监听（避免内存泄漏）
  beforeUnmount() {
    window.removeEventListener('beforeunload', () => { });
  }
};
</script>

<style lang="scss" scoped>
#app {
  width: 90%;
  height: 100%;
  margin: 0 auto;
}
</style>