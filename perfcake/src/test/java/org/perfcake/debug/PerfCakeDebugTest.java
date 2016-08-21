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
package org.perfcake.debug;

import org.perfcake.PerfCakeConst;
import org.perfcake.PerfCakeException;
import org.perfcake.ScenarioExecution;
import org.perfcake.TestSetup;

import org.testng.annotations.Test;

import java.util.Properties;

/**
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class PerfCakeDebugTest extends TestSetup {

   @Test
   public void runAgent() throws PerfCakeException {
      final Properties props = new Properties();
      props.setProperty(PerfCakeConst.DEBUG_PROPERTY, "true");
      props.setProperty("perfcake.test.duration", "100000");
      //props.setProperty("org.jboss.byteman.verbose", "true");
      //props.setProperty("org.jboss.byteman.debug", "true");
      props.setProperty("org.jboss.byteman.compileToBytecode", "true");
      ScenarioExecution.execute("test-debug-agent", props);
   }

}