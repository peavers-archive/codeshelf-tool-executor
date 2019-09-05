package io.codeshelf.tool.executor.service;

import java.io.IOException;
import java.util.List;

/** @author Chris Turner (chris@forloop.space) */
public interface ProcessService {

  String execute(List<String> command, String deliveryStream)
      throws IOException, InterruptedException;
}
