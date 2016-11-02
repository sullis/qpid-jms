/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.qpid.jms.provider.amqp.message;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

/**
 * Tests for behavior of AmqpWritableBuffer
 */
public class AmqpWritableBufferTest {

    @Test
    public void testGetBuffer() {
        ByteBuf buffer = Unpooled.buffer(1024);
        AmqpWritableBuffer writable = new AmqpWritableBuffer(buffer);

        assertSame(buffer, writable.getBuffer());
    }

    @Test
    public void testRemaining() {
        ByteBuf buffer = Unpooled.buffer(1024);
        AmqpWritableBuffer writable = new AmqpWritableBuffer(buffer);

        assertEquals(buffer.maxCapacity(), writable.remaining());
        writable.put((byte) 0);
        assertEquals(buffer.maxCapacity() - 1, writable.remaining());
    }

    @Test
    public void testHasRemaining() {
        ByteBuf buffer = Unpooled.buffer(100, 100);
        AmqpWritableBuffer writable = new AmqpWritableBuffer(buffer);

        assertTrue(writable.hasRemaining());
        writable.put((byte) 0);
        assertTrue(writable.hasRemaining());
        buffer.writerIndex(buffer.maxCapacity());
        assertFalse(writable.hasRemaining());
    }

    @Test
    public void testGetPosition() {
        ByteBuf buffer = Unpooled.buffer(1024);
        AmqpWritableBuffer writable = new AmqpWritableBuffer(buffer);

        assertEquals(0, writable.position());
        writable.put((byte) 0);
        assertEquals(1, writable.position());
    }

    @Test
    public void testSetPosition() {
        ByteBuf buffer = Unpooled.buffer(1024);
        AmqpWritableBuffer writable = new AmqpWritableBuffer(buffer);

        assertEquals(0, writable.position());
        writable.position(1);
        assertEquals(1, writable.position());
    }
}