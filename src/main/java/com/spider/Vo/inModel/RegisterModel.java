package com.spider.Vo.inModel;

import com.spider.annotation.SerializeSign;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

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
