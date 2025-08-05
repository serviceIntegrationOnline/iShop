package com.example.service;

import io.vertx.core.json.JsonObject;

public interface XmlService {
  JsonObject convertXmlToJson(String xmlData);

}
