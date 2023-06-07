package com.example.cloudera.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.net.URISyntaxException;

import static com.example.cloudera.awsSnsUtilityMethods.AwsSnsUtilityMethods.getSnsClient;

@Component
@RestController
public class Controller {


    @RequestMapping("/createTopic")
    private String createTopic(@RequestParam("topic_name") String topicName) throws URISyntaxException {
        //topic name cannot contain spaces
        final CreateTopicRequest topicCreateRequest = CreateTopicRequest.builder().name(topicName).build();

        SnsClient snsClient = getSnsClient();

        final CreateTopicResponse topicCreateResponse = snsClient.createTopic(topicCreateRequest);

        if (topicCreateResponse.sdkHttpResponse().isSuccessful()) {
            System.out.println("Topic creation successful");
            System.out.println("Topics: " + snsClient.listTopics());
        } else {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, topicCreateResponse.sdkHttpResponse().statusText().get()
            );
        }

        snsClient.close();
        return "Topic ARN: " + topicCreateResponse.topicArn();
    }


    @RequestMapping("/addSubscribers")
    private String addSubscriberToTopic(@RequestParam("arn") String arn, @RequestParam("endpointUrl") String endpointUrl) throws URISyntaxException {

        SnsClient snsClient = getSnsClient();

        SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .topicArn(arn)
                .protocol("http") // or "https" for secure endpoint
                .endpoint(endpointUrl)
                .build();

        SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);

        if (subscribeResponse.sdkHttpResponse().isSuccessful()) {
            System.out.println("Subscriber creation successful");
        } else {
            throw new ResponseStatusException(
                    HttpStatus.INTERNAL_SERVER_ERROR, subscribeResponse.sdkHttpResponse().statusText().get()
            );
        }

        snsClient.close();

        return "Subscribed Successfully at ARN : "+ arn;
    }
}