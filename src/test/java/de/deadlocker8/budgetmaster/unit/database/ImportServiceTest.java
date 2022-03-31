package de.deadlocker8.budgetmaster.unit.database;

import de.deadlocker8.budgetmaster.Main;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.accounts.AccountRepository;
import de.deadlocker8.budgetmaster.accounts.AccountState;
import de.deadlocker8.budgetmaster.accounts.AccountType;
import de.deadlocker8.budgetmaster.categories.CategoryRepository;
import de.deadlocker8.budgetmaster.charts.Chart;
import de.deadlocker8.budgetmaster.charts.ChartRepository;
import de.deadlocker8.budgetmaster.charts.ChartService;
import de.deadlocker8.budgetmaster.charts.ChartType;
import de.deadlocker8.budgetmaster.database.DatabaseParser;
import de.deadlocker8.budgetmaster.database.InternalDatabase;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatch;
import de.deadlocker8.budgetmaster.database.accountmatches.AccountMatchList;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.images.ImageService;
import de.deadlocker8.budgetmaster.repeating.RepeatingTransactionUpdater;
import de.deadlocker8.budgetmaster.services.ImportService;
import de.deadlocker8.budgetmaster.tags.TagRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroup;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupRepository;
import de.deadlocker8.budgetmaster.templategroup.TemplateGroupType;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateRepository;
import de.deadlocker8.budgetmaster.transactions.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = Main.class)
class ImportServiceTest
{
	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	@Autowired
	private TagRepository tagRepository;

	@Autowired
	private TemplateGroupRepository templateGroupRepository;

	@Autowired
	private TemplateRepository templateRepository;

	@Autowired
	private ChartService chartService;

	@Autowired
	private ImageService imageService;

	@Autowired
	private RepeatingTransactionUpdater repeatingTransactionUpdater;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private IconService iconService;

	@Autowired
	private ImportService importService;

	@Test
	void test_importFullDatabase() throws URISyntaxException, IOException
	{
		final Path jsonPath = Paths.get(getClass().getClassLoader().getResource("ImportServiceTest.json").toURI());
		final String fileContent = Files.readString(jsonPath, StandardCharsets.UTF_8);
		final DatabaseParser parser = new DatabaseParser(fileContent);
		final InternalDatabase importedDatabase = parser.parseDatabaseFromJSON();

		// source accounts
		final Account sourceAccount1 = new Account("Source_Account_1", AccountType.CUSTOM);
		sourceAccount1.setID(2);
		final Account sourceAccount2 = new Account("Source_Account_2", AccountType.CUSTOM);
		sourceAccount2.setID(3);

		// destination accounts
		Account destAccount1 = new Account("Destination_Account_1", AccountType.CUSTOM);
		destAccount1.setAccountState(AccountState.FULL_ACCESS);
		destAccount1 = accountRepository.save(destAccount1);
		Account destAccount2 = new Account("Destination_Account_2", AccountType.CUSTOM);
		destAccount2.setAccountState(AccountState.FULL_ACCESS);
		destAccount2 = accountRepository.save(destAccount2);

		// account matches
		final AccountMatch match1 = new AccountMatch(sourceAccount1);
		match1.setAccountDestination(destAccount1);

		final AccountMatch match2 = new AccountMatch(sourceAccount2);
		match2.setAccountDestination(destAccount2);

		final List<AccountMatch> matches = List.of(match1, match2);
		final AccountMatchList accountMatchList = new AccountMatchList(matches);

		// act
		importService.importDatabase(importedDatabase, accountMatchList, true, true, true);
		final InternalDatabase databaseResult = importService.getDatabase();

		final Image image = new Image(new Byte[0], "awesomeIcon.png", ImageFileExtension.PNG);
		image.setID(1);

		assertThat(imageService.getRepository().findAll())
				.hasSize(1)
				.containsExactly(image);


		assertThat(iconService.getRepository().findAll())
				.hasSize(12)
				.containsExactlyInAnyOrder(createIcon(1, "fas fa-landmark", null, null),
						createIcon(3, null, null, null),
						createIcon(4, null, null, null),
						createIcon(6, "fas fa-ban", "#2eb952ff", null),
						createIcon(7, null, null, image),  //
						createIcon(8, null, "#2e79b9ff", null),
						createIcon(11, "fas fa-ambulance", null, null),
						createIcon(12, null, null, image), //
						createIcon(13, "fas fa-battery-three-quarters", "#e34f4fff", null),
						createIcon(15, null, "#212121ff", null),
						createIcon(17, "fas fa-award", "#212121ff", null),
						createIcon(18, null, null, image));


//		new Category("No Category", "#FFFFFF", CategoryType.NONE, )


		// assert
//		assertThat(categoryRepository.findAll())
//				.hasSize(4)
//				.contains(expectedCategory1, expectedCategory2);
//		assertThat(databaseResult.getTransactions())
//				.hasSize(3)
//				.contains(expectedTransaction1, expectedTransaction2, expectedTransaction3);
//		assertThat(databaseResult.getTemplateGroups())
//				.hasSize(1)
//				.contains(expectedTemplateGroup);
//		assertThat(databaseResult.getTemplates())
//				.hasSize(3)
//				.contains(expectedTemplate1, expectedTemplate2, expectedTemplate3);
//		assertThat(databaseResult.getCharts())
//				.hasSize(1)
//				.contains(expectedC);
		assertThat(importService.getCollectedErrorMessages()).isEmpty();
	}

