<template>
    <div class="bank-page-container">
        <!-- 主页面内容 -->
        <div class="bank-container">
            <h1 class="title">银行服务一览</h1>
            <div class="bank-grid">
                <!-- 循环遍历银行列表，生成银行卡片 -->
                <div v-for="item in bankList" :key="item.bankId" class="bank-card" @click="showBankDetails(item)">
                    <div class="bank-logo">
                        <!-- 银行Logo图片 -->
                        <img :src="'bankImgs/' + item.bankName + '.png'" :alt="item.bankName + ' logo'"
                            class="logo-image">
                    </div>
                    <div class="bank-name">{{ item.bankName }}</div>
                </div>
            </div>
        </div>

        <!-- 银行详情对话框 -->
        <!-- 使用el-dialog组件显示银行详情 -->
        <el-dialog :title="selectedBank ? selectedBank.bankName : '银行详情'" :visible.sync="bankDialogVisible"
            width="600px" :close-on-click-modal="true" class="bank-detail-dialog" @closed="selectedBank = null">
            <!-- 当有选中的银行时，显示银行详情内容 -->
            <div v-if="selectedBank" class="bank-detail-content">
                <div class="bank-header">
                    <!-- 银行详情页的Logo -->
                    <img :src="'bankImgs/' + selectedBank.bankName + '.png'" :alt="selectedBank.bankName + ' logo'"
                        class="detail-logo">
                </div>

                <!-- 分割线 -->
                <el-divider></el-divider>

                <!-- 银行详细信息部分 -->
                <div class="detail-section">
                    <!-- 银行介绍 -->
                    <div class="detail-item">
                        <span class="detail-label"><i class="el-icon-info"></i> 银行介绍</span>
                        <p class="detail-text">{{ selectedBank.introduce || '暂无介绍' }}</p>
                    </div>

                    <!-- 使用网格布局显示多个银行详细信息 -->
                    <div class="detail-grid">
                        <!-- 联系电话 -->
                        <div class="detail-item">
                            <span class="detail-label"><i class="el-icon-phone"></i> 联系电话</span>
                            <p class="detail-text">{{ selectedBank.bankPhone || '暂无电话' }}</p>
                        </div>

                        <!-- 存款金额 -->
                        <div class="detail-item">
                            <span class="detail-label"><i class="el-icon-money"></i> 存款金额</span>
                            <p class="detail-text">¥{{ selectedBank.money ? selectedBank.money.toFixed(2) : '0.00' }}
                            </p>
                        </div>

                        <!-- 利率 -->
                        <div class="detail-item">
                            <span class="detail-label"><i class="el-icon-trend-charts"></i> 利率</span>
                            <p class="detail-text rate-value">{{ formatRate(selectedBank.rate) }}</p>
                        </div>

                        <!-- 还款期限 -->
                        <div class="detail-item">
                            <span class="detail-label"><i class="el-icon-date"></i> 还款期限</span>
                            <p class="detail-text">{{ selectedBank.repayment || '0' }}个月</p>
                        </div>
                    </div>
                </div>
            </div>

            <!-- 对话框底部 -->
            <span slot="footer" class="dialog-footer">
                <!-- 关闭按钮 -->
                <el-button @click="bankDialogVisible = false">关 闭</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { getBankList } from "../apis/BankService.js";

export default {
    data() {
        return {
            bankList: [], // 存储银行列表数据
            bankDialogVisible: false, // 控制银行详情对话框的显示与隐藏
            selectedBank: null // 存储当前选中的银行信息
        }
    },
    methods: {
        /**
         * 显示银行详情
         * @param {Object} bank - 选中的银行对象
         */
        showBankDetails(bank) {
            this.selectedBank = bank; // 设置当前选中的银行
            this.bankDialogVisible = true; // 显示银行详情对话框
        },

        /**
         * 格式化利率显示
         * @param {number|string|Object} rate - 利率值，可以是数字、字符串或BigDecimal对象
         * @returns {string} 格式化后的利率字符串
         */
        formatRate(rate) {
            if (!rate) return '0.00%'; // 如果没有利率值，返回默认值
            if (typeof rate === 'number') return (rate * 100).toFixed(2) + '%'; // 如果是数字类型，格式化为百分比
            if (typeof rate === 'string') {
                const rateNum = parseFloat(rate);
                return isNaN(rateNum) ? '0.00%' : (rateNum * 100).toFixed(2) + '%'; // 如果是字符串类型，尝试转换为数字并格式化
            }
            if (typeof rate.toNumber === 'function') return (rate.toNumber() * 100).toFixed(2) + '%'; // 如果是BigDecimal对象，调用toNumber方法并格式化
            return '0.00%'; // 默认返回0.00%
        }
    },
    async mounted() {
        try {
            const response = await getBankList(); // 获取银行列表数据
            this.bankList = response.data; // 将获取的数据赋值给bankList
        } catch (error) {
            console.error("获取银行列表失败:", error); // 打印错误信息
            this.$message.error('获取银行列表失败'); // 显示错误提示
        }
    }
}
</script>

