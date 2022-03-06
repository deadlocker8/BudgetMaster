package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.utils.ProvidesID;

import java.util.Optional;

public interface AccessEntityByID<T extends ProvidesID>
{
	Optional<T> findById(Integer ID);
}
