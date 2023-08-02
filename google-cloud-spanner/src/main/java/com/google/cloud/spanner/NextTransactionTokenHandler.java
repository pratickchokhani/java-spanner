/*
 * Copyright 2023 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
