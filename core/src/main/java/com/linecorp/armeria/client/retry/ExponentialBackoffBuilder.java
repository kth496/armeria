/*
 * Copyright 2024 LY Corporation
 *
 * LY Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */
package com.linecorp.armeria.client.retry;

import static com.google.common.base.Preconditions.checkArgument;

import com.linecorp.armeria.common.annotation.UnstableApi;

/**
 * A builder for creating instances of {@link ExponentialBackoff}.
 *
 * <p>This builder allows you to configure an exponential backoff strategy by specifying
 * the initial delay, the maximum delay, and a multiplier. The exponential backoff
 * increases the delay between retries exponentially, starting from the initial delay and
 * multiplying the delay by the specified multiplier after each retry, up to the maximum delay.</p>
 *
 * <p>Example usage:</p>
 *
 * <pre>
 * {@code
 * ExponentialBackoff backoff = new ExponentialBackoffBuilder()
 *     .initialDelayMillis(100)
 *     .maxDelayMillis(10000)
 *     .multiplier(2.0)
 *     .build();
 * }
 * </pre>
 *
 * @see ExponentialBackoff
 */
@UnstableApi
public final class ExponentialBackoffBuilder {
    private long initialDelayMillis;
    private long maxDelayMillis;
    private double multiplier = 2.0;

    ExponentialBackoffBuilder() {}

    /**
     * Builds and returns a new {@link ExponentialBackoff} instance with the configured
     * initial delay, maximum delay, and multiplier.
     *
     * @return a newly created {@link ExponentialBackoff} with the configured delays and multiplier
     */
    public Backoff build() {
        return new ExponentialBackoff(initialDelayMillis, maxDelayMillis, multiplier);
    }

    /**
     * Sets the initial delay in milliseconds for the {@link ExponentialBackoff}.
     *
     * <p>The initial delay is the starting value for the exponential backoff, determining
     * the delay before the first retry. Subsequent delays will increase exponentially
     * based on the multiplier.</p>
     *
     * @param initialDelayMillis the initial delay in milliseconds
     * @return this {@code ExponentialBackoffBuilder} instance for method chaining
     */
    public ExponentialBackoffBuilder initialDelayMillis(long initialDelayMillis) {
        checkArgument(initialDelayMillis >= 0, "initialDelayMillis: %s (expected: >= 0)", initialDelayMillis);
        checkArgument(initialDelayMillis <= maxDelayMillis, "initialDelayMillis: %s (expected: <= %s)",
                      initialDelayMillis, maxDelayMillis);
        this.initialDelayMillis = initialDelayMillis;
        return this;
    }

    /**
     * Sets the maximum delay in milliseconds for the {@link ExponentialBackoff}.
     *
     * <p>The maximum delay is the upper limit for the backoff delay. Once the delay reaches
     * this value, it will not increase further, even if the multiplier would result in a higher value.</p>
     *
     * @param maxDelayMillis the maximum delay in milliseconds
     * @return this {@code ExponentialBackoffBuilder} instance for method chaining
     */
    public ExponentialBackoffBuilder maxDelayMillis(long maxDelayMillis) {
        checkArgument(maxDelayMillis >= 0, "maxDelayMillis: %s (expected: >= 0)", maxDelayMillis);
        checkArgument(initialDelayMillis <= maxDelayMillis, "maxDelayMillis: %s (expected: >= %s)",
                      maxDelayMillis, initialDelayMillis);
        this.maxDelayMillis = maxDelayMillis;
        return this;
    }

    /**
     * Sets the multiplier for the {@link ExponentialBackoff}.
     *
     * <p>The multiplier controls how much the delay increases after each retry.
     * The delay for each retry is determined by multiplying the previous delay by this value,
     * until the maximum delay is reached.</p>
     *
     * @param multiplier the multiplier for the exponential backoff
     * @return this {@code ExponentialBackoffBuilder} instance for method chaining
     */
    public ExponentialBackoffBuilder multiplier(double multiplier) {
        checkArgument(multiplier > 1.0, "multiplier: %s (expected: > 1.0)", multiplier);
        this.multiplier = multiplier;
        return this;
    }
}
