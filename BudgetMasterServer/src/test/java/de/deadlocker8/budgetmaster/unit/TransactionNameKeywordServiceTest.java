package de.deadlocker8.budgetmaster.unit;

import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeyword;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeywordRepository;
import de.deadlocker8.budgetmaster.transactions.keywords.TransactionNameKeywordService;
import de.deadlocker8.budgetmaster.unit.helpers.LocalizedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@LocalizedTest
class TransactionNameKeywordServiceTest
{
	private static final TransactionNameKeyword KEYWORD_1 = new TransactionNameKeyword("income");
	private static final TransactionNameKeyword KEYWORD_2 = new TransactionNameKeyword("abc");

	@Mock
	private TransactionNameKeywordRepository transactionNameKeywordRepository;

	@InjectMocks
	private TransactionNameKeywordService transactionNameKeywordService;

	@Test
	void test_getMatchingKeywords_emptyString()
	{
		Mockito.when(transactionNameKeywordRepository.findAll()).thenReturn(List.of(KEYWORD_1, KEYWORD_2));

		final List<String> matchingKeywords = transactionNameKeywordService.getMatchingKeywords("");

		assertThat(matchingKeywords).isEmpty();
	}

	@Test
	void test_getMatchingKeywords_noMatch()
	{
		Mockito.when(transactionNameKeywordRepository.findAll()).thenReturn(List.of(KEYWORD_1, KEYWORD_2));

		final List<String> matchingKeywords = transactionNameKeywordService.getMatchingKeywords("0815");

		assertThat(matchingKeywords).isEmpty();
	}

	@Test
	void test_getMatchingKeywords_matchFirstKeyword()
	{
		Mockito.when(transactionNameKeywordRepository.findAll()).thenReturn(List.of(KEYWORD_1, KEYWORD_2));

		final List<String> matchingKeywords = transactionNameKeywordService.getMatchingKeywords("income");

		assertThat(matchingKeywords)
				.containsExactly(KEYWORD_1.getValue());
	}

	@Test
	void test_getMatchingKeywords_matchAnotherKeyword()
	{
		Mockito.when(transactionNameKeywordRepository.findAll()).thenReturn(List.of(KEYWORD_1, KEYWORD_2));

		final List<String> matchingKeywords = transactionNameKeywordService.getMatchingKeywords("Lorem ipsum abc sadsad");

		assertThat(matchingKeywords)
				.containsExactly(KEYWORD_2.getValue());
	}

	@Test
	void test_getMatchingKeywords_matchMultipleKeywords()
	{
		Mockito.when(transactionNameKeywordRepository.findAll()).thenReturn(List.of(KEYWORD_1, KEYWORD_2));

		final List<String> matchingKeywords = transactionNameKeywordService.getMatchingKeywords("abcincome");

		assertThat(matchingKeywords)
				.containsExactly(KEYWORD_1.getValue(), KEYWORD_2.getValue());
	}

	@Test
	void test_getMatchingKeywords_ignoreCase()
	{
		Mockito.when(transactionNameKeywordRepository.findAll()).thenReturn(List.of(KEYWORD_1, KEYWORD_2));

		final List<String> matchingKeywords = transactionNameKeywordService.getMatchingKeywords("asdsad InCoMEasdasdsad");

		assertThat(matchingKeywords)
				.containsExactly(KEYWORD_1.getValue());
	}
}
