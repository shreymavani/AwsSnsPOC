package com.example.cloudera;

import com.amazonaws.services.sns.AmazonSNS;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.aws.messaging.core.NotificationMessagingTemplate;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ClouderaSns {

	public static void main(String[] args) {
		SpringApplication.run(ClouderaSns.class, args);
	}
}
