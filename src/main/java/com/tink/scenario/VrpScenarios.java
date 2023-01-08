package com.tink.scenario;

import static com.tink.integrations.AuthRequests.getAuthenticationTokenForClient;
import static com.tink.integrations.AuthRequests.getAuthenticationTokenForUser;
import static com.tink.integrations.AuthRequests.getAuthorizationCodeForUser;
import static com.tink.integrations.ConsentRequests.authorizeConsentRequest;
import static com.tink.integrations.ConsentRequests.createConsentRequest;
import static com.tink.integrations.ConsentRequests.getAuthorization;
import static com.tink.integrations.ConsentRequests.getTemplateRequest;
import static io.gatling.javaapi.core.CoreDsl.exec;
import static io.gatling.javaapi.core.CoreDsl.jsonPath;
import static io.gatling.javaapi.core.CoreDsl.pace;
import static io.gatling.javaapi.core.CoreDsl.scenario;
import static io.gatling.javaapi.http.HttpDsl.status;

import io.gatling.javaapi.core.ScenarioBuilder;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class VrpScenarios {

  public ScenarioBuilder minimalContinuesLoad(String clientId, String clientSecret, String userId) {
    return consentCreationAndAuthorization(clientId, clientSecret, userId)
        .exec(
            session -> {
              log.info("Making payment for consentId " + session.getString("consentId"));
              return session;
            }); // <- Add single payment part
  }

  public ScenarioBuilder sustainableLoadGrowth(
      String clientId, String clientSecret, String userId) {
    return consentCreationAndAuthorization(clientId, clientSecret, userId)
        .forever()
        .on(
            pace(Duration.ofSeconds(1))
                .exec(
                    session -> {
                      log.info("Making payment for consentId " + session.getString("consentId"));
                      return session;
                    })); // <- Add single payment part
  }

  private ScenarioBuilder consentCreationAndAuthorization(
      String clientId, String clientSecret, String userId) {
    return scenario("Create & Authorize Consent - Constant Traffic")
        .exec(session -> session.set("userId", userId))
        .exec(
            getAuthenticationTokenForClient(clientId, clientSecret)
                .check(status().is(200))
                .check(jsonPath("$..access_token").saveAs("client_token")))
        .exec(
            getAuthorizationCodeForUser(userId)
                .check(status().is(200))
                .check(jsonPath("$..code").saveAs("authorizationCode")))
        .exec(
            getAuthenticationTokenForUser(clientId, clientSecret)
                .check(status().is(200))
                .check(jsonPath("$..access_token").saveAs("user_token")))
        .exec(getTemplateRequest().check(status().is(200)))
        .exec(
            createConsentRequest()
                .check(status().is(201))
                .check(jsonPath("$..id").saveAs("consentId"))
                .check(jsonPath("$..state").is("INITIALIZED")))
        .exec(
            authorizeConsentRequest()
                .check(status().is(202))
                .check(jsonPath("$..id").saveAs("authorizationId"))
                .check(jsonPath("$..state").is("ONGOING").saveAs("authorizationStatus")))
        .asLongAs(
            session -> {
              String authorizationStatus = session.getString("authorizationStatus");
              log.info("##### Authorization Status: " + authorizationStatus);
              return authorizationStatus.equals("ONGOING");
            })
        .on(
            exec(
                getAuthorization()
                    .check(status().is(200))
                    .check(jsonPath("$..state").saveAs("authorizationStatus"))));
    //        .exec(
    //            loginToDemoBank()
    //                .check(status().is(302))
    //                .check(header("Cookie").saveAs("demobankCookie")))
    //        .exec(authorizeConsentInDemoBank().check(status().is(204)))
    //        .exec(thirdPartyCallbackRequest().check(status().is(200)))
    //        .asLongAs(
    //            session -> {
    //              String authorizationStatus = session.getString("authorizationStatus");
    //              log.info("##### Authorization Status: " + authorizationStatus);
    //              return authorizationStatus.equals("AWAITING_INPUT");
    //            })
    //        .on(
    //            exec(
    //                getAuthorization()
    //                    .check(status().is(200))
    //                    .check(jsonPath("$..state").saveAs("authorizationStatus"))))
    //        .exec(getConsent().check(status().is(200)).check(jsonPath("$..state").is("ACTIVE")))

  }
}