<style lang="scss" scoped>
.bank-page-container {
    background-color: #f5f7fa;
    min-height: 100vh;
    padding: 20px;
}

.bank-container {
    max-width: 1200px;
    margin: 0 auto;
    padding: 20px;
    background: white;
    border-radius: 8px;
    box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);

    .title {
        text-align: center;
        color: #303133;
        margin-bottom: 30px;
        font-size: 24px;
        font-weight: 500;
        padding-bottom: 15px;
        border-bottom: 1px solid #ebeef5;
    }

    .bank-grid {
        display: grid;
        grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
        gap: 20px;
        padding: 10px;
    }

    .bank-card {
        background: white;
        border-radius: 8px;
        box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
        transition: all 0.3s ease;
        cursor: pointer;
        overflow: hidden;
        text-align: center;
        border: 1px solid #ebeef5;

        &:hover {
            transform: translateY(-5px);
            box-shadow: 0 6px 16px rgba(0, 0, 0, 0.15);
            border-color: #409EFF;
        }

        .bank-logo {
            padding: 20px;
            background: linear-gradient(135deg, #f5f7fa 0%, #e6e9f0 100%);

            .logo-image {
                width: 100px;
                height: 100px;
                object-fit: contain;
                transition: transform 0.3s;
            }
        }

        &:hover .logo-image {
            transform: scale(1.05);
        }

        .bank-name {
            padding: 15px;
            font-size: 16px;
            font-weight: 500;
            color: #606266;
            border-top: 1px solid #ebeef5;
            transition: color 0.3s;
        }

        &:hover .bank-name {
            color: #409EFF;
        }
    }
}

/* 银行详情对话框样式 */
.bank-detail-dialog {
    .el-dialog {
        border-radius: 8px;
        overflow: hidden;

        &__header {
            background: linear-gradient(135deg, #409EFF 0%, #3375b9 100%);
            padding: 15px 20px;

            .el-dialog__title {
                color: white;
                font-size: 18px;
            }

            .el-dialog__headerbtn {
                top: 15px;

                .el-dialog__close {
                    color: white;
                }
            }
        }

        &__body {
            padding: 20px;
        }
    }

    .bank-detail-content {
        .bank-header {
            text-align: center;
            margin-bottom: 15px;

            .detail-logo {
                width: 80px;
                height: 80px;
                object-fit: contain;
                border-radius: 50%;
                background: #f5f7fa;
                padding: 5px;
                box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
            }
        }

        .el-divider {
            margin: 10px 0;
        }

        .detail-section {
            .detail-item {
                margin-bottom: 15px;

                &:last-child {
                    margin-bottom: 0;
                }

                .detail-label {
                    display: flex;
                    align-items: center;
                    font-size: 14px;
                    color: #909399;
                    margin-bottom: 5px;

                    i {
                        margin-right: 8px;
                        font-size: 16px;
                        color: #409EFF;
                    }
                }

                .detail-text {
                    margin: 0;
                    font-size: 15px;
                    color: #303133;
                    line-height: 1.6;
                    padding-left: 24px;
                }

                .rate-value {
                    color: #F56C6C;
                    font-weight: 500;
                }
            }

            .detail-grid {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
                gap: 15px;
                margin-top: 10px;

                .detail-item {
                    background: #f9f9f9;
                    border-radius: 6px;
                    padding: 12px 15px;
                    transition: all 0.3s;

                    &:hover {
                        background: #f0f7ff;
                        transform: translateY(-2px);
                    }
                }
            }
        }
    }

    .dialog-footer {
        .el-button {
            padding: 10px 20px;
            border-radius: 4px;
        }
    }
}

@media (max-width: 768px) {
    .bank-container .bank-grid {
        grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
        gap: 15px;
    }

    .bank-detail-dialog {
        width: 90% !important;

        .detail-grid {
            grid-template-columns: 1fr !important;
        }
    }
}
</style>