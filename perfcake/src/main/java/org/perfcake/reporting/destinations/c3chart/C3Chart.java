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
package org.perfcake.reporting.destinations.c3chart;

import org.perfcake.PerfCakeConst;
import org.perfcake.PerfCakeException;
import org.perfcake.common.PeriodType;
import org.perfcake.reporting.Measurement;
import org.perfcake.reporting.Quantity;
import org.perfcake.reporting.ReportingException;
import org.perfcake.util.StringUtil;
import org.perfcake.util.Utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Represents a single Google chart data file(s) stored in the file system.
 * Charts once read from a description file cannot be further modified and stored again!
 *
 * @author <a href="mailto:marvenec@gmail.com">Martin Večeřa</a>
 */
public class C3Chart {

   /**
    * Prefix of the files of the charts created as a combination of measured results.
    */
   protected static final String DATA_ARRAY_PREFIX = "data_array_";

   /**
    * Name of the time column.
    */
   protected static final String COLUMN_TIME = "Time";

   /**
    * Name of the iteration column.
    */
   protected static final String COLUMN_ITERATION = "Iteration";

   /**
    * Name of the percentage column.
    */
   protected static final String COLUMN_PERCENT = "Percents";

   /**
    * A logger for the class.
    */
   private static final Logger log = LogManager.getLogger(C3Chart.class);

   /**
    * Base of the file name of the chart file. E.g. from '/some/path/data/stats201501272232.js' it is just 'stats201501272232'.
    */
   private String baseName;

   /**
    * Name of this chart.
    */
   private String name;

   /**
    * The legend of the X axis of this chart.
    */
   private String xAxis;

   /**
    * The legend of the Y axis of this chart.
    */
   private String yAxis;

   /**
    * The type of the X axis. It can display the overall progress of the test in Percents, Time, or Iteration numbers.
    */
   private PeriodType xAxisType = PeriodType.TIME;

   /**
    * Attributes that should be stored from the Measurement.
    */
   private List<String> attributes;

   /**
    * The chart's group name. Charts from multiple measurements that have the same group name are later searched for matching attributes.
    */
   private String group;

   /**
    * Gets the base name of the data files of this chart.
    *
    * @return The base name of the data files of this chart.
    */
   public String getBaseName() {
      if (baseName == null) {
         baseName = group + System.getProperty(PerfCakeConst.NICE_TIMESTAMP_PROPERTY);
      }

      return baseName;
   }

   /**
    * Gets the name of the chart.
    *
    * @return The name of the chart.
    */
   public String getName() {
      return name;
   }

   /**
    * Gets the legend of the X axis of the chart.
    *
    * @return The legend of the X axis of the chart.
    */
   public String getxAxis() {
      return xAxis;
   }

   /**
    * Gets the legend of the Y axis of the chart.
    *
    * @return The legend of the Y axis of the chart.
    */
   public String getyAxis() {
      return yAxis;
   }

   /**
    * Gets the attributes stored in the chart as a List.
    *
    * @return The attributes list.
    */
   public List<String> getAttributes() {
      return attributes;
   }

   /**
    * Gets the group of the current chart.
    *
    * @return The group name of this chart.
    */
   public String getGroup() {
      return group;
   }

   /**
    * Gets the type of the X axis. It can be either Time, Percents, or Iteration number.
    *
    * @return The type of the X axis.
    */
   public PeriodType getxAxisType() {
      return xAxisType;
   }

   public void setBaseName(final String baseName) {
      this.baseName = baseName;
   }

   public void setName(final String name) {
      this.name = name;
   }

   public void setxAxis(final String xAxis) {
      this.xAxis = xAxis;
   }

   public void setyAxis(final String yAxis) {
      this.yAxis = yAxis;
   }

   public void setxAxisType(final PeriodType xAxisType) {
      this.xAxisType = xAxisType;
   }

   public void setAttributes(final List<String> attributes) {
      this.attributes = attributes;
   }

   public void setGroup(final String group) {
      this.group = group;
   }

   @Override
   public String toString() {
      return "C3Chart{" +
            "baseName='" + baseName + '\'' +
            ", name='" + name + '\'' +
            ", xAxis='" + xAxis + '\'' +
            ", yAxis='" + yAxis + '\'' +
            ", xAxisType=" + xAxisType +
            ", attributes=" + attributes +
            ", group='" + group + '\'' +
            '}';
   }
}