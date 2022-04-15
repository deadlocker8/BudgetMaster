package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.database.importer.IconImporter;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class IconImporterTest
{
	@Autowired
	private ImageRepository imageRepository;

	@Autowired
	private IconRepository iconRepository;

	@Test
	void test_importIcons()
	{
		Image image1 = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
		image1 = imageRepository.save(image1);

		final Icon icon1 = new Icon(image1);
		icon1.setID(3);

		final Icon icon2 = new Icon("fas fa-icons");
		icon2.setID(4);

		final IconImporter importer = new IconImporter(iconRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(icon1, icon2));

		final ImportResultItem expected = new ImportResultItem(EntityType.ICON, 2, 2, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(icon1).hasFieldOrPropertyWithValue("ID", 1);
		assertThat(icon2).hasFieldOrPropertyWithValue("ID", 2);
	}

	@Test
	void test_importIcons_alreadyExisting()
	{
		final Icon icon = new Icon("fas fa-icons");
		icon.setID(1);
		iconRepository.save(icon);

		final IconImporter importer = new IconImporter(iconRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(icon));

		final ImportResultItem expected = new ImportResultItem(EntityType.ICON, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(icon).hasFieldOrPropertyWithValue("ID", 2);
	}
}