	private Icon createIcon(int ID, String builtinIdentifier, String fontColor, Image image)
	{
		Icon icon;
		if(image == null)
		{
			icon = new Icon(builtinIdentifier, fontColor);
		}
		else
		{
			icon = new Icon(image);
		}

		icon.setID(ID);
		return icon;
	}

	@Test
	void test_skipTemplates()
	{
		Template template = new Template();
		template.setTemplateName("myTemplate");
		template.setAmount(200);
		template.setName("Test");
		template.setTags(new ArrayList<>());

		// database
		InternalDatabase database = new InternalDatabase(List.of(), List.of(), List.of(), List.of(), List.of(template), List.of(), List.of(), List.of());

		// act
		importService.importDatabase(database, new AccountMatchList(List.of()), true, false, true);

		// assert
		Mockito.verify(templateRepository, Mockito.never()).save(Mockito.any());
	}

	@Test
	void test_skipTemplateGroups()
	{
		TemplateGroup templateGroup = new TemplateGroup(1, "My Template Group", TemplateGroupType.CUSTOM);

		Template templateWithGroup = new Template();
		templateWithGroup.setTemplateName("myTemplate");
		templateWithGroup.setTags(new ArrayList<>());
		templateWithGroup.setTemplateGroup(templateGroup);

		TemplateGroup templateGroupDefault = new TemplateGroup(1, "Default", TemplateGroupType.DEFAULT);
		Mockito.when(templateGroupRepository.findFirstByType(TemplateGroupType.DEFAULT)).thenReturn(templateGroupDefault);

		// database
		InternalDatabase database = new InternalDatabase(List.of(), List.of(), List.of(), List.of(templateGroup), List.of(templateWithGroup), List.of(), List.of(), List.of());

		// act
		importService.importDatabase(database, new AccountMatchList(List.of()), false, true, true);

		// assert
		Mockito.verify(templateGroupRepository, Mockito.never()).save(Mockito.any());

		Template expectedTemplate = new Template();
		expectedTemplate.setTemplateName("myTemplate");
		expectedTemplate.setTags(new ArrayList<>());
		expectedTemplate.setTemplateGroup(templateGroupDefault);

		Mockito.verify(templateRepository, Mockito.atLeast(1)).save(expectedTemplate);
	}

	@Test
	void test_skipCharts()
	{
		Chart chart = new Chart();
		chart.setID(9);
		chart.setName("The best chart");
		chart.setType(ChartType.CUSTOM);
		chart.setVersion(7);

		// database
		InternalDatabase database = new InternalDatabase(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(chart), List.of(), List.of());

		Mockito.when(chartService.getHighestUsedID()).thenReturn(8);
		final ChartRepository chartRepositoryMock = Mockito.mock(ChartRepository.class);
		Mockito.when(chartService.getRepository()).thenReturn(chartRepositoryMock);

		// act
		importService.importDatabase(database, new AccountMatchList(List.of()), true, true, false);

		// assert
		Mockito.verify(chartRepositoryMock, Mockito.never()).save(Mockito.any());
	}
}