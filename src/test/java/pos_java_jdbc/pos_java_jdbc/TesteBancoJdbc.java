package pos_java_jdbc.pos_java_jdbc;

import java.util.List;

import org.junit.Test;

import dao.UserposDAO;
import model.Userpos;

public class TesteBancoJdbc {

	@Test
	public void initInsert() {
		UserposDAO userposDAO = new UserposDAO();
		Userpos userpos = new Userpos();
		
		userpos.setId(2L);
		userpos.setNome("Dariane Regine Sacramento Teixeira");
		userpos.setEmail("regineteixeira15@gmail.com");
		
		userposDAO.salvar(userpos);
	}

	@Test
	public void initListar() {
		try {
			UserposDAO userposDAO = new UserposDAO();
			
			for (Userpos userpos : userposDAO.listar()) {
				System.out.println(userpos);
				System.out.println("=======================================");
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void initBuscar() {
		try {
			UserposDAO userposDAO = new UserposDAO();
			Userpos userpos = userposDAO.buscar(2L);
			
			System.out.println(userpos);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
