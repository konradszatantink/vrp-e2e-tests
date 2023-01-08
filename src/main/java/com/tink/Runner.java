package com.tink;

import com.tink.simulation.MinimumContinuesLoad;
import com.tink.simulation.SustainableLoadGrowth;

public class Runner {

  public static final String CLIENT_ID = "to_add";
  public static final String CLIENT_SECRET = "to_add";
  public static final String USER_ID = "to_add";

  public static void main(String[] args) {
    new Engine()
        .env("gatling.durationSeconds", 3600)
        .env("gatling.users", 1)
        .env("gatling.baseUrl", "https://main.staging.oxford.tink.se")
        .env("gatling.clientId", CLIENT_ID)
        .env("gatling.clientSecret", CLIENT_SECRET)
        .env("gatling.userId", USER_ID)
        .run(MinimumContinuesLoad.class);

    new Engine()
        .env("gatling.durationSeconds", 3600)
        .env("gatling.users", 10)
        .env("gatling.baseUrl", "https://main.staging.oxford.tink.se")
        .env("gatling.clientId", CLIENT_ID)
        .env("gatling.clientSecret", CLIENT_SECRET)
        .env("gatling.userId", USER_ID)
        .run(SustainableLoadGrowth.class);
  }
}
