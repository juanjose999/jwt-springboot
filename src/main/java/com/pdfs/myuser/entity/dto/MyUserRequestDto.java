package com.pdfs.myuser.entity.dto;

import org.springframework.validation.annotation.Validated;

public record MyUserRequestDto(@Validated String name, @Validated String email, @Validated String password) {
}
