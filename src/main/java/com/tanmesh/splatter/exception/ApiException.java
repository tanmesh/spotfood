package com.tanmesh.splatter.exception;

import javax.ws.rs.core.Response;

public class ApiException extends Exception {
    private final Response.Status status;
    private final String message;

    public ApiException(Response.Status status, String message) {
        this.status = status;
        this.message = message;
    }

    public Response.Status getStatus() {
        return status;
    }


    public String getMessage() {
        return message;
    }
}
