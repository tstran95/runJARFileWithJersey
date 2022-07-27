package com.vn.runjar.resources;

import com.vn.runjar.model.ClassInfo;
import com.vn.runjar.services.impl.AppServiceImpl;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Slf4j
@Path("/fly")
public class AppResource {
    AppServiceImpl appService = AppServiceImpl.getInstance();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response fly(ClassInfo classInfo) {
        log.info("AppResource fly() START");
        try {
            appService.flying(classInfo);
        }catch (Exception e) {
            return null;
        }
        log.info("AppResource fly() END");
        return Response.ok().build();
    }
}
