import com.tanmesh.splatter.configuration.KafkaConsumerConfig;
import com.tanmesh.splatter.utils.CustomKafkaConsumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.Test;


public class KafkaTest {

  @Test
  public void testingKafkaConsumer() {
    CustomKafkaConsumer consumer = new CustomKafkaConsumer("topic-user", new KafkaConsumerConfig());
    
    ConsumerRecords<String, String> records = consumer.poll();
    for (ConsumerRecord<String, String> record : records) {
      System.out.println("Received message: " + record.value());
    }
  }
}
