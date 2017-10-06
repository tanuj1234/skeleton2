package controllers;

import api.CreateReceiptRequest;
import api.ReceiptResponse;
import dao.ReceiptDao;
import dao.TagDao;
import generated.tables.records.ReceiptsRecord;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Path("")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class TagController {
    final TagDao tags;
    final ReceiptDao receipts;

    public TagController(TagDao tags, ReceiptDao receipts) {
        this.tags = tags;
        this.receipts = receipts;
    }

    @PUT
    @Path("/tags/{tag}")
    public void toggleTag(@PathParam("tag") String tag, int receiptId) {
        if (tags.receiptTaggedAlready(tag, receiptId)){
            // delete entry
            tags.delete(tag, receiptId);
        }
        else {
            // insert entry
            tags.insert(tag, receiptId);
        }
    }

    @GET
    @Path("/tags/{tag}")
    public List<ReceiptResponse> getReceipts(@PathParam("tag") String tagName) {
        //with the tagName, get all response receipts
        List<Integer> receiptIds = tags.getReceiptIdsWithTag(tagName);
        List<ReceiptsRecord> receiptRecords = receipts.getAllReceiptsWithReceiptId(receiptIds);
        return receiptRecords.stream().map(ReceiptResponse::new).collect(toList());
    }



}
