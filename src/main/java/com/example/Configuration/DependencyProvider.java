package com.example.Configuration;

import com.example.controller.XmlController;
import com.example.service.ClientService;
import com.example.service.ClientServiceImpl;
import com.example.service.XmlService;
import com.example.service.XmlServiceImpl;

import io.vertx.core.Vertx;
import io.vertx.mysqlclient.MySQLPool;

public class DependencyProvider {

  // Services
  private final ClientService clientService;
  private final XmlService xmlService;

  // Controllers
  private final XmlController xmlController;

  public DependencyProvider(MySQLPool client, Vertx vertx, String apiHost, int apiPort, String apiPath) {

    // Services
    this.clientService = new ClientServiceImpl(vertx, apiHost, apiPort, apiPath);
    this.xmlService = new XmlServiceImpl(clientService);
    // Controllers
    this.xmlController = new XmlController(xmlService, clientService);
  }

  public DependencyProvider(Vertx vertx, String apiHost, int apiPort, String apiPath) {
    this.clientService = new ClientServiceImpl(vertx, apiHost, apiPort, apiPath);
    this.xmlService = new XmlServiceImpl(clientService);
    // Controllers
    this.xmlController = new XmlController(xmlService, clientService);
  }

  // Controller Getters

  public XmlController getXmlController() {
    return xmlController;
  }
}
