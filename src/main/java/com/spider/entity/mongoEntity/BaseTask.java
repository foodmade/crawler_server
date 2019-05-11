package com.spider.entity.mongoEntity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.HashMap;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "m_base_task")
@ToString
public class BaseTask {

    @Field
    @ApiModelProperty(value = "爬虫采集url")
    private String url;

    @Field
    @ApiModelProperty(value = "采集最大页码")
    private Integer maxPage;

    @Field
    @ApiModelProperty(value = "任务类型")
    private String task_type;

    @Field
    @ApiModelProperty(value = "描述")
    private String desc;

    @Field
    @ApiModelProperty(value = "任务状态")
    private Integer status;

    @Field
    @ApiModelProperty(value = "父类目Id")
    private Integer parent_id;

    @Field
    @ApiModelProperty(value = "子任务url")
    private HashMap<String,String> childUrl;

    @Field
    @ApiModelProperty(value = "子任务所属类目信息")
    private HashMap<String,String> childCateInfo;


}
