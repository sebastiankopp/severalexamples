package de.sebikopp.dummyjaxrs.logging.boundary;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.apache.logging.log4j.Logger;
import org.bson.Document;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@Path("messages")
public class ExternalMessagesConsumingResource {
	
	@Inject
	@CustomLogger(LoggerName.EXTERNAL_RES_LGGR)
	private Logger extResLggr;
	
	@Inject
	MongoDatabase db;
	
	@POST
	@Consumes({"text/*", "application/json", "application/xml"})
	public void postMsg(String messageBody, @Context HttpServletRequest request) {
		extResLggr.info(messageBody);
		MongoCollection<Document> collection = db.getCollection("logCollection");
		switch (request.getContentType()) {
		case MediaType.APPLICATION_JSON:
			Document doc = new Document(JsonToMapParser.fromJsonString(messageBody));
			collection.insertOne(doc);
			break;
		case MediaType.APPLICATION_XML:
		case MediaType.TEXT_XML:
			
			break;
		default:
			// text/plain
			
		}
		
	}
	
}
