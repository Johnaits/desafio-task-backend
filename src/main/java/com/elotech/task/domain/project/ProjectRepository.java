package com.elotech.task.domain.project;

import com.elotech.task.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    Page<Project> findAllByOwner(User owner, Pageable pageable);

    Optional<Project> findByIdAndOwner(Long id, User owner);
}
