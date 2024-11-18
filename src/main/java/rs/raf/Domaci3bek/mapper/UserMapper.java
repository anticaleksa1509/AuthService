package rs.raf.Domaci3bek.mapper;

import org.springframework.stereotype.Component;
import rs.raf.Domaci3bek.dto.UserDto;
import rs.raf.Domaci3bek.model.User;

@Component
public class UserMapper {

    public UserDto UserToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setEmail(user.getEmail());
        userDto.setCanDelete(user.isCan_delete());
        userDto.setCanRead(user.isCan_read());
        userDto.setCanUpdate(user.isCan_update());
        userDto.setCanCreate(user.isCan_create());
        return userDto;
    }

}
