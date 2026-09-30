package com.thanhdat.jwtnimbus.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

    @NotBlank(message = "Họ tên không được để trống")
    @Size(
        min = 2,
        max = 120,
        message = "Họ tên phải có từ 2 đến 120 ký tự"
    )
    String fullName,

    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(
        max = 180,
        message = "Email không được vượt quá 180 ký tự"
    )
    String email,

    @NotBlank(message = "Mật khẩu không được để trống")
    @Size(
        min = 6,
        max = 72,
        message = "Mật khẩu phải có từ 6 đến 72 ký tự"
    )
    String password
) {
}
