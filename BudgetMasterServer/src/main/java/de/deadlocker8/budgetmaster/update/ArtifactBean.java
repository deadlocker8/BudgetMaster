package de.deadlocker8.budgetmaster.update;

import de.deadlocker8.budgetmaster.Build;
import de.thecodelabs.versionizer.config.Artifact;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
public class ArtifactBean
{
	@Bean
	public Artifact artifact()
	{
		Artifact newArtifact = new Artifact();
		newArtifact.setVersion(Build.getInstance().getVersionName());
		newArtifact.setGroupId("de.deadlocker8");
		newArtifact.setArtifactId("BudgetMaster");
		newArtifact.setArtifactType(Artifact.ArtifactType.RUNTIME);
		return newArtifact;
	}
}
