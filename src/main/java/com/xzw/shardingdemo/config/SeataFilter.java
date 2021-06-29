package com.xzw.shardingdemo.config;

import io.seata.common.util.StringUtils;
import io.seata.core.context.RootContext;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


@Component
public class SeataFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        String xid = req.getHeader(RootContext.KEY_XID.toLowerCase());//RootContext seata的全局事务唯一标识
        System.out.println("seata_transaction_request_xid:"+xid);
        boolean isBind = false;
        if (StringUtils.isNotBlank(xid)) {
            //RootContext绑定xid，即绑定同一个分布式事务
            RootContext.bind(xid);
            isBind = true;
        }
        // 执行完请求后一定要解绑
        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            if (isBind) {
                RootContext.unbind();
            }
        }
    }

    @Override
    public void destroy() {

    }
}
