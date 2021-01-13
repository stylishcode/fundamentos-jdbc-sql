package pos_java_jdbc.pos_java_jdbc;

import java.util.List;

import org.junit.Test;

import dao.UserposDao;
import model.BeanUserFone;
import model.Telefone;
import model.Userpos;

public class TesteBancoJdbc {

	@Test
	public void initInsert() {
		UserposDao userposDao = new UserposDao();
		Userpos userpos = new Userpos();
		
		userpos.setNome("Dariane Regine Sacramento Teixeira");
		userpos.setEmail("regineteixeira15@gmail.com");
		
		userposDao.salvar(userpos);
	}

	@Test
	public void initListar() {
		try {
			UserposDao userposDAO = new UserposDao();
			
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
			UserposDao userposDao = new UserposDao();
			Userpos userpos = userposDao.buscar(2L);
			
			System.out.println(userpos);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void initAtualizar() {
		UserposDao userposDao = new UserposDao();
		Userpos userpos = userposDao.buscar(2L); // Retorna um resultado com ID
		userpos.setNome("Dariane Regine Mozão");
		userpos.setEmail("darianeregine123@gmail.com");
		
		boolean atualizado = userposDao.atualizar(userpos);
		
		if (atualizado == true) {
			System.out.println("Atualizado com sucesso!!");
		} else {
			System.out.println("Falha!!");
		}
	}
	
	@Test
	public void initDeletar() {
		UserposDao userposDao = new UserposDao();
		
		boolean deletado = userposDao.deletar(1L);
		
		if (deletado == true) {
			System.out.println("Deletado com sucesso!!");
		} else {
			System.out.println("Falha!!");
		}
	}
	
	@Test
	public void testeInsertTelefone() {
		Telefone telefone = new Telefone();
		telefone.setNumero("(85) 94545-4545");
		telefone.setTipo("Casa");
		telefone.setUsuario(2L);
		
		UserposDao userposDao = new UserposDao();
		userposDao.salvarTelefone(telefone);
	}
	
	@Test
	public void testeCarregaFonesUser() {
		UserposDao userposDao = new UserposDao();
		
		List<BeanUserFone> beanUserFones = userposDao.listaUserFone(2L);
		
		for (BeanUserFone buf : beanUserFones) {
			System.out.println(buf);
		}
	}
	
}
