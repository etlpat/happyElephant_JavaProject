<template>
    <div id="askAQuestion" class="container">
        <el-card shadow="hover" class="form-card">
            <div slot="header" class="clearfix">
                <h3>提问专家</h3>
            </div>

            <el-form ref="form" :model="form" label-width="100px" class="question-form" :rules="rules"
                @submit.native.prevent="onSubmit">
                <span class="error-message">
                    <br v-if="error === ''" />
                    <template v-else>{{ error }}</template>
                </span>

                <el-form-item label="问题标题" prop="title">
                    <el-input v-model="form.title" placeholder="请输入问题标题" clearable></el-input>
                </el-form-item>

                <el-form-item label="问题描述" prop="question">
                    <el-input v-model="form.question" type="textarea" :rows="5" placeholder="请详细描述您的问题"
                        resize="none"></el-input>
                </el-form-item>

                <el-form-item label="农作物名称" prop="plantName">
                    <el-input v-model="form.plantName" placeholder="请输入农作物名称" clearable></el-input>
                </el-form-item>

                <el-form-item label="联系方式" prop="phone">
                    <el-input v-model="form.phone" placeholder="请输入您的手机号" clearable></el-input>
                </el-form-item>

                <div class="form-actions">
                    <el-button type="primary" native-type="submit" :loading="submitting">提交问题</el-button>
                    <el-button @click="exit">取消</el-button>
                </div>
            </el-form>
        </el-card>
    </div>
</template>

<script>
import { save } from "../apis/QuestionService.js";

export default {
    data() {
        return {
            form: {
                title: "",
                question: "",
                plantName: "",
                phone: "",
            },
            error: '',
            expert: null,// 本页面对应的专家
            submitting: false,
            rules: {// 定义表的规则
                title: [
                    { required: true, message: '请输入问题标题', trigger: 'blur' },
                ],
                question: [
                    { required: true, message: '请输入问题描述', trigger: 'blur' },
                ],
                plantName: [
                    { required: true, message: '请输入农作物名称', trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '请输入联系方式', trigger: 'blur' },
                ]
            }
        }
    },
    methods: {
        async onSubmit() {
            try {
                this.submitting = true;
                await this.$refs.form.validate();

                const question = {// 创建question对象
                    expertName: this.expert.userName,
                    questioner: this.$store.state.loginUsername,// 当前登录用户
                    phone: this.form.phone,
                    plantName: this.form.plantName,
                    title: this.form.title,
                    question: this.form.question,
                    answer: "",
                    status: 0,
                };

                await save(question);// 将question对象插入数据库
                this.$message.success('问题提交成功！');
                this.$router.push("/menu/expertGuidance");// 返回专家指导界面
            } catch (error) {
                console.error('提交失败:', error);
                this.error = '提交失败，请检查表单后重试';
            } finally {
                this.submitting = false;
            }
        },

        exit() {
            this.$router.back();// 返回上一页
        }
    },
    mounted() {
        if (this.$route.params.expert) {
            this.expert = JSON.parse(this.$route.params.expert);// 接收传来的数据
            // 获取专家
        }
    }
}
</script>

<style lang="scss" scoped>
.container {
    padding: 20px;
    min-height: calc(100vh - 60px);
    display: flex;
    justify-content: center;
    align-items: flex-start;
}

.form-card {
    width: 100%;
    max-width: 800px;
    margin-top: 30px;
    border-radius: 4px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .clearfix {
        h3 {
            margin: 0;
            color: #303133;
            font-weight: 500;
        }
    }
}

.question-form {
    padding: 10px 20px;

    .el-form-item {
        margin-bottom: 22px;
    }

    .el-textarea {
        font-family: inherit;
    }
}

.form-actions {
    display: flex;
    justify-content: flex-end;
    margin-top: 30px;

    .el-button {
        padding: 12px 20px;
        border-radius: 4px;
    }
}

.error-message {
    color: #f56c6c;
    font-size: 14px;
    margin-bottom: 15px;
    display: block;
    text-align: center;
}

@media (max-width: 768px) {
    .container {
        padding: 10px;
    }

    .form-card {
        margin-top: 15px;
    }

    .question-form {
        padding: 10px;
    }
}
</style>