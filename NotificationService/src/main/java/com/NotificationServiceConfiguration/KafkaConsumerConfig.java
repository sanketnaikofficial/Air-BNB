package com.NotificationServiceConfiguration;

import com.NotificationService.AppConstants.AppConstants;
import com.NotificationService.Dto.EmailKafkaMessageDto;
import com.NotificationService.Dto.EmailRequest;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaConsumerConfig {

	@Bean
	public ConsumerFactory<String, EmailKafkaMessageDto> consumerFactory() {
	    JsonDeserializer<EmailKafkaMessageDto> deserializer = new JsonDeserializer<>(EmailKafkaMessageDto.class);
	    deserializer.setRemoveTypeHeaders(false);
	    deserializer.addTrustedPackages("com.NotificationService.Dto");
	    deserializer.setUseTypeMapperForKey(true);

	    Map<String, Object> props = new HashMap<>();
	    props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, AppConstants.KAFKA_HOST);
	    props.put(ConsumerConfig.GROUP_ID_CONFIG, AppConstants.TOPIC);
	    props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
	    props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
	    props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, deserializer);

	    return new DefaultKafkaConsumerFactory<>(props, new StringDeserializer(), deserializer);
	}

	@Bean
	public ConcurrentKafkaListenerContainerFactory<String, EmailKafkaMessageDto> kafkaListenerContainerFactory() {
	    ConcurrentKafkaListenerContainerFactory<String, EmailKafkaMessageDto> factory = new ConcurrentKafkaListenerContainerFactory<>();
	    factory.setConsumerFactory(consumerFactory());
	    return factory;
	}

}

