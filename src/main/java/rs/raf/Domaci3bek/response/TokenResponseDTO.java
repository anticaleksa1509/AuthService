package rs.raf.Domaci3bek.response;

import lombok.Data;

@Data
public class TokenResponseDTO {

    private String token;

    public TokenResponseDTO(String token){
        this.token = token;
    }
}
