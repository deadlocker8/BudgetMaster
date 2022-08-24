package de.deadlocker8.budgetmaster.unit.database.importer;

import de.deadlocker8.budgetmaster.database.importer.IconImporter;
import de.deadlocker8.budgetmaster.database.importer.TransactionImporter;
import de.deadlocker8.budgetmaster.database.importer.TransactionNameKeywordImporter;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconRepository;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.images.ImageFileExtension;
import de.deadlocker8.budgetmaster.images.ImageRepository;
import de.deadlocker8.budgetmaster.services.EntityType;
import de.deadlocker8.budgetmaster.services.ImportResultItem;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeyword;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeywordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.as;
import static org.assertj.core.api.Assertions.assertThat;

class TransactionNameKeywordImporterTest extends ImporterTestBase
{
	@Override
	List<String> getTableNamesToResetSequence()
	{
		return List.of("transaction_name_keyword");
	}

	@Autowired
	private TransactionNameKeywordRepository keywordRepository;

	@Test
	void test_importKeywords()
	{
		final TransactionNameKeyword keyword1 = new TransactionNameKeyword(12, "income");
		final TransactionNameKeyword keyword2 = new TransactionNameKeyword(13, "xyz");

		final TransactionNameKeywordImporter importer = new TransactionNameKeywordImporter(keywordRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(keyword1, keyword2));

		final ImportResultItem expected = new ImportResultItem(EntityType.TRANSACTION_NAME_KEYWORD, 2, 2, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(keyword1).hasFieldOrPropertyWithValue("ID", 1);
		assertThat(keyword2).hasFieldOrPropertyWithValue("ID", 2);
	}

	@Test
	void test_importKeywords_skipAlreadyExisting()
	{
		final TransactionNameKeyword existingKeyword = new TransactionNameKeyword(12, "income");
		keywordRepository.save(existingKeyword);

		final TransactionNameKeywordImporter importer = new TransactionNameKeywordImporter(keywordRepository);
		final ImportResultItem resultItem = importer.importItems(List.of(existingKeyword));

		final ImportResultItem expected = new ImportResultItem(EntityType.TRANSACTION_NAME_KEYWORD, 1, 1, List.of());
		assertThat(resultItem).isEqualTo(expected);
		assertThat(keywordRepository.findAll()).hasSize(1);
	}
}