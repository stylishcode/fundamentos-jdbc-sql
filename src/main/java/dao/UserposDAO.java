package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
}
