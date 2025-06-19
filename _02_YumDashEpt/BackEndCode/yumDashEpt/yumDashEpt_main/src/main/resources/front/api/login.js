// yes
function loginApi(data) {
    return $axios({
        'url': '/user/login',
        'method': 'post',
        data
    })
}

// yes
function loginoutApi() {
    return $axios({
        'url': '/user/loginout',
        'method': 'post',
    })
}

  