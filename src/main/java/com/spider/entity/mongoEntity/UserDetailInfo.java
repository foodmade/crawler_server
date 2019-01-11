package com.spider.entity.mongoEntity;

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
@Document(collection = "userDetailInfo")
public class UserDetailInfo {

    @Id
    private String id;

    @Field
    @ApiModelProperty(value = "身份证号码")
    private String cordId;

    @Field
    @ApiModelProperty(value = "真实姓名")
    private String realName;
}
