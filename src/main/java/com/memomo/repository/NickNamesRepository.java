package com.memomo.repository;

import com.memomo.entity.NickNames;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NickNamesRepository extends JpaRepository<NickNames, String> {
}
