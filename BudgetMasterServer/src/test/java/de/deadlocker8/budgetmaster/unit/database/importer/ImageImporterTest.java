package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.database.importer.ImageImporter;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ImageImporterTest extends ImporterTestBase
{
	@Override
	List<String> getTableNamesToResetSequence()
	{
		return List.of("image");
	}

	@Autowired
	private ImageRepository imageRepository;

	@Test
	void test_importImages()
	{
		final Image image1 = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
		image1.setID(3);

		final Image image2 = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.JPG);
		image2.setID(4);

		final ImageImporter importer = new ImageImporter(imageRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(image1, image2));

		final ImportResultItem expected = new ImportResultItem(EntityType.IMAGE, 2, 2, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(image1).hasFieldOrPropertyWithValue("ID", 1);
		assertThat(image2).hasFieldOrPropertyWithValue("ID", 2);
	}

	@Test
	void test_importImages_alreadyExisting()
	{
		final Image image = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
		imageRepository.save(image);
		image.setID(1);
		imageRepository.save(image);

		final ImageImporter importer = new ImageImporter(imageRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(image));

		final ImportResultItem expected = new ImportResultItem(EntityType.IMAGE, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(image).hasFieldOrPropertyWithValue("ID", 2);
	}
}