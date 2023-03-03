package com.portaria.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
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

import com.infra.dominio.Usuario;
import com.portaria.model.ParametrosPesquisa;
import com.portaria.model.pessoa.DetalhamentoPessoaDto;
import com.portaria.model.pessoa.FileStorageService;
import com.portaria.model.pessoa.Pessoa;
import com.portaria.model.pessoa.PessoaAtualizaraDto;
import com.portaria.model.pessoa.PessoaCadastrarDto;
import com.portaria.repository.PessoaRepository;
import com.portaria.repository.relatorioDao;

import lombok.var;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;

@RestController
@RequestMapping("/pessoa")
public class PessoaController{

    @Autowired
    private PessoaRepository  repository;
    @Autowired
    private FileStorageService fileStorageService;
    @Autowired
    private relatorioDao relatorioDao;
    
    
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
    					,@RequestPart(name="foto", required=false) MultipartFile file) throws IOException{
    	
    	if(isCpfCadastrado(dto.getCpf())) {
    		throw new RuntimeException("Pessoa já cadastrada no sistema. Pesquise pelo CPF! ");
    	}
    	var entidade = new Pessoa(dto);
    	
    	if(file != null)  {
    		String enderecoFoto = fileStorageService.storeFile(file);
    		entidade.setCaminhoFoto(enderecoFoto);
    	}
    		
		repository.save(entidade);
		
		//retornar a foto para o front end
		if(entidade.getCaminhoFoto() != null) {
    		entidade.setFotoSalva( fileStorageService.downloadImageFromFileSystem( entidade.getCaminhoFoto()   ) );
    	}
    	
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

    @PutMapping(consumes = "multipart/form-data")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Transactional
    public ResponseEntity<DetalhamentoPessoaDto> atualizar(@RequestPart(name="pessoa", required=false) @Validated PessoaAtualizaraDto dto
			,@RequestPart(name="foto", required=false) MultipartFile file) throws IOException{
    	var entidade = repository.getReferenceById(dto.getId());
    	
    	
    	if(file != null)  {
    		String enderecoFoto = fileStorageService.storeFile(file);
    		dto.setCaminhoFoto(enderecoFoto);
    	}
    		
    	entidade.atualizarDados(dto);
		
		//retornar a foto para o front end
		if(entidade.getCaminhoFoto() != null) {
    		entidade.setFotoSalva( fileStorageService.downloadImageFromFileSystem( entidade.getCaminhoFoto()   ) );
    	}
    	
    	
    	
    	
        return  ResponseEntity.ok()
        						.body(new DetalhamentoPessoaDto(entidade)); 
    }

    @GetMapping("/{id}")
    @ResponseBody
    public ResponseEntity<DetalhamentoPessoaDto>  buscaPorId(@PathVariable("id") Long id ) throws IOException{
    	
    	var entidade = repository.getReferenceById( id);
    	//retornar a foto para o front end
    	if(entidade.getCaminhoFoto() != null) {
    		entidade.setFotoSalva( fileStorageService.downloadImageFromFileSystem( entidade.getCaminhoFoto()   ) );
    	}
		 
    	
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
        		parametrosPesquisa.getCpf(), 
        	//	parametrosPesquisa.getDataToTime(),
        		parametrosPesquisa.isAtivo(), 
        		pageRequest) ;
        
       
        return ResponseEntity.ok(retorno);
    }
    
    @PostMapping("/relatorio")
    public ResponseEntity<byte[]> criarRelatorio(@RequestBody ParametrosPesquisa parametrosPesquisa) throws FileNotFoundException, JRException, SQLException{
    	Map<String,Object> parametros = new HashMap<>();
    	byte[] bytes = null;
    	parametros.put("APP_REPORT_PATH",ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX ).getAbsolutePath() 
    			);
    	parametros.put("INT_MATRICULA_LOGADO_ID", new Usuario().getUsuarioLogado()); 
    	parametros.put("APP_TITULO_RELATORIO", "Relatório de pessoas cadastradas");
    	String relatorioJasper = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + 
    			"relatorios/portaria_pessoas_geral.jasper").getAbsolutePath();
    	JRBeanCollectionDataSource resultadoDaConsulta = new JRBeanCollectionDataSource( relatorioDao.relatorioPessoa(parametrosPesquisa) );
  
    	JasperPrint jasperPrint = JasperFillManager.fillReport(relatorioJasper, parametros, resultadoDaConsulta);
    	if("pdf".equals(parametrosPesquisa.getFormatoRelatorio())) {
    		bytes = JasperExportManager.exportReportToPdf(jasperPrint);
        	return ResponseEntity
        		      .ok()
        		      .header("Content-Type", "application/pdf; charset=UTF-8")
        		      .header("Content-Disposition", "inline;")
        		      .body(bytes);
    	}else {
    		 var input = new SimpleExporterInput(jasperPrint);
    		 var byteArray = new ByteArrayOutputStream();
    		 var output = new SimpleOutputStreamExporterOutput(byteArray);
    		 var exporter = new JRXlsxExporter();
            
    		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
    		configuration.setSheetNames(new String[] { "plan1" });
            configuration.setOnePagePerSheet(false);
            configuration.setDetectCellType(true);
            configuration.setRemoveEmptySpaceBetweenRows(true);
            configuration.setWhitePageBackground(false);
            exporter.setConfiguration(configuration);
            
            exporter.setExporterInput(input);
            exporter.setExporterOutput(output);
            exporter.exportReport(); 
            bytes = byteArray.toByteArray();
            output.close();
        	return ResponseEntity
        		      .ok()
        		      .header("Content-Type", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet; charset=UTF-8")
        		      .header("Content-Disposition", "inline;" )
        		      .body(bytes);
    	}
    	
    	
    }
    

}