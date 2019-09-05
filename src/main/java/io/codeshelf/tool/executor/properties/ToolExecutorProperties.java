package io.codeshelf.tool.executor.properties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

/** @author Chris Turner (chris@forloop.space) */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ConfigurationProperties(prefix = "codeshelf")
public class ToolExecutorProperties {

  /** If true, will send results to Firehose. */
  private boolean dry;

  /** Directory to run the tool in. */
  private String workingDir;

  /** AWS Kinesis Firehose name */
  private String deliveryStream;
}
