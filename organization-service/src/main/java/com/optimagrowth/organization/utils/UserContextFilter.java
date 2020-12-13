package com.optimagrowth.organization.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import static com.optimagrowth.organization.utils.UserContext.getCorrelationId;
import static com.optimagrowth.organization.utils.UserContext.setAuthToken;
import static com.optimagrowth.organization.utils.UserContext.setCorrelationId;
import static com.optimagrowth.organization.utils.UserContext.setOrgId;
import static com.optimagrowth.organization.utils.UserContext.setUserId;

import java.io.IOException;

@Component
public class UserContextFilter implements Filter {
	private static final Logger logger = LoggerFactory
			.getLogger(UserContextFilter.class);

	@Override
	public void doFilter(ServletRequest servletRequest,
			ServletResponse servletResponse, FilterChain filterChain)
			throws IOException, ServletException {

		HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;

		setCorrelationId(
				httpServletRequest.getHeader(UserContext.CORRELATION_ID));
		setUserId(httpServletRequest.getHeader(UserContext.USER_ID));
		setAuthToken(httpServletRequest.getHeader(UserContext.AUTH_TOKEN));
		setOrgId(httpServletRequest.getHeader(UserContext.ORG_ID));

		logger.debug("Organization Service Incoming Correlation id: {}",
				getCorrelationId());
		filterChain.doFilter(httpServletRequest, servletResponse);
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}
