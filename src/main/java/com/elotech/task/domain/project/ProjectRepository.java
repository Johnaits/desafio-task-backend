package com.elotech.task.domain.project;

import com.elotech.task.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT p FROM Project p WHERE p.owner = :user OR :user MEMBER OF p.members")
    Page<Project> findAllProjectsByOwnerOrByMember(User user, Pageable pageable);

    @Query("SELECT p FROM Project p WHERE p.id = :id AND (p.owner = :user OR :user MEMBER OF p.members)")
    Optional<Project> findByIdAndOwnerOrByMember(Long id, User user);

    Optional<Project> findByIdAndOwner(Long id, User owner);

}
