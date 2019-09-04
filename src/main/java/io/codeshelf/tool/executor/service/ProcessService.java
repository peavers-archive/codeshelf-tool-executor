package io.codeshelf.tool.executor.service;

import java.io.IOException;
import java.util.List;

/** @author Chris Turner (chris@forloop.space) */
public interface ProcessService {

  void execute(List<String> command) throws IOException, InterruptedException;
}
