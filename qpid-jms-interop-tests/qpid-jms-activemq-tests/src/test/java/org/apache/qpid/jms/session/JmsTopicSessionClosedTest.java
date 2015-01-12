/**
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
package org.apache.qpid.jms.session;

import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;

import org.apache.qpid.jms.JmsConnectionFactory;
import org.apache.qpid.jms.support.AmqpTestSupport;
import org.junit.Before;
import org.junit.Test;

/**
 * Tests behaviour after a TopicSession is closed.
 */
public class JmsTopicSessionClosedTest extends AmqpTestSupport {

    private TopicSession session;
    private TopicPublisher publisher;
    private TopicSubscriber subscriber;

    protected void createAndCloseSession() throws Exception {
        JmsConnectionFactory factory = new JmsConnectionFactory(getBrokerAmqpConnectionURI());
        connection = factory.createTopicConnection();

        session = ((TopicConnection) connection).createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
        Topic destination = session.createTopic(name.getMethodName());

        publisher = session.createPublisher(destination);
        subscriber = session.createSubscriber(destination);

        // Close the session explicitly, without closing the above.
        session.close();
    }

    @Before
    @Override
    public void setUp() throws Exception {
        super.setUp();
        createAndCloseSession();
    }

    @Test(timeout=30000)
    public void testSessionCloseAgain() throws Exception {
        // Close it again
        session.close();
    }

    @Test(timeout=30000)
    public void testSubscriberCloseAgain() throws Exception {
        // Close it again (closing the session should have closed it already).
        subscriber.close();
    }

    @Test(timeout=30000)
    public void testPublisherCloseAgain() throws Exception {
        // Close it again (closing the session should have closed it already).
        publisher.close();
    }

    @Test(timeout=30000, expected=javax.jms.IllegalStateException.class)
    public void testSubscriberGetTopicFails() throws Exception {
        subscriber.getTopic();
    }

    @Test(timeout=30000, expected=javax.jms.IllegalStateException.class)
    public void testPublisherGetTopicFails() throws Exception {
        publisher.getTopic();
    }
}
