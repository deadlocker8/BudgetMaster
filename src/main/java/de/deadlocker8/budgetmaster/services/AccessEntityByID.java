package de.deadlocker8.budgetmaster.services;

import de.deadlocker8.budgetmaster.icon.Iconizable;

import java.util.Optional;

public interface AccessEntityByID<T extends Iconizable>
{
	Optional<T> findById(Integer ID);
}
