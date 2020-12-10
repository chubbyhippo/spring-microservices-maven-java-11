package com.optimagrowth.license.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.stereotype.Component;

import com.optimagrowth.license.model.Organization;

@Component
public class OrganizationRestTemplateClient {
	@Autowired
	private OAuth2RestTemplate restTemplate;

	@Autowired
	private Environment env;

	public Organization getOrganization(String organizationId) {
		ResponseEntity<Organization> restExchange = restTemplate.exchange(
				env.getProperty("gateway.url"), HttpMethod.GET, null,
				Organization.class, organizationId);

		return restExchange.getBody();
	}
}
