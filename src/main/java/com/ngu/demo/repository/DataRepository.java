package com.ngu.demo.repository;

import com.ngu.demo.model.entity.Datanya;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DataRepository extends JpaRepository<Datanya, Integer> {

}
