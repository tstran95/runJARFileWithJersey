package com.vn.runjar.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.vn.runjar.constant.Constant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Response {
    private String code;
    private String description;
    private String message;

    public static Response getResponse(String code, String description , String message) {
        return Response.builder()
                        .code(code)
                        .description(description)
                        .message(message)
                        .build();
    }

    @JsonIgnore
    public static Response responseError() {
        return Response.builder()
                .code(Constant.ERROR)
                .description(Constant.FAIL)
                .message(null)
                .build();
    }
}
