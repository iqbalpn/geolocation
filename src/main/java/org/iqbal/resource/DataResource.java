package org.iqbal.resource;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.logging.Logger;
import org.jboss.resteasy.annotations.providers.multipart.MultipartForm;
import org.jboss.resteasy.plugins.providers.multipart.MultipartFormDataInput;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

@Path("/data")
public class DataResource {

    private static final Logger log = Logger.getLogger(DataResource.class);

    private static Map<String, JsonNode> processedData = new HashMap<>();

    @POST
    @Path("/upload")
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    @Produces(MediaType.APPLICATION_JSON)
    public Response uploadFile(@MultipartForm MultipartFormDataInput input) {

        log.info("Input : " + input);

        try {
            InputStream fileInputStream = input.getFormDataPart("file", InputStream.class, null);
            byte[] fileBytes = fileInputStream.readAllBytes();
            String fileContent = new String(fileBytes);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(fileContent);
            processedData.put("uploadedData", jsonNode);

            return Response.ok("File uploaded and processed successfully").build();
        } catch (IOException e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("File upload failed").build();
        }
    }

    @GET
    @Path("/processed")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProcessedData() {
        JsonNode data = processedData.get("uploadedData");
        if (data != null) {
            return Response.ok(data).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("No data available").build();
        }
    }

}
