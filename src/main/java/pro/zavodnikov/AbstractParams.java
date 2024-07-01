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
package pro.zavodnikov;

import java.util.Properties;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

/**
 * Class that useful for parameter initialization.
 *
 * Just add fields with {@link Parameter} annotation and call constructor.
 */
public abstract class AbstractParams {

    @Parameter(names = { "--help", "-h" }, help = true, description = "Print help")
    private boolean help = false;

    /**
     * Initialized parameters. <strong>Do not call it in constructor!</strong>
     */
    public void init(final String[] args) {
        final JCommander commander = JCommander.newBuilder().addObject(this).build();
        commander.setProgramName(getClass().getCanonicalName());
        commander.parse(args);

        if (this.help) {
            commander.usage();
            System.exit(0);
        }
    }

    protected void initProps(final Properties props, final String[] requiredProperties,
            final String[] optionalProperties) {
        for (String key : requiredProperties) {
            final String value = System.getProperty(key);
            if (value == null) {
                throw new RuntimeException(String.format("Property '%s' was not defined", key));
            }
            System.out.println(String.format("    %s: %s", key, value));
            props.setProperty(key, value);
        }

        for (String key : optionalProperties) {
            final String value = System.getProperty(key);
            if (value != null) {
                System.out.println(String.format("    %s: %s", key, value));
                props.setProperty(key, value);
            }
        }
    }
}
