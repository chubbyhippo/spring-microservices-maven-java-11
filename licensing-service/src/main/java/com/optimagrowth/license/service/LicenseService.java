package com.optimagrowth.license.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import com.optimagrowth.license.config.ServiceConfig;
import com.optimagrowth.license.model.License;
import com.optimagrowth.license.model.Organization;
import com.optimagrowth.license.repository.LicenseRepository;
import com.optimagrowth.license.service.client.OrganizationDiscoveryClient;
import com.optimagrowth.license.service.client.OrganizationRestTemplateClient;
import com.optimagrowth.license.utils.UserContext;
import com.optimagrowth.license.utils.UserContextHolder;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class LicenseService {

	@Autowired
	MessageSource messages;

	@Autowired
	private LicenseRepository licenseRepository;

	@Autowired
	ServiceConfig config;

	@Autowired
	OrganizationRestTemplateClient organizationRestClient;

	@Autowired
	OrganizationDiscoveryClient organizationDiscoveryClient;

	public License getLicense(String licenseId, String organizationId,
			String clientType) {
		License license = licenseRepository
				.findByOrganizationIdAndLicenseId(organizationId, licenseId);
		if (null == license) {
			throw new IllegalArgumentException(String.format(messages
					.getMessage("license.search.error.message", null, null),
					licenseId, organizationId));
		}

		Organization organization = retrieveOrganizationInfo(organizationId,
				clientType);
		if (null != organization) {
			license.setOrganizationName(organization.getName());
			license.setContactName(organization.getContactName());
			license.setContactEmail(organization.getContactEmail());
			license.setContactPhone(organization.getContactPhone());
		}

		return license.withComment(config.getExampleProperty());
	}

	private Organization retrieveOrganizationInfo(String organizationId,
			String clientType) {
		Organization organization = null;

		switch (clientType) {
		case "rest":
			System.out.println("I am using the rest client");
			organization = organizationRestClient
					.getOrganization(organizationId);
			break;
		case "discovery":
			System.out.println("I am using the discovery client");
			organization = organizationDiscoveryClient
					.getOrganization(organizationId);
			break;
		default:
			organization = organizationRestClient
					.getOrganization(organizationId);
			break;
		}

		return organization;
	}

	public License createLicense(License license) {
		license.setLicenseId(UUID.randomUUID().toString());
		licenseRepository.save(license);

		return license.withComment(config.getExampleProperty());
	}

	public License updateLicense(License license) {
		licenseRepository.save(license);

		return license.withComment(config.getExampleProperty());
	}

	public String deleteLicense(String licenseId) {
		String responseMessage = null;
		License license = new License();
		license.setLicenseId(licenseId);
		licenseRepository.delete(license);
		responseMessage = String.format(
				messages.getMessage("license.delete.message", null, null),
				licenseId);
		return responseMessage;

	}

	private void randomlyRunLong() throws TimeoutException {
		Random rand = new Random();
		int randomNum = rand.nextInt((3 - 1) + 1) + 1;
		if (randomNum == 3)
			sleep();
	}

	private void sleep() throws TimeoutException {
		try {
			Thread.sleep(5000);
			throw new java.util.concurrent.TimeoutException();
		} catch (InterruptedException e) {
			log.error(e.getMessage());
		}
	}

	@SuppressWarnings("unused")
	private List<License> buildFallbackLicenseList(String organizationId,
			Throwable t) {
		List<License> fallbackList = new ArrayList<>();
		License license = new License();
		license.setLicenseId("0000000-00-00000");
		license.setOrganizationId(organizationId);
		license.setProductName(
				"Sorry no licensing information currently available");
		fallbackList.add(license);
		return fallbackList;
	}

	@CircuitBreaker(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
	@RateLimiter(name = "licenseService", fallbackMethod = "buildFallbackLicenseList")
	@Retry(name = "retryLicenseService", fallbackMethod = "buildFallbackLicenseList")
//	@Bulkhead(name = "bulkheadLicenseService", fallbackMethod = "buildFallbackLicenseList", type = Type.THREADPOOL)
	@Bulkhead(name = "bulkheadLicenseService", fallbackMethod = "buildFallbackLicenseList")
	public List<License> getLicensesByOrganization(String organizationId)
			throws TimeoutException {

		UserContextHolder.getContext();
		log.debug("getLicensesByOrganization Correlation id: {}",
				UserContext.getCorrelationId());
		randomlyRunLong();
		return licenseRepository.findByOrganizationId(organizationId);
	}
}
