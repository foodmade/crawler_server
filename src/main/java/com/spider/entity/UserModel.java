package com.spider.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class UserModel {

    private String usernick;

    private String email;

    private String password;

    private String code;
}
