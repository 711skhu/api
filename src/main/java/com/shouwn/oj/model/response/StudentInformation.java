package com.shouwn.oj.model.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class StudentInformation {

	private String name;

	private String username;

	private String email;
}
