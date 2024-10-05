package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.*;
import de.deadlocker8.budgetmaster.authentication.UserRepository;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.templates.TemplateService;
import de.deadlocker8.budgetmaster.transactions.TransactionService;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@LocalizedTest
class AccountServiceTest
{
	@Mock
	private AccountRepository accountRepository;

	@Mock
	private TransactionService transactionService;

	@Mock
	private TemplateService templateService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private IconService iconService;

	@Mock
	private IconRepository iconRepository;

	private AccountService accountService;

	private Account ACCOUNT_DEFAULT;
	private Account ACCOUNT_PLACEHOLDER;
	private Account ACCOUNT_NORMAL;
	private Account ACCOUNT_READONLY;
	private Account ACCOUNT_HIDDEN;

	private Icon ICON_PLACEHOLDER;

	@BeforeEach
	void beforeEach()
	{
		ACCOUNT_DEFAULT = new Account(Localization.getString(Strings.ACCOUNT_DEFAULT_NAME), "", AccountType.CUSTOM, null);

		ACCOUNT_PLACEHOLDER = new Account("Placeholder", "", AccountType.ALL, null);
		ACCOUNT_PLACEHOLDER.setID(1);

		ACCOUNT_NORMAL = new Account("Normal account", "awesome description", AccountType.CUSTOM, null);
		ACCOUNT_NORMAL.setID(3);

		ACCOUNT_READONLY = new Account("Readonly account", "", AccountType.CUSTOM, null);
		ACCOUNT_READONLY.setAccountState(AccountState.READ_ONLY);

		ACCOUNT_HIDDEN = new Account("Hidden account", "", AccountType.CUSTOM, null);
		ACCOUNT_HIDDEN.setAccountState(AccountState.HIDDEN);

		ACCOUNT_DEFAULT.setID(15);
		ACCOUNT_DEFAULT.setDefault(true);

		ICON_PLACEHOLDER = new Icon("fas fa-landmark", null);

		Mockito.when(accountRepository.save(new Account(Localization.getString(Strings.ACCOUNT_DEFAULT_NAME), "", AccountType.CUSTOM, null))).thenReturn(ACCOUNT_DEFAULT);
		Mockito.when(accountRepository.findByIsDefault(true)).thenReturn(ACCOUNT_DEFAULT);
		Mockito.when(accountRepository.findAllByType(AccountType.ALL)).thenReturn(List.of(ACCOUNT_PLACEHOLDER));
		Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(ACCOUNT_PLACEHOLDER));
		Mockito.when(accountRepository.findById(3)).thenReturn(Optional.of(ACCOUNT_NORMAL));

		Mockito.when(iconService.getRepository()).thenReturn(iconRepository);
		Mockito.when(iconRepository.save(new Icon("fas fa-landmark", null))).thenReturn(ICON_PLACEHOLDER);

