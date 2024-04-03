package com.evm.ms.auth.infrastructure.factory;

import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;

@Component
public class RequestMatcherFactoryDefault implements RequestMatcherFactory {

    @Override
    public RequestMatcher getRequestMatcher(String matcher) {
        return new AntPathRequestMatcher(matcher);
    }

}
