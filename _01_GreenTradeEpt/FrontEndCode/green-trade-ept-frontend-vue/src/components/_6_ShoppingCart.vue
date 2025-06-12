<template>
    <div id="shoppingCart" v-if="user" class="cart-container">
        <!-- 收货地址卡片 -->
        <el-card class="address-card">
            <div slot="header" class="card-header">
                <span>我的收货地址</span>
                <el-button type="primary" plain size="small">更换地址</el-button>
            </div>
            <div class="address-info">
                <div class="info-item">
                    <i class="el-icon-user"></i>
                    <span>收货人：{{ user.nickName ? user.nickName : user.userName }}</span>
                </div>
                <div class="info-item">
                    <i class="el-icon-location-outline"></i>
                    <span>收货地址：{{ user.address || '暂无地址信息' }}</span>
                </div>
                <div class="info-item">
                    <i class="el-icon-phone"></i>
                    <span>收货手机号：{{ user.phone || '暂无手机号' }}</span>
                </div>
            </div>
        </el-card>

        <!-- 购物车商品表格 -->
        <el-card class="cart-table">
            <el-table ref="multipleTable" :data="tableData" tooltip-effect="dark" style="width: 100%"
                @selection-change="handleSelectionChange"
                :header-cell-style="{ background: '#f5f7fa', color: '#606266' }">
                <el-table-column type="selection" width="55" align="center"></el-table-column>

                <el-table-column label="商品" min-width="300">
                    <template slot-scope="scope">
                        <div class="product-info">
                            <img :src="'orderImgs/' + scope.row.order.picture" alt="商品图片" class="product-img">
                            <div class="product-desc">
                                <div class="product-title">{{ scope.row.order.title }}</div>
                                <div class="product-content">{{ scope.row.order.content }}</div>
                            </div>
                        </div>
                    </template>
                </el-table-column>

                <el-table-column label="单价" width="120" align="center">
                    <template slot-scope="scope">
                        <span class="price">¥{{ scope.row.order.price.toFixed(2) }}</span>
                    </template>
                </el-table-column>

                <el-table-column label="数量" width="150" align="center">
                    <template slot-scope="scope">
                        <div class="quantity-control">
                            <!-- 当count值<=1时，不能触发sub点击事件 -->
                            <el-button @click="sub(scope.row.shoppingId)" icon="el-icon-minus" size="mini"
                                :disabled="scope.row.count <= 1" circle></el-button>
                            <span class="quantity">{{ scope.row.count }}</span>
                            <el-button @click="add(scope.row.shoppingId)" icon="el-icon-plus" size="mini"
                                circle></el-button>
                        </div>
                    </template>
                </el-table-column>

                <!-- 计算多个同一商品的总价钱 -->
                <el-table-column label="小计" width="120" align="center">
                    <template slot-scope="scope">
                        <span class="subtotal">¥{{ (scope.row.order.price * scope.row.count).toFixed(2) }}</span>
                    </template>
                </el-table-column>

                <el-table-column label="删除" width="100" align="center">
                    <template slot-scope="scope">
                        <el-button type="danger" size="mini" @click="removeCart(scope.row.shoppingId)"
                            icon="el-icon-delete" circle></el-button>
                    </template>
                </el-table-column>
            </el-table>

            <!-- 底部操作栏 -->
            <div class="cart-footer">
                <div class="footer-left">
                    <el-button @click="toggleSelection()">取消选择</el-button>
                    <el-button @click="removeSelected" type="danger"
                        :disabled="!multipleSelection || multipleSelection.length === 0">
                        删除选中({{ multipleSelection ? multipleSelection.length : 0 }})
                    </el-button>
                </div>
                <div class="footer-right">
                    <span class="total-text">已选择 {{ selectedCount }} 件商品</span>
                    <span class="total-price">总计: ¥{{ totalPrice.toFixed(2) }}</span>
                    <el-button type="primary" :disabled="!multipleSelection || multipleSelection.length === 0"
                        @click="submitPurchase">
                        提交购物清单
                    </el-button>
                </div>
            </div>
        </el-card>
        <!-- 分页 -->
        <div class="pagination-wrapper">
            <el-pagination layout="prev, pager, next" :page-count="totalPages" :current-page="pageNum"
                @current-change="handleCurrentChange">
            </el-pagination>
        </div>
    </div>

    <div v-else class="empty-cart">
        <el-empty description="购物车信息加载中..."></el-empty>
    </div>
</template>

<script>
import { userInfo } from "../apis/UserService.js";
import { getPage, remove, updateCount } from "../apis/ShoppingcartService.js";
import { savePurchase } from "../apis/PurchaseService.js";

