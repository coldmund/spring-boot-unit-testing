package com.luv2code.springmvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.jdbc.Sql;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;
import com.luv2code.springmvc.service.StudentAndGradeService;

@TestPropertySource("/application.properties")
@SpringBootTest
public class StudentAndGradeServiceTest {

  @Autowired
  private StudentAndGradeService studentService;

  @Autowired
  private StudentDao studentDao;
  @Autowired
  private MathGradesDao mathGradeDao;
  @Autowired
  private ScienceGradesDao scienceGradeDao;
  @Autowired
  private HistoryGradesDao historyGradeDao;

  @Autowired
  private JdbcTemplate jdbc;

  @Value("${sql.scripts.create.student}")
  private String sqlAddStudent;
  @Value("${sql.scripts.create.math.grade}")
  private String sqlAddMathGrade;
  @Value("${sql.scripts.create.science.grade}")
  private String sqlAddScienceGrade;
  @Value("${sql.scripts.create.history.grade}")
  private String sqlAddHistoryGrade;
  @Value("${sql.scripts.delete.student}")
  private String sqlDeleteStudent;
  @Value("${sql.scripts.delete.math.grade}")
  private String sqlDeleteMathGrade;
  @Value("${sql.scripts.delete.science.grade}")
  private String sqlDeleteScienceGrade;
  @Value("${sql.scripts.delete.history.grade}")
  private String sqlDeleteHistoryGrade;

  @BeforeEach
  public void setupDatabase() {

    jdbc.execute(sqlAddStudent);

    jdbc.execute(sqlAddMathGrade);
    jdbc.execute(sqlAddScienceGrade);
    jdbc.execute(sqlAddHistoryGrade);
  }

  @AfterEach
  public void setupAfterTransaction() {

    jdbc.execute(sqlDeleteStudent);

    jdbc.execute(sqlDeleteMathGrade);
    jdbc.execute(sqlDeleteScienceGrade);
    jdbc.execute(sqlDeleteHistoryGrade);
  }

  @Test
  public void createStudentService() {

    studentService.createStudent("Chad", "Darby", "chad.darby@luv2code_school.com");

    CollegeStudent student = studentDao.findByEmailAddress("chad.darby@luv2code_school.com");

    assertEquals("chad.darby@luv2code_school.com", student.getEmailAddress(),
        "find by email");
  }

  @Test
  public void isStudentNullCheck() {

    assertTrue(studentService.checkIfStudentIsNull(1));

    assertFalse(studentService.checkIfStudentIsNull(0));
  }

  @Test
  public void deleteStudentService() {

    Optional<CollegeStudent> deletedCollegeStudent = studentDao.findById(1);
    Optional<MathGrade> deletedMathGrade = mathGradeDao.findById(1);
    Optional<HistoryGrade> deletedHistoryGrade = historyGradeDao.findById(1);
    Optional<ScienceGrade> deletedScienceGrade = scienceGradeDao.findById(1);

    assertTrue(deletedCollegeStudent.isPresent(), "Return True");
    assertTrue(deletedMathGrade.isPresent());
    assertTrue(deletedScienceGrade.isPresent());
    assertTrue(deletedHistoryGrade.isPresent());

    studentService.deleteStudentService(1);

    deletedCollegeStudent = studentDao.findById(1);
    deletedMathGrade = mathGradeDao.findById(1);
    deletedScienceGrade = scienceGradeDao.findById(1);
    deletedHistoryGrade = historyGradeDao.findById(1);

    assertFalse(deletedCollegeStudent.isPresent(), "Return False");
    assertFalse(deletedMathGrade.isPresent());
    assertFalse(deletedScienceGrade.isPresent());
    assertFalse(deletedHistoryGrade.isPresent());
  }

  @Sql("/insertData.sql")
  @Test
  public void getGradebookService() {

    Iterable<CollegeStudent> iterableCollegeStudent = studentService.getGradebook();

    List<CollegeStudent> collegeStudents = new ArrayList<>();

    for (CollegeStudent collegeStudent : iterableCollegeStudent) {
      collegeStudents.add(collegeStudent);
    }

    assertEquals(5, collegeStudents.size());
  }

  @Test
  public void createGradeService() {

    // create the grade
    assertTrue(studentService.createGrade(80.50, 1, "math"));
    assertTrue(studentService.createGrade(80.50, 1, "science"));
    assertTrue(studentService.createGrade(80.50, 1, "history"));

    // get all grades with studentId
    Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(1);
    Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(1);
    Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(1);

    // verify there is grades
    assertTrue(((Collection<MathGrade>)mathGrades).size() == 2, "Student has math grades");
    assertTrue(((Collection<ScienceGrade>)scienceGrades).size() == 2, "Student has science grades");
    assertTrue(((Collection<HistoryGrade>)historyGrades).size() == 2, "Student has history grades");
  }

  @Test
  public void createGradeServiceReturnFalse() {

    assertFalse(studentService.createGrade(105, 1, "math"));
    assertFalse(studentService.createGrade(-5, 1, "math"));
    assertFalse(studentService.createGrade(80.50, 2, "math"));
    assertFalse(studentService.createGrade(80.50, 1, "literature"));
  }

  @Test
  public void deleteGradeService() {

    assertEquals(1, studentService.deleteGrade(1, "math"),
        "Returns student id after delete");
    assertEquals(1, studentService.deleteGrade(1, "science"),
        "Returns student id after delete");
    assertEquals(1, studentService.deleteGrade(1, "history"),
        "Returns student id after delete");
  }

  @Test
  public void deleteGradeServiceReturnStudentIdOfZero() {

    assertEquals(0, studentService.deleteGrade(0, "science"),
        "No student should have 0 id");
    assertEquals(0, studentService.deleteGrade(1, "literature"),
        "No student should have a literature class");
  }

  @Test
  public void studentInformation() {

    GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(1);

    assertNotNull(gradebookCollegeStudent);
    assertEquals(1, gradebookCollegeStudent.getId());
    assertEquals("Eric", gradebookCollegeStudent.getFirstname());
    assertEquals("Roby", gradebookCollegeStudent.getLastname());
    assertEquals("eric.roby@luv2code_school.com", gradebookCollegeStudent.getEmailAddress());
    assertTrue(gradebookCollegeStudent.getStudentGrades().getMathGradeResults().size() == 1);
    assertTrue(gradebookCollegeStudent.getStudentGrades().getScienceGradeResults().size() == 1);
    assertTrue(gradebookCollegeStudent.getStudentGrades().getHistoryGradeResults().size() == 1);
  }

  @Test
  public void studentInformationServiceReturnNull() {

    GradebookCollegeStudent gradebookCollegeStudent = studentService.studentInformation(0);

    assertNull(gradebookCollegeStudent);
  }
}
