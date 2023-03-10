package com.portaria.model;

import lombok.Data;

@Data
public class CodigoBarras {
	private String codigo;
	
	public CodigoBarras(String codigo) {
		this.codigo = codigo;
	}
}
