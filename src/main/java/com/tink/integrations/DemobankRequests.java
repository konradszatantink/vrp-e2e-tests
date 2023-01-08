package com.tink.integrations;

import static com.tink.integrations.ApiConstants.ACCEPT;
import static com.tink.integrations.ApiConstants.APPLICATION_JSON;
import static com.tink.integrations.ApiConstants.APPLICATION_X_WWW_FORM_URLENCODED;
import static com.tink.integrations.ApiConstants.CONTENT_TYPE;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

public class DemobankRequests {

  private DemobankRequests() {}

  public static HttpRequestActionBuilder loginToDemoBank() {
    return http("Login to Demobank")
        .post("/demobank/login")
        .header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
        .header(ACCEPT, APPLICATION_JSON)
        .formParam("username", "zzz")
        .formParam("password", "zzz");
  }

  public static HttpRequestActionBuilder authorizeConsentInDemoBank() {
    return http("Authorize request in demobank")
        .post(
            "/demobank/api/payment/v1/internal/variable-recurring-payment/consent/#{externalId}/authorize")
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .header(ACCEPT, APPLICATION_JSON)
        .header("Cookie", "#{demobankCookie}");
  }
}
