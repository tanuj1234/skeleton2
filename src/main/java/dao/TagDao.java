package dao;

import generated.tables.records.ReceiptsRecord;
import generated.tables.records.TagsRecord;
import org.jooq.Configuration;
import org.jooq.DSLContext;
import org.jooq.Result;
import org.jooq.impl.DSL;

import java.util.List;

import static com.google.common.base.Preconditions.checkState;
import static generated.Tables.RECEIPTS;
import static generated.Tables.TAGS;

public class TagDao {
    DSLContext dsl;

    public TagDao(Configuration jooqConfig) {
        this.dsl = DSL.using(jooqConfig);
    }

    public int insert(String tag, int receipt_id) {
        TagsRecord TagsRecord = dsl
                .insertInto(TAGS, TAGS.TAG, TAGS.RECEIPT_ID)
                .values(tag, receipt_id)
                .returning(TAGS.ID)
                .fetchOne();

        checkState(TagsRecord != null && TagsRecord.getId() != null, "Could Not Insert");

        return TagsRecord.getId();
    }

    public void delete(String tagName, int receiptId){
        // delete record from tags where name = tagName and receipt_id = receiptId
        dsl.delete(TAGS)
                .where(TAGS.TAG.eq(tagName).and(TAGS.RECEIPT_ID.eq(receiptId)))
                .execute();
    }

    public List<TagsRecord> getAllTags() {
        return dsl.selectFrom(TAGS).fetch();
    }

    public boolean receiptTaggedAlready(String tagName, Integer receiptId){
        // find if there exists a record in tags where name = tagName and receipt_id = receiptId
        TagsRecord hasTag = dsl
                .selectFrom(TAGS)
                .where(TAGS.TAG.eq(tagName).and(TAGS.RECEIPT_ID.eq(receiptId)))
                .fetchOne();

        if(hasTag != null) {
            return true;
        } else {
            return false;
        }

    }

    public List<Integer> getReceiptIdsWithTag(String tagName) {
        return dsl.selectFrom(TAGS)
                .where(TAGS.TAG.eq(tagName))
                .fetch(TAGS.RECEIPT_ID);

    }

}
