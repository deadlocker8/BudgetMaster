package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@LocalizedTest
class IconServiceTest
{
	@Mock
	private IconRepository iconRepository;

	@Mock
	private ImageRepository imageRepository;

	@InjectMocks
	private IconService iconService;

	@Test
	void test_deleteIcon_null()
	{
		iconService.deleteIcon(null);
		Mockito.verify(iconRepository, Mockito.never()).delete(Mockito.any());
	}

	@Test
	void test_deleteIcon_withReferringAccount()
	{
		final Icon icon = Mockito.spy(new Icon("fas fa-icons"));
		final Account account = Mockito.spy(new Account("account with icon", AccountType.CUSTOM, icon));

		Mockito.when(icon.getReferringAccount()).thenReturn(account);

		iconService.deleteIcon(icon);
		Mockito.verify(iconRepository, Mockito.times(1)).delete(icon);
		Mockito.verify(account, Mockito.times(1)).setIconReference(null);
	}

	@Test
	void test_deleteIcon_withReferringTemplate()
	{
		final Icon icon = Mockito.spy(new Icon("fas fa-icons"));

		final Template template = new Template();
		template.setTemplateName("template with icon");
		template.setIconReference(icon);

		final Template templateSpy = Mockito.spy(template);

		Mockito.when(icon.getReferringTemplate()).thenReturn(templateSpy);

		iconService.deleteIcon(icon);
		Mockito.verify(iconRepository, Mockito.times(1)).delete(icon);
		Mockito.verify(templateSpy, Mockito.times(1)).setIconReference(null);
	}

	@Test
	void test_deleteIcon_withReferringCategory()
	{
		final Icon icon = Mockito.spy(new Icon("fas fa-icons"));

		final Category category = Mockito.spy(new Category("category with icon", "#FFFFFF", CategoryType.CUSTOM, icon));

		Mockito.when(icon.getReferringCategory()).thenReturn(category);

		iconService.deleteIcon(icon);
		Mockito.verify(iconRepository, Mockito.times(1)).delete(icon);
		Mockito.verify(category, Mockito.times(1)).setIconReference(null);
	}

	@Test
	void test_createIconReference_nothingSet()
	{
		assertThat(iconService.createIconReference(null, null, null))
				.isEqualTo(new Icon(null, null));
	}

	@Test
	void test_createIconReference_builtinIcon()
	{
		final String builtinIdentifier = "fas fa-icons";
		assertThat(iconService.createIconReference(null, builtinIdentifier, null))
				.isEqualTo(new Icon(builtinIdentifier, null));
	}

	@Test
	void test_createIconReference_builtinIcon_withFontColor()
	{
		final String builtinIdentifier = "fas fa-icons";
		final String fontColor = "#FF0000";
		assertThat(iconService.createIconReference(null, builtinIdentifier, fontColor))
				.isEqualTo(new Icon(builtinIdentifier, fontColor));
	}

	@Test
	void test_createIconReference_imageIcon()
	{
		final Image image = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
		image.setID(12);

		Mockito.when(imageRepository.findById(Mockito.any())).thenReturn(Optional.of(image));

		assertThat(iconService.createIconReference(12, null, null))
				.isEqualTo(new Icon(image));
	}
}
