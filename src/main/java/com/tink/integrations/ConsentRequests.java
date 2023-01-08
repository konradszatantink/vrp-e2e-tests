package com.tink.integrations;

import static com.tink.integrations.ApiConstants.APPLICATION_JSON;
import static com.tink.integrations.ApiConstants.AUTHORIZATION;
import static com.tink.integrations.ApiConstants.CLIENT_BEARER_TOKEN;
import static com.tink.integrations.ApiConstants.CONTENT_TYPE;
import static com.tink.integrations.ApiConstants.IDEMPOTENCY_KEY;
import static com.tink.integrations.ApiConstants.USER_BEARER_TOKEN;
import static io.gatling.javaapi.core.CoreDsl.ElFileBody;
import static io.gatling.javaapi.core.CoreDsl.RawFileBody;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.http.HttpRequestActionBuilder;
import java.util.UUID;

public class ConsentRequests {

  private ConsentRequests() {}

  public static HttpRequestActionBuilder getTemplateRequest() {
    return http("Get Template Request")
        .get(
            "/connectivity/v2/consent-templates/uk-natwest-oauth2?type=MANDATE_PAYMENTS&subType=VRP_SWEEPING")
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .header(AUTHORIZATION, CLIENT_BEARER_TOKEN);
  }

  public static HttpRequestActionBuilder createConsentRequest() {
    return http("Create Consent Request")
        .post("/connectivity/v2/consents")
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .header(AUTHORIZATION, CLIENT_BEARER_TOKEN)
        .header(IDEMPOTENCY_KEY, UUID.randomUUID().toString())
        .body(ElFileBody("createConsentBody.json"));
  }

  public static HttpRequestActionBuilder authorizeConsentRequest() {
    return http("Authorize Consent Request")
        .post("/connectivity/v2/consents/#{consentId}/authorizations")
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .header(AUTHORIZATION, USER_BEARER_TOKEN)
        .header(IDEMPOTENCY_KEY, UUID.randomUUID().toString())
        .body(RawFileBody("authorizeConsentBody.json"));
  }

  public static HttpRequestActionBuilder getConsent() {
    return http("Get Consent Request")
        .get("/connectivity/v2/consents/#{consentId}")
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .header(AUTHORIZATION, CLIENT_BEARER_TOKEN);
  }

  public static HttpRequestActionBuilder getAuthorization() {
    return http("Get Authorization Request")
        .get("/connectivity/v2/consents/#{consentId}/authorizations/#{authorizationId}")
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .header(AUTHORIZATION, USER_BEARER_TOKEN);
  }

  public static HttpRequestActionBuilder thirdPartyCallbackRequest() {
    return http("Third Party Callback Request")
        .post("/connectivity/v2/third-party-callback")
        .header(CONTENT_TYPE, APPLICATION_JSON)
        .header(AUTHORIZATION, CLIENT_BEARER_TOKEN);
  }
}
