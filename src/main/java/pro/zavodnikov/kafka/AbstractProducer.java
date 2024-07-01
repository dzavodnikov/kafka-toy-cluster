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

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 * Produce some messages to Kafka.
 */
public abstract class AbstractProducer extends AbstractClient {

    /**
     * @return messages that needs to be send. If <code>null</code> received stop
     *         the loop.
     */
    protected abstract KafkaMessage getMessage();

    /**
     * Start sending messages.
     */
    public void run() {
        try (KafkaProducer<String, String> producer = new KafkaProducer<>(this.props)) {
            while (true) {
                final KafkaMessage message = getMessage();
                if (message == null) {
                    System.out.println("All required messages was sent");
                    break;
                }

                final ProducerRecord<String, String> record = new ProducerRecord<>(message.getTopicName(),
                        message.getKafkaKey(), message.getValue());

                producer.send(record);
                producer.flush();

                System.out.println(String.format("Send message to '%s' topic", message.getTopicName()));
            }
        }
    }
}
