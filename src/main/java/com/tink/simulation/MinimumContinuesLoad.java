package com.tink.simulation;

import static io.gatling.javaapi.core.CoreDsl.forAll;
import static io.gatling.javaapi.core.CoreDsl.global;
import static io.gatling.javaapi.core.CoreDsl.rampUsers;
import static io.gatling.javaapi.http.HttpDsl.http;

import com.tink.scenario.VrpScenarios;
import io.gatling.javaapi.core.PopulationBuilder;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;
import java.time.Duration;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MinimumContinuesLoad extends Simulation {

  public MinimumContinuesLoad() {
    VrpScenarios vrpScenarios = new VrpScenarios();
    setUp(
        http.baseUrl(System.getProperty("gatling.baseUrl")),
        Integer.valueOf(System.getProperty("gatling.users")),
        Integer.valueOf(System.getProperty("gatling.durationSeconds")),
        vrpScenarios.minimalContinuesLoad(
            System.getProperty("gatling.clientId"),
            System.getProperty("gatling.clientSecret"),
            System.getProperty("gatling.userId")));
  }

  private void setUp(
      HttpProtocolBuilder httpProtocol,
      Integer users,
      Integer durationSeconds,
      ScenarioBuilder scenarioBuilder) {
    PopulationBuilder populationBuilder =
        scenarioBuilder
            .injectOpen(rampUsers(users).during(durationSeconds))
            .protocols(httpProtocol);

    setUp(populationBuilder)
        .maxDuration(Duration.ofSeconds(100))
        .assertions(
            global().successfulRequests().percent().shouldBe(100.0),
            forAll().responseTime().percentile4().lt(500));
  }

  @Override
  public void before() {
    log.info("Starting up {}", this.getClass().getName());
  }

  @Override
  public void after() {
    log.info("Finished {}", this.getClass().getName());
  }
}
