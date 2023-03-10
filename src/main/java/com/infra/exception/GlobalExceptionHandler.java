package com.infra.exception;

import java.io.FileNotFoundException;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.crossstore.ChangeSetPersister.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException.Forbidden;

@ControllerAdvice
public class GlobalExceptionHandler{

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ExceptionDto> NotFoundException(NotFoundException e){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
            new ExceptionDto(HttpStatus.NOT_FOUND, e.getMessage())
        );
    }

    @ExceptionHandler(Forbidden.class)
    public ResponseEntity<ExceptionDto> ForbiddenException(Forbidden e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
            new ExceptionDto(HttpStatus.FORBIDDEN, e.getMessage())
        );
    }

   

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ExceptionDto> IllegalArgumentException(IllegalArgumentException e){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ExceptionDto(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ExceptionDto> DataIntegrityViolationException(DataIntegrityViolationException e){

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
            new ExceptionDto(HttpStatus.BAD_REQUEST, e.getMessage())
        );
    } 
    
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionDto> BadRequestException(BadCredentialsException e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
        		.body( new ExceptionDto(HttpStatus.FORBIDDEN,  "Usuário ou senha incorretos!")
        );
    }
    
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public ResponseEntity<ExceptionDto> InternalAuthenticationServiceException(InternalAuthenticationServiceException e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
        		.body( new ExceptionDto(HttpStatus.FORBIDDEN, "Usuário ou senha incorretos!")
        );
    }
    
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionDto> RuntimeException(RuntimeException e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
        		.body( new ExceptionDto(HttpStatus.FORBIDDEN, e.getMessage())
        );
    }
    
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ExceptionDto> FileNotFoundException(FileNotFoundException e){

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
        		.body( new ExceptionDto(HttpStatus.FORBIDDEN, e.getMessage())
        );
    }
    
  
	

}
