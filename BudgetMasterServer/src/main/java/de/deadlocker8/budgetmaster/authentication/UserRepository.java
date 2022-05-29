package de.deadlocker8.budgetmaster.authentication;

import org.springframework.data.jpa.repository.JpaRepository;


public interface UserRepository extends JpaRepository<User, Integer>
{
	User findByName(String name);
}