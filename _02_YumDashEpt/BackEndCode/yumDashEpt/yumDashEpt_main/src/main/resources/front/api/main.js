//获取所有的菜品分类 yes
function categoryListApi() {
    return $axios({
        'url': '/category/list',
        'method': 'get',
    })
}

//获取菜品分类对应的菜品 yes
function dishListApi(data) {
    return $axios({
        'url': '/dish/list',
        'method': 'get',
        params: {...data}
    })
}

//获取菜品分类对应的套餐 yes
function setmealListApi(data) {
    return $axios({
        'url': '/setmeal/list',
        'method': 'get',
        params: {...data}
    })
}

//获取购物车内商品的集合 yes
function cartListApi(data) {
    return $axios({
        'url': '/shoppingCart/list',
        'method': 'get',
        params: {...data}
    })
}

//购物车中添加商品 yes
function addCartApi(data) {
    return $axios({
        'url': '/shoppingCart/add',
        'method': 'post',
        data
    })
}

//购物车中修改商品 yes
function updateCartApi(data) {
    return $axios({
        'url': '/shoppingCart/sub',
        'method': 'post',
        data
    })
}

//删除购物车的商品 yes
function clearCartApi() {
    return $axios({
        'url': '/shoppingCart/clean',
        'method': 'delete',
    })
}

//获取套餐的全部菜品 yes
function setMealDishDetailsApi(id) {
    return $axios({
        'url': `/setmeal/dish/${id}`,
        'method': 'get',
    })
}


