package org.example.error;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.model.AbstractResponseDto;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseError extends AbstractResponseDto {
    @Schema(name = "code", example = "21", description = "API error code")
    private int code;

    @Schema(name = "error", example = "Subscription expired", description = "Short error message")
    private String error;

    @Schema(name = "http_reason", example = "Bad Request", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            description = "HTTP reason of the error (same as the HTTP response)")
    @JsonProperty("http_reason")
    private String httpReason;

    @Schema(name = "http_status", example = "400",
            description = "HTTP status code of the error (same as the HTTP response)")
    @JsonProperty("http_status")
    private String httpStatus;

    @Schema(name = "path", example = "/v1/subscription/WHBBv/expire",
            description = "The path generating the error response")
    private String path;

    @Schema(name = "request_id", example = "2015-06-12T06:38:33.876+0000",
            description = "Date when the error occurred. In ISO-8601 extended offset date-time format.")
    private String timestamp;

    @Schema(name = "request_id", example = "de03e86d0a6d44359b249340f967ddc9",
            description = "Unique request id for the failed request.")
    @JsonProperty("request_id")
    private String requestId;

    @Schema(name = "transaction_error", example = "credit_card_expired", requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            description = "Optional transaction error in the case the request involved transaction processing.")
    @JsonProperty("transaction_error")
    private String transactionError;
}
