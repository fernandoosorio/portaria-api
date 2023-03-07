package com.infra.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;

public class DatasUtil {
	
	public static LocalDate getStringDataToLocalDate(String data) {
		LocalDate dataFormatada =  LocalDate.parse(data,DateTimeFormatter.ofPattern("dd/MM/yyyy"));
		
		return dataFormatada;
	}
	
	 public static LocalDateTime getStringToLocalDateTimeInicioDoDia(String data) {
    	 DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    	 DateTimeFormatter DATEFORMATTER = new DateTimeFormatterBuilder().append(df)
    			    .parseDefaulting(ChronoField.HOUR_OF_DAY, 0)
    			    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 0)
    			    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
    			    .toFormatter();
    	 if(data != null && !data.isEmpty()) {
    		 return LocalDateTime.parse(data, DATEFORMATTER);
    	 }
    	 
    	
    	
    	return  null;
    }
    
    public static LocalDateTime getStringToLocalDateTimeFimDoDia(String data) {
   	 DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   	 DateTimeFormatter DATEFORMATTER = new DateTimeFormatterBuilder().append(df)
   			    .parseDefaulting(ChronoField.HOUR_OF_DAY, 23)
   			    .parseDefaulting(ChronoField.MINUTE_OF_HOUR, 59)
   			    .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 59)
   			    .toFormatter();
   	 if(data != null && !data.isEmpty()) {
   		 return LocalDateTime.parse(data, DATEFORMATTER);
   	 }
   	 
   	
   	
   	return  null;
   }

}
