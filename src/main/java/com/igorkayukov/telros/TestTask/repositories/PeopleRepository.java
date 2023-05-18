package com.igorkayukov.telros.TestTask.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.igorkayukov.telros.TestTask.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
	
	Optional<Person> findByEmail(String email);
	
	Optional<Person> findByPhoneNumber(String phoneNumber);
}