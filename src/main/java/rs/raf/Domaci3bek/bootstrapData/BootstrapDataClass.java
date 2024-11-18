package rs.raf.Domaci3bek.bootstrapData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.Domaci3bek.model.User;
import rs.raf.Domaci3bek.repository.UserRepo;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class BootstrapDataClass implements CommandLineRunner {

    @Autowired
    UserRepo userRepo;


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

    @Override
    public void run(String... args) throws Exception {
        User user1 = new User();
        user1.setIme("Aleksa");
        user1.setPrezime("Antic");
        user1.setEmail("aleksa@gmail.com");
        user1.setCan_create(false);
        user1.setCan_read(true);
        user1.setCan_delete(true);
        user1.setCan_update(false);
        user1.setSifra(hashSifre("aleksacar123"));

        User user2 = new User();
        user2.setIme("Marko");
        user2.setPrezime("Markovic");
        user2.setEmail("marko@gmail.com");
        user2.setCan_create(true);
        user2.setCan_read(false);
        user2.setCan_delete(false);
        user2.setCan_update(true);
        user2.setSifra(hashSifre("markocar123"));

        User user3 = new User();
        user3.setIme("Stefan");
        user3.setPrezime("Stefanovic");
        user3.setEmail("stefan@gmail.com");
        user3.setCan_create(false);
        user3.setCan_read(true);
        user3.setCan_delete(false);
        user3.setCan_update(false);
        user3.setSifra(hashSifre("stefancar123"));

        User user4 = new User();
        user4.setIme("Admin");
        user4.setPrezime("AdminP");
        user4.setEmail("admin@gmail.com");
        user4.setSifra(hashSifre("admincar123"));
        user4.setCan_update(true);
        user4.setCan_delete(true);
        user4.setCan_create(true);
        user4.setCan_read(true);

        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
        userRepo.save(user4);

    }
}
