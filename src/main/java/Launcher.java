import io.vertx.core.Vertx;
import verticles.database.DBVerticle;
import verticles.server.VendingVerticle;

public class Launcher extends io.vertx.core.Launcher {
    public static void main(String[] args) {
        Vertx vertx = Vertx.vertx();
        vertx.deployVerticle(new VendingVerticle());
        vertx.deployVerticle(new DBVerticle());
    }
}
