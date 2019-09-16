package pl.twitterApp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import pl.twitterApp.entity.User;

public interface UserRepository extends JpaRepository<User, Long>{

	User findOneByEmail(String email);

	
}
