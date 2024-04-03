package com.evm.ms.auth.infrastructure.factory;

import org.springframework.security.web.util.matcher.RequestMatcher;

public interface RequestMatcherFactory {

    RequestMatcher getRequestMatcher(String matcher);

}
