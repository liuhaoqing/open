package com.liuhq.open.exception;

import lombok.Getter;
import lombok.Setter;

public class OpenException extends RuntimeException {

	private static final long serialVersionUID = 7516283785904804072L;

	@Getter
	@Setter
	private String message;

	public OpenException(String message) {
		super();
		this.message = message;
	}

}
