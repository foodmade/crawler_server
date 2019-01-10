package com.spider.Vo.inModel;

import com.spider.annotation.SerializeSign;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Document
@Getter
@Setter
@NoArgsConstructor
public class RegisterModel {

    @ApiModelProperty(value = "用户唯一id")
    @Field(value = "userId")
    @Id
    private Long id;

    @ApiModelProperty(value = "用户名")
    private String username;

    @Field
    private String usernick;

    @Field
    private String password;

    @Field
    //标记不需要序列化
    @SerializeSign
    private String confirmPassword;

    @Field
    @SerializeSign
    private Integer code;

}
