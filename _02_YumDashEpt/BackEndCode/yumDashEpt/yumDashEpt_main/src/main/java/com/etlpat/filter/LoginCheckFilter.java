package com.etlpat.filter;

import com.alibaba.fastjson.JSON;
import com.etlpat.pojo.R;
import com.etlpat.utils.ThreadLocalUtil;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.util.AntPathMatcher;

import java.io.IOException;


// 过滤器 -- 用于检查用户是否已经完成登录
//
//  过滤器具体的处理逻辑如下:
//      1、获取本次请求的URI
//      2、判断本次请求是否需要处理
//      3、如果不需要处理,则直接放行
//      4、判断登录状态,如果已登录,则直接放行
//      5、如果未登录则返回未登录结果
//


@WebFilter(filterName = "loginCheckFilter" // 过滤器名称
        , urlPatterns = "/*") // 拦截路径（拦截全部请求）
public class LoginCheckFilter implements Filter {

    // 路径匹配器，支持通配符（用于进行路径比较的对象）
    public static final AntPathMatcher antPathMatcher = new AntPathMatcher();


    // 过滤方法
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        try {
            // 1、获取本次请求的URI
            String requestURI = request.getRequestURI();

            // 定义直接放行的路径
            String[] uris = new String[]{
                    "/employee/login",// 登录请求
                    "/employee/logout",// 登出请求
                    "/common/**",// 文件上传/下载请求
                    "/backend/**",// 服务端的静态页面（并非请求）
                    "/front/**",// 客户端的静态页面（并非请求）
                    "/user/login",// 客户端登录
                    "/user/loginout"// 客户端登出
            };

            // 2、判断本次请求是否需要处理
            boolean isMatch = checkMatch(requestURI, uris);
            if (isMatch) {// 若与放行路径匹配
                filterChain.doFilter(request, response);// 放行
                return;
            }

            // 3、判断管理端登录状态, 如果已登录, 则直接放行，并将登录用户id存入ThreadLocal中
            Long adminLoginId = (Long) request.getSession().getAttribute("employee");
            if (adminLoginId != null) {// 管理端已登录
                ThreadLocalUtil.set(adminLoginId);// 将当前登录用户id存入ThreadLocal中
                filterChain.doFilter(request, response);// 放行
                return;
            }

//            // 疑似bug代码如下：
//            // 4、判断客户端登录状态, 如果已登录, 则直接放行，并将登录用户id存入ThreadLocal中
//            Long clientLoginId = (Long) request.getSession().getAttribute("user");
//            if (clientLoginId != null) {// 客户端已登录
//                ThreadLocalUtil.set(clientLoginId);// 将当前登录用户id存入ThreadLocal中
//                filterChain.doFilter(request, response);// 放行
//                return;
//            }

            // 5、如果未登录则返回未登录结果
            // 注意：向前端发送R.error("NOTLOGIN")，前端会处理并跳转到登录界面
            response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));// 前端接收后，跳转到登录界面
            return;

        } finally {
            // 移除ThreadLocal中用户数据，防止内存泄漏
            ThreadLocalUtil.remove();
        }
    }


    // 用于判断路径是否匹配
    public boolean checkMatch(String requestURI, String[] urls) {
        // 遍历urls数组，判断每项是否匹配
        for (String url : urls) {
            // 若匹配，返回true
            if (antPathMatcher.match(url, requestURI)) {
                return true;
            }
        }
        return false;
    }

}
