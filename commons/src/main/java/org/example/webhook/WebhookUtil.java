package org.example.webhook;

import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

public class WebhookUtil {
    public String signature(String secret, String timestamp, int id) {
        return new HmacUtils(HmacAlgorithms.HMAC_SHA_256, secret).hmacHex(timestamp + id);
    }
}
