package com.tink.integrations;

final class ApiConstants {

  private ApiConstants() {}

  public static final String CONTENT_TYPE = "Content-Type";
  public static final String ACCEPT = "Accept";
  public static final String APPLICATION_JSON = "application/json";
  public static final String APPLICATION_X_WWW_FORM_URLENCODED =
      "application/x-www-form-urlencoded";
  public static final String AUTHORIZATION = "Authorization";
  public static final String IDEMPOTENCY_KEY = "Idempotency-Key";
  public static final String CLIENT_BEARER_TOKEN = "Bearer #{client_token}";
  public static final String USER_BEARER_TOKEN = "Bearer #{user_token}";
  public static final String GRANT_TYPE_CLIENT_CREDENTIALS = "client_credentials";
  public static final String GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code";
}
