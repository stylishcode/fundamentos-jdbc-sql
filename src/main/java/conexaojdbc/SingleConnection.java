package conexaojdbc;

import java.sql.Connection;
import java.sql.DriverManager;

public class SingleConnection {

	private static String url = "jdbc:postgresql://localhost:5432/posjava";
	private static String password = "fallen";
	private static String user = "matheus";
	private static Connection connection = null;

//	Executa uma única vez após o ClassLoader carregar a classe na memória
	static {
		conectar();
	}

	public SingleConnection() {
		conectar();
	}

	private static void conectar() {
		try {
			if (connection == null) {
//				Carrega a classe e registra no DriverManager
				Class.forName("org.postgresql.Driver");
				connection = DriverManager.getConnection(url, user, password);
				/*
				 * Se uma conexão estiver no modo de confirmação automática, todas as instruções
				 * SQL serão executadas e confirmadas como transações individuais. Caso
				 * contrário, suas instruções SQL serão agrupadas em transações que são
				 * terminadas por uma chamada aos métodos commit ou rollback. Por padrão, as
				 * novas conexões ficam no modo de confirmação automática.
				 */
				connection.setAutoCommit(false);
				System.out.println("Conectou com sucesso!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static Connection getConnection() {
		return connection;
	}
}
