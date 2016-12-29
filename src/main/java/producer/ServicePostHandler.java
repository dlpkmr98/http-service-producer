/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producer;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author DILIP
 */
public class ServicePostHandler implements HttpHandler {   
    

    @Override
    public void handle(final HttpExchange he) throws IOException {
        // parse request

        QUEUE_EXECUTOR.submit(new Runnable() {
            @Override
            public void run() {
                String response = "";
                try {

                    InputStreamReader isr = new InputStreamReader(he.getRequestBody(), "utf-8");
                    StringBuilder builder = new StringBuilder();
                    int ptr = 0;
                    while ((ptr = isr.read()) != -1) {
                        builder.append((char) ptr);
                    }
                    PNRProducer.sendDataToPNRProducer(builder.toString());
                } catch (Exception ex) {
                    response = ex.toString();
                    return;
                }
                try {
                    he.sendResponseHeaders(200, response.length());
                    OutputStream os = he.getResponseBody();
                    os.write(response.toString().getBytes());
                    os.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                //System.out.println("theread++++++" + Thread.currentThread().getName());
            }
        });
          

    }
    private static final ExecutorService QUEUE_EXECUTOR = Executors.newFixedThreadPool(Integer.parseInt(ProducerPropertiesLoader.getProducerPropertiesLoader().getProp().get("max_thread_pool_size").toString().trim()));

}
