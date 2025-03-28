package com.srnr.vidhatasocietymlm.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.srnr.vidhatasocietymlm.model.Level;

@Repository
public interface LevelRepository extends JpaRepository<Level, String>
{
	Optional<Level> findByLevelNum(Integer levelNum);

}
