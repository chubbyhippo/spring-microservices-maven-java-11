package com.optimagrowth.organization.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Getter;

@Component
@Getter
public class ServiceConfig {

	@Value("${signing.key}")
	private String jwtSigningKey = "";

}