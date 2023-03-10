package com.portaria.controller;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.infra.dominio.Usuario;
import com.portaria.model.ParametrosPesquisa;
import com.portaria.model.visita.DetalhamentoVisitaDto;
import com.portaria.model.visita.Visita;
import com.portaria.model.visita.VisitaAtualizaraDto;
import com.portaria.model.visita.VisitaCadastrarDto;
import com.portaria.repository.PessoaRepository;
import com.portaria.repository.VisitaRepository;
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
@RequestMapping("/visita")
public class VisitaController{

    @Autowired
    private VisitaRepository  repository;
    
    @Autowired
    private PessoaRepository  pessoaRepository;
    
    @Autowired
    private relatorioDao relatorioDao;
    
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
    public ResponseEntity< Page<DetalhamentoVisitaDto>> buscarComParametros(@RequestBody ParametrosPesquisa parametrosPesquisa){
        List<Order> orders = new ArrayList<Order>();
        orders.add( new Order(Sort.Direction.DESC, "id") );     

        PageRequest pageRequest = PageRequest.of(parametrosPesquisa.getPagina(),
        parametrosPesquisa.getTamanho(),
        Sort.by(orders));
        
        var  retorno = repository.findByParametros		(
        		parametrosPesquisa.getNome(), 
        		parametrosPesquisa.getCpf(), 
        	//	parametrosPesquisa.getDataToTime(),
        		parametrosPesquisa.isAtivo(), 
        		pageRequest)
        		.map(DetalhamentoVisitaDto::new )
        		;
        
       
        return ResponseEntity.ok(retorno);
    }
    
    @PostMapping("/relatorio")
    public ResponseEntity<byte[]> criarRelatorio(@RequestBody ParametrosPesquisa parametrosPesquisa) throws FileNotFoundException, JRException, SQLException{
    	Map<String,Object> parametros = new HashMap<>();
    	byte[] bytes = null;
    	parametros.put("APP_REPORT_PATH",ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX ).getAbsolutePath() 
    			);
    	parametros.put("USUARIO_LOGADO", new Usuario().getUsuarioLogado()); 
    	parametros.put("APP_TITULO_RELATORIO", "Relatório de visitas");
    	String relatorioJasper = ResourceUtils.getFile(ResourceUtils.CLASSPATH_URL_PREFIX + 
    			"relatorios/portaria_visitas.jasper").getAbsolutePath();
    	Collection<Visita> visitas = relatorioDao.relatorioVisitas(parametrosPesquisa);
    	if( visitas != null && visitas.size() > 0) {
    		JRBeanCollectionDataSource resultadoDaConsulta = new JRBeanCollectionDataSource(visitas);
    		  
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
    	}else {
    		return ResponseEntity
      		      .notFound().build();
    	}
    	
    	
    	
    }
    @GetMapping("/iscartaoemuso/{idCartao}")
    @ResponseBody
    public ResponseEntity<Boolean>  isCartaoVisitaEmUso(@PathVariable("idCartao") String cartao ){
    	
    	var entidades = repository.findByCodigoVisitaAndDataSaidaIsNull(cartao);
    	if(entidades != null && entidades.size() > 0) {
    		return  ResponseEntity.ok(Boolean.TRUE); 
    	}
    	return  ResponseEntity.ok(Boolean.FALSE); 
    }
    
    

}