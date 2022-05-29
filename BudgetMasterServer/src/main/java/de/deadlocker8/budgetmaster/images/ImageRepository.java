package de.deadlocker8.budgetmaster.images;

import org.springframework.data.jpa.repository.JpaRepository;


public interface ImageRepository extends JpaRepository<Image, Integer>
{
	Image findByImage(Byte[] image);
}