package com.pdfs;

import com.pdfs.myuser.entity.MyUser;
import com.pdfs.myuser.repository.MyUserRepository;
import com.pdfs.permissions.MyPermissions;
import com.pdfs.permissions.MyPermissionsRepository;
import com.pdfs.roles.entity.Roles;
import com.pdfs.roles.repository.RolesRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.net.PasswordAuthentication;
import java.util.Set;

@SpringBootApplication
public class PdfsApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(PdfsApplication.class, args);

		RolesRepository rolesRepository = context.getBean(RolesRepository.class);
		MyUserRepository myUserRepository = context.getBean(MyUserRepository.class);
		PasswordEncoder passwordEncoder = context.getBean(PasswordEncoder.class);

		MyPermissionsRepository permissionsRepository = context.getBean(MyPermissionsRepository.class);
		if(permissionsRepository.findByName("CREATE").isEmpty()){
			MyPermissions create = new MyPermissions("CREATE");
			MyPermissions read = new MyPermissions("READ");
			MyPermissions update = new MyPermissions("UPDATE");
			MyPermissions delete = new MyPermissions("DELETE");
			permissionsRepository.save(create);
			permissionsRepository.save(read);
			permissionsRepository.save(update);
			permissionsRepository.save(delete);
		}
		if(rolesRepository.findByName("USER").isEmpty()){
			Roles rolUser = new Roles("USER","This is a rol only read data.",Set.of(permissionsRepository.findByName("READ").get()));
			rolesRepository.save(rolUser);

			MyUser myUser = new MyUser("farid", passwordEncoder.encode("111"), "farid@gmail.com");
			myUser.setRoles(rolUser);
			myUserRepository.save(myUser);
		}

		if(rolesRepository.findByName("ADMIN").isEmpty()){
			Roles rolAdmin = new Roles("ADMIN","This is a rol with all permission in the system.",
					Set.of(permissionsRepository.findByName("CREATE").get(),
							permissionsRepository.findByName("READ").get(),
							permissionsRepository.findByName("UPDATE").get(),
							permissionsRepository.findByName("DELETE").get()));
			rolesRepository.save(rolAdmin);
			MyUser myUser = new MyUser("Juan", passwordEncoder.encode("222"),"juan@gmail.com" );
			myUser.setRoles(rolAdmin);
			myUserRepository.save(myUser);
		}

	}

}
