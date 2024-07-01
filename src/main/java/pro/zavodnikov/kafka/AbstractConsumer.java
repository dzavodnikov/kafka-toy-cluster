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

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import com.beust.jcommander.Parameter;

/**
 * Consumer messages from Kafka.
 */
public abstract class AbstractConsumer extends AbstractClient {

    @Parameter(names = { "--topic-list" }, required = true, description = "List of Kafka topics separated by comma.")
    private String topicList;

    /**
     * @return <code>true</code> value if we still want to consume messages and
     *         <code>false<code> if we want to stop.
     */
    protected abstract boolean acceptMessage(String key, String value, int partition, long offset);

    /**
     * Start receiving messages.
     */
    public void run() {
        try (KafkaConsumer<String, String> consumer = new KafkaConsumer<>(this.props)) {
            final List<String> topics = Arrays.stream(topicList.split(",")).map(String::strip)
                    .collect(Collectors.toList());
            System.out.println(String.format("Start consuming from %s", String.join(", ", topics)));
            consumer.subscribe(topics);

            while (true) {
                final ConsumerRecords<String, String> records = consumer.poll(Duration.ofSeconds(1));

                for (ConsumerRecord<String, String> r : records) {
                    final boolean isContinue = acceptMessage(r.key(), r.value(), r.partition(), r.offset());
                    if (!isContinue) {
                        System.out.println("Stop consuming");
                        return;
                    }
                }
            }
        }
    }
}
