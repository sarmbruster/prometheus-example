package org.example;

import io.prometheus.client.Gauge;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.exporter.PushGateway;
import io.prometheus.client.hotspot.DefaultExports;

import java.io.IOException;
import java.util.Random;

public class SampleService {

    private final Random random;
    private final Gauge gauge;
//    private final PushGateway pushgateway;
    private int currentValue;

    /**
     * sample service is a placeholder for your real service
     * every 3 secs it picks another random int value between 0 and 100
     */
    public SampleService() {
        random = new Random();

        // amend a value to the list of exposed values
        gauge = Gauge.build("current_value", "Current Value").register();

        // uncomment the block below for pushgateway
        /*pushgateway = new PushGateway("127.0.0.1:9091");
        try {
            pushgateway.pushAdd(gauge, "abc");
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

    public static void main(String[] args) {
        try {

            // start a http port which is then accessed from prometheus
            // access your webbrowser using http://localhost:1234 to see the value and refresh the page to see changes
            HTTPServer server = new HTTPServer(1234);

            // uncomment the following line to expose JVM internals as well
            //DefaultExports.initialize();

            SampleService sampleService = new SampleService();
            sampleService.run();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void run() {
        while (true) {
            try {
                setCurrentValue(random.nextInt(100));
                System.out.println("current value " + currentValue);
                Thread.sleep(3_000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void setCurrentValue(int currentValue) {
        this.currentValue = currentValue;

        // update the value for prometheus
        gauge.set(currentValue);


    }

    public int getCurrentValue() {
        return currentValue;
    }
}
