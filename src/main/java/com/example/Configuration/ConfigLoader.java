package com.example.Configuration;

import io.vertx.core.json.JsonObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class ConfigLoader {
  public static JsonObject load(String path) throws Exception {
    String configStr = new String(Files.readAllBytes(Paths.get(path)));
    return new JsonObject(configStr);
  }
}
