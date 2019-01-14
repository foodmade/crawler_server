package com.spider.Vo.inModel;

import com.spider.annotation.SerializeSign;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RegisterModel {

    private Long id;

    private String username;

    private String usernick;

    private String password;

    //标记不需要序列化
    @SerializeSign
    private String confirmPassword;

    @SerializeSign
    private Integer code;

}
