package com.redhat.developers;

import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;

@Path("/prompt")
public class PromptResource {

    @Inject
    Assistant assistant;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String prompt(@QueryParam("message") String message) {
        return assistant.chat(message);
    }

    @Path("quarkus")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String prompt() {
        return assistant.chat("What is the advantage of Quarkus?");
    }
}