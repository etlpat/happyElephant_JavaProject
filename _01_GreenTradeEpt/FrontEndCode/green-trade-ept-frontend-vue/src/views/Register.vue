<template>
    <div id="register">
        <span>
            <h2>用户注册</h2>
        </span>
        <div>
            <el-form ref="form" :model="form" label-width="80px">
                <span class="error">
                    <!-- 若error为空，则换行，否则显示error并换行 -->
                    <br v-if="error === ''" />
                    <template v-else>{{ error }}</template>
                </span>
                <el-form-item label="注册账户">
                    <el-input v-model="form.username" ref="usernameInput"></el-input>
                </el-form-item>
                <el-form-item label="注册密码">
                    <el-input v-model="form.password1" ref="password1Input"></el-input>
                </el-form-item>
                <el-form-item label="确认密码">
                    <el-input v-model="form.password2" ref="password2Input"></el-input>
                </el-form-item>
                <el-form-item label="保存信息">
                    <el-switch v-model="form.remember"></el-switch>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="onSubmit">注册</el-button>
                    <el-button @click="exit">取消</el-button>
                </el-form-item>
            </el-form>
        </div>
        <span class="reg">已经注册？立即登录！</span>
    </div>
</template>

<script>
import { register } from '../apis/UserService';

export default {
    data() {
        return {
            form: {
                username: '',
                password1: '',
                password2: '',
                remember: false,
            },
            error: '',
        }
    },
    methods: {
        async onSubmit() {
            // 调用注册请求
            const result = await register(this.form.username, this.form.password1, this.form.password2);
            // 若状态码不为0 -- 出现异常
            if (result.code != 0) {
                this.error = result.message;// 显示错误信息

            } else {// 注册成功
                this.error = '';

                // 是否记忆账户信息
                if (this.form.remember) {
                    localStorage.setItem("loginUsername", this.form.username);
                    localStorage.setItem("loginPassword", this.form.password1);
                }

                this.$router.push("/menu");// 跳转到首页
            }
        },

        exit() {// 退出登录页面
            this.$router.push("/menu");// 跳转到首页
        }
    },
}
</script>

<style lang="scss" scoped>
#register {
    position: relative; // 父容器设为相对定位
    width: 100%;
    height: 820px;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    // 添加伪元素作为背景层
    &::before {
        content: "";
        position: absolute;
        top: 0;
        left: 0;
        width: 100%;
        height: 100%;
        background-image: url(../assets/imgs/login.png);
        background-size: cover;
        opacity: 0.88; // 背景图透明度 80%
        z-index: -1; // 确保背景在内容下方
    }

    >span:nth-child(1) {
        font-size: 20px;
        color: white;
    }

    >div {
        // 第一层div
        width: 350px;
        height: 360px;
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