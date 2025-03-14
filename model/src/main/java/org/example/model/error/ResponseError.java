package org.example.model.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError {
    private int code; // 13
    private String error; // "Subscription expired"
    private String httpReason; // "Bad Request"
    private String httpStatus; // 400
    private String path; // "/v1/subscription/WHBBv/expire"
    private String timestamp; // "2015-06-12T06:38:33.876+0000"
    private String requestId; // "de03e86d0a6d44359b249340f967ddc9"
    private String transactionError; // "credit_card_expired"
}
