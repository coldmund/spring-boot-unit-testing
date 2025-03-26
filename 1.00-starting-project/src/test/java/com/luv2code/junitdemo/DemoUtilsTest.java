package com.luv2code.junitdemo;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertLinesMatch;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DisplayNameGeneration;
import org.junit.jupiter.api.DisplayNameGenerator;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

// @DisplayNameGeneration(DisplayNameGenerator.IndicativeSentences.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DemoUtilsTest {

  private DemoUtils demoUtils;

  // @BeforeAll
  // static void setupBeforeEachClass() {

  //   System.out.println("@BeforeAll");
  // }

  // @AfterAll
  // static void tearDownAfterAll() {

  //   System.out.println("@AfterAll");
  // }

  @BeforeEach
  public void setupBeforeEach() {

    demoUtils = new DemoUtils();
    // System.out.println("@BeforeEach");
  }

  // @AfterEach
  // void tearDownAfterEach() {
  //   System.out.println("@AfterEach");
  // }

  @Test
  @DisplayName("Equals and Not Equals")
  @Order(3)
  public void testEqualsAndNotEquals() {

    // System.out.println("Running test: testEqualsAndNotEquals");

    assertEquals(6, demoUtils.add(2, 4), "2+4 must be 6");
    assertNotEquals(6, demoUtils.add(1, 9), "1+9 must be 6");
  }

  @Test
  @DisplayName("Null and Not Null")
  @Order(1)
  public void testNullAndNotNull() {

    // System.out.println("Running test: testNullAndNotNull");

    String str1 = null;
    String str2 = "luv2code";

    assertNull(demoUtils.checkNull(str1), "Object should be null");
    assertNotNull(demoUtils.checkNull(str2), "Object should not be null");
  }

  @Test
  @DisplayName("Same and Not Same")
  public void testSameAndNotSame() {

    String str = "luv2code";

    assertSame(demoUtils.getAcademy(), demoUtils.getAcademyDuplicate(), "Objects should refer to same");
    assertNotSame(str, demoUtils.getAcademy(), "Objects should not refer to same object");
  }

  @Test
  @DisplayName("True and False")
  public void testTrueFalse() {

    int gradeOne = 10;
    int gradeTwo = 5;
    assertTrue(demoUtils.isGreater(gradeOne, gradeTwo), "this should return true");
    assertFalse(demoUtils.isGreater(gradeTwo, gradeOne), "this should return false");
  }

  @Test
  @DisplayName("Array Equals")
  @Order(-7)
  public void testArrayEquals() {

    String[] stirngArray = {"A", "B", "C"};

    assertArrayEquals(stirngArray, demoUtils.getFirstThreeLettersOfAlphabet(), "Arrays should be the same");
  }

  @Test
  @DisplayName("Iterable equals")
  public void testIterableEquals() {

    List<String> theList = List.of("luv", "2", "code");

    assertIterableEquals(theList, demoUtils.getAcademyInList(), "Expected list should be same as actual list");
  }

  @Test
  @DisplayName("Lines match")
  public void testLinesMatch() {

    List<String> theList = List.of("luv", "2", "code");

    assertLinesMatch(theList, demoUtils.getAcademyInList(), "Lines should match");
  }

  @Test
  @DisplayName("Throws and does not throw")
  public void testThrows() {

    assertThrows(Exception.class, () -> { demoUtils.throwException(-1); }, "Should throw exception");
    assertDoesNotThrow(() -> { demoUtils.throwException(1); }, "Should throw exception");
  }

  @Test
  @DisplayName("Timeout")
  public void testTimeout() {

    assertTimeoutPreemptively(Duration.ofSeconds(3), () -> { demoUtils.checkTimeout();} , "Method should execute in 3 seconds");
  }

  @Test
  @DisplayName("Multiply")
  public void testMultiply() {

    assertEquals(12, demoUtils.multiply(4,3), "4*3 must be 12");
  }
}
