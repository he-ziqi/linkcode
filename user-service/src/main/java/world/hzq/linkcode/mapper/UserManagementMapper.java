package world.hzq.linkcode.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import world.hzq.linkcode.dto.RegistrationDTO;
import world.hzq.linkcode.entity.User;

/**
 * @author hzq
 * @version 1.0
 * @description TODO
 * @date 2023/2/6 19:27
 */
@Mapper
public interface UserManagementMapper {

    User getUserByNickName(String nickName);

    User getUserByEmail(String email);

    User getUserByPhoneNumber(String phoneNumber);

    Integer addUser(@Param("userInfo") RegistrationDTO userInfo);

    User getUserByPhoneNumberAndPassword(@Param("phoneNumber") String phoneNumber,@Param("password") String password,@Param("roleType") String roleType);

    User getUserByNickNameAndPassword(@Param("nickName") String nickName,@Param("password") String password,@Param("roleType") String roleType);

    User getUserById(@Param("id") Long id);

    Integer updatePasswordById(@Param("id") Long id,@Param("password") String password);

    Integer updateAvatarPathById(@Param("id") Long id,@Param("avatarPath") String avatarPath);

    Integer updatePasswordByPhoneNumberAndEmail(@Param("phoneNumber") String phoneNumber,@Param("mail") String mail,@Param("newPassword") String newPassword);

    Integer addRole(@Param("userId") Long userId,@Param("roleName") String roleName,@Param("authorityLevel") Short authorityLevel);

    User getUserByEmailAndRole(@Param("email") String email,@Param("roleType") String roleType);
}
