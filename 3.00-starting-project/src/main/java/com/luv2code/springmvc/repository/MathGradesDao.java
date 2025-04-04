package com.luv2code.springmvc.repository;

import org.springframework.data.repository.CrudRepository;

import com.luv2code.springmvc.models.MathGrade;

public interface MathGradesDao extends CrudRepository<MathGrade, Integer> {

  public Iterable<MathGrade> findGradeByStudentId(int id);
  public void deleteByStudentId(int id);
}
