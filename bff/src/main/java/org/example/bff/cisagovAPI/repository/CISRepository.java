package org.example.bff.cisagovAPI.repository;

import org.example.bff.cisagovAPI.model.CISData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CISRepository extends JpaRepository<CISData, String> {

    List<CISData> findFirstById(Long id);
}
