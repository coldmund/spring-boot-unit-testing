package com.luv2code.springmvc.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.luv2code.springmvc.models.CollegeStudent;
import com.luv2code.springmvc.models.Grade;
import com.luv2code.springmvc.models.GradebookCollegeStudent;
import com.luv2code.springmvc.models.HistoryGrade;
import com.luv2code.springmvc.models.MathGrade;
import com.luv2code.springmvc.models.ScienceGrade;
import com.luv2code.springmvc.models.StudentGrades;
import com.luv2code.springmvc.repository.HistoryGradesDao;
import com.luv2code.springmvc.repository.MathGradesDao;
import com.luv2code.springmvc.repository.ScienceGradesDao;
import com.luv2code.springmvc.repository.StudentDao;

@Service
@Transactional
public class StudentAndGradeService {

  @Autowired
  private StudentDao studentDao;

  @Autowired
  @Qualifier("mathGrades")
  private MathGrade mathGrade;

  @Autowired
  @Qualifier("scienceGrades")
  private ScienceGrade scienceGrade;

  @Autowired
  @Qualifier("historyGrades")
  private HistoryGrade historyGrade;

  @Autowired
  private MathGradesDao mathGradeDao;
  @Autowired
  private ScienceGradesDao scienceGradeDao;
  @Autowired
  private HistoryGradesDao historyGradeDao;

  @Autowired
  StudentGrades studentGrades;

  public void createStudent(String firstname, String lastname, String emailAddress) {

    CollegeStudent student = new CollegeStudent(firstname, lastname, emailAddress);
    student.setId(0);
    studentDao.save(student);
  }

  public boolean checkIfStudentIsNull(int id) {

    Optional<CollegeStudent> student = studentDao.findById(id);
    if (student.isPresent()) {
      return true;
    }
    return false;
  }

  public void deleteStudentService(int id) {

    if (checkIfStudentIsNull(id)) {
      studentDao.deleteById(id);
      mathGradeDao.deleteByStudentId(id);
      scienceGradeDao.deleteByStudentId(id);
      historyGradeDao.deleteByStudentId(id);
    }
  }

  public Iterable<CollegeStudent> getGradebook() {

    Iterable<CollegeStudent> collegeStudents = studentDao.findAll();
    return collegeStudents;
  }

  public boolean createGrade(double grade, int studentId, String gradeType) {

    if (!checkIfStudentIsNull(studentId)) {
      return false;
    }

    if (grade >= 0 && grade <= 100) {
      if (gradeType.equals("math")) {
        mathGrade.setId(0);
        mathGrade.setGrade(grade);
        mathGrade.setStudentId(studentId);
        mathGradeDao.save(mathGrade);
        return true;
      } else if (gradeType.equals("science")) {
        scienceGrade.setId(0);
        scienceGrade.setGrade(grade);
        scienceGrade.setStudentId(studentId);
        scienceGradeDao.save(scienceGrade);
        return true;
      } else if (gradeType.equals("history")) {
        historyGrade.setId(0);
        historyGrade.setGrade(grade);
        historyGrade.setStudentId(studentId);
        historyGradeDao.save(historyGrade);
        return true;
      }
    }
    return false;
  }

  public Integer deleteGrade(int id, String gradeType) {

    int studentId = 0;

    if (gradeType.equals("math")) {
      Optional<MathGrade> grade = mathGradeDao.findById(id);
      if (!grade.isPresent()) {
        return studentId;
      }
      studentId = grade.get().getStudentId();
      mathGradeDao.deleteById(id);
    } else if (gradeType.equals("science")) {
      Optional<ScienceGrade> grade = scienceGradeDao.findById(id);
      if (!grade.isPresent()) {
        return studentId;
      }
      studentId = grade.get().getStudentId();
      scienceGradeDao.deleteById(id);
    } else if (gradeType.equals("history")) {
      Optional<HistoryGrade> grade = historyGradeDao.findById(id);
      if (!grade.isPresent()) {
        return studentId;
      }
      studentId = grade.get().getStudentId();
      historyGradeDao.deleteById(id);
    }

    return studentId;
  }

  public GradebookCollegeStudent studentInformation(int id) {

    if (!checkIfStudentIsNull(id)) {
      return null;
    }

    Optional<CollegeStudent> student = studentDao.findById(id);

    Iterable<MathGrade> mathGrades = mathGradeDao.findGradeByStudentId(id);
    Iterable<ScienceGrade> scienceGrades = scienceGradeDao.findGradeByStudentId(id);
    Iterable<HistoryGrade> historyGrades = historyGradeDao.findGradeByStudentId(id);

    List<Grade> mathGradeList = new ArrayList<>();
    mathGrades.forEach(mathGradeList::add);

    List<Grade> scienceGradeList = new ArrayList<>();
    scienceGrades.forEach(scienceGradeList::add);

    List<Grade> historyGradeList = new ArrayList<>();
    historyGrades.forEach(historyGradeList::add);

    studentGrades.setMathGradeResults(mathGradeList);
    studentGrades.setScienceGradeResults(scienceGradeList);
    studentGrades.setHistoryGradeResults(historyGradeList);

    GradebookCollegeStudent gradebookCollegeStudent = new GradebookCollegeStudent(
        student.get().getId(), student.get().getFirstname(), student.get().getLastname(),
        student.get().getEmailAddress(), studentGrades);

    return gradebookCollegeStudent;
  }
}
