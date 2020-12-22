package com.optimagrowth.gateway.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties(prefix = "config")
@Getter
@Setter
public class ConfigProperties {
	
	private String logbackDestination;

}
