package com.spider.listener.Inckeystrategy;

import com.spider.commonUtil.CommonUtils;
import com.spider.entity.mongoEntity.SeqInfo;
import org.springframework.data.mongodb.core.FindAndModifyOptions;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

/**
 * 自增生成策略
 */
public class IncreaseStrategy extends AbsStrategy {
    public IncreaseStrategy(CommonUtils commonUtils,String collName) {
        super(commonUtils,collName);
    }

    /**
     * 获取下一个自增ID
     * 这儿有根据表名进行区分
     * @return 唯一id
     */
    @Override
    public <T> T doGenId() {
        Query query = new Query(Criteria.where("collName").is(collName));
        Update update = new Update();
        update.inc("seqId", 1);
        FindAndModifyOptions options = new FindAndModifyOptions();
        options.upsert(true);
        options.returnNew(true);
        SeqInfo seq = super.commonUtils.getMongoTemplate().findAndModify(query, update, options, SeqInfo.class);
        return (T)seq.getSeqId();
    }
}
