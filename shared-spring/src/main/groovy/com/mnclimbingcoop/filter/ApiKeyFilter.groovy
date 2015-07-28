package com.mnclimbingcoop.filter

import com.mnclimbingcoop.config.ApiKeyConfiguration

import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j

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

@CompileStatic
@Named
@Slf4j
class ApiKeyFilter implements Filter {

    private final Map<String, String> apiKeys

    @Inject
    ApiKeyFilter(ApiKeyConfiguration apiConfig) {
        this.apiKeys = apiConfig.keys
    }

    @Override
    void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req
        HttpServletResponse response = (HttpServletResponse) res

        String headerAuthorization = request.getHeader('Authorization')
        String headerToken = null
        if (headerAuthorization?.startsWith('token ')) {
            headerToken = headerAuthorization.replaceFirst('token ', '')
        }
        String requestToken = request.getParameter('access_token')

        String keyHolder = getHolder(headerToken, requestToken)

        if (!keyHolder) {
            response.setContentLength(0)
            response.sendError(403, 'Invalid API Token')
        } else {
            log.info "Access granted to ${keyHolder}"
            chain.doFilter(req, res)
        }
    }

    protected String getHolder(String headerToken, String requestToken) {
        if (apiKeys['ALL'] == 'enabled') { return '*' }
        String keyHolder = apiKeys[headerToken]
        if (!keyHolder) {
            keyHolder = apiKeys[requestToken]
        }
        return keyHolder
    }

    @Override
    void init(FilterConfig filterConfig) {}

    @Override
    void destroy() {}

}

