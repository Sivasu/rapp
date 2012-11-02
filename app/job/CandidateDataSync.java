package job;


import akka.util.Duration;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import play.libs.Akka;

import java.io.IOException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;


public class CandidateDataSync implements Runnable{

    private static Runnable candidateDataSync;

    public static void syncCandidateData() {

        System.out.println("syncCandidateData called");
        Akka.system().scheduler().schedule(
                Duration.create(0, TimeUnit.MILLISECONDS),
                Duration.create(1, TimeUnit.MINUTES),
                getInstance()
        );
    }

    private static Runnable getInstance() {
        if(candidateDataSync == null) {
            candidateDataSync = new CandidateDataSync();
        }
        return candidateDataSync;
    }

    @Override
    public void run() {
        System.out.println("in run method");
        try {
            Document doc = Jsoup.connect("http://jobs.thoughtworks.com/PublicLists/5EA73BA03FE4D0E5DC82B9EE2C6F16DD").timeout(0).get();
            System.out.println(doc);
            Element table = doc.select("table").first();

            Iterator<Element> rows = table.select("tr").iterator();

            while (rows.hasNext()){
                Element row = rows.next();
                Iterator<Element> data = row.select("td").iterator();
                while (data.hasNext()){
                    Element datum = data.next();
                    System.out.println(datum.text());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("run complete");

    }
}
