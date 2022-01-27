package de.deadlocker8.budgetmaster.templategroup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;


public interface TemplateGroupRepository extends JpaRepository<TemplateGroup, Integer>, JpaSpecificationExecutor<TemplateGroup>
{
	List<TemplateGroup> findAllByOrderByNameAsc();

	TemplateGroup findFirstByType(TemplateGroupType type);

	List<TemplateGroup> findAllByType(TemplateGroupType type);
}