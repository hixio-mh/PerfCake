/*
 * -----------------------------------------------------------------------\
 * PerfCake
 *
 * Copyright (C) 2010 - 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -----------------------------------------------------------------------/
 */
package org.perfcake.message.sender;

import org.perfcake.PerfCakeException;
import org.perfcake.message.Message;
import org.perfcake.util.ObjectFactory;
import org.perfcake.util.Utils;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Properties;

/**
 * Tests {@link org.perfcake.message.sender.CommandSender}.
 *
 * Dependent on BASH script greetings.sh which acts as a mock application.
 * Sends commands to the script to achieve 100% code coverage of all semantically
 * reachable basic blocks.
 *
 * Testing criterion: edge-pair coverage
 *
 * @author <a href="mailto:karasek.jose@gmail.com">Josef Karásek</a>
 */
@Test(groups = { "unit" })
public class CommandSenderTest {

   private static String scriptFile;

   @BeforeClass
   public static void determineOS() throws PerfCakeException {

      if (System.getProperty("os.name").toLowerCase().contains("windows")) {
         scriptFile = Utils.getResource("/") + File.separator + "greeting.bat";
      } else {
         scriptFile = "./src/test/resources/greeting.sh";
      }
   }

   @Test
   public void nullMessageNoPayloadFakeArgumentTest() {
      final Properties senderProperties = new Properties();
      senderProperties.setProperty("target", scriptFile + " Pepo");
      final Message message = null;
      String response = sendUsingCommandSender(senderProperties, message, null);
      Assert.assertEquals(response.trim(), "Greetings Pepo! From ARG #1.");
   }

   @Test
   public void emptyMessageNoPayloadFakeArgumentTest() {
      final Properties senderProperties = new Properties();
      senderProperties.setProperty("target", scriptFile + " Pepo");
      final Message message = new Message();
      String response = sendUsingCommandSender(senderProperties, message, null);
      Assert.assertEquals(response.trim(), "Greetings Pepo! From ARG #1.");
   }

   @Test
   public void messageWithPayloadFromStdinTest() {
      final Properties senderProperties = new Properties();
      senderProperties.setProperty("target", scriptFile);
      final Message message = new Message();
      message.setPayload("Pepo");
      String response = sendUsingCommandSender(senderProperties, message, null);
      Assert.assertEquals(response.trim(), "Greetings Pepo! From STDIN.");
   }

   @Test
   public void messageWithPayloadFromArgumentTest() {
      final Properties senderProperties = new Properties();
      senderProperties.setProperty("target", scriptFile);
      senderProperties.setProperty("messageFrom", "ARGUMENTS");
      final Message message = new Message();
      message.setPayload("Pepo");
      String response = sendUsingCommandSender(senderProperties, message, null);
      Assert.assertEquals(response.trim(), "Greetings Pepo! From ARG #1.");
   }

   @Test
   public void messageWithPayloadFromArgumentWithGlobalPropertyTest() {
      final Properties senderProperties = new Properties();
      senderProperties.setProperty("target", scriptFile);
      senderProperties.setProperty("messageFrom", "ARGUMENTS");
      final Message message = new Message();
      message.setPayload("Pepo");
      final Properties additionalMessageProperties = new Properties();
      additionalMessageProperties.setProperty("TEST_VARIABLE", "testing");
      String response = sendUsingCommandSender(senderProperties, message, additionalMessageProperties);
      Assert.assertEquals(response.trim(), "Greetings Pepo! From ARG #1. TEST_VARIABLE=testing.");
   }

   @Test
   public void messageWithHeaderAndPayloadFromArgumentTest() {
      final Properties senderProperties = new Properties();
      senderProperties.setProperty("target", scriptFile);
      senderProperties.setProperty("messageFrom", "ARGUMENTS");
      final Message message = new Message();
      message.setPayload("Pepo");
      message.setHeader("TEST_VARIABLE", "testing");
      String response = sendUsingCommandSender(senderProperties, message, null);
      Assert.assertEquals(response.trim(), "Greetings Pepo! From ARG #1. TEST_VARIABLE=testing.");
   }

   @Test
   public void messageWithPropertyAndPayloadFromArgumentTest() {
      final Properties senderProperties = new Properties();
      senderProperties.setProperty("target", scriptFile);
      senderProperties.setProperty("messageFrom", "ARGUMENTS");
      final Message message = new Message();
      message.setPayload("Pepo");
      message.setProperty("TEST_VARIABLE", "testing");
      String response = sendUsingCommandSender(senderProperties, message, null);
      Assert.assertEquals(response.trim(), "Greetings Pepo! From ARG #1. TEST_VARIABLE=testing.");
   }

   private String sendUsingCommandSender(final Properties senderProperties, final Message message, final Properties additionalProperties) {
      try {
         final CommandSender sender = (CommandSender) ObjectFactory.summonInstance(CommandSender.class.getName(), senderProperties);
         return _sendMessage(sender, message, additionalProperties);
      } catch (Exception e) {
         e.printStackTrace();
         Assert.fail(e.getMessage());
      }

      return null;
   }

   private String _sendMessage(final CommandSender sender, final Message message, final Properties additionalProperties) throws Exception {
      String response = null;
      sender.init();
      sender.preSend(message, additionalProperties);
      response = (String) sender.send(message, null);
      sender.postSend(message);
      sender.close();
      return response;
   }
}
