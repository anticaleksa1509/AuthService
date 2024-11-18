package rs.raf.Domaci3bek.dto;

import lombok.Data;

@Data
public class UserDto {


    private String email;
    private boolean canRead;
    private boolean canUpdate;
    private boolean canDelete;
    private boolean canCreate;

}
