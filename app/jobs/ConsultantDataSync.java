package jobs;

import akka.util.Duration;
import play.Logger;
import play.db.DB;
import play.libs.Akka;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * User: sathish
 * Date: 11/9/12
 * Time: 5:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class ConsultantDataSync implements Runnable {

    private static Runnable consultantDataSync;
    private Connection jigsawConnection = DB.getConnection("jigsaw");

    public static void syncConsultantData() {

        Logger.info("syncConsultantData called");
        Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS),
                Duration.create(1, TimeUnit.MINUTES),
                getInstance()
        );
    }

    private static Runnable getInstance() {
        if (consultantDataSync == null) {
            consultantDataSync = new ConsultantDataSync();
        }
        return consultantDataSync;
    }


    @Override
    public void run() {
        try {
            Logger.info(jigsawConnection.getMetaData().toString());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
