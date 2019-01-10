package com.spider.entity.mongoEntity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 用于记录主键生成的表
 * 序列类
 */
@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "seqInfo")
public class SeqInfo{

    @Id
    private String id;// 主键

    @Field
    private String collName;// 集合名称

    @Field
    private Long seqId;// 序列值

}
