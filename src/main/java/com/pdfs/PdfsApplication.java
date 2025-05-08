package com.pdfs;

import com.pdfs.myuser.entity.MyUser;
import com.pdfs.myuser.repository.MyUserRepository;
import com.pdfs.roles.entity.Roles;
import com.pdfs.roles.repository.RolesRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class PdfsApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(PdfsApplication.class, args);
		RolesRepository rolesRepository = context.getBean(RolesRepository.class);
		MyUserRepository myUserRepository = context.getBean(MyUserRepository.class);
		if(rolesRepository.findByName("ROLE_USER").isEmpty()){
			Roles role = new Roles("ROLE_USER","This a role only permission the user");
			rolesRepository.save(role);
		}
		if(rolesRepository.findByName("ROLE_ADMIN").isEmpty()){
			Roles roleAdmin = new Roles("ROLE_ADMIN","This a role only permission the admin");
			rolesRepository.save(roleAdmin);
			MyUser myUser = new MyUser("juan","123","juan@gmail.com");
			myUser.setRoles(roleAdmin);
			myUserRepository.save(myUser);
		}
	}

}
