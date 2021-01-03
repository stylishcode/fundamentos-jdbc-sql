package pos_java_jdbc.pos_java_jdbc;

import org.junit.Test;

import dao.UserposDAO;
import model.Userpos;

public class TesteBancoJdbc {
	
	@Test
	public void initBanco() {
		UserposDAO userposDAO = new UserposDAO();
		Userpos userpos = new Userpos();
		
		userpos.setId(2L);
		userpos.setNome("Dariane Regine Sacramento Teixeira");
		userpos.setEmail("regineteixeira15@gmail.com");
		
		userposDAO.salvar(userpos);
	}
}
