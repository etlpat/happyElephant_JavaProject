<!-- 根组件App -->
<template>
  <div id="app">
    <!-- 只显示组件 -->
    <router-view></router-view>
  </div>
</template>

<script>
export default {
};
</script>

<style lang="scss" scoped>
#app {
  width: 90%;
  height: 100%;
  margin: 0 auto;
}
</style>