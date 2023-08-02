package com.google.cloud.spanner.v1;

import com.google.common.base.Optional;
import com.google.protobuf.ByteString;

public class NextTransactionTokenHandler {

  private static final long TIME_GAP_MILLIS = 8 * 1000;

  private Optional<ByteString> ntt = Optional.absent();
  private ByteString issuerTransactionId = null;
  private long refreshTime = 0;


  public void setNtt(ByteString ntt, ByteString issuerTransactionId) {
    if (ntt == null) {
      return;
    }
    refreshTime = System.currentTimeMillis();
    this.ntt = Optional.of(ntt);
  }

  public Optional<ByteString> fetchNtt() {
    if ()
  }


}