export default {
    data() {
        return {
            // 购物车项
            pageNum: 1,
            pageSize: 0,
            pageBean: null,
            pageList: [],
            tableData: [], // element表单的数据
            multipleSelection: [], // 选中的商品

            // 当前登录用户
            username: "",
            user: null,
        }
    },

    methods: {
        // 根据关键字分页查询
        async search(isHandleCurrentChange) {
            if (isHandleCurrentChange != true) { // 若参数不为true，表示并非页数变化
                this.pageNum = 1; // 则置当前页数为1
            }
            const result = await getPage(this.pageNum, this.pageSize, this.username);
            this.pageBean = result.data;
            this.pageList = result.data.items;
            this.tableData = this.pageList;
        },

        // 当页数改变
        handleCurrentChange(val) { // val为当前页
            this.pageNum = val;
            this.search(true); // 仅当页数改变，传入参数true
        },

        // 使订单数量-1
        async sub(id) {
            await updateCount(-1, id);
            this.$message.success('数量减少成功');
            await this.search(true);// 刷新页面
        },

        // 使订单数量+1
        async add(id) {
            await updateCount(1, id);
            this.$message.success('数量增加成功');
            await this.search(true);// 刷新页面
        },

        // 删除订单
        async removeCart(id) {
            try {
                await this.$confirm('确定要删除该商品吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                });
                await remove(id);
                this.$message.success('删除成功');
                await this.search();// 刷新页面
            } catch (error) {
                if (error !== 'cancel') {
                    this.$message.error('删除失败: ' + error.message);
                }
            }
        },

        // 将选中的内容整体删除
        async removeSelected() {
            try {
                await this.$confirm(`确定要删除选中的${this.multipleSelection.length}件商品吗?`, '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                });
                const deletePromises = this.multipleSelection.map(item =>
                    remove(item.shoppingId)
                );
                await Promise.all(deletePromises);
                this.$message.success('删除成功');
                await this.search();// 刷新页面
                this.toggleSelection(); // 清空选择
            } catch (error) {
                if (error !== 'cancel') {
                    this.$message.error('删除失败: ' + error.message);
                }
            }
        },

        // 提交购物清单
        async submitPurchase() {
            try {
                // 确认是否提交
                await this.$confirm('确认提交订单吗?', '提示', {
                    confirmButtonText: '确定',
                    cancelButtonText: '取消',
                    type: 'warning'
                });
                // 创建purchase对象
                const purchase = {
                    ownName: this.username,
                    totalPrice: this.totalPrice,
                    address: this.user.address || '暂无地址信息',
                    purchaseType: 1,
                    purchaseStatus: 1,
                };
                await savePurchase(purchase);// 提交数据到数据库
                this.$message.success('购物清单提交成功');
                this.toggleSelection(); // 清空选择
                // await this.search(); // 刷新购物车
            } catch (error) {
                if (error !== 'cancel') {
                    this.$message.error('订单提交失败: ' + (error.message));
                }
            }
        },

        // elementUI处理表格多选
        toggleSelection(rows) {
            if (rows) {
                rows.forEach(row => {
                    this.$refs.multipleTable.toggleRowSelection(row);
                });
            } else {
                this.$refs.multipleTable.clearSelection();
            }
        },

        // elementUI处理表格多选
        handleSelectionChange(val) {
            this.multipleSelection = val;
        },
    },

    async mounted() {
        // 获取当前用户
        this.username = this.$store.state.loginUsername;
        this.user = (await userInfo(this.username)).data;

        // 获取分页列表
        this.pageSize = 5;
        const result = await getPage(1, this.pageSize, this.username);
        this.pageBean = result.data;
        this.pageList = result.data.items;
        this.tableData = this.pageList;
    },

    computed: {
        // 计算属性，用于获取总页数
        totalPages() {
            if (!this.pageBean || !this.pageBean.total || !this.pageSize) return 0;
            // 总页数 = (总项数 / 每页项数) 向上取整
            return Math.ceil(Number(this.pageBean.total) / Number(this.pageSize));
        },

        // 选中的商品数量
        selectedCount() {
            return this.multipleSelection ? this.multipleSelection.length : 0;
        },

        // 计算被选中商品的总价
        totalPrice() {
            if (!this.multipleSelection || this.multipleSelection.length === 0) return 0;
            // 遍历所有选中的商品（multipleSelection 数组），将每个商品的 单价×数量 累加，返回最终的总金额。
            return this.multipleSelection.reduce((total, item) => {
                return total + (item.order.price * item.count);
            }, 0);
        }
    }
}
</script>

<style lang="scss" scoped>
.cart-container {
    width: 85%;
    margin: 20px auto;
    padding: 0 15px;
}

.address-card {
    margin-bottom: 20px;

    .card-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .address-info {
        .info-item {
            margin-bottom: 10px;
            display: flex;
            align-items: center;

            i {
                margin-right: 8px;
                color: #409EFF;
            }

            &:last-child {
                margin-bottom: 0;
            }
        }
    }
}

.cart-table {
    margin-bottom: 20px;

    .product-info {
        display: flex;
        align-items: center;

        .product-img {
            width: 80px;
            height: 80px;
            object-fit: cover;
            margin-right: 15px;
            border-radius: 4px;
        }

        .product-desc {
            .product-title {
                font-weight: bold;
                margin-bottom: 5px;
                color: #333;
            }

            .product-content {
                font-size: 12px;
                color: #999;
                line-height: 1.5;
            }
        }
    }

    .price,
    .subtotal {
        color: #f56c6c;
        font-weight: bold;
    }

    .quantity-control {
        display: flex;
        align-items: center;
        justify-content: center;

        .quantity {
            margin: 0 10px;
            min-width: 30px;
            text-align: center;
        }
    }
}

.cart-footer {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-top: 20px;
    padding-top: 20px;
    border-top: 1px solid #ebeef5;

    .footer-right {
        display: flex;
        align-items: center;

        .total-text {
            margin-right: 20px;
            color: #666;
        }

        .total-price {
            margin-right: 20px;
            font-size: 18px;
            color: #f56c6c;
            font-weight: bold;
        }
    }
}

.empty-cart {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 300px;
}

.el-button.is-circle {
    padding: 6px;
}
</style>