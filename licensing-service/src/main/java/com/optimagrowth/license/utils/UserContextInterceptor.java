package com.optimagrowth.license.utils;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class UserContextInterceptor implements ClientHttpRequestInterceptor {
	@Override
	public ClientHttpResponse intercept(HttpRequest request, byte[] body,
			ClientHttpRequestExecution execution) throws IOException {

		HttpHeaders headers = request.getHeaders();
		UserContextHolder.getContext();
		headers.add(UserContext.CORRELATION_ID,
				UserContext.getCorrelationId());
		UserContextHolder.getContext();
		headers.add(UserContext.AUTH_TOKEN, UserContext.getAuthToken());

		return execution.execute(request, body);
	}
}