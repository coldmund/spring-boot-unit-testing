package com.luv2code.junitdemo;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledForJreRange;
import org.junit.jupiter.api.condition.EnabledIfEnvironmentVariable;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.junit.jupiter.api.condition.EnabledOnJre;
import org.junit.jupiter.api.condition.EnabledOnOs;
import org.junit.jupiter.api.condition.JRE;
import org.junit.jupiter.api.condition.OS;

public class ConditionalTest {

  @Test
  @Disabled("Don't run until JIRA #123 is resolved")
  void basicTest() {
  }

  @Test
  @EnabledOnOs(OS.LINUX)
  void testForLinuxOnly() {
  }

  @Test
  @EnabledOnOs(OS.WINDOWS)
  void testForWindowsOnly() {
  }

  @Test
  @EnabledOnOs({OS.LINUX, OS.WINDOWS})
  void testForLinuxAndWindowsOnly() {
  }


  @Test
  @EnabledOnJre(JRE.JAVA_17)
  void testForJava17() {
  }

  @Test
  @EnabledOnJre(JRE.JAVA_21)
  void testForJava21() {
  }

  @Test
  @EnabledForJreRange(min = JRE.JAVA_17, max = JRE.JAVA_25)
  void testForJava17to25() {
  }

  @Test
  @EnabledForJreRange(max = JRE.JAVA_11)
  void testForUnderJava11() {
  }

  @Test
  @EnabledIfEnvironmentVariable(named = "LUV2CODE_ENV", matches = "DEV")
  void testOnlyForDevEnvironment() {
  }

  @Test
  @EnabledIfSystemProperty(named = "LUV2CODE_SYS_PROP", matches = "CI_CD_DEPLOY")
  void testOnlyForSystemProperty() {
  }
}
