/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package producer;

import java.io.IOException;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.log4j.Level;    
import org.apache.log4j.Logger;

/**
 *
 ** @author DILIP
 */
public class PNRProducer {

    final static Logger logger = Logger.getLogger(PNRProducer.class);

    public static void sendDataToPNRProducer(String data) throws IOException {

        KafkaProducer<String, String> producer = null;
        try {
            //loading properties files         
            producer = new KafkaProducer<>(ProducerPropertiesLoader.getProducerPropertiesLoader().getProp());
            //send message
            //System.out.println("sending message...");
            producer.send(new ProducerRecord<String,String>((String) ProducerPropertiesLoader.getProducerPropertiesLoader().getProp().get("topic.name"), data));
            logger.info("Message Sent....");
            //uncomment when you want ack..
//            producer.send(new ProducerRecord<String,String>(topic, data), new Callback() {
//                @Override
//                public void onCompletion(RecordMetadata metadata, Exception e) {
//                    if (e != null) {
//                        // log.debug("Send failed for record {}", record, e);
//                        System.out.println("Send failed for record {}" + new ProducerRecord<String,String>(topic, data).value() + " " + e);
//                    } else {
//                        System.out.println("send successfully…." + new ProducerRecord<String,String>(topic, data));
//                    }
//                }
//            });

        } catch (Exception ex) {
            logger.error(ex.fillInStackTrace());
        } finally {
            producer.close();
        }

    }
}
