package verticles.database;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.eventbus.EventBus;
import io.vertx.core.json.Json;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mongo.MongoClient;
import io.vertx.ext.mongo.UpdateOptions;
import model.Beverage;
import model.Machine;
import services.MachineService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DBVerticle extends AbstractVerticle {


    @Override
    public void start(Future<Void> startFuture) throws Exception {
        super.start(startFuture);

        JsonObject config = new JsonObject().put("connection_string", "mongodb://localhost:27017").put("db_name", "test2");
        MongoClient mongoClient = MongoClient.createShared(vertx, config, "test2");

        final EventBus eventBus = vertx.eventBus();

        eventBus.consumer("machine.state", receivedMessage -> {
            JsonObject query = new JsonObject();
            query.put("_id", Machine.MACHINE_ID);
            mongoClient.findOne("machine", query, null, res -> {
                if (res.succeeded()) {
                    System.out.println(res.result());
                    receivedMessage.reply(res.result());
                } else res.cause().printStackTrace();
            });
        });

        eventBus.consumer("create.insert", receivedMessage -> {
//            System.out.println(receivedMessage.body());
            JsonObject machine = JsonObject.mapFrom(receivedMessage.body());

            mongoClient.insert("machine", machine, res -> {
                if (res.succeeded()) {
                    receivedMessage.reply(res.result());
                } else {
                    res.cause().printStackTrace();
                }
            });
        });

        eventBus.consumer("updateMachine", receivedMessage -> {
            JsonObject query = new JsonObject().put("_id", Machine.MACHINE_ID);
            JsonObject machine = (JsonObject) receivedMessage.body();
            JsonObject update = new JsonObject().put("$set", new JsonObject()
                    .put("beverages", machine.getJsonArray("beverages"))
                    .put("moneyAmount", machine.getDouble("moneyAmount"))
            );

            mongoClient.updateCollection("machine", query, update, res -> {
                if (res.succeeded()) {
                    receivedMessage.reply("Updated");
                } else res.cause().printStackTrace();
            });
        });

        eventBus.consumer("beverages.all", handle -> {
            JsonObject query = new JsonObject();
            query.put("_id", Machine.MACHINE_ID);
            mongoClient.findOne("machine", query, null, res -> {
                if (res.succeeded()) {
//                   System.out.println(res.result());
//                   Machine machine = res.result().mapTo(Machine.class);
                    handle.reply(res.result().getJsonArray("beverages"));
                } else res.cause().printStackTrace();
            });
        });

        eventBus.consumer("getBeverage", handle -> {
            JsonObject query = new JsonObject();
            query.put("_id", Machine.MACHINE_ID);
            Integer id = (Integer) handle.body();
            mongoClient.findOne("machine", query, null, res -> {
                if (res.succeeded()) {
                    Machine machine = res.result().mapTo(Machine.class);
                    System.out.println(machine);
                    Beverage beverage = machine.getBeverages().stream().filter(beverage1 -> beverage1.getId() == id).findFirst().orElse(null);
                    handle.reply(Json.encodePrettily(beverage));
                } else res.cause().printStackTrace();
            });
        });

        eventBus.consumer("updateBeverage", handle -> {
            JsonObject query = new JsonObject().put("_id", Machine.MACHINE_ID);
            JsonObject machine = (JsonObject) handle.body();
            JsonObject update = new JsonObject().put("$set", new JsonObject()
                    .put("beverages", machine.getJsonArray("beverages"))
                    .put("moneyAmount", machine.getDouble("moneyAmount"))
            );
            mongoClient.updateCollection("machine", query, update, res -> {
                if (res.succeeded()) {
                    handle.reply("Updated");
                } else res.cause().printStackTrace();
            });
        });

        eventBus.consumer("deleteBeverage", handle -> {
            JsonObject query = new JsonObject();
            query.put("_id", Machine.MACHINE_ID);
            JsonObject machine = (JsonObject) handle.body();
            JsonObject update = new JsonObject().put("$set", new JsonObject()
                    .put("beverages", machine.getJsonArray("beverages"))
                    .put("moneyAmount", machine.getDouble("moneyAmount"))
            );
            mongoClient.updateCollection("machine", query, update, res -> {
                if (res.succeeded()) {
                    handle.reply("Deleted");
                } else res.cause().printStackTrace();
            });
        });

        eventBus.consumer("deleteMachine", handle -> {
            JsonObject query = new JsonObject();
            query.put("_id", Machine.MACHINE_ID);
            mongoClient.removeDocument("machine", query, res -> {
                if (res.succeeded()) {
                    handle.reply("Deleted");
                } else res.cause().printStackTrace();
            });
        });


    }
}
