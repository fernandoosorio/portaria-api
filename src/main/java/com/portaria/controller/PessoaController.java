package com.portaria.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.portaria.model.ParametrosPesquisa;
import com.portaria.model.pessoa.DetalhamentoPessoaDto;
import com.portaria.model.pessoa.PessoaAtualizaraDto;
import com.portaria.model.pessoa.PessoaCadastrarDto;
import com.portaria.model.pessoa.Pessoa;
import com.portaria.repository.PessoaRepository;

import lombok.var;

@RestController
@RequestMapping("/pessoa")
public class PessoaController{

    @Autowired
    private PessoaRepository  repository;
    
    @GetMapping
    public ResponseEntity <List<Pessoa> > listarTodos(){
    	               
        return ResponseEntity.ok(repository.findAll() ) ;
    }

    @PostMapping("/buscarPaginado")
    public ResponseEntity<Page<DetalhamentoPessoaDto>> listarPaginada(@RequestBody ParametrosPesquisa parametrosPesquisa){
        PageRequest pageRequest = PageRequest.of(parametrosPesquisa.getPagina(),
        parametrosPesquisa.getTamanho(),
        Sort.Direction.ASC, "id");
        var pagina = repository.buscarPaginado(pageRequest)
        		.map(DetalhamentoPessoaDto::new );

        return ResponseEntity.ok(pagina);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DetalhamentoPessoaDto> salvar(@RequestBody @Validated PessoaCadastrarDto laudoDto, UriComponentsBuilder uriBuilder){
    	var laudo = new Pessoa(laudoDto);
    	
    	repository.save(laudo);
    	var uri = uriBuilder.path("/eticket-api/laudos/{id}").buildAndExpand(laudo.getId()).toUri();
        return  ResponseEntity.created(uri).body(new DetalhamentoPessoaDto(laudo) );
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Transactional
    public ResponseEntity<DetalhamentoPessoaDto> atualizar(@RequestBody @Validated PessoaAtualizaraDto atualizarDto){
    	var laudo = repository.getReferenceById(atualizarDto.getId());
    	laudo.atualizarDados(atualizarDto);
    	
        return  ResponseEntity.ok(new DetalhamentoPessoaDto(laudo)); 
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetalhamentoPessoaDto>  buscaPorId(@PathVariable("id") Long laudoId ){
    	
    	var laudo = repository.getReferenceById( laudoId);
    	
    	return  ResponseEntity.ok(new DetalhamentoPessoaDto(laudo)); 
    }

    @PostMapping("/buscarPorParametrosPaginado")
    public ResponseEntity< Page<Pessoa>> buscarComParametros(@RequestBody ParametrosPesquisa parametrosPesquisa){
        List<Order> orders = new ArrayList<Order>();
        orders.add( new Order(Sort.Direction.DESC, "ativo") );
        orders.add( new Order(Sort.Direction.ASC, "id") );

        PageRequest pageRequest = PageRequest.of(parametrosPesquisa.getPagina(),
        parametrosPesquisa.getTamanho(),
        Sort.by(orders));
        
        Page<Pessoa>  retorno = repository.findByParametros		(
        		parametrosPesquisa.getNome(), 
        	//	parametrosPesquisa.getDataToTime(),
        		parametrosPesquisa.isAtivo(), 
        		pageRequest) ;
        
       
        return ResponseEntity.ok(retorno);
    }

}