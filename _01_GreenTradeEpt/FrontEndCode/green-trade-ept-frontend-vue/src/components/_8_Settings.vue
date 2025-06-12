<template>
    <div id="setting" class="setting-container">
        <div class="setting-header">
            <h2>个人资料设置</h2>
        </div>

        <!-- 主要内容区域，仅在用户数据加载后显示 -->
        <div class="setting-content" v-if="user">
            <!-- 头像上传区域 -->
            <div class="avatar-section">
                <div class="current-avatar">
                    <el-avatar :size="140" @error="errorHandler">
                        <!-- 默认头像，当自定义头像加载失败时显示 -->
                        <img src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" />
                    </el-avatar>
                    <p class="avatar-hint">当前头像</p>
                </div>

                <!-- 头像上传组件 -->
                <div class="avatar-upload">
                    <el-upload class="avatar-uploader" action="https://jsonplaceholder.typicode.com/posts/"
                        :show-file-list="false" :on-success="handleAvatarSuccess" :before-upload="beforeAvatarUpload">
                        <div class="upload-content">
                            <i class="el-icon-upload avatar-uploader-icon"></i>
                            <div class="upload-text">
                                <p>点击上传新头像</p>
                                <p class="upload-tip">支持JPG格式，大小不超过2MB</p>
                            </div>
                        </div>
                    </el-upload>
                </div>
            </div>

            <!-- 个人信息表单区域 -->
            <div class="form-section">
                <el-form ref="form" :model="user" label-width="110px" label-position="left">
                    <el-form-item label="昵称">
                        <el-input v-model="user.nickName" placeholder="请输入昵称" />
                    </el-form-item>
                    <el-form-item label="姓名">
                        <el-input v-model="user.realName" placeholder="请输入真实姓名" />
                    </el-form-item>
                    <el-form-item label="手机号">
                        <el-input v-model="user.phone" placeholder="请输入手机号码" />
                    </el-form-item>
                    <el-form-item label="身份证">
                        <el-input v-model="user.identityNum" placeholder="请输入身份证号码" />
                    </el-form-item>
                    <el-form-item label="地址">
                        <el-input v-model="user.address" placeholder="请输入详细地址" />
                    </el-form-item>
                </el-form>
                <div class="action-buttons">
                    <el-button type="primary" size="medium" class="save-btn" @click="updateUserDetail">
                        保存信息
                    </el-button>
                    <el-button type="info" size="medium" plain class="change-btn" @click="showPasswordDialog">
                        修改密码
                    </el-button>
                </div>
            </div>
        </div>

        <!-- 密码修改对话框 -->
        <el-dialog title="修改密码" :visible.sync="passwordDialogVisible" width="500px" :close-on-click-modal="false"
            @closed="resetPasswordForm">
            <el-form ref="passwordForm" :model="passwordForm" :rules="passwordRules" label-width="120px">
                <el-form-item label="原密码" prop="old_pwd">
                    <el-input v-model="passwordForm.old_pwd" type="password" placeholder="请输入原密码" show-password />
                </el-form-item>
                <el-form-item label="新密码" prop="new_pwd">
                    <el-input v-model="passwordForm.new_pwd" type="password" placeholder="请输入新密码" show-password />
                </el-form-item>
                <el-form-item label="确认新密码" prop="re_pwd">
                    <el-input v-model="passwordForm.re_pwd" type="password" placeholder="请再次输入新密码" show-password />
                </el-form-item>
            </el-form>

            <!-- 对话框底部按钮 -->
            <span slot="footer" class="dialog-footer">
                <el-button @click="passwordDialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="handleUpdatePassword">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { userInfo, updateDetail, updatePassword } from "../apis/UserService.js";

