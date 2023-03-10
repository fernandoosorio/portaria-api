package com.portaria.controller;

import java.io.FileNotFoundException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;

@RestController
@RequestMapping("/gerar-codigo-barras")
public class CodBarrasController {
	
	@GetMapping("/{quantidade}")
	public ResponseEntity<byte[]> gerarCodigoBarras(@PathVariable("quantidade") Long quantidade) throws JRException, FileNotFoundException {
		Map<String,Object> parametros = new HashMap<>();
    	byte[] bytes = null;
    	
    	String relatorioJasper = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + 
    			"relatorios/gerar-codigo-visita.jasper").getAbsolutePath();
    	
    	List<String> codigoBarras = new ArrayList<>();
    	
    
    	do {	
    		long valor = Instant.now().toEpochMilli();
    		if(!codigoBarras.contains(String.valueOf( valor))) {
    			codigoBarras.add(String.valueOf( valor));
    		}
    		
    	}while(codigoBarras.size() < quantidade );
    	
    	JRBeanCollectionDataSource resultadoDaConsulta = new JRBeanCollectionDataSource(codigoBarras);
		  
    	JasperPrint jasperPrint = JasperFillManager.fillReport(relatorioJasper, parametros, resultadoDaConsulta);
    	
    	bytes = JasperExportManager.exportReportToPdf(jasperPrint);
    	return ResponseEntity
  		      .ok()
  		      .header("Content-Type", "application/pdf; charset=UTF-8")
  		      .header("Content-Disposition", "inline;")
  		      .body(bytes);
		
	}

}
