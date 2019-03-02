package com.spider.entity.mongoEntity;

import com.spider.annotation.BranchSave;
import com.spider.annotation.Encrypted;
import com.spider.annotation.IncKey;
import com.spider.listener.Inckeystrategy.IncreaseStrategy;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;
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
    @IncKey(strategyCls = IncreaseStrategy.class)
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
    @Encrypted
    private String password;

    @Field
    @ApiModelProperty(value = "权限级别")
    private int permissionLevel;

    @Field(value = "userDetailInfo")
    @DBRef
    @BranchSave
    @ApiModelProperty(value = "用户详情")
    private UserDetailInfo userDetailInfo;

}
