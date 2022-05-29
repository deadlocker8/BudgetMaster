package de.deadlocker8.budgetmaster.hints;

import org.springframework.data.jpa.repository.JpaRepository;


public interface HintRepository extends JpaRepository<Hint, Integer>
{
	Hint findByLocalizationKey(String localizationKey);
}