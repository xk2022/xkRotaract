package com.xk.demo.dao.repository;

import com.xk.demo.model.po.DemoExample;
import org.springframework.data.jpa.repository.JpaRepository;

import java.io.Serializable;

public interface DemoExampleRepository extends JpaRepository<DemoExample, Long>, Serializable {

}