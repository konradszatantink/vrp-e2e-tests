package com.tink;

import static java.util.Objects.requireNonNull;

import io.gatling.app.Gatling;
import io.gatling.core.config.GatlingPropertiesBuilder;
import io.gatling.javaapi.core.Simulation;

public class Engine {
  public void run(Class<? extends Simulation> simulation) {
    var properties =
        new GatlingPropertiesBuilder()
            .simulationClass(simulation.getName())
            .resourcesDirectory(
                requireNonNull(simulation.getClassLoader().getResource(".")).getPath())
            .build();
    Gatling.fromMap(properties);
  }

  public Engine env(String key, Object value) {
    System.setProperty(key, value.toString());
    return this;
  }
}
