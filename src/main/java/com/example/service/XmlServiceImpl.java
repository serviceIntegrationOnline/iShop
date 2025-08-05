package com.example.service;

import io.vertx.core.json.JsonObject;
import org.json.JSONObject;
import org.json.XML;

public class XmlServiceImpl implements XmlService {

  private final ClientService clientService;

  public XmlServiceImpl(ClientService clientService) {
    this.clientService = clientService;
  }

  @Override
  public JsonObject convertXmlToJson(String xml) {
    try {
      JSONObject jsonObject = XML.toJSONObject(xml);
      return new JsonObject(jsonObject.toString());
    } catch (Exception e) {
      System.err.println("Failed to convert XML to JSON: " + e.getMessage());
      return new JsonObject().put("error", "Invalid XML");
    }
  }
}
