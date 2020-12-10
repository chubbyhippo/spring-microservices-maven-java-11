package com.optimagrowth.license.service.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.optimagrowth.license.model.Organization;

@Component
public class OrganizationRestTemplateClient {
	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private Environment env;

	public Organization getOrganization(String organizationId) {
		ResponseEntity<Organization> restExchange = restTemplate.exchange(
				env.getProperty("gateway.url")
						+ "organization/v1/organization/{organizationId}",
				HttpMethod.GET, null, Organization.class, organizationId);

		return restExchange.getBody();
	}
}
