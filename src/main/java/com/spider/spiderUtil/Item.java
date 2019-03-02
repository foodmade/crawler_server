package com.spider.spiderUtil;

import com.spider.annotation.IncKey;
import com.spider.commonUtil.mongoUtil.MongoTable;
import com.spider.listener.Inckeystrategy.UUIDStrategy;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
@Document(collection = "m_video_sources")
public class Item {
    @Field
    @ApiModelProperty(value = "电影封面图")
    private String coverImg;

    @Field
    @ApiModelProperty(value = "影片类型")
    private String videoType;

    @Field
    @ApiModelProperty(value = "电影名称")
    private String videoName;

    @Field
    @ApiModelProperty(value = "电影详细信息 导演 演员")
    private HashMap<String, List<String>> figures;

    @Field
    @ApiModelProperty(value = "电影id")
    private String videoId;

    @Field
    @ApiModelProperty(value = "电影播放地址 key --> 地址描述  value-->视频地址")
    private List<HashMap<String,String>> videoSourceList = new ArrayList<>();

    @Field
    @ApiModelProperty(value = "电影简介")
    private String videoDesc;

    @Field
    @ApiModelProperty(value = "被喜电影父类Id欢数量")
    private Integer parentId;

    @Field
    @ApiModelProperty(value = "电影所属类目")
    private Integer cateId;

    @Field
    @ApiModelProperty(value = "地区")
    private String areacity;

    @Field
    @ApiModelProperty(value = "年份")
    private Integer year;

    @Field
    @ApiModelProperty(value = "评分")
    private Double score = 5.2;

    @Field
    @ApiModelProperty(value = "被喜欢数量")
    private Integer favoritecnt = 0;

    @Field
    @ApiModelProperty(value = "点赞")
    private Integer dotcnt = 0;

    @Field
    @ApiModelProperty(value = "电影id")
    @Id
    private String movieid;

}
