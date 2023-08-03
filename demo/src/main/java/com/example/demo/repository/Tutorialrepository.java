package com.example.demo.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.demo.model.Tutorial;

public interface Tutorialrepository extends JpaRepository<Tutorial,Long >{
    
}
