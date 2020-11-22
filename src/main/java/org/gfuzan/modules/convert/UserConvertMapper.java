package org.gfuzan.modules.convert;

import java.util.List;

import org.gfuzan.modules.dto.UserVo;
import org.gfuzan.modules.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserConvertMapper {
   User toEntity(UserVo vo);

   UserVo toDto(User vo);

   List<User> toEntity(List<UserVo> vo);

   List<UserVo> toDto(List<User> vo);
}
