//提交订单 yes
function addOrderApi(data) {
    return $axios({
        'url': '/order/submit',
        'method': 'post',
        data
    })
}

//查询所有订单
function orderListApi() {
    return $axios({
        'url': '/order/list',
        'method': 'get',
    })
}

//分页查询订单 yes
function orderPagingApi(data) {
    return $axios({
        'url': '/order/userPage',
        'method': 'get',
        params: {...data}
    })
}

//再来一单 yes
function orderAgainApi(data) {
    return $axios({
        'url': '/order/again',
        'method': 'post',
        data
    })
}