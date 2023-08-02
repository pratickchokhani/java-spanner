package com.google.cloud.spanner;

import com.google.protobuf.ByteString;
import java.util.Optional;
import javax.annotation.Nullable;

public class NextTransactionTokenHandler {

  private static final long TIME_GAP_MILLIS = 8 * 1000;

  private ByteString ntt = null;
  private ByteString issuerTransactionId = null;
  private long refreshTime = 0;


  public void setNtt(ByteString ntt, @Nullable ByteString issuerTransactionId) {
    if (ntt == null) {
      return;
    }
    this.ntt = ntt;
    this.issuerTransactionId = issuerTransactionId;
    resetRefreshTime();
  }

  public Optional<ByteString> fetchNtt() {
    if (refreshTime < System.currentTimeMillis()) {
      return Optional.ofNullable(ntt);
    }
    return Optional.empty();
  }

  public void refreshTtl(ByteString currentTransactionId) {
    if (currentTransactionId == null) {
      if (issuerTransactionId != null) {
        return;
      }
    } else if (issuerTransactionId != null && !currentTransactionId.equals(issuerTransactionId)) {
      return;
    }
    resetRefreshTime();
  }

  private void resetRefreshTime() {
    refreshTime = System.currentTimeMillis() + TIME_GAP_MILLIS;
  }


}
