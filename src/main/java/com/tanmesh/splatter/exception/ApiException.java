package com.tanmesh.splatter.exception;

import javax.ws.rs.core.Response;

public class ApiException extends Exception {
    private Response.Status status;
    private String messageName;


    public ApiException(Response.Status status, String s) {
        this.status = status;
        this.messageName = s;
    }
}
