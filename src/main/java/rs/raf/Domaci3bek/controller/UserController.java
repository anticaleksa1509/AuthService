package rs.raf.Domaci3bek.controller;

import jakarta.validation.Valid;
import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rs.raf.Domaci3bek.bootstrapData.BootstrapDataClass;
import rs.raf.Domaci3bek.dto.UserDto;
import rs.raf.Domaci3bek.model.User;
import rs.raf.Domaci3bek.repository.UserRepo;
import rs.raf.Domaci3bek.security.Can_Create;
import rs.raf.Domaci3bek.security.Can_Delete;
import rs.raf.Domaci3bek.security.Can_read;
import rs.raf.Domaci3bek.security.Can_update;
import rs.raf.Domaci3bek.service.UserService;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    UserRepo userRepo;
    @Autowired
    UserService userService;

    public static String hashSifre(String sifra) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(sifra.getBytes());

            // Pretvaranje bajtova u heksadecimalni format
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Can_Create
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE,value = "/create")
    public ResponseEntity<?> kreirajKorisnika(@RequestHeader("Authorization")
                                                  String authorization,
                                              @Valid @RequestBody User user){

        Optional<User> postojeci = userRepo.findUserByEmail(user.getEmail());
        if(postojeci.isPresent()){
            return ResponseEntity.badRequest().body("Korisnik sa unetom email adresom " +
                    "vec postoji");
        }else{
            userService.sacuvajKorisnika(user);
            return ResponseEntity.ok("Uspesno kreiran korisnik");

        }

    }
    @Can_read()
    @GetMapping(value = "/sviKorisnici",produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<UserDto>> sviKorisnici(@RequestHeader("Authorization")
                                                          String authorization){
        return new ResponseEntity<>(userService.nadjiSveKorisnike(), HttpStatus.OK);
    }

    @Can_Delete()
    @DeleteMapping("/poId/{id_user}")
    public ResponseEntity<?> obrisi(@RequestHeader("Authorization") String authorization,
                                    @PathVariable Integer id_user){
        try {
            userService.deleteById(id_user);
            return ResponseEntity.ok().body("Uspesno obrisano");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

        @Can_update()
        @PutMapping(
            value = "/{id_user}/{email}/{ime}/{prezime}/{sifra}/{can_create}/{can_read}" +
                    "/{can_update}/{can_delete}")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authorization,
                                        @PathVariable Integer id_user,
                                        @PathVariable String email,
                                        @PathVariable String ime,@PathVariable String prezime,
                                        @PathVariable String sifra,
                                        @PathVariable Boolean can_create,
                                        @PathVariable Boolean can_read,
                                        @PathVariable Boolean can_update,
                                        @PathVariable Boolean can_delete){


        try {
            userService.updateUser(id_user,email,ime,prezime,sifra,can_create,can_read,
                    can_update,can_delete);
            return ResponseEntity.ok().body("Uspesan update korisnika");
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @Can_read()
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE,value = "/{email}/{sifra}")
    public ResponseEntity<?> nadji(@RequestHeader("Authorization") String authorization,
                                       @PathVariable String email,@PathVariable String sifra) {
        Optional<User> optionalUser = userRepo.findUserByEmailAndSifra(email,hashSifre(sifra));
        if(optionalUser.isPresent())
            return ResponseEntity.ok().body("Nadjen je korisnik" + optionalUser.get());
        return ResponseEntity.badRequest().body("Nije nadjen korsnik");

    }
}
