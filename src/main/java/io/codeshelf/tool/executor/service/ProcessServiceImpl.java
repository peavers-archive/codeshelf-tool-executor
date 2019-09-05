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

  @Value("${codeshelf.dry}")
  private boolean dry;

  @Override
  public String execute(final List<String> command, final String deliveryStream)
      throws IOException {

    log.info("command {}", Arrays.toString(command.toArray()).replace(",", ""));

    final ProcessBuilder processBuilder = new ProcessBuilder(command);
    final Process process = processBuilder.start();
    final String output = consoleOutput(process.getInputStream());
    final String error = consoleOutput(process.getErrorStream());
    final String outputJson = convertToJson(output);

    if (StringUtils.isNotBlank(error)) {
      log.error(error);
    } else {
      log.info("raw {}", output);
      log.info("json {}", outputJson);
    }

    if (dry) {
      log.info("skipping push to firehose - codeshelf.dry=true");
    } else {
      firehoseService.pushRecord(deliveryStream, outputJson.getBytes());
    }

    process.getInputStream().close();
    process.getErrorStream().close();

    return output;
  }

  private String convertToJson(final String xml) {

    return xml.startsWith("<") ? XML.toJSONObject(xml).toString() : xml;
  }

  private String consoleOutput(final InputStream stream) {

    return new BufferedReader(new InputStreamReader(stream))
        .lines()
        .map(line -> line + System.getProperty("line.separator"))
        .collect(Collectors.joining());
  }
}
