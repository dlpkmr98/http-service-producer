/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producer;

import com.sun.net.httpserver.HttpServer;
import java.io.FileInputStream;
import java.io.IOException;  
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Properties;
import org.apache.log4j.Logger;


/**
 *
 * @author DILIP
 */
public class EmbeddedHttpServer {

    final static Logger logger = Logger.getLogger(EmbeddedHttpServer.class);

    public static void main(String[] args) throws IOException {
        logger.info("\"\\\"sync resources\\\" Kafka Producer HTTTP SERVER API........\")");
        InputStream input = null;
        Properties properties = new Properties();
        try {
            input = new FileInputStream("./resources/config.properties");
            properties.load(input);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        int port = Integer.parseInt((String) properties.get("port"));
        String host = (String) properties.get("host");
        //create HttpServer server..
        logger.info("Starting HttpServer.....");
        final HttpServer server = HttpServer.create(new InetSocketAddress(host, port), 0);
        server.createContext("/", new ServiceHeaderHandler());
        server.createContext("/" + (String) properties.get("s_info"), new ServiceHeaderHandler());
        server.createContext("/" + (String) properties.get("f_end_point"), new ServiceGetHandler());
        server.createContext("/" + (String) properties.get("s_end_point"), new ServicePostHandler());
        logger.info("End Points Context Has Been Created..");
       
        server.setExecutor(null);

        //stop server
        try {
            Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
                @Override
                public void run() {
                    server.stop(0);
                    logger.info("Shouting down.......");
                   
                }
            }));
            //starting server
            server.start();
            logger.info(String.format("Application started and available at "
                    + "%s\n Stop the application using CTRL+C", host + ":" + port)); 
             System.out.println(String.format("Application started and available at "
                    + "%s\n Stop the application using CTRL+C", host + ":" + port));  
            Thread.currentThread().join();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
