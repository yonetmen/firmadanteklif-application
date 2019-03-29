package com.firmadanteklif.application.repository;

import com.firmadanteklif.application.entity.HomePage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HomeRepository extends JpaRepository<HomePage, Integer> {
}
