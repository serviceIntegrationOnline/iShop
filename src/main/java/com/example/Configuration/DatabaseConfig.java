package com.example.Configuration;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.mysqlclient.MySQLConnectOptions;
import io.vertx.mysqlclient.MySQLPool;
import io.vertx.sqlclient.PoolOptions;

public class DatabaseConfig {

  public static MySQLPool createMySQLPool(Vertx vertx, JsonObject dbConfig) {
    MySQLConnectOptions connectOptions = new MySQLConnectOptions()
      .setPort(dbConfig.getInteger("port"))
      .setHost(dbConfig.getString("host"))
      .setDatabase(dbConfig.getString("database"))
      .setUser(dbConfig.getString("user"))
      .setPassword(dbConfig.getString("password"));

    PoolOptions poolOptions = new PoolOptions().setMaxSize(5);
    return MySQLPool.pool(vertx, connectOptions, poolOptions);
  }
}
