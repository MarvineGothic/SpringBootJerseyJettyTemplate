package org.example.webhook;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import org.example.model.AbstractResponseDto;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WebhookDto extends AbstractResponseDto {
    @Schema(name = "id", example = "8ab4b56439944e62ababca7954355578",
            description = """
                    Unique id for webhook. If multiple URL's are used each request
                    will have different id's but same event_id""")
    private String id;

    @Schema(name = "event_id", example = "680bd8055b5c444bb467dcb731c65bf9",
            description = "Id of event triggering webhook")
    @JsonProperty("event_id")
    private String eventId;

    @Schema(name = "event_type", example = "invoice_settled", description = "The event type")
    @JsonProperty("event_type")
    private String eventType;

    @Schema(name = "timestamp", example = "2015-06-25T12:10:00.64Z",
            description = "Timestamp in ISO-8601 when the webhook was triggered.")
    private String timestamp;

    @Schema(name = "signature", example = "7a591eddc400af4c8a64ff551ff90b37d79471bd2c9a5a2fcd4ed6944f39cb09",
            description = """
                    Signature to verify the authenticity of the webhook.
                    signature = hexencode(hmac_sha_256(webhook_secret, timestamp + id))""")
    private String signature;

    @Schema(name = "customer", example = "KFHFPO",
            description = "(Optional) Customer handle. Included if event relates to a customer resource.")
    private String customer;

    @Schema(name = "payment_method", example = "ca_b6506f005dce813e408919475f39bffd",
            description = "(Optional) Payment method id. Included for the customer_payment_method_added event.")
    @JsonProperty("payment_method")
    private String paymentMethod;

    @Schema(name = "payment_method_reference", example = "cs_4468ca21dfde3c37a5736684cf608111",
            description = """
                    (Optional) Payment method reference. For a Checkout session the reference
                    will be the session id if it has not been explicitly defined when creating the session.""")
    @JsonProperty("payment_method_reference")
    private String paymentMethodReference;

    @Schema(name = "subscription", example = "sub001",
            description = """
                    (Optional) Subscription handle. Included if event relates to a subscription resource.""")
    private String subscription;

    @Schema(name = "invoice", example = "inv-1234",
            description = """
                    (Optional) Invoice handle. Is inv-<invoice_number> if invoice was created automatically
                    for subscription. Included if event relates to an invoice resource.""")
    private String invoice;

    @Schema(name = "transaction", example = "28b1af53d7ecd1c487292402a908e2b3",
            description = """
                    (Optional) For invoice events a transaction id is included if the event was result of a transaction,
                     e.g. a card settle transaction. The transaction id the same as refund id for refunds.""")
    private String transaction;

    @Schema(name = "credit_note", example = "a26c1bf5f656f489f295d0c4748dd003",
            description = """
                    (Optional) Credit note id. Included if the event relates to an invoice refund, credit or cancellation.""")
    @JsonProperty("credit_note")
    private String creditNote;

    @Schema(name = "credit", example = "inv-12-credit-3241",
            description = """
                    (Optional) Credit id. Included if the event is an invoice credit.""")
    private String credit;
}
