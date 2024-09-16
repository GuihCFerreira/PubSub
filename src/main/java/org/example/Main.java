package org.example;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void main(String[] args) {
        String projectId = "serjava-demo";
        String subscriptionId = "sobrancelha-sub";

        try {
            Connection db = DBConnection.getConnection();
            if (db != null && !db.isClosed()) {
                System.out.println("Conectado ao banco de dados com sucesso!");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        subscribeAsyncExample(projectId, subscriptionId);
    }

    public static void subscribeAsyncExample(String projectId, String subscriptionId) {
        ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
        MessageReceiver receiver =
                (PubsubMessage message, AckReplyConsumer consumer) -> {

                    try {
                        JsonNode jsonNode = objectMapper.readTree(message.getData().toStringUtf8());

                        //Valida se a mensagem retornada é um JSON
                        if (jsonNode.isObject()) {

                            //Converte esse JSON para minha classe Order
                            Order order = objectMapper.readValue(message.getData().toStringUtf8(), Order.class);
                            DBUtils dbUtils = new DBUtils();
                            dbUtils.insertOnDB(order);

                            System.out.println("Sucesso ao gravar mensagens no banco de dados!");

                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }

                    //consumer.ack();
                };

        Subscriber subscriber = null;
        try {
            subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
            // Start the subscriber.
            subscriber.startAsync().awaitRunning();
            System.out.printf("Listening for messages on %s:\n", subscriptionName);
            // Allow the subscriber to run for 30s unless an unrecoverable error occurs.
            subscriber.awaitTerminated(30, TimeUnit.SECONDS);
        } catch (TimeoutException timeoutException) {
            // Shut down the subscriber after 30s. Stop receiving messages.
            subscriber.stopAsync();
        }
    }
}