package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.images.*;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@LocalizedTest
class ImageServiceTest
{
	@Mock
	private ImageRepository imageRepository;

	@InjectMocks
	private ImageService imageService;

	@Test
	void test_getFileExtension_valid()
	{
		assertThat(ImageService.getFileExtension("abc.png")).isNotEmpty()
				.get().isEqualTo("png");
	}

	@Test
	void test_getFileExtension_validUppercase()
	{
		assertThat(ImageService.getFileExtension("abc.PNG")).isNotEmpty()
				.get().isEqualTo("png");
	}

	@Test
	void test_getFileExtension_validMultipleDots()
	{
		assertThat(ImageService.getFileExtension("abc.jpeg.png")).isNotEmpty()
				.get().isEqualTo("png");
	}

	@Test
	void test_getFileExtension_noDot()
	{
		assertThat(ImageService.getFileExtension("abc")).isEmpty();
	}

	@Test
	void test_saveImageFile_noFileExtension()
	{
		final MultipartFile multipartFile = new MockMultipartFile("abc", "abc", "text/plain", new byte[0]);

		assertThatThrownBy(() -> imageService.saveImageFile(multipartFile))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	void test_saveImageFile_invalidFileExtension()
	{
		final MultipartFile multipartFile = new MockMultipartFile("abc.pdf", "abc.pdf", "text/plain", new byte[0]);

		assertThatThrownBy(() -> imageService.saveImageFile(multipartFile))
				.isInstanceOf(InvalidFileExtensionException.class);
	}

	@Test
	void test_saveImageFile_valid() throws IOException, InvalidFileExtensionException
	{
		final MultipartFile multipartFile = new MockMultipartFile("abc.png", "abc.png", "text/plain", new byte[0]);

		imageService.saveImageFile(multipartFile);

		final Image expectedImage = new Image(new Byte[0], "abc.png", ImageFileExtension.PNG);
		Mockito.verify(imageRepository, Mockito.atLeast(1)).save(expectedImage);
	}

}