export default {
    data() {
        return {
            // 当前登录用户
            username: "",
            user: null,

            // 头像上传临时URL
            imageUrl: '',

            // 密码修改对话框显示状态
            passwordDialogVisible: false,

            // 密码表单数据
            passwordForm: {
                old_pwd: '',
                new_pwd: '',
                re_pwd: ''
            },

            // 密码表单验证规则
            passwordRules: {
                old_pwd: [
                    { required: true, message: '请输入原密码', trigger: 'blur' },
                ],
                new_pwd: [
                    { required: true, message: '请输入新密码', trigger: 'blur' },
                ],
                re_pwd: [
                    { required: true, message: '请再次输入新密码', trigger: 'blur' },
                ]
            }
        };
    },

    methods: {
        /**
         * 更新用户详细信息
         */
        async updateUserDetail() {
            try {
                // 显示确认对话框
                await this.$confirm('确认保存修改的用户信息吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                });

                // 构造要更新的用户数据对象
                const userToUpdate = {
                    userName: this.username,
                    nickName: this.user.nickName,
                    realName: this.user.realName,
                    phone: this.user.phone,
                    identityNum: this.user.identityNum,
                    address: this.user.address
                };

                // 调用API更新用户信息
                await updateDetail(userToUpdate);

                // 显示成功提示
                this.$message({
                    message: '用户信息保存成功',
                    type: 'success',
                    duration: 2000
                });
            } catch (error) {
                if (error !== 'cancel') {
                    this.$message.error('保存用户信息失败: ' + (error.message || '未知错误'));
                    console.error('保存用户信息出错:', error);
                }
            }
        },

        /**
         * 显示密码修改对话框
         */
        showPasswordDialog() {
            this.passwordDialogVisible = true;
        },

        /**
         * 重置密码表单
         */
        resetPasswordForm() {
            this.$refs.passwordForm.resetFields();
        },

        /**
         * 处理密码更新逻辑
         */
        async handleUpdatePassword() {
            try {
                // 验证表单
                await this.$refs.passwordForm.validate();

                // 调用API更新密码
                const result = await updatePassword({
                    old_pwd: this.passwordForm.old_pwd,
                    new_pwd: this.passwordForm.new_pwd,
                    re_pwd: this.passwordForm.re_pwd
                });

                // 根据返回结果处理
                if (result.code == 0) {
                    // 密码修改成功处理
                    this.$message({
                        message: '密码修改成功，请重新登录',
                        type: 'success',
                        duration: 2000
                    });
                    this.passwordDialogVisible = false;
                    // 2秒后跳转到登录页
                    setTimeout(() => {
                        this.$store.dispatch('logout');
                        location.reload();// 刷新当前页面
                    }, 2000);
                } else {
                    // 密码修改失败处理
                    this.$message.error(result.message || '密码修改失败');
                }
            } catch (error) {
                // 如果不是用户取消操作，则记录错误
                if (error !== 'cancel') {
                    console.error('修改密码出错:', error);
                }
            }
        },

        /**
         * 头像上传成功处理
         */
        handleAvatarSuccess(res, file) {
            this.imageUrl = URL.createObjectURL(file.raw);
        },

        /**
         * 头像上传前验证
         */
        beforeAvatarUpload(file) {
            const isJPG = file.type === 'image/jpeg';
            const isLt2M = file.size / 1024 / 1024 < 2;

            if (!isJPG) {
                this.$message.error('上传头像图片只能是 JPG 格式!');
            }
            if (!isLt2M) {
                this.$message.error('上传头像图片大小不能超过 2MB!');
            }
            return isJPG && isLt2M;
        },

        /**
         * 头像加载失败处理
         */
        errorHandler() {
            return true; // 返回true表示使用默认头像
        }
    },

    async mounted() {
        // 组件挂载时获取当前用户信息
        this.username = this.$store.state.loginUsername;
        this.user = (await userInfo(this.username)).data;
    }
}
</script>

<style lang="scss" scoped>
.setting-container {
    max-width: 1000px;
    margin: 0 auto;
    padding: 30px;
    background: #fff;
    border-radius: 12px;
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.08);

    .setting-header {
        margin-bottom: 30px;

        h2 {
            color: #303133;
            font-size: 22px;
            font-weight: 600;
            border-bottom: 1px solid #ebeef5;
            padding-bottom: 16px;
            margin: 0;
        }
    }

    .setting-content {
        display: flex;
        flex-direction: column;
        gap: 30px;

        @media (min-width: 768px) {
            flex-direction: row;
            align-items: flex-start;
            gap: 40px;
        }
    }

    .avatar-section {
        display: flex;
        flex-direction: column;
        align-items: center;
        gap: 24px;
        padding: 10px 20px 20px 0;
        flex-shrink: 0;

        @media (min-width: 768px) {
            width: 240px;
            border-right: 1px solid #f0f2f5;
        }

        .current-avatar {
            display: flex;
            flex-direction: column;
            align-items: center;
            gap: 16px;

            .avatar-hint {
                color: #909399;
                font-size: 14px;
                font-weight: 500;
            }
        }

        .avatar-upload {
            .avatar-uploader {
                display: block;
                width: 160px;
                height: 150px;
                border: 2px dashed #e1e4e8;
                border-radius: 10px;
                transition: all 0.3s ease;
                background-color: #fafbfc;

                &:hover {
                    border-color: #409EFF;
                    transform: translateY(-2px);
                    box-shadow: 0 6px 16px rgba(32, 160, 255, 0.12);
                }

                .upload-content {
                    display: flex;
                    flex-direction: column;
                    justify-content: center;
                    align-items: center;
                    height: 100%;
                    color: #8c939d;
                    padding: 20px;

                    .avatar-uploader-icon {
                        font-size: 28px;
                        margin-bottom: 12px;
                        color: #c0c4cc;
                    }

                    .upload-text {
                        text-align: center;

                        p {
                            margin: 0;
                            line-height: 1.6;
                            font-size: 14px;
                        }

                        .upload-tip {
                            font-size: 12px;
                            color: #c0c4cc;
                            margin-top: 4px;
                        }
                    }
                }
            }
        }
    }

    .form-section {
        flex: 1;
        margin-top: 10px;
        padding: 0 10px;
        display: flex;
        flex-direction: column;
        min-height: 100%;

        @media (min-width: 768px) {
            padding-left: 30px;
        }

        .el-form {
            flex: 1;

            .el-form-item {
                margin-bottom: 24px;

                &:last-child {
                    margin-bottom: 40px;
                }

                .el-form-item__label {
                    font-weight: 500;
                    color: #606266;
                    padding-right: 15px;
                    font-size: 15px;
                }
            }

            .el-input {
                width: 100%;
                max-width: 420px;

                &:hover .el-input__inner {
                    border-color: #c0c4cc;
                }

                .el-input__inner {
                    height: 42px;
                    line-height: 42px;
                    border-radius: 6px;
                    transition: all 0.3s;
                    font-size: 15px;
                }
            }
        }

        .action-buttons {
            display: flex;
            justify-content: space-between;
            margin-top: auto;
            padding: 0 0 10px;
            max-width: 420px;

            .save-btn {
                margin-right: auto;
            }

            .change-btn {
                margin-left: auto;
                margin-right: 0;
            }

            .el-button {
                padding: 12px 28px;
                font-weight: 500;
                letter-spacing: 0.5px;
                font-size: 15px;
                border-radius: 4px;
            }
        }
    }
}
</style>