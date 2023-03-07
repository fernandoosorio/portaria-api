package com.portaria.model;

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
    private String dataInicioPesquisa;
    private String dataFimPesquisa;
    
    
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
