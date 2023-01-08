package com.tink.integrations;

import static com.tink.integrations.ApiConstants.ACCEPT;
import static com.tink.integrations.ApiConstants.APPLICATION_JSON;
import static com.tink.integrations.ApiConstants.APPLICATION_X_WWW_FORM_URLENCODED;
import static com.tink.integrations.ApiConstants.AUTHORIZATION;
import static com.tink.integrations.ApiConstants.CLIENT_BEARER_TOKEN;
import static com.tink.integrations.ApiConstants.CONTENT_TYPE;
import static com.tink.integrations.ApiConstants.GRANT_TYPE_AUTHORIZATION_CODE;
import static com.tink.integrations.ApiConstants.GRANT_TYPE_CLIENT_CREDENTIALS;
import static io.gatling.javaapi.http.HttpDsl.http;

import io.gatling.javaapi.http.HttpRequestActionBuilder;

public class AuthRequests {

  private AuthRequests() {}

  public static HttpRequestActionBuilder getAuthenticationTokenForClient(
      String clientId, String clientSecret) {
    return http("Get Client Auth Token Request")
        .post("/api/v1/oauth/token")
        .header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
        .header(ACCEPT, APPLICATION_JSON)
        .formParam("client_id", clientId)
        .formParam("client_secret", clientSecret)
        .formParam("grant_type", GRANT_TYPE_CLIENT_CREDENTIALS)
        .formParam("scope", "consents:readonly,consents,authorization:grant");
  }

  public static HttpRequestActionBuilder getAuthorizationCodeForUser(String userId) {
    return http("Get Authorization Grant Request")
        .post("/api/v1/oauth/authorization-grant")
        .header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
        .header(ACCEPT, APPLICATION_JSON)
        .header(AUTHORIZATION, CLIENT_BEARER_TOKEN)
        .formParam("user_id", userId)
        .formParam("scope", "consents:readonly,consents");
  }

  public static HttpRequestActionBuilder getAuthenticationTokenForUser(
      String clientId, String clientSecret) {
    return http("Get User Auth Token Request")
        .post("/api/v1/oauth/token")
        .header(CONTENT_TYPE, APPLICATION_X_WWW_FORM_URLENCODED)
        .header(ACCEPT, APPLICATION_JSON)
        .formParam("client_id", clientId)
        .formParam("client_secret", clientSecret)
        .formParam("grant_type", GRANT_TYPE_AUTHORIZATION_CODE)
        .formParam("code", "#{authorizationCode}");
  }
}
