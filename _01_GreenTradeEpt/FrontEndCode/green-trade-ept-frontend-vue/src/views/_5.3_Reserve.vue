<template>
    <div id="reserve" class="container">
        <el-card shadow="hover" class="form-card">
            <div slot="header" class="clearfix">
                <h3>预约专家</h3>
            </div>

            <el-form ref="form" :model="form" label-width="120px" class="question-form" :rules="rules"
                @submit.native.prevent="onSubmit">
                <span class="error-message">
                    <br v-if="error === ''" />
                    <template v-else>{{ error }}</template>
                </span>

                <el-form-item label="地址" prop="address">
                    <el-input v-model="form.address" placeholder="请输入您的地址" clearable></el-input>
                </el-form-item>

                <el-form-item label="种植作物" prop="plantName">
                    <el-input v-model="form.plantName" placeholder="请输入种植的作物名称" clearable></el-input>
                </el-form-item>

                <el-form-item label="作物详细信息" prop="plantDetail">
                    <el-input v-model="form.plantDetail" type="textarea" :rows="3" placeholder="请输入作物的详细信息（如品种、生长周期等）"
                        resize="none"></el-input>
                </el-form-item>

                <el-form-item label="作物条件" prop="plantCondition">
                    <el-input v-model="form.plantCondition" placeholder="请输入作物当前生长条件（如温度、湿度等）" clearable></el-input>
                </el-form-item>

                <el-form-item label="土壤条件" prop="soilCondition">
                    <el-input v-model="form.soilCondition" placeholder="请输入土壤条件（如酸碱度、肥力等）" clearable></el-input>
                </el-form-item>

                <el-form-item label="面积（亩）" prop="area">
                    <el-input v-model="form.area" placeholder="请输入种植面积" clearable>
                        <template slot="append">亩</template>
                    </el-input>
                </el-form-item>

                <el-form-item label="电话" prop="phone">
                    <el-input v-model="form.phone" placeholder="请输入您的联系电话" clearable></el-input>
                </el-form-item>

                <el-form-item label="咨询者" prop="questioner">
                    <el-input v-model="form.questioner" placeholder="自动填充当前用户" disabled></el-input>
                </el-form-item>

                <div class="form-actions">
                    <el-button type="primary" native-type="submit" :loading="submitting">提交预约</el-button>
                    <el-button @click="exit">取消</el-button>
                </div>
            </el-form>
        </el-card>
    </div>
</template>

<script>
import { save } from "../apis/ReserveService.js"; // 修改为预约服务

export default {
    data() {
        return {
            form: {
                address: "",
                plantName: "",
                plantDetail: "",
                plantCondition: "",
                soilCondition: "",
                area: "",
                phone: "",
                questioner: ""
            },
            error: '',
            expert: null, // 本页面对应的专家
            submitting: false,
            rules: {
                address: [
                    { required: true, message: '请输入地址', trigger: 'blur' },
                ],
                plantName: [
                    { required: true, message: '请输入种植作物名称', trigger: 'blur' }
                ],
                plantDetail: [
                    { required: true, message: '请输入作物详细信息', trigger: 'blur' }
                ],
                phone: [
                    { required: true, message: '请输入联系电话', trigger: 'blur' },
                ],
                area: [
                    { required: true, message: '请输入种植面积', trigger: 'blur' },
                ]
            }
        }
    },
    methods: {
        async onSubmit() {
            try {
                this.submitting = true;
                await this.$refs.form.validate();

                // 创建reserve对象
                const reserve = {
                    expertName: this.expert.userName,
                    questioner: this.form.questioner || this.$store.state.loginUsername,
                    address: this.form.address,
                    plantName: this.form.plantName,
                    plantDetail: this.form.plantDetail,
                    plantCondition: this.form.plantCondition,
                    soilCondition: this.form.soilCondition,
                    area: this.form.area,
                    phone: this.form.phone,
                    status: 0,
                };

                await save(reserve);// 将信息交给数据库
                this.$message.success('预约提交成功！');
                this.$router.push("/menu/expertGuidance");
            } catch (error) {
                console.error('提交失败:', error);
                this.error = '提交失败，请检查表单后重试';
                if (error.response && error.response.data && error.response.data.message) {
                    this.error = error.response.data.message;
                }
            } finally {
                this.submitting = false;
            }
        },

        exit() {
            this.$router.back();
        }
    },
    mounted() {
        if (this.$route.params.expert) {
            this.expert = JSON.parse(this.$route.params.expert);
            this.form.questioner = this.$store.state.loginUsername;
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