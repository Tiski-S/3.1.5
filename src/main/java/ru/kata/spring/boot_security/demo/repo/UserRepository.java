package ru.kata.spring.boot_security.demo.repo;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.kata.spring.boot_security.demo.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

//    @EntityGraph(attributePaths = {"roles"})
//    User findByUsername( String username);
@Query("select u from User u where u.username = :username")
User findByUsername(@Param("username") String username);


}
