package com.spider.entity.mongoEntity;

import com.spider.annotation.BranchSave;
import com.spider.annotation.IncKey;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "account")
@ToString
public class Account {

    @Field
    @ApiModelProperty(value = "用户id")
    @IncKey
    @Id
    private Long userId = 0L;

    @Field
    @ApiModelProperty(value = "用户登陆账号")
    private String userName;

    @Field
    @ApiModelProperty(value = "用户名")
    private String userNick;

    @Field
    @ApiModelProperty(value = "密码")
    private String password;

    @Field
    @ApiModelProperty(value = "权限级别")
    private String permissionLevel;

    @Field(value = "userDetailInfo")
    @DBRef
    @BranchSave
    @ApiModelProperty(value = "用户详情")
    private UserDetailInfo userDetailInfo;

}
