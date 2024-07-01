/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2023-2025 Dmitry Zavodnikov
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package pro.zavodnikov.kafka;

import java.io.IOException;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.beust.jcommander.Parameter;

/**
 * Generate random text messages.
 */
public class TextMessageProducer extends AbstractProducer {

    protected static final String LATEST_MSG = "LATEST";

    @Parameter(names = { "--topic" }, required = true, description = "Topic name.")
    private String topic;

    @Parameter(names = {
            "--total" }, description = "Number of text messages that will be generated. Default is unlimited.")
    private Integer total;

    @Parameter(names = { "--delay" }, description = "Delay between messages (in seconds). Default is 10 seconds.")
    private Integer delaySeconds = 10;

    @Override
    protected KafkaMessage getMessage() {
        final boolean isLatest = this.total != null && this.total == 1;
        if (this.total != null && this.total <= 0) {
            return null;
        } else {
            --this.total;
        }

        try {
            TimeUnit.SECONDS.sleep(this.delaySeconds);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        final int randomValue = ThreadLocalRandom.current().nextInt(0, Integer.MAX_VALUE);

        return new KafkaMessage() {

            @Override
            public String getTopicName() {
                return TextMessageProducer.this.topic;
            }

            @Override
            public String getKafkaKey() {
                return String.format("Title_%d", randomValue);
            }

            @Override
            public String getValue() {
                if (isLatest) {
                    return LATEST_MSG;
                } else {
                    return String.format("Value_%d", randomValue);
                }
            }
        };
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        final TextMessageProducer prod = new TextMessageProducer();
        prod.init(args);
        prod.run();
    }
}
