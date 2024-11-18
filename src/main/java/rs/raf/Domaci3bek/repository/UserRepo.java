package rs.raf.Domaci3bek.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rs.raf.Domaci3bek.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepo extends JpaRepository<User,Long> {

    Optional<User> findUserByEmail(String email);
    Optional<User> findUserByEmailAndSifra(String email,String sifra);







}