		accountService = new AccountService(accountRepository, transactionService, templateService, userRepository, iconService);
	}

	@Test
	void test_getAllEntitiesAsc()
	{
		final List<Account> accounts = new ArrayList<>();
		accounts.add(ACCOUNT_NORMAL);
		accounts.add(ACCOUNT_READONLY);
		accounts.add(ACCOUNT_HIDDEN);

		final Account accountNormal2 = new Account("normal account", "", AccountType.CUSTOM, null);
		accounts.add(accountNormal2);
		final Account accountNormal3 = new Account("123 account", "", AccountType.CUSTOM, null);
		accounts.add(accountNormal3);

		Mockito.when(accountRepository.findAllByType(AccountType.ALL)).thenReturn(List.of(ACCOUNT_PLACEHOLDER));
		Mockito.when(accountRepository.findAllByTypeOrderByNameAsc(AccountType.CUSTOM)).thenReturn(accounts);

		assertThat(accountService.getAllEntitiesAsc()).hasSize(6)
				.containsExactly(ACCOUNT_PLACEHOLDER, accountNormal3, ACCOUNT_HIDDEN, ACCOUNT_NORMAL, accountNormal2, ACCOUNT_READONLY);
	}

	@Test
	void test_getAllActivatedAccountsAsc()
	{
		final List<Account> accounts = new ArrayList<>();
		accounts.add(ACCOUNT_NORMAL);

		final Account accountNormal2 = new Account("normal account", "", AccountType.CUSTOM, null);
		accounts.add(accountNormal2);
		final Account accountNormal3 = new Account("123 account", "", AccountType.CUSTOM, null);
		accounts.add(accountNormal3);

		Mockito.when(accountRepository.findAllByType(AccountType.ALL)).thenReturn(List.of(ACCOUNT_PLACEHOLDER));
		Mockito.when(accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.FULL_ACCESS)).thenReturn(accounts);

		assertThat(accountService.getAllActivatedAccountsAsc()).hasSize(4)
				.containsExactly(ACCOUNT_PLACEHOLDER, accountNormal3, ACCOUNT_NORMAL, accountNormal2);
	}

	@Test
	void test_getAllReadableAccounts()
	{
		final List<Account> accounts = new ArrayList<>();
		accounts.add(ACCOUNT_NORMAL);

		final Account accountNormal2 = new Account("normal account", "", AccountType.CUSTOM, null);
		accounts.add(accountNormal2);
		final Account accountNormal3 = new Account("123 account", "", AccountType.CUSTOM, null);
		accounts.add(accountNormal3);

		Mockito.when(accountRepository.findAllByType(AccountType.ALL)).thenReturn(List.of(ACCOUNT_PLACEHOLDER));
		Mockito.when(accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.FULL_ACCESS)).thenReturn(accounts);
		Mockito.when(accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.READ_ONLY)).thenReturn(List.of(ACCOUNT_READONLY));

		assertThat(accountService.getAllReadableAccounts()).hasSize(5)
				.containsExactly(ACCOUNT_PLACEHOLDER, accountNormal3, ACCOUNT_NORMAL, accountNormal2, ACCOUNT_READONLY);
	}

	@Test
	void test_deleteAccount()
	{
		Mockito.when(accountRepository.findById(1)).thenReturn(Optional.of(ACCOUNT_PLACEHOLDER));
		Mockito.when(accountRepository.findById(15)).thenReturn(Optional.of(ACCOUNT_DEFAULT));
		Mockito.when(accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.FULL_ACCESS)).thenReturn(new ArrayList<>(List.of(ACCOUNT_NORMAL, ACCOUNT_DEFAULT)));

		accountService.deleteAccount(15);

		Mockito.verify(transactionService, Mockito.atLeast(1)).deleteTransactionsWithAccount(Mockito.any());
		Mockito.verify(templateService, Mockito.atLeast(1)).unsetTemplatesWithAccount(Mockito.any());

		// placeholder account is set as selected account
		final Account accountSelected = new Account("Placeholder", "", AccountType.ALL, null);
		accountSelected.setSelected(true);
		accountSelected.setID(1);
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(accountSelected);

		// default account is set another account
		final Account newDefault = new Account("Normal account", "awesome description", AccountType.CUSTOM, null);
		newDefault.setDefault(true);
		newDefault.setID(3);
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(newDefault);
	}

	@Test
	void test_createDefaults()
	{
		accountService.createDefaults();

		// createDefaults() may also be called in constructor so 2 calls are possible
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(new Account("Placeholder", "", AccountType.ALL, null));
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(new Account(Localization.getString(Strings.ACCOUNT_DEFAULT_NAME), "", AccountType.CUSTOM, null));
	}

	@Test
	void test_selectAccount()
	{
		accountService.selectAccount(3);

		final Account accountSelected = new Account(ACCOUNT_NORMAL.getName(), "awesome description", AccountType.CUSTOM, null);
		accountSelected.setSelected(true);
		accountSelected.setID(ACCOUNT_NORMAL.getID());
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(accountSelected);
	}

	@Test
	void test_setAsDefaultAccount()
	{
		Mockito.when(accountRepository.findAll()).thenReturn(List.of(ACCOUNT_NORMAL, ACCOUNT_DEFAULT));

		accountService.setAsDefaultAccount(3);

		// old default unset
		final Account oldDefault = new Account(ACCOUNT_DEFAULT.getName(), "", AccountType.CUSTOM, null);
		oldDefault.setDefault(false);
		oldDefault.setID(ACCOUNT_DEFAULT.getID());
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(oldDefault);

		// new default set
		final Account newDefault = new Account(ACCOUNT_NORMAL.getName(), "awesome description", AccountType.CUSTOM, null);
		newDefault.setDefault(true);
		newDefault.setID(ACCOUNT_NORMAL.getID());
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(newDefault);
	}

	@Test
	void test_updateExistingAccount()
	{
		final Account account = new Account("my account", "awesome new description", AccountType.CUSTOM, null);
		account.setAccountState(AccountState.FULL_ACCESS);
		account.setID(22);

		final Account existingAccount = new Account("existing account", "awesome description", AccountType.CUSTOM, null);
		existingAccount.setAccountState(AccountState.READ_ONLY);
		existingAccount.setIconReference(new Icon());
		existingAccount.setID(22);

		Mockito.when(accountRepository.findById(22)).thenReturn(Optional.of(existingAccount));

		accountService.updateExistingAccount(account);

		final Account expected = new Account("my account", "awesome new description", AccountType.CUSTOM, null);
		expected.setAccountState(AccountState.FULL_ACCESS);
		expected.setID(22);

		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(expected);
	}

	@Test
	void test_updateExistingAccount_isDefaultAccount_noFullAccessAnymore()
	{
		final Account account = new Account("my account", "", AccountType.CUSTOM, null);
		account.setAccountState(AccountState.READ_ONLY);
		account.setDefault(true);
		account.setID(22);

		final Account existingAccount = new Account("existing account", "", AccountType.CUSTOM, null);
		existingAccount.setAccountState(AccountState.FULL_ACCESS);
		existingAccount.setIconReference(new Icon());
		existingAccount.setDefault(true);
		existingAccount.setID(22);

		Mockito.when(accountRepository.findById(22)).thenReturn(Optional.of(existingAccount));
		Mockito.when(accountRepository.findAllByTypeAndAccountStateOrderByNameAsc(AccountType.CUSTOM, AccountState.FULL_ACCESS)).thenReturn(new ArrayList<>(List.of(ACCOUNT_NORMAL)));

		accountService.updateExistingAccount(account);

		// account updated
		final Account expected = new Account("my account", "", AccountType.CUSTOM, null);
		expected.setAccountState(AccountState.READ_ONLY);
		expected.setDefault(true);
		expected.setID(22);
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(expected);

		// new default set
		final Account newDefault = new Account(ACCOUNT_NORMAL.getName(), "awesome description", AccountType.CUSTOM, null);
		newDefault.setDefault(true);
		newDefault.setID(ACCOUNT_NORMAL.getID());
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(newDefault);
	}

	@Test
	void test_updateExistingAccount_isSelected_nowHidden()
	{
		final Account account = new Account("my account", "", AccountType.CUSTOM, null);
		account.setAccountState(AccountState.HIDDEN);
		account.setSelected(true);
		account.setID(22);

		final Account existingAccount = new Account("existing account", "", AccountType.CUSTOM, null);
		existingAccount.setAccountState(AccountState.FULL_ACCESS);
		existingAccount.setIconReference(new Icon());
		existingAccount.setSelected(true);
		existingAccount.setID(22);

		Mockito.when(accountRepository.findById(22)).thenReturn(Optional.of(existingAccount));

		accountService.updateExistingAccount(account);

		// account updated
		final Account expected = new Account("my account", "", AccountType.CUSTOM, null);
		expected.setAccountState(AccountState.HIDDEN);
		expected.setSelected(true);
		expected.setID(22);
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(expected);

		// placeholder set as selected account
		final Account accountSelected = new Account("Placeholder", "", AccountType.ALL, null);
		accountSelected.setSelected(true);
		accountSelected.setID(1);
		Mockito.verify(accountRepository, Mockito.atLeast(1)).save(accountSelected);
	}

	@Test
	void test_getSelectedAccountOrDefaultAsFallback()
	{
		Mockito.when(accountRepository.findByIsSelected(true)).thenReturn(ACCOUNT_NORMAL);
		Mockito.when(accountRepository.findByIsDefault(true)).thenReturn(ACCOUNT_DEFAULT);

		assertThat(accountService.getSelectedAccountOrDefaultAsFallback()).isEqualTo(ACCOUNT_NORMAL);
	}

	@Test
	void test_getSelectedAccountOrDefaultAsFallback_fallback()
	{
		Mockito.when(accountRepository.findByIsSelected(true)).thenReturn(null);
		Mockito.when(accountRepository.findByIsDefault(true)).thenReturn(ACCOUNT_DEFAULT);

		assertThat(accountService.getSelectedAccountOrDefaultAsFallback()).isEqualTo(ACCOUNT_DEFAULT);
	}

	@Test
	void test_getSelectedAccountOrDefaultAsFallback_fallbackIfPlaceholderAccountIsSelected()
	{
		Mockito.when(accountRepository.findByIsSelected(true)).thenReturn(ACCOUNT_PLACEHOLDER);
		Mockito.when(accountRepository.findByIsDefault(true)).thenReturn(ACCOUNT_DEFAULT);

		assertThat(accountService.getSelectedAccountOrDefaultAsFallback()).isEqualTo(ACCOUNT_DEFAULT);
	}
}
