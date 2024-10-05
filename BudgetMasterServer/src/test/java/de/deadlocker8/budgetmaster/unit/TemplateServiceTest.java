package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.accounts.*;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.categories.CategoryService;
import de.deadlocker8.budgetmaster.categories.CategoryType;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.templates.TemplateService;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@LocalizedTest
class TemplateServiceTest
{
	private static final Category CATEGORY_NONE = new Category("No category", "#FFFFFF", CategoryType.NONE);

	private static final Account ACCOUNT_SELECTED = new Account("Selected Account", "", AccountType.CUSTOM, null);

	@Mock
	private TemplateRepository templateRepository;

	@Mock
	private AccountService accountService;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private CategoryService categoryService;

	@Mock
	private ImageService imageService;

	@Mock
	private IconService iconService;

	@InjectMocks
	private TemplateService templateService;

	@Test
	void test_prepareTemplateForNewTransaction_noCategory()
	{
		final Template template = new Template();

		Mockito.when(categoryService.findByType(CategoryType.NONE)).thenReturn(CATEGORY_NONE);

		final Template expectedTemplate = new Template();
		expectedTemplate.setCategory(CATEGORY_NONE);

		templateService.prepareTemplateForNewTransaction(template, false, new Account("my account", "", AccountType.CUSTOM, null));
		assertThat(template).isEqualTo(expectedTemplate);
	}

	@Test
	void test_prepareTemplateForNewTransaction_accountIsFullAccess_noPreparation()
	{
		final Account account = new Account("Account", "", AccountType.CUSTOM, null);
		account.setAccountState(AccountState.FULL_ACCESS);

		final Template template = new Template();
		template.setCategory(CATEGORY_NONE);
		template.setAccount(account);

		final Template expectedTemplate = new Template();
		expectedTemplate.setCategory(CATEGORY_NONE);
		expectedTemplate.setAccount(account);

		templateService.prepareTemplateForNewTransaction(template, false, account);
		assertThat(template).isEqualTo(expectedTemplate);
	}

	@Test
	void test_prepareTemplateForNewTransaction_accountIsNotFullAccess_noPreparation()
	{
		final Account account = new Account("Account", "", AccountType.CUSTOM, null);
		account.setAccountState(AccountState.READ_ONLY);

		final Template template = new Template();
		template.setCategory(CATEGORY_NONE);
		template.setAccount(account);

		Mockito.when(accountService.getRepository()).thenReturn(accountRepository);
		Mockito.when(accountService.getSelectedAccountOrDefaultAsFallback()).thenReturn(ACCOUNT_SELECTED);

		final Template expectedTemplate = new Template();
		expectedTemplate.setCategory(CATEGORY_NONE);
		expectedTemplate.setAccount(ACCOUNT_SELECTED);

		templateService.prepareTemplateForNewTransaction(template, false, ACCOUNT_SELECTED);
		assertThat(template).isEqualTo(expectedTemplate);
	}

	@Test
	void test_prepareTemplateForNewTransaction_transferAccountIsFullAccess_noPreparation()
	{
		final Account account = new Account("Account", "", AccountType.CUSTOM, null);
		account.setAccountState(AccountState.FULL_ACCESS);

		final Account transferAccount = new Account("Transfer Account", "", AccountType.CUSTOM, null);
		transferAccount.setAccountState(AccountState.FULL_ACCESS);

		final Template template = new Template();
		template.setCategory(CATEGORY_NONE);
		template.setAccount(account);
		template.setTransferAccount(transferAccount);

		final Template expectedTemplate = new Template();
		expectedTemplate.setCategory(CATEGORY_NONE);
		expectedTemplate.setAccount(account);
		expectedTemplate.setTransferAccount(transferAccount);

		templateService.prepareTemplateForNewTransaction(template, false, account);
		assertThat(template).isEqualTo(expectedTemplate);
	}

	@Test
	void test_prepareTemplateForNewTransaction_transferAccountIsNotFullAccess_noPreparation()
	{
		final Account account = new Account("Account", "", AccountType.CUSTOM, null);
		account.setAccountState(AccountState.FULL_ACCESS);

		final Account transferAccount = new Account("Transfer Account", "", AccountType.CUSTOM, null);
		transferAccount.setAccountState(AccountState.READ_ONLY);

		final Template template = new Template();
		template.setCategory(CATEGORY_NONE);
		template.setAccount(account);
		template.setTransferAccount(transferAccount);

		Mockito.when(accountService.getRepository()).thenReturn(accountRepository);
		Mockito.when(accountService.getSelectedAccountOrDefaultAsFallback()).thenReturn(ACCOUNT_SELECTED);

		final Template expectedTemplate = new Template();
		expectedTemplate.setCategory(CATEGORY_NONE);
		expectedTemplate.setAccount(account);
		expectedTemplate.setTransferAccount(ACCOUNT_SELECTED);

		templateService.prepareTemplateForNewTransaction(template, false, ACCOUNT_SELECTED);
		assertThat(template).isEqualTo(expectedTemplate);
	}

	@Test
	void test_prepareTemplateForNewTransaction_noAccount_withPreparation()
	{
		final Template template = new Template();
		template.setCategory(CATEGORY_NONE);

		Mockito.when(accountService.getRepository()).thenReturn(accountRepository);
		Mockito.when(accountService.getSelectedAccountOrDefaultAsFallback()).thenReturn(ACCOUNT_SELECTED);

		final Template expectedTemplate = new Template();
		expectedTemplate.setCategory(CATEGORY_NONE);
		expectedTemplate.setAccount(ACCOUNT_SELECTED);

		templateService.prepareTemplateForNewTransaction(template, true, ACCOUNT_SELECTED);
		assertThat(template).isEqualTo(expectedTemplate);
	}
}
