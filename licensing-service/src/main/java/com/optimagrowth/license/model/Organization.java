package com.optimagrowth.license.model;

import org.springframework.data.redis.core.RedisHash;
import org.springframework.hateoas.RepresentationModel;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@RedisHash("organization")
public class Organization extends RepresentationModel<Organization> {

	@Id
	String id;
	String name;
	String contactName;
	String contactEmail;
	String contactPhone;

}