package rs.raf.Domaci3bek.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import rs.raf.Domaci3bek.dto.UserDto;
import rs.raf.Domaci3bek.mapper.UserMapper;
import rs.raf.Domaci3bek.model.User;
import rs.raf.Domaci3bek.repository.UserRepo;
import rs.raf.Domaci3bek.request.TokenRequestDTO;
import rs.raf.Domaci3bek.response.TokenResponseDTO;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    UserRepo userRepo;
    @Autowired
    TokenService tokenService;
    @Autowired
    UserMapper userMapper;

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

    public TokenResponseDTO login(TokenRequestDTO tokenRequestDTO){
//        List<User> korisnici = userRepo.findAll();
//        for(User u : korisnici){
//            if(u.getEmail().equals(tokenRequestDTO.getEmail())){
//                Long id_korisnika = u.getId_user();
//                User user = userRepo.findById(id_korisnika).orElseThrow();
//                throw new EntityNotFoundException("Postoji korisnik sa istom e-mail adresom a to je korisnik" +
//                        " " + user.getIme() + " " + user.getPrezime());
//            }
//        }
        User user = userRepo.findUserByEmailAndSifra(tokenRequestDTO.getEmail(),
                hashSifre(tokenRequestDTO.getSifra())).orElseThrow(() ->
                new EntityNotFoundException("Nije nadjen korisnik sa unetim kredencijalima"));
        //sada ukoliko smo nasli korisnika mozemo da kreiramo claimove
        Claims claims = Jwts.claims();
        claims.put("id",user.getId_user());
        claims.put("email",user.getEmail());
        claims.put("can_read",user.isCan_read());
        claims.put("can_delete",user.isCan_delete());
        claims.put("can_update",user.isCan_update());
        claims.put("can_create",user.isCan_create());


        return new TokenResponseDTO(tokenService.generateToken(claims));

    }

    public List<UserDto> nadjiSveKorisnike(){
        return userRepo.findAll().stream().map(userMapper::UserToUserDto).toList();
    }

    public void sacuvajKorisnika(User user){
        user.setSifra(hashSifre(user.getSifra()));
         userRepo.save(user);
    }
    public ResponseEntity<?> deleteById(Integer user_id){
        User user = userRepo.findById(Long.valueOf(user_id)).orElseThrow(() ->
                new EntityNotFoundException("Nije pronadjen korisnik sa tim Id-jem"));
        userRepo.deleteById(Long.valueOf(user_id));
        return ResponseEntity.ok().body("Uspesno obrisan");
    }

    public void updateUser(Integer id_user,String email,String ime,
                           String prezime,String sifra,Boolean can_create,Boolean can_read,
                           Boolean can_update, Boolean can_delete) throws Exception {
        User user = userRepo.findById(Long.valueOf(id_user)).orElseThrow(() ->
                new EntityNotFoundException("Korisnik sa unetim id-jem ne postoji"));

        user.setSifra(hashSifre(sifra));
        user.setIme(ime);
        user.setPrezime(prezime);
        user.setEmail(email);
        user.setCan_delete(can_delete);
        user.setCan_read(can_read);
        user.setCan_update(can_update);
        user.setCan_create(can_create);

        List<User> list = userRepo.findAll();
        Optional<User> optionalUser = userRepo.findUserByEmail(email);

        if(optionalUser.isPresent())
            throw new Exception("Postoji korisnik sa tom email adresom");

        userRepo.save(user);
    }
}
