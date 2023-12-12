package com.memomo.repository;

import com.memomo.entity.PostFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostFileRepository extends JpaRepository<PostFile, Long> {
    public PostFile findByPnoAndFstatus(Long pno, String fstatus);
}
