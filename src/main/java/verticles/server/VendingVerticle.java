package verticles.server;

import controllers.MachineController;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;

public class VendingVerticle extends AbstractVerticle {

    private MachineController machineController;


    @Override
    public void start() throws Exception {
        super.start();
        machineController = new MachineController(vertx);
        HttpServerOptions options = new HttpServerOptions().setLogActivity(true);
        HttpServer server = vertx.createHttpServer(options);

        server.requestHandler(machineController.getRouter()).listen(8080);

    }



}

//    public void start(Future<Void> fut) {
//        createSomeData();
//// Create a router object.
//        Router router = Router.router(vertx);
//// Bind "/" to our hello message.
//        router.route("/").handler(routingContext -> {
//            HttpServerResponse response = routingContext.response();
//            response
//                    .putHeader("content-type", "text/html")
//                    .end("<h1>Hello from my first Vert.x 3 application</h1>");
//        });
//        router.route("/assets/*").handler(StaticHandler.create("assets"));
//        router.get("/api/whiskies").handler(this::getAll);
//        router.route("/api/whiskies*").handler(BodyHandler.create());
//        router.post("/api/whiskies").handler(this::addOne);
//        router.get("/api/whiskies/:id").handler(this::getOne);
//        router.put("/api/whiskies/:id").handler(this::updateOne);
//        router.delete("/api/whiskies/:id").handler(this::deleteOne);
//// Create the HTTP verticles.server and pass the "accept" method to the request handler.
//        vertx
//                .createHttpServer()
//                .requestHandler(router::accept)
//                .listen(
//// Retrieve the port from the configuration,
//// default to 8080.
//                        config().getInteger("http.port", 8080),
//                        result -> {
//                            if (result.succeeded()) {
//                                fut.complete();
//                            } else {
//                                fut.fail(result.cause());
//                            }
//                        }
//                );
//    }
//    private void addOne(RoutingContext routingContext) {
//// Read the request's content and create an instance of Whisky.
//        final Whisky whisky = Json.decodeValue(routingContext.getBodyAsString(),
//                Whisky.class);
//// Add it to the backend map
//        products.put(whisky.get_id(), whisky);
//// Return the created whisky as JSON
//        routingContext.response()
//                .setStatusCode(201)
//                .putHeader("content-type", "application/json; charset=utf-8")
//                .end(Json.encodePrettily(whisky));
//    }
//    private void getOne(RoutingContext routingContext) {
//        final String id = routingContext.request().getParam("id");
//        if (id == null) {
//            routingContext.response().setStatusCode(400).end();
//        } else {
//            final Integer idAsInteger = Integer.valueOf(id);
//            Whisky whisky = products.get(idAsInteger);
//            if (whisky == null) {
//                routingContext.response().setStatusCode(404).end();
//            } else {
//                routingContext.response()
//                        .putHeader("content-type", "application/json; charset=utf-8")
//                        .end(Json.encodePrettily(whisky));
//            }
//        }
//    }
//    private void updateOne(RoutingContext routingContext) {
//        final String id = routingContext.request().getParam("id");
//        JsonObject json = routingContext.getBodyAsJson();
//        if (id == null || json == null) {
//            routingContext.response().setStatusCode(400).end();
//        } else {
//            final Integer idAsInteger = Integer.valueOf(id);
//            Whisky whisky = products.get(idAsInteger);
//            if (whisky == null) {
//                routingContext.response().setStatusCode(404).end();
//            } else {
//                whisky.setType(json.getString("type"));
//                whisky.setOrigin(json.getString("origin"));
//                routingContext.response()
//                        .putHeader("content-type", "application/json; charset=utf-8")
//                        .end(Json.encodePrettily(whisky));
//            }
//        }
//    }
//    private void deleteOne(RoutingContext routingContext) {
//        String id = routingContext.request().getParam("id");
//        if (id == null) {
//            routingContext.response().setStatusCode(400).end();
//        } else {
//            Integer idAsInteger = Integer.valueOf(id);
//            products.remove(idAsInteger);
//        }
//        routingContext.response().setStatusCode(204).end();
//    }
//    private void getAll(RoutingContext routingContext) {
//// Write the HTTP response
//// The response is in JSON using the utf-8 encoding
//// We returns the list of bottles
//        routingContext.response()
//                .putHeader("content-type", "application/json; charset=utf-8")
//                .end(Json.encodePrettily(products.values()));
//    }
//    private void createSomeData() {
//        Whisky bowmore = new Whisky("Bowmore 15 Years Laimrig", "Scotland, Islay");
//        products.put(bowmore.get_id(), bowmore);
//        Whisky talisker = new Whisky("Talisker 57° North", "Scotland, Island");
//        products.put(talisker.get_id(), talisker);
//    }

