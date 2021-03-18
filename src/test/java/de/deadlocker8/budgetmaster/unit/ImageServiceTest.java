package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.images.InvalidFileExtensionException;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.verification.VerificationMode;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@RunWith(SpringJUnit4ClassRunner.class)
@LocalizedTest
public class ImageServiceTest
{
	@Mock
	private ImageRepository imageRepository;

	@InjectMocks
	private ImageService imageService;

	@Test
	public void test_getFileExtension_valid()
	{
		assertThat(ImageService.getFileExtension("abc.png")).isNotEmpty()
				.get().isEqualTo("png");
	}

	@Test
	public void test_getFileExtension_validUppercase()
	{
		assertThat(ImageService.getFileExtension("abc.PNG")).isNotEmpty()
				.get().isEqualTo("png");
	}

	@Test
	public void test_getFileExtension_validMultipleDots()
	{
		assertThat(ImageService.getFileExtension("abc.jpeg.png")).isNotEmpty()
				.get().isEqualTo("png");
	}

	@Test
	public void test_getFileExtension_noDot()
	{
		assertThat(ImageService.getFileExtension("abc")).isEmpty();
	}

	@Test
	public void test_saveImageFile_noFileExtension()
	{
		final MultipartFile multipartFile = new MockMultipartFile("abc", "abc", "text/plain", new byte[0]);

		assertThatThrownBy(() -> imageService.saveImageFile(multipartFile))
				.isInstanceOf(IllegalArgumentException.class);
	}

	@Test
	public void test_saveImageFile_invalidFileExtension()
	{
		final MultipartFile multipartFile = new MockMultipartFile("abc.pdf", "abc.pdf", "text/plain", new byte[0]);

		assertThatThrownBy(() -> imageService.saveImageFile(multipartFile))
				.isInstanceOf(InvalidFileExtensionException.class);
	}

	@Test
	public void test_saveImageFile_valid() throws IOException, InvalidFileExtensionException
	{
		final MultipartFile multipartFile = new MockMultipartFile("abc.png", "abc.png", "text/plain", new byte[0]);

		imageService.saveImageFile(multipartFile);

		final Image expectedImage = new Image(new Byte[0], "abc.png", "png");
		Mockito.verify(imageRepository, Mockito.atLeast(1)).save(expectedImage);
	}

}
