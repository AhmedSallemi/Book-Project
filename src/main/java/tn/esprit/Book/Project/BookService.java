package tn.esprit.Book.Project;

import java.io.StringWriter;
import java.util.Set;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.json.JsonArray;
import javax.json.JsonArrayBuilder;
import javax.json.Json;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.Produces;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.validation.Validator;
import javax.validation.ConstraintViolation;

import com.mongodb.client.FindIterable;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;

import com.mongodb.client.MongoDatabase;

@Path("/book")
@ApplicationScoped
public class BookService {
	@Inject
    MongoDatabase db;

    @Inject
    Validator validator;
    
    
    private JsonArray getViolations(Book book) {
        Set<ConstraintViolation<Book>> violations = validator.validate(
                book);

        JsonArrayBuilder messages = Json.createArrayBuilder();

        for (ConstraintViolation<Book> v : violations) {
            messages.add(v.getMessage());
        }

        return messages.build();
    }
    @POST
    @Path("/hello")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Successfully added crew member."),
        @APIResponse(
            responseCode = "400",
            description = "Invalid  book configuration.") })
    @Operation(summary = "Add a new book to the database.")
    public Response add(Book book) {
      

        MongoCollection<Document> bookk = db.getCollection("testdb");

        Document newbook = new Document();
        newbook.put("Title", book.getTitle());
        newbook.put("Price", book.getPrice());
        newbook.put("description", book.getDescription());
        newbook.put("isbn", book.getIsbn());
        newbook.put("publisher",book.getPublisher());
        newbook.put("author",book.getAuthor());
        newbook.put("pages", book.getPages());
        newbook.put("language", book.getLanguage());
        

        bookk.insertOne(newbook); 
       
        System.out.println(newbook+"*******************");
        return Response
                .status(Response.Status.OK)
                .entity(newbook.toJson())
                .build();
        
        }
    
    
    
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Successfully listed the crew members."),
        @APIResponse(
            responseCode = "500",
            description = "Failed to list the crew members.") })
    @Operation(summary = "List the crew members from the database.")
    public Response retrieve() {
        StringWriter sb = new StringWriter();

        try {
            MongoCollection<Document> bookk = db.getCollection("testdb");
            sb.append("[");
            boolean first = true;
            FindIterable<Document> docs = bookk.find();
            for (Document d : docs) {
                if (!first) sb.append(",");
                else first = false;
                sb.append(d.toJson());
            }
            sb.append("]");
        } catch (Exception e) {
            e.printStackTrace(System.out);
            return Response
                .status(Response.Status.INTERNAL_SERVER_ERROR)
                .entity("[\"Unable to list crew members!\"]")
                .build();
        }

        return Response
            .status(Response.Status.OK)
            .entity(sb.toString())
            .build();
    }
    
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @APIResponses({
        @APIResponse(
            responseCode = "200",
            description = "Successfully updated crew member."),
        @APIResponse(
            responseCode = "400",
            description = "Invalid object id or crew member configuration."),
        @APIResponse(
            responseCode = "404",
            description = "Crew member object id was not found.") })
    @Operation(summary = "Update a crew member in the database.")
    public Response update(Book book,
        @Parameter(
            description = "Object id of the crew member to update.",
            required = true
        )
        @PathParam("id") String id) {

      

        ObjectId oid;

        try {
            oid = new ObjectId(id);
        } catch (Exception e) {
            return Response
                .status(Response.Status.BAD_REQUEST)
                .entity("[\"Invalid object id!\"]")
                .build();
        }

        MongoCollection<Document> bookk = db.getCollection("testdb");

        Document query = new Document("_id", oid);

        Document newbook = new Document();
        newbook.put("Title", book.getTitle());
        newbook.put("Price", book.getPrice());
        newbook.put("description", book.getDescription());
        newbook.put("isbn", book.getIsbn());
        newbook.put("publisher",book.getPublisher());
        newbook.put("author",book.getAuthor());
        newbook.put("pages", book.getPages());
        newbook.put("language", book.getLanguage());

        UpdateResult updateResult = bookk.replaceOne(query, newbook);

        if (updateResult.getMatchedCount() == 0) {
            return Response
                .status(Response.Status.NOT_FOUND)
                .entity("[\"_id was not found!\"]")
                .build();
        }

        newbook.put("_id", oid);

        return Response
            .status(Response.Status.OK)
            .entity(newbook .toJson())
            .build();
    }


}
