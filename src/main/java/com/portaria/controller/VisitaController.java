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
import com.portaria.model.visita.DetalhamentoVisitaDto;
import com.portaria.model.visita.Visita;
import com.portaria.model.visita.VisitaAtualizaraDto;
import com.portaria.model.visita.VisitaCadastrarDto;
import com.portaria.repository.PessoaRepository;
import com.portaria.repository.VisitaRepository;

import lombok.var;

@RestController
@RequestMapping("/visita")
public class VisitaController{

    @Autowired
    private VisitaRepository  repository;
    
    @Autowired
    private PessoaRepository  pessoaRepository;
    
    @GetMapping
    public ResponseEntity <List<Visita> > listarTodos(){
    	               
        return ResponseEntity.ok(repository.findAll() ) ;
    }

    @PostMapping("/buscarPaginado")
    public ResponseEntity<Page<DetalhamentoVisitaDto>> listarPaginada(@RequestBody ParametrosPesquisa parametrosPesquisa){
        PageRequest pageRequest = PageRequest.of(parametrosPesquisa.getPagina(),
        parametrosPesquisa.getTamanho(),
        Sort.Direction.DESC, "id");
        var pagina = repository.buscarPaginado(pageRequest)
        		.map(DetalhamentoVisitaDto::new );

        return ResponseEntity.ok(pagina);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<DetalhamentoVisitaDto> salvar(@RequestBody @Validated VisitaCadastrarDto dto, UriComponentsBuilder uriBuilder){
    	var entidade = new Visita(dto);
    	var pessoa = pessoaRepository.findById(dto.getPessoaId()).get();
    	entidade.setPessoa(pessoa);
    	
    	repository.save(entidade);
    	
    	//var uri = uriBuilder.path("/visita-api/visita/{id}").buildAndExpand(entidadeBanco.getId()).toUri();
        return  ResponseEntity.ok().body(new DetalhamentoVisitaDto(entidade) );
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Transactional
    public ResponseEntity<DetalhamentoVisitaDto> atualizar(@RequestBody @Validated VisitaAtualizaraDto atualizarDto){
    	var entidade = repository.getReferenceById(atualizarDto.getId());
    	entidade.atualizarDados(atualizarDto);
    	
        return  ResponseEntity.ok(new DetalhamentoVisitaDto(entidade)); 
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetalhamentoVisitaDto>  buscaPorId(@PathVariable("id") Long id ){
    	
    	var entidade = repository.getReferenceById( id);
    	
    	return  ResponseEntity.ok(new DetalhamentoVisitaDto(entidade)); 
    }

    @PostMapping("/buscarPorParametrosPaginado")
    public ResponseEntity< Page<Visita>> buscarComParametros(@RequestBody ParametrosPesquisa parametrosPesquisa){
        List<Order> orders = new ArrayList<Order>();
        orders.add( new Order(Sort.Direction.DESC, "id") );     

        PageRequest pageRequest = PageRequest.of(parametrosPesquisa.getPagina(),
        parametrosPesquisa.getTamanho(),
        Sort.by(orders));
        
        Page<Visita>  retorno = repository.findByParametros		(
        		parametrosPesquisa.getNome(), 
        	//	parametrosPesquisa.getDataToTime(),
        		parametrosPesquisa.isAtivo(), 
        		pageRequest) ;
        
       
        return ResponseEntity.ok(retorno);
    }

}