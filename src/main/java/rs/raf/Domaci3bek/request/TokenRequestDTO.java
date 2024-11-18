package rs.raf.Domaci3bek.request;

import lombok.Data;

@Data
public class TokenRequestDTO {

    private String email;
    private String sifra;

    public TokenRequestDTO(String email, String sifra) {
        this.email = email;
        this.sifra = sifra;
    }
}
