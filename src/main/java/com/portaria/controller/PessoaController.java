package com.portaria.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.portaria.model.ParametrosPesquisa;
import com.portaria.model.pessoa.DetalhamentoPessoaDto;
import com.portaria.model.pessoa.FileStorageService;
import com.portaria.model.pessoa.Pessoa;
import com.portaria.model.pessoa.PessoaAtualizaraDto;
import com.portaria.model.pessoa.PessoaCadastrarDto;
import com.portaria.repository.PessoaRepository;

import lombok.var;

@RestController
@RequestMapping("/pessoa")
public class PessoaController{

    @Autowired
    private PessoaRepository  repository;
    @Autowired
    private FileStorageService fileStorageService;
    
    
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
    public ResponseEntity< DetalhamentoPessoaDto > salvar(@RequestPart(name="pessoa", required=false) PessoaCadastrarDto dto
    					,@RequestPart(name="foto", required=false) MultipartFile file){
    	
    	if(isCpfCadastrado(dto.getCpf())) {
    		throw new RuntimeException("Pessoa já cadastrada no sistema. Pesquise pelo CPF! ");
    	}
    	var entidade = new Pessoa(dto);
    	
    	if(file != null)  {
    		String enderecoFoto = fileStorageService.storeFile(file);
    		entidade.setCaminhoFoto(enderecoFoto);
    	}
    		
		repository.save(entidade);
    	
		return  ResponseEntity.ok()
				.body(new DetalhamentoPessoaDto(entidade)); 
    }
    
    private boolean isCpfCadastrado(String cpf) {
    	if(cpf != null && !cpf.isEmpty()) {
    		List<Pessoa> pessoas = repository.findByCpf(cpf);
    		if(pessoas != null && !pessoas.isEmpty()) {
    			return true;
    		}
    	}
		
		return false;
	}

	@GetMapping("/foto/{pessoaid}")
    @ResponseBody
    public ResponseEntity<?>  buscaFotoByPath(@PathVariable Long pessoaid ) throws IOException{
    	
    	var pessoa = repository.findById(pessoaid).get();
    	if(pessoa.getCaminhoFoto() == null) {
    		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registro sem foto cadastrada");
    	}
    
    	byte[] image = fileStorageService.downloadImageFromFileSystem( pessoa.getCaminhoFoto()   );
    	
    	
    	
    	return ResponseEntity.status(HttpStatus.OK)
				.body(image);
    }

    @PutMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Transactional
    public ResponseEntity<DetalhamentoPessoaDto> atualizar(@RequestBody @Validated PessoaAtualizaraDto atualizarDto){
    	var entidade = repository.getReferenceById(atualizarDto.getId());
    	entidade.atualizarDados(atualizarDto);
    	
        return  ResponseEntity.ok()
        						.body(new DetalhamentoPessoaDto(entidade)); 
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetalhamentoPessoaDto>  buscaPorId(@PathVariable("id") Long id ){
    	
    	var entidade = repository.getReferenceById( id);
    	
    	return  ResponseEntity.ok(new DetalhamentoPessoaDto(entidade)); 
    }
    
   

    @PostMapping("/buscarPorParametrosPaginado")
    public ResponseEntity< Page<Pessoa>> buscarComParametros(@RequestBody ParametrosPesquisa parametrosPesquisa){
        List<Order> orders = new ArrayList<Order>();
        orders.add( new Order(Sort.Direction.DESC, "ativo") );
        orders.add( new Order(Sort.Direction.ASC, "id") );

        PageRequest pageRequest = PageRequest.of(parametrosPesquisa.getPagina(),
        parametrosPesquisa.getTamanho(), Sort.by(orders) );
        
        Page<Pessoa>  retorno = repository.findByParametros		(
        		parametrosPesquisa.getNome(), 
        	//	parametrosPesquisa.getDataToTime(),
        		parametrosPesquisa.isAtivo(), 
        		pageRequest) ;
        
       
        return ResponseEntity.ok(retorno);
    }
    

}