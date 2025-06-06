<template>
    <div id="login">
        <span>
            <h2>用户登录</h2>
        </span>
        <div>
            <el-form ref="form" :model="form" label-width="50px">
                <span class="error">
                    <!-- 若error为空，则换行，否则显示error并换行 -->
                    <br v-if="error === ''" />
                    <template v-else>{{ error }}</template>
                </span>
                <el-form-item label="账户">
                    <el-input v-model="form.username" ref="usernameInput"></el-input>
                </el-form-item>
                <el-form-item label="密码">
                    <el-input v-model="form.password" ref="passwordInput"></el-input>
                </el-form-item>
                <el-form-item label="存密">
                    <el-switch v-model="form.remember"></el-switch>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit">登录</el-button>
                    <el-button @click="exit">取消</el-button>
                </el-form-item>
            </el-form>
        </div>
        <span class="reg">没有账号？立即注册！</span>
    </div>
</template>

<script>
// 引入login
import { login } from '../apis/UserService';

export default {
    data() {
        return {
            form: {
                username: '',
                password: '',
                remember: false,
            },
            error: '',
        }
    },
    methods: {
        async onSubmit() {
            // 调用login方法，接收返回的result对象
            const result = await login(this.form.username, this.form.password);
            // 若状态码不为0 -- 出现异常
            if (result.code != 0) {
                this.error = result.message;// 显示错误信息

            } else {// 登录成功
                this.error = '';
                let token = result.data;// 获取token(JWT令牌)

                // 将token和登录用户存于vuex中（会同时存到localStorage中）
                this.$store.commit("changeLoginUser", this.form.username);
                this.$store.commit("changeAuthorization", token);


                // 是否记忆账户信息（存于localStorage）
                if (this.form.remember) {// 若remember为true
                    // 保存用户名、密码
                    localStorage.setItem("loginUsername", this.form.username);
                    localStorage.setItem("loginPassword", this.form.password);
                } else {
                    localStorage.removeItem("loginUsername");
                    localStorage.removeItem("loginPassword");
                }

                this.$router.push("/menu");// 跳转到首页
            }
        },

        exit() {// 退出登录页面
            this.$router.push("/menu");// 跳转到首页
        }
    },
    mounted() {// 钩子函数
        // 记忆之前的用户名、密码
        if (localStorage.getItem("loginUsername") != null) {
            this.form.username = localStorage.getItem("loginUsername");
            this.form.password = localStorage.getItem("loginPassword");
        }
    }
}
</script>

<style lang="scss" scoped>
#login {
    background-image: url(../assets/imgs/login.png);
    width: 100%;
    height: 820px;
    background-size: cover;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    >span:nth-child(1) {
        font-size: 20px;
        color: white;
    }

    >div {
        // 第一层div
        width: 300px;
        height: 300px;
        background-color: white;
        border-radius: 10px;
        font-size: 12px;
        font-weight: 500;
        padding: 20px;

        .error {
            color: red;
            display: flex;
            flex-direction: column;
            align-items: center;
            padding-bottom: 10px;
            font-size: 13px;
        }
    }

    .reg {
        display: inline-block;
        width: 335px;
        height: 30px;
        font-size: 13px;
        color: white;
        border: 1px solid #f1f3f5;
        text-align: center;
        line-height: 30px;
        margin-top: 10px;
        border-radius: 8px;
        background: rgba(255, 255, 255, 0.28);
    }
}
</style>