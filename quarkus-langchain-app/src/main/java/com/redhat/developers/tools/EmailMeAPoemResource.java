package com.redhat.developers.tools;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.QueryParam;

@Path("/email-me-a-poem")
public class EmailMeAPoemResource {

    private final AssistantWithContext service;

    public EmailMeAPoemResource(AssistantWithContext service) {
        this.service = service;
    }

    @GET
    public String emailMeAPoem(@QueryParam("topic") String topic,
                               @QueryParam("lines") int lines) {

        return service.writeAPoem(topic, lines);
    }


    @Path("quarkus-and-langchain4j")
    @GET()
    public String emailMeAPoem() {
        return service.writeAPoem("Qarkus and LangChain4j", 4);
    }

}