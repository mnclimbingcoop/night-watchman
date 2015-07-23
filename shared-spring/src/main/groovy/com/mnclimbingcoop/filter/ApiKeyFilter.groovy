package com.mnclimbingcoop.filter

import groovy.transform.CompileStatic

import javax.inject.Inject
import javax.inject.Named
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.beans.factory.annotation.Value

@CompileStatic
@Named
class ApiKeyFilter implements Filter {

    private final String apiKey

    @Inject
    ApiKeyFilter(@Value('${apiKey}') String apiKey) {
        this.apiKey = apiKey
    }

    void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req
        HttpServletResponse response = (HttpServletResponse) res

        String headerAuthorization = request.getHeader('Authorization')
        String headerToken = null
        if (headerAuthorization?.startsWith('token ')) {
            headerToken = headerAuthorization.replaceFirst('token ', '')
        }
        String requestToken = request.getParameter('access_token')

        if (headerToken != apiKey && requestToken != apiKey) {
            response.setContentLength(0)
            response.sendError(403, 'Invalid API Token')
        }

        //chain.doFilter(req, res)
    }

    void init(FilterConfig filterConfig) {}

    void destroy() {}

}

