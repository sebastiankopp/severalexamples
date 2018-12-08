package de.sebastiankopp.severalexamples.dummyjaxrs.logging.boundary;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

import de.sebastiankopp.severalexamples.dummyjaxrs.ConstantValues;

@Path("logMessages")
public class LogMessagesResource {
	
	private static final String COLL_NAME_LOG_COLLECTION = "logCollection";

	private static final String BSON_KEY_ORIGIN = "origin";

	private static final String BSON_KEY_TIMESTAMP = "timestamp";

	private static final String BSON_KEY_MSG = "msg";

	@Inject
	@CustomLogger(LoggerName.EXTERNAL_RES_LGGR)
	private Logger extResLggr;
	
	@Inject
	MongoDatabase db;
	
	@POST
	@Consumes({"text/*", "application/json"})
	public void postMsg(String messageBody, @Context HttpServletRequest request) {
		extResLggr.info(messageBody);
		MongoCollection<Document> collection = db.getCollection(COLL_NAME_LOG_COLLECTION);
		final Document doc = new Document();
		switch (request.getContentType()) {
		case MediaType.APPLICATION_JSON:
			final Map<String, Object> msgMap = JsonToMapParser.fromJsonString(messageBody);
			doc.put(BSON_KEY_MSG, msgMap);
			doc.put(BSON_KEY_TIMESTAMP, ZonedDateTime.now().format(ConstantValues.COOMON_TSTAMP_FORMAT));
			doc.put(BSON_KEY_ORIGIN, request.getRemoteHost() + ":" + request.getRemotePort());
			break;
		default:
			doc.put(BSON_KEY_MSG, messageBody);
			doc.put(BSON_KEY_TIMESTAMP, LocalDateTime.now().format(ConstantValues.COOMON_TSTAMP_FORMAT));
			doc.put(BSON_KEY_ORIGIN, request.getRemoteHost() + ":" + request.getRemotePort());
		}
		collection.insertOne(doc);
		
	}
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public String getAllLogMessages() {
		MongoCollection<Document> collection = db.getCollection(COLL_NAME_LOG_COLLECTION);
		FindIterable<Document> result = collection.find();
		return StreamSupport.stream(result.spliterator(), false)
				.map(Document::toJson)
				.collect(Collectors.joining(",", "[", "]"));
	}
	
}
