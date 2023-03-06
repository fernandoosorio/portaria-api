package com.portaria.repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Component;

import com.infra.converter.LocalDateTimeConverter;
import com.infra.dominio.Usuario;
import com.portaria.model.ParametrosPesquisa;
import com.portaria.model.pessoa.Pessoa;
import com.portaria.model.visita.Visita;

@Component
public class relatorioDao {
		
	@Autowired
	DataSource dataSource;
	
	
	
	public List<Pessoa> relatorioPessoa(ParametrosPesquisa parametrosPesquisa) throws SQLException {
		Connection con = DataSourceUtils.getConnection(dataSource);
		List<Pessoa> pessoas = new ArrayList<>();
		
		String sql = " select p.id, p.nome as pnome, p.cpf as pcpf,p.data_criacao,"
				+ " u.id as uid,  u.nome as unome , u.matricula , u.cpf as ucpf, u.matricula "
	    		+ "from portaria.pessoa p "
	    		+ "join public.usuario u  on p.id_usuario_cadastrador = u.id "
	    		+ "order by 1 ";
		
		PreparedStatement st = con.prepareStatement(sql);
		
		ResultSet rs = st.executeQuery();
	
		
		while (rs.next()) {
			Pessoa p = new Pessoa();
			p.setId(rs.getLong("id"));
			
			p.setNome(rs.getString("pnome"));
			p.setCpf(rs.getString("pcpf"));
			p.setDataCriacao( new LocalDateTimeConverter().convertToEntityAttribute(rs.getTimestamp("data_criacao"))  );
			
			p.setUsuario( new Usuario());
			p.getUsuario().setId(rs.getLong("uid"));
			p.getUsuario().setNome(rs.getString("unome") );
			p.getUsuario().setCpf(rs.getString("ucpf"));
			p.getUsuario().setMatricula(rs.getString("matricula"));
			pessoas.add(p);
			
		}
		
		return pessoas;
		
	}



	public Collection<Visita> relatorioVisitas(ParametrosPesquisa parametrosPesquisa) throws SQLException {
		Connection con = DataSourceUtils.getConnection(dataSource);
		List<Visita> visitas = new ArrayList<>();
		
		String sql = "select p.nome, v.data_entrada , v.data_saida, v.id, u.nome as unome, u.id as uid "
				+ "from portaria.visita v "
				+ "join portaria.pessoa p  on v.pessoaid = p.id  "
				+ "join public.usuario u on u.id = v.id_usuario_cadastrador ";
		
		PreparedStatement st = con.prepareStatement(sql);
		
		ResultSet rs = st.executeQuery(); 
	
		
		while (rs.next()) {
			Visita v = new Visita();
			v.setId(rs.getLong("id"));
			if( rs.getTimestamp("data_entrada") != null) {
				v.setDataEntrada( new LocalDateTimeConverter().convertToEntityAttribute(rs.getTimestamp("data_entrada")));
			}
			if( rs.getTimestamp("data_saida") != null) {
				v.setDataSaida( new LocalDateTimeConverter().convertToEntityAttribute(rs.getTimestamp("data_saida")) );
			}
			
			v.setDataSaida( new LocalDateTimeConverter().convertToEntityAttribute(rs.getTimestamp("data_saida")) );
			
					
			Pessoa p = new Pessoa();
			p.setNome(rs.getString("nome"));
			
			v.setPessoa(p);
			
			Usuario usuario = new Usuario();
			usuario.setId(rs.getLong("uid"));
			usuario.setNome(rs.getString("unome"));
			v.setUsuario(usuario);
			
			visitas.add(v);
			
		}
		
		return visitas;
	}
}
