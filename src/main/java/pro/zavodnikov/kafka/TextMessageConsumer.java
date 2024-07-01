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

/**
 * Consume all text messages.
 */
public class TextMessageConsumer extends AbstractConsumer {

    @Override
    protected boolean acceptMessage(final String key, final String value, final int partition, final long offset) {
        System.out.println(
                String.format("Message '%s':'%s' from (partition %d with offset %d)", key, value, partition, offset));

        if (value.equals(TextMessageProducer.LATEST_MSG)) {
            System.out.println("Got latest message: stop the consumer");
            return false;
        }

        return true;
    }

    public static void main(final String[] args) throws IOException, InterruptedException {
        final TextMessageConsumer prod = new TextMessageConsumer();
        prod.init(args);
        prod.run();
    }
}
