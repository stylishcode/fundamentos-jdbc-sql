package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import conexaojdbc.SingleConnection;
import model.BeanUserFone;
import model.Telefone;
import model.Userpos;

public class UserposDao {

	private Connection connection;

	public UserposDao() {
		/* Instância uma conexão sempre que esse DAO for chamado */
		connection = SingleConnection.getConnection();
	}

	public void salvar(Userpos userpos) {
		String sql = "INSERT INTO userposjava (nome, email) VALUES (?,?)";

		try {
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
			PreparedStatement pstmt = connection.prepareStatement(sql);
			pstmt.setString(2, userpos.getNome());
			pstmt.setString(3, userpos.getEmail());
			pstmt.execute(); // Executa o SQL
			connection.commit(); // Salva no banco de dados

		} catch (Exception e) {

			try {
				connection.rollback(); // Reverte a operação
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();

		} finally {

			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}

	}

	public void salvarTelefone(Telefone userTel) {
		String sql = "INSERT INTO telefoneuser (numero,tipo,usuariopessoa) VALUES (?,?,?)";

		try {
			PreparedStatement insert = connection.prepareStatement(sql);
			insert.setString(1, userTel.getNumero());
			insert.setString(2, userTel.getTipo());
			insert.setLong(3, userTel.getUsuario());
			insert.execute();
			connection.commit();

		} catch (SQLException e) {

			e.printStackTrace();
			try {
				connection.rollback();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}

		} finally {

			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	public List<Userpos> listar() {
		List<Userpos> list = new ArrayList<Userpos>();

		String sql = "SELECT * FROM userposjava";

		try {
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

			/* Percorre cada row(linha) do ResultSet, que contém os resultados do SQL */
			while (resultado.next()) {
				Userpos userpos = new Userpos();
				/*
				 * Pega os campos existem no ResultSet e coloca no objetos userpos que serão
				 * instânciados conforme encontrar userpos
				 */
				userpos.setId(resultado.getLong("id"));
				userpos.setNome(resultado.getString("nome"));
				userpos.setEmail(resultado.getString("email"));
				/* Adiciona os userpos na lista */
				list.add(userpos);
			}

			/* Fecha o result set */
			resultado.close();

		} catch (SQLException e) {
			e.printStackTrace();

		} finally {

			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return list;
	}

	public Userpos buscar(Long id) {
		Userpos userpos = new Userpos();

		String sql = "SELECT * FROM userposjava WHERE ID = ?";

		try {
			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			ResultSet resultado = statement.executeQuery();

			if (resultado.next()) { /*
									 * Posiciono o cursor no primeiro e único resultado encontrado, retorna o usuario
									 * vazio se não encontrar ninguém
									 */
				userpos.setId(resultado.getLong("id"));
				userpos.setNome(resultado.getString("nome"));
				userpos.setEmail(resultado.getString("email"));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {
			
			try {
				connection.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
			
		}
		
		return userpos;
	}

	public List<BeanUserFone> listaUserFone(Long userId) {
		List<BeanUserFone> beanUserFonesList = new ArrayList<BeanUserFone>();

		String sql = " select nome, numero, email from telefoneuser as fone ";
		sql += " inner join userposjava as userpj ";
		sql += " on fone.usuariopessoa = userpj.id ";
		sql += " where userpj.id = ? ";

		try {
			PreparedStatement stmt = connection.prepareStatement(sql);
			stmt.setLong(1, userId);

			ResultSet resultado = stmt.executeQuery();

			while (resultado.next()) {
				BeanUserFone beanUserFone = new BeanUserFone();
				beanUserFone.setEmail(resultado.getString("email"));
				beanUserFone.setNome(resultado.getString("nome"));
				beanUserFone.setNumero(resultado.getString("numero"));

				beanUserFonesList.add(beanUserFone);
			}

			resultado.close();

		} catch (SQLException e) {
			e.printStackTrace();
			
		} finally {

			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		
		return beanUserFonesList;
	}

	public boolean atualizar(Userpos userpos) {
		try {
			String sql = "UPDATE userposjava SET nome = ?, email = ? WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setString(1, userpos.getNome());
			statement.setString(2, userpos.getEmail());
			statement.setLong(3, userpos.getId());
			statement.execute();

			connection.commit();

			return true;

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
			e.printStackTrace();

		} finally {

			try {
				connection.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}

		return false;
	}

	public boolean deletar(Long id) {
		try {
			String sql = "DELETE FROM userposjava WHERE id = ?";

			PreparedStatement statement = connection.prepareStatement(sql);
			statement.setLong(1, id);
			statement.execute();

			connection.commit();

			return true;

		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException e2) {
				e.printStackTrace();
			}
			e.printStackTrace();

		} finally {
			try {
				connection.close();
			} catch (SQLException e2) {
				e2.printStackTrace();
			}
		}
		return false;
	}
}
