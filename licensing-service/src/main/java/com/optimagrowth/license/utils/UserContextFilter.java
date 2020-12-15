package com.optimagrowth.license.utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class UserContextFilter implements Filter {

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

		UserContextHolder.getContext();
		UserContext.setCorrelationId(
				httpServletRequest.getHeader(UserContext.CORRELATION_ID));
		UserContextHolder.getContext();
		UserContext
				.setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
		UserContextHolder.getContext();
		UserContext.setAuthToken(
				httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
		UserContextHolder.getContext();
		UserContext.setOrganizationId(
				httpServletRequest.getHeader(UserContext.ORGANIZATION_ID));

		UserContextHolder.getContext();
		log.debug("UserContextFilter Correlation id: {}",
				UserContext.getCorrelationId());

		filterChain.doFilter(httpServletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
