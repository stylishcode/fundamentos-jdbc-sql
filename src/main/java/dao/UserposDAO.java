package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import conexaojdbc.SingleConnection;
import model.Userpos;

public class UserposDAO {

	private Connection connection;

	public UserposDAO() {
//		Instância uma conexão sempre que esse DAO for chamado
		connection = SingleConnection.getConnection();
	}

	public void salvar(Userpos userpos) {
		try {
			String sql = "INSERT INTO userposjava (id, nome, email) VALUES (?,?,?)";
			/*
			 * uma forma de você fazer uma inserção no banco mais segura, onde você prepara
			 * os parametros para serem inseridos.
			 * 
			 * evitando assim ataques como o sql injection.
			 * 
			 * O PreparedStatement não só é mais seguro, mas também trata automaticamente
			 * caracteres como as '. Ele também lida sozinho com formatos de datas (que não
			 * são padronizados no SQL puro, e portanto, dependem do banco) e outros
			 * detalhes chatos.
			 * 
			 * Finalmente, o PreparedStatement mantém a query pré-compilada no banco de
			 * dados. Assim, se vc for enviar várias vezes a mesma query, a execução
			 * provavelmente será muito mais rápida, já que vc só precisa trocar os valores
			 * associados a consulta, reaproveitando o plano de execução já criados.
			 * 
			 * Existe o statement, que não faz nada do que o acima faz, então sempre use
			 * preparedStatement sempre que possível
			 */
			PreparedStatement insert = connection.prepareStatement(sql);
			insert.setLong(1, userpos.getId());
			insert.setString(2, userpos.getNome());
			insert.setString(3, userpos.getEmail());
			insert.execute(); // Executa o SQL
			connection.commit(); // Salva no banco de dados

		} catch (Exception e) {
			try {
				connection.rollback(); // Reverte a operação
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();
		}

	}

	public List<Userpos> listar() {
		try {
			List<Userpos> list = new ArrayList<Userpos>();
			String sql = "SELECT * FROM userposjava";
			/*
			 * Uma instrução/Statement é uma interface que representa uma instrução SQL.
			 * Você executa objetos Statement e eles geram objetos ResultSet, que é uma
			 * tabela de dados que representa um conjunto de resultados do banco de dados.
			 * Você precisa de um objeto Connection para criar um objeto Statement.
			 */

			PreparedStatement statement = connection.prepareStatement(sql);
			/*
			 * Um ResultSet é um objeto Java que contém os resultados da execução de uma
			 * consulta SQL. Em outras palavras, ele contém as linhas que satisfazem as
			 * condições da consulta. Os dados armazenados em um objeto ResultSet são
			 * recuperados por meio de um conjunto de métodos get que permite acesso às
			 * várias colunas da linha atual. O método ResultSet.next() é usado para mover
			 * para a próxima linha do ResultSet, tornando-se a linha atual.
			 */
			ResultSet resultado = statement.executeQuery();
			
			/*Percorre cada row(linha) do ResultSet, que contém os resultados do SQL*/
			while (resultado.next()) {
				Userpos userpos = new Userpos();
//				Pega os campos existem no ResultSet e coloca no objetos userpos que serão instânciados conforme encontrar userpos
				userpos.setId(resultado.getLong("id"));
				userpos.setNome(resultado.getString("nome"));
				userpos.setEmail(resultado.getString("email"));
				// Adiciona os userpos na lista
				list.add(userpos);
			}
			
			return list; 
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public Userpos buscar(Long id) {
		try {
			Userpos userpos = new Userpos();
			String sql = "SELECT * FROM userposjava WHERE ID = ?";
		
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			ResultSet resultado = statement.executeQuery();
			
			if (resultado.next()) { //Posiciono o cursor no primeiro e único resultado encontrado, pelo menos é esperado encontrar apenas um!
				userpos.setId(resultado.getLong("id"));
				userpos.setNome(resultado.getString("nome"));
				userpos.setEmail(resultado.getString("email"));	
				
				return userpos;
			}
			
			System.out.println("ID não encontrado!");
			return null;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
}
