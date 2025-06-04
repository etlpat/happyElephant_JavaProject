<template>
    <div id="menu">
        <!-- 引入布局 -->
        <el-container>
            <!-- 1.顶部logo模块 -->
            <div id="logo">
                <div id="info">
                    <img src="../assets/logo.png" alt="">
                    <div>
                        <span>融销通</span>
                        <span>农产品融销一体平台</span>
                    </div>
                </div>
                <div>
                    <!-- 为登录/注册绑定事件 -->
                    <a @click="login()">登录</a> |
                    <a>注册</a>
                </div>
            </div>
            <!-- 2.顶部菜单模块 -->
            <el-header>
                <el-menu :default-active="activeIndex" class="el-menu-demo" mode="horizontal" @select="handleSelect">
                    <el-menu-item index="1" class="item">首页</el-menu-item>
                    <el-menu-item index="2" class="item">商品货源</el-menu-item>
                    <el-menu-item index="3" class="item">求购需求</el-menu-item>
                    <el-menu-item index="4" class="item">农业知识</el-menu-item>
                    <el-menu-item index="5" class="item">专家指导</el-menu-item>
                    <el-menu-item index="6" class="item">购物车</el-menu-item>
                    <el-submenu index="7" class="item">
                        <template slot="title">融资申请</template>
                        <el-menu-item index="7-1">选项1</el-menu-item>
                        <el-menu-item index="7-2">选项2</el-menu-item>
                        <el-menu-item index="7-3">选项3</el-menu-item>
                    </el-submenu>
                    <el-submenu index="8" v-if="setIsVisible" class="item">
                        <template slot="title">设置</template>
                        <el-menu-item index="8-1">选项1</el-menu-item>
                        <el-menu-item index="8-2">选项2</el-menu-item>
                        <el-menu-item index="8-3">选项3</el-menu-item>
                    </el-submenu>
                </el-menu>
            </el-header>

            <!-- 3.中间内容模块 -->
            <el-main>
                <router-view></router-view>
            </el-main>
        </el-container>
    </div>
</template>


<script>
export default {
    data() {
        return {
            activeIndex: '1',
            setIsVisible: false// 是否显示设置栏
        };
    },
    methods: {

        // 菜单的点击事件，用于路由跳转不同的模块
        handleSelect(key, keyPath) {// 参数：key--模块index；keyPath--模块选项index
            // 当点击菜单项，下方的main部分通过路由，显示对应模块的组件
            if (key == 1) {// 若key为1
                this.$router.push('/menu/home');// 通过路由器开启对应模块的组件
            } else if (key == 2) {
                this.$router.push('/menu/commoditySource');
            } else if (key == 3) {
                this.$router.push('/menu/purchaseDemand');
            } else if (key == 4) {
                this.$router.push('/menu/agriculturalKnowledge');
            } else if (key == 5) {
                this.$router.push('/menu/expertGuidance');
            } else if (key == 6) {
                this.$router.push('/menu/shoppingCart');
            } else if (key == 7) {
                this.$router.push('/menu/financingApplication');
            } else if (key == 8) {
                this.$router.push('/menu/settings');
            }
        },

        login() {
            // 跳转到login路径
            this.$router.push("/login")
        }
    }, mounted() {// 钩子函数
        // 获取route传来的参数
        let siv = this.$route.query.setIsVisible;
        if (this.setIsVisible != null) {
            this.setIsVisible = siv;
        }

        // 默认开启首页内容
        this.$router.push('/menu/home');
    }
}
</script>


<style lang="scss" scoped>
.el-header {
    background-color: white;
    color: #333;
    // text-align: center;
    display: flex;
    justify-content: center;
    line-height: 50px;

    .el-menu {
        margin-left: 3%;
        width: 100%;
    }

    .item {
        width: 12%;
        text-align: center;
    }
}

.el-main {
    background-color: white;
    color: #333;
    text-align: center;
    // line-height: 600px;
}

body>.el-container {
    margin-bottom: 40px;
}

body {
    padding: 0;
    margin: 0;
}

// 顶部logo以及登录模块
#logo {
    background-color: white;
    color: black;
    height: 60px;
    display: flex;
    justify-content: space-between;
    padding-left: 30px;
    padding-right: 30px;
    align-items: center;
}

// 左侧logo信息模块
#info {
    width: 300px;
    height: 50px;
    display: flex;

    // logo图片
    img {
        width: 50px;
        height: 50px;
        border-radius: 50% 50%;
        margin-right: 5px;
    }

    div {
        display: flex;
        flex-direction: column;
        justify-content: space-between;
        font-size: 12px;
        height: 50px;
        padding-top: 1px;
        padding-bottom: 5px;
        box-sizing: border-box;

        // 设置内部第1个span
        span:nth-child(1) {
            font-size: 20px;
            font-weight: 600;
        }

        // 设置内部第2个span
        span:nth-child(2) {
            font-size: 10px;
            color: rgb(59, 56, 56);
        }
    }
}
</style>