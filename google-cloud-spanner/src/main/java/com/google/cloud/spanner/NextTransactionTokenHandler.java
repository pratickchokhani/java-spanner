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
import com.google.spanner.v1.NextTransactionToken;
import java.util.Optional;
import javax.annotation.Nullable;

public class NextTransactionTokenHandler {

  private Optional<ByteString> token = Optional.empty();
  private long TTL_IN_MILLIS = 0;
  private ByteString issuerTransactionId = null;
  private long refreshTime = Long.MAX_VALUE;

  public void setNtt(NextTransactionToken nextTransactionToken,
      @Nullable ByteString issuerTransactionId) {
    if (nextTransactionToken == null || nextTransactionToken.getToken() == ByteString.EMPTY) {
      return;
    }
    this.token = Optional.of(nextTransactionToken.getToken());
    this.TTL_IN_MILLIS = nextTransactionToken.getTtlMillis();
    this.issuerTransactionId = issuerTransactionId;
    resetRefreshTime();
  }

  public Optional<ByteString> fetchNtt() {
    if (token.isPresent() && refreshTime >= System.currentTimeMillis()) {
      refreshTime = Long.MAX_VALUE;
      return token;
    }
    return Optional.empty();
  }

  public void refreshTtl(ByteString currentTransactionId) {
    if (currentTransactionId == null || !currentTransactionId.equals(issuerTransactionId)) {
      return;
    }
    resetRefreshTime();
  }

  private void resetRefreshTime() {
    refreshTime = System.currentTimeMillis() + TTL_IN_MILLIS;
  }
}
