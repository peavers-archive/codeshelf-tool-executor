package io.codeshelf.tool.executor.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.json.XML;
import org.springframework.beans.factory.annotation.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/** @author Chris Turner (chris@forloop.space) */
@Slf4j
@RequiredArgsConstructor
public class ProcessServiceImpl implements ProcessService {

  private final FirehoseService firehoseService;

  /** Should the results be sent to Firehose */
  @Value("${codeshelf.dry}")
  private boolean dry;

  /**
   * Handles the leg work of the tools. Executes the command passed in, then passes off the results
   * to the {@link FirehoseService} to send the results to AWS Kinesis Firehose.
   *
   * @param command to execute through the process builder
   * @param deliveryStream the firehose channel to send the output too
   * @return the json representation of the output from the process builder
   */
  @Override
  public String execute(final List<String> command, final String deliveryStream)
      throws IOException {

    log.info("command {}", Arrays.toString(command.toArray()).replace(",", ""));

    final ProcessBuilder processBuilder = new ProcessBuilder(command);
    final Process process = processBuilder.start();
    final String output = readConsole(process.getInputStream());
    final String error = readConsole(process.getErrorStream());
    final String outputJson = convertToJson(output);

    logResult(output, error, outputJson);

    if (dry) {
      log.info("skipping push to firehose - codeshelf.dry=true");
    } else {
      firehoseService.pushRecord(deliveryStream, outputJson.getBytes());
    }

    process.getInputStream().close();
    process.getErrorStream().close();

    return output;
  }

  /**
   * Simple helper method to output the results of the process builder to the console.
   *
   * @param output input stream from process builder
   * @param error error stream from process builder
   * @param outputJson the json representation of the output stream
   */
  private void logResult(final String output, final String error, final String outputJson) {

    if (StringUtils.isNotBlank(error)) {
      log.error(error);
    } else {
      log.info("raw {}", output);
      log.info("json {}", outputJson);
    }
  }

  /**
   * Simple helper method to check if a string starts with '<', if so, convert it to Json
   *
   * @param xml string to convert to json
   * @return json regardless of what is sent in
   */
  private String convertToJson(final String xml) {

    return xml.startsWith("<") ? XML.toJSONObject(xml).toString() : xml;
  }

  /**
   * Collect the result from the console and report it back.
   *
   * @param stream input stream from the process builder
   * @return collection of all error messages
   */
  private String readConsole(final InputStream stream) {

    return new BufferedReader(new InputStreamReader(stream))
        .lines()
        .map(line -> line + System.getProperty("line.separator"))
        .collect(Collectors.joining());
  }
}
