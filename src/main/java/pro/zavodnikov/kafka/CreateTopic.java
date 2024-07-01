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
import java.util.Arrays;
import java.util.concurrent.ExecutionException;

import org.apache.kafka.clients.admin.Admin;
import org.apache.kafka.clients.admin.NewTopic;

import com.beust.jcommander.Parameter;

/**
 * Create provided Kafka topic.
 */
public class CreateTopic extends AbstractClient {

    @Parameter(names = { "--name" }, required = true, description = "Kafka topic name.")
    private String name;

    @Parameter(names = { "--partitions" }, description = "Number of partitions. By default is 1.")
    private Integer partitions = 1;

    @Parameter(names = { "--replication" }, description = "Replication factor. By default is 1.")
    private Short replication = 1;

    public void run() throws IOException, InterruptedException, ExecutionException {
        try (Admin admin = Admin.create(this.props)) {
            if (admin.listTopics().names().get().contains(this.name)) {
                System.out.println(String.format("Topic '%s' exists", this.name));
            } else {
                admin.createTopics(Arrays.asList(new NewTopic(this.name, this.partitions, this.replication)));

                System.out.println(String.format("Topic '%s' created", this.name));
            }
        }
    }

    public static void main(final String[] args) throws IOException, InterruptedException, ExecutionException {
        final CreateTopic prod = new CreateTopic();
        prod.init(args);
        prod.run();
    }
}
