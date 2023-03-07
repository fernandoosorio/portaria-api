package com.infra.utils;

public class CpfUtil {
	
	public static String getCpfSomenteNumeros(String cpf) {
		if(cpf == null || cpf.isEmpty()) return null;
		return cpf.replaceAll("[^0-9]", "");
	}

}
