package io.codeshelf.tool.executor.service;

import com.amazonaws.services.kinesisfirehose.model.PutRecordResult;

/** @author Chris Turner (chris@forloop.space) */
public interface FirehoseService {

  PutRecordResult pushRecord(String deliveryStream, byte[] bytes);
}
