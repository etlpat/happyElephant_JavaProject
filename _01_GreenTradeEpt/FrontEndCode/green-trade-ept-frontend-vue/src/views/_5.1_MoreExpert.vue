<template>
    <div id="expert-container">
        <div class="right-section">
            <div class="expert-list">
                <div class="expert-card" v-for="item in pageList" :key="item.userName">
                    <div class="expert-main">
                        <div class="expert-avatar">
                            <img src="else/expert.png" alt="专家头像">
                        </div>
                        <div class="expert-info-container">
                            <div class="expert-details">
                                <div class="detail-item">
                                    <span class="detail-label">专家姓名：</span>
                                    <span class="detail-value">{{ item.realName }}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">职称：</span>
                                    <span class="detail-value">{{ item.position }}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">专业：</span>
                                    <span class="detail-value">{{ item.profession }}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">电话：</span>
                                    <span class="detail-value">{{ item.phone }}</span>
                                </div>
                                <div class="detail-item">
                                    <span class="detail-label">单位：</span>
                                    <span class="detail-value">{{ item.belong }}</span>
                                </div>
                            </div>
                            <div class="expert-actions">
                                <el-button type="success" size="small" plain class="action-button">提问</el-button>
                                <el-button type="success" size="small" plain class="action-button">预约</el-button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 页码框 -->
            <div class="pagination">
                <el-pagination layout="prev, pager, next" :page-count="totalPages" :current-page="pageNum"
                    @current-change="handleCurrentChange">
                </el-pagination>
            </div>
        </div>
    </div>
</template>

<script>
import { getPageAll } from "../apis/ExpertService.js";

export default {
    data() {
        return {
            // 专家分页数据
            pageNum: 1,
            pageSize: 0,
            pageBean: null,
            pageList: [],
        }
    },

    methods: {
        // 分页查询
        async search(isHandleCurrentChange) {
            if (isHandleCurrentChange != true) {// 若参数不为true，表示并非页数变化
                this.pageNum = 1;// 则置当前页数为1
            }
            const result = await getPageAll(this.pageNum, this.pageSize);
            this.pageBean = result.data;
            this.pageList = result.data.items;
        },

        // 当页数改变
        handleCurrentChange(val) {// val为当前页
            this.pageNum = val;
            this.search(true);// 仅当页数改变，传入参数true
        },
    },

    async mounted() {
        // 获取专家列表
        this.pageSize = 3;
        const result = await getPageAll(1, this.pageSize);
        this.pageBean = result.data;
        this.pageList = result.data.items;
    },

    computed: {
        // 计算属性，用于获取总页数
        totalPages() {
            if (!this.pageBean || !this.pageBean.total || !this.pageSize) return 0;
            // 总页数 = (总项数 / 每页项数) 向上取整
            return Math.ceil(Number(this.pageBean.total) / Number(this.pageSize));
        }
    }
}
</script>

<style lang="scss" scoped>
#expert-container {
    width: 80%;
    margin: 20px auto;
    display: flex;
    gap: 20px;
    height: 600px;

    .right-section {
        flex: 1;
        background: #fff;
        border-radius: 8px;
        padding: 18px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
        min-width: 350px;
        /* 确保有足够宽度 */

        .expert-list {
            display: flex;
            flex-direction: column;
            gap: 20px;

            .expert-card {
                background: #f9f9f9;
                border-radius: 8px;
                padding: 15px;
                transition: all 0.3s ease;

                &:hover {
                    transform: translateY(-2px);
                    box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
                }

                .expert-main {
                    display: flex;
                    gap: 15px;
                }

                .expert-avatar {
                    flex-shrink: 0;

                    img {
                        width: 80px;
                        height: 80px;
                        border-radius: 50%;
                        object-fit: cover;
                        border: 2px solid #e1f5fe;
                        margin-left: 30px;
                        margin-right: 50px;
                    }
                }

                .expert-info-container {
                    flex: 1;
                    display: flex;
                    flex-direction: column;
                    min-width: 0;
                    /* 防止flex项目溢出 */
                }

                .expert-details {
                    display: grid;
                    grid-template-columns: repeat(2, 1fr);
                    gap: 12px 15px;
                    margin-bottom: 10px;

                    .detail-item {
                        display: flex;
                        min-width: 0;
                        /* 防止文本溢出 */
                        white-space: nowrap;
                        /* 防止换行 */

                        .detail-label {
                            color: #666;
                            font-size: 14px;
                            margin-right: 5px;
                            flex-shrink: 0;
                            /* 防止标签被压缩 */
                        }

                        .detail-value {
                            color: #333;
                            font-weight: 500;
                            font-size: 14px;
                            overflow: hidden;
                            text-overflow: ellipsis;
                        }
                    }
                }

                .expert-actions {
                    display: flex;
                    gap: 10px;
                    justify-content: flex-end;

                    .action-button {
                        width: 80px;
                    }
                }
            }
        }
    }
}

@media (max-width: 1200px) {
    .right-section {
        .expert-list {
            .expert-card {
                .expert-details {
                    grid-template-columns: 1fr;
                    /* 小屏幕时改为单列 */
                }
            }
        }
    }
}
</style>