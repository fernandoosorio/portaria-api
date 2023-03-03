package com.portaria.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import lombok.Data;

@Data
public class ParametrosPesquisa {

    private String nome;
    private String data;
    private int pagina; 
    private int tamanho;
    private String ativoString;
    private String caminho;
    private String cpf;
    private String matriculaUsuarioLogado;
    private String formatoRelatorio;

 
    public LocalDateTime getDataToTime() {
    	 DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	 if(data != null && !data.isEmpty()) {
    		 LocalDate ld = LocalDate.parse(data, df);
    		 return LocalDateTime.of(ld, LocalDateTime.now().toLocalTime());
    	 }
    	 
    	
    	
    	return  null;
    }

  
    public Boolean isAtivo() {
        if(this.ativoString != null) {
            switch(this.ativoString){
                case "T" : return null; 
                case "A" : return true; 
                case "I" : return false; 
                default : return null;
            }
        }
        return null; 
    }

    public String getAtivoString() {
        return ativoString;
    }

    public void setAtivoString(String ativoString) {
        this.ativoString = ativoString;
    }

    

    
    
}
