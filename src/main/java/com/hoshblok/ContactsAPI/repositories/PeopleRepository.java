package com.hoshblok.ContactsAPI.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hoshblok.ContactsAPI.models.Person;

@Repository
public interface PeopleRepository extends JpaRepository<Person, Integer> {
	
	Optional<Person> findByEmail(String email);
	
	Optional<Person> findByPhoneNumber(String phoneNumber);
}