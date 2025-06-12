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
                <div id="user-area">
                    <!-- 用户欢迎信息 -->
                    <el-tag v-if="$store.state.authorization" class="user-welcome" type="info">
                        <i class="el-icon-user"></i>
                        <span class="username">{{ $store.state.loginUsername }}</span>
                    </el-tag>
                    <!-- 登录注册按钮 -->
                    <el-button-group class="auth-btns">
                        <el-button type="primary" size="small" @click="login" plain>登录</el-button>
                        <el-button type="primary" size="small" @click="register" plain>注册</el-button>
                    </el-button-group>
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
                    <el-menu-item index="7" class="item">融资申请</el-menu-item>
                    <el-submenu index="8" v-if="setIsVisible" class="item">
                        <template slot="title">设置</template>
                        <el-menu-item index="8-1">个人中心</el-menu-item>
                        <el-menu-item index="8-2">关于我们</el-menu-item>
                        <el-menu-item index="8-3">退出登录</el-menu-item>
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
            setIsVisible: false
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
            } else if (key == "8-1") {
                this.$router.push('/menu/settings');
            } else if (key == "8-2") {
                this.$router.push('/menu/aboutUs');
            } else if (key == "8-3") {
                this.$store.dispatch('logout');// 退出登录
                location.reload();// 刷新当前页面
            }
        },

        login() {
            this.$router.push("/login")// 跳转到login路径
        },

        register() {
            this.$router.push("/register")// 跳转到register路径
        }
    },
    mounted() {// 钩子函数
        // 若token存在vuex中，设置setIsVisible为true（登录后才可见）
        if (this.$store.state.authorization) {
            this.setIsVisible = true;
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
    border-bottom: 1px solid #f0f0f0;
}

// 左侧logo信息模块
#info {
    width: 300px;
    height: 50px;
    display: flex;

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

        span:nth-child(1) {
            font-size: 20px;
            font-weight: 600;
        }

        span:nth-child(2) {
            font-size: 10px;
            color: rgb(59, 56, 56);
        }
    }
}

// 用户区域样式
#user-area {
    display: flex;
    align-items: center;
    gap: 12px;

    .user-welcome {
        display: inline-flex;
        align-items: center;
        height: 32px;
        padding: 0 12px;
        border-radius: 16px;
        background-color: #f5f7fa;
        border-color: #e4e7ed;

        .el-icon-user {
            margin-right: 6px;
            color: #909399;
        }

        .username {
            color: #606266;
            font-weight: 500;
            font-size: 13px;
        }
    }

    .auth-btns {
        .el-button {
            padding: 7px 15px;
            font-size: 12px;
            border-radius: 4px;

            &+.el-button {
                margin-left: 8px;
            }
        }
    }
}
</style>