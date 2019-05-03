package com.spider.entity.mongoEntity;

import com.spider.annotation.IncKey;
import com.spider.listener.Inckeystrategy.IncreaseStrategy;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "m_collect_movies")
@ToString
public class CollectMapper {

    @Field
    @IncKey(strategyCls = IncreaseStrategy.class)
    @Id
    @ApiModelProperty(value = "唯一ID")
    private Long collectId = 0L;

    @Field
    @ApiModelProperty(value = "影片ID")
    private String videoId;

    @Field
    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @Field
    @ApiModelProperty(value = "加入收藏时间")
    private String collectTime;
}
