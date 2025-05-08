package com.pdfs.myuser.entity.dto;

import com.pdfs.myuser.entity.MyUser;

public class MyUserMapper {

    public static MyUser myUserRequestDtoToMyUser(MyUserRequestDto myUserRequestDto) {
        return new MyUser(myUserRequestDto.name(), myUserRequestDto.password(), myUserRequestDto.email());
    }

    public static MyUserResponseDto myUserToMyUserResponseDto(MyUser myUser) {
        return new MyUserResponseDto(myUser.getEmail());
    }

}
