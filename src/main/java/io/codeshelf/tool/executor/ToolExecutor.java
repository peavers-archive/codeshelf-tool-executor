package io.codeshelf.tool.executor;

import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehose;
import com.amazonaws.services.kinesisfirehose.AmazonKinesisFirehoseClientBuilder;
import io.codeshelf.tool.executor.properties.ToolExecutorProperties;
import io.codeshelf.tool.executor.service.FirehoseService;
import io.codeshelf.tool.executor.service.FirehoseServiceImpl;
import io.codeshelf.tool.executor.service.ProcessService;
import io.codeshelf.tool.executor.service.ProcessServiceImpl;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/** @author Chris Turner (chris@forloop.space) */
@Configuration
@EnableConfigurationProperties(ToolExecutorProperties.class)
public class ToolExecutor {

  @Bean
  protected FirehoseService firehoseService() {

    final AmazonKinesisFirehose firehose = AmazonKinesisFirehoseClientBuilder.defaultClient();

    return new FirehoseServiceImpl(firehose);
  }

  @Bean
  protected ProcessService processService() {

    final FirehoseService firehoseService = firehoseService();

    return new ProcessServiceImpl(firehoseService);
  }
}
