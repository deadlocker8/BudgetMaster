package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountService;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@LocalizedTest
class IconizableTest
{
	@Mock
	private IconRepository iconRepository;

	@Mock
	private AccountService accountService;

	@Mock
	private IconService iconService;

	@Test
	void test_updateIcon_noExistingItem_noNewIcon()
	{
		final Account account = Mockito.spy(new Account("account with icon", AccountType.CUSTOM));

		Mockito.when(accountService.findById(Mockito.any())).thenReturn(Optional.empty());

		account.updateIcon(iconService, null, null, accountService);

		assertThat(account.getIconReference()).isNull();
		Mockito.verify(iconService, Mockito.never()).deleteIcon(Mockito.any());
		Mockito.verify(iconRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	void test_updateIcon_noExistingItem_newBuiltinIcon()
	{
		final Account account = Mockito.spy(new Account("account with icon", AccountType.CUSTOM));

		final String builtinIdentifier = "fas fa-icons";
		final Icon icon = new Icon(builtinIdentifier);

		Mockito.when(accountService.findById(Mockito.any())).thenReturn(Optional.empty());
		Mockito.when(iconService.getRepository()).thenReturn(iconRepository);
		Mockito.when(iconService.createIconReference(null, builtinIdentifier)).thenReturn(Optional.of(icon));

		account.updateIcon(iconService, null, builtinIdentifier, accountService);

		assertThat(account.getIconReference())
				.isEqualTo(icon);
		Mockito.verify(iconService, Mockito.never()).deleteIcon(Mockito.any());
		Mockito.verify(iconRepository, Mockito.times(1)).save(icon);
	}

	@Test
	void test_updateIcon_existingItem_newBuiltinIcon()
	{
		final String builtinIdentifier = "fas fa-icons";
		final Icon icon = new Icon(builtinIdentifier);

		final Account account = new Account("account with icon", AccountType.CUSTOM, icon);
		account.setID(18);
		final Account accountSpy = Mockito.spy(account);

		Mockito.when(accountService.findById(Mockito.any())).thenReturn(Optional.of(accountSpy));
		Mockito.when(iconService.getRepository()).thenReturn(iconRepository);
		Mockito.when(iconService.createIconReference(null, builtinIdentifier)).thenReturn(Optional.of(icon));

		accountSpy.updateIcon(iconService, null, builtinIdentifier, accountService);

		assertThat(accountSpy.getIconReference())
				.isEqualTo(icon);
		Mockito.verify(iconService, Mockito.times(1)).deleteIcon(icon);
		Mockito.verify(iconRepository, Mockito.times(1)).save(icon);
	}

	@Test
	void test_updateIcon_existingItem_noNewIcon()
	{
		final Icon icon = new Icon("fas fa-icons");
		final Account account = new Account("account with icon", AccountType.CUSTOM, icon);
		account.setID(18);
		final Account accountSpy = Mockito.spy(account);

		Mockito.when(accountService.findById(Mockito.any())).thenReturn(Optional.of(accountSpy));
		Mockito.when(iconService.getRepository()).thenReturn(iconRepository);
		Mockito.when(iconService.createIconReference(null, null)).thenReturn(Optional.empty());

		accountSpy.updateIcon(iconService, null, null, accountService);

		assertThat(accountSpy.getIconReference()).isNull();
		Mockito.verify(iconService, Mockito.times(1)).deleteIcon(icon);
		Mockito.verify(iconRepository, Mockito.never()).save(Mockito.any());
	}
}