package rs.raf.Domaci3bek.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
@Data
//@Table(indexes = {@Index(columnList = "email", unique = true)})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id_user;



    @Column(unique = true)
    private String email;//ne moze da se kreira vise korisnika sa istim username-om
    private String ime;
    private String prezime;
    private boolean can_read;
    private boolean can_create;
    private boolean can_update;
    private boolean can_delete;
    private String sifra;


}
