package de.deadlocker8.budgetmaster.services.report;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.*;
import de.deadlocker8.budgetmaster.entities.Tag;
import de.deadlocker8.budgetmaster.entities.Transaction;
import de.deadlocker8.budgetmaster.entities.report.ReportColumn;
import de.deadlocker8.budgetmaster.reports.*;
import de.deadlocker8.budgetmaster.reports.categoryBudget.CategoryBudget;
import de.deadlocker8.budgetmaster.services.HelpersService;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;
import javafx.scene.paint.Color;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReportGeneratorService
{
	@Autowired
	HelpersService helpersService;

	private final static String FONT = Fonts.OPEN_SANS;

	@Autowired
	public ReportGeneratorService(HelpersService helpersService)
	{
		this.helpersService = helpersService;
	}

	private Chapter generateHeader(ReportConfiguration reportConfiguration)
	{
		Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.BOLDITALIC, BaseColor.BLACK);
		Chunk chunk = new Chunk(Localization.getString(Strings.REPORT_HEADLINE, reportConfiguration.getReportSettings().getDate().toString("MMMM yyyy")), font);
		Chapter chapter = new Chapter(new Paragraph(chunk), 1);
		chapter.setNumberDepth(0);

		Font fontAccount = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.BOLD, BaseColor.BLACK);
		Chunk chunkAccount = new Chunk(Localization.getString(Strings.REPORT_HEADLINE_ACCOUNT, reportConfiguration.getAccountName()), fontAccount);
		chapter.add(chunkAccount);
		chapter.add(Chunk.NEWLINE);
		return chapter;
	}

	private PdfPTable generateTable(ReportConfiguration reportConfiguration, int tableWidth, AmountType amountType)
	{
		List<ReportColumn> columns = reportConfiguration.getReportSettings().getColumnsSortedAndFiltered();
		int numberOfColumns = columns.size();

		if(numberOfColumns > 0)
		{
			float[] proportions = new float[numberOfColumns];
			for(int i = 0; i < columns.size(); i++)
			{
				ReportColumn column = columns.get(i);
				proportions[i] = ColumnType.getByName(column.getKey()).getProportion();
			}

			PdfPTable table = new PdfPTable(proportions);
			table.setWidthPercentage(tableWidth);
			Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 8, Font.NORMAL, GrayColor.BLACK);

			// add table header
			for(ReportColumn column : columns)
			{
				ColumnType columnType = ColumnType.getByName(column.getKey());

				PdfPCell cell = new PdfPCell(new Phrase(columnType.getName(), font));
				cell.setBackgroundColor(GrayColor.LIGHT_GRAY);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				table.addCell(cell);
			}

			int index = 0;
			for(Transaction currentItem : reportConfiguration.getTransactions())
			{
				if(amountType.equals(AmountType.INCOME) && currentItem.getAmount() <= 0)
				{
					continue;
				}

				if(amountType.equals(AmountType.EXPENDITURE) && currentItem.getAmount() > 0)
				{
					continue;
				}

				index++;

				for(ReportColumn column : columns)
				{
					ColumnType columnType = ColumnType.getByName(column.getKey());
					PdfPCell cell = new PdfPCell(new Phrase(getProperty(currentItem, columnType, index), font));
					cell.setBackgroundColor(getBaseColor(Color.WHITE));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					table.addCell(cell);
				}
			}

			PdfPCell cellTotal;
			String total = "";
			String totalIncomeString = helpersService.getCurrencyString(reportConfiguration.getBudget().getIncomeSum());
			String totalExpenditureString = helpersService.getCurrencyString(reportConfiguration.getBudget().getExpenditureSum());
			switch(amountType)
			{
				case BOTH:
					total = Localization.getString(Strings.REPORT_SUM_TOTAL, totalIncomeString, totalExpenditureString);
					break;
				case INCOME:
					total = Localization.getString(Strings.REPORT_SUM, totalIncomeString);
					break;
				case EXPENDITURE:
					total = Localization.getString(Strings.REPORT_SUM, totalExpenditureString);
					break;
			}

			cellTotal = new PdfPCell(new Phrase(total, font));
			cellTotal.setBackgroundColor(getBaseColor(Color.WHITE));
			cellTotal.setColspan(numberOfColumns);
			cellTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
			cellTotal.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cellTotal);

			return table;
		}
		return null;
	}

	public byte[] generate(ReportConfiguration reportConfiguration) throws DocumentException
	{
		Document document = new Document();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		PdfWriter writer = PdfWriter.getInstance(document, byteArrayOutputStream);
		writer.setPageEvent(new HeaderFooterPageEvent());
		document.open();
		document.setMargins(50, 45, 50, 70);
		Font headerFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.BOLD, BaseColor.BLACK);
		Font smallHeaderFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);

		document.add(generateHeader(reportConfiguration));
		document.add(Chunk.NEWLINE);

		if(reportConfiguration.getReportSettings().isIncludeBudget())
		{
			Font fontGreen = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.NORMAL, new BaseColor(36, 122, 45));
			Font fontRed = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.RED);
			Font fontBlack = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);

			Budget budget = reportConfiguration.getBudget();

			document.add(new Paragraph(Localization.getString(Strings.REPORT_BUDGET), headerFont));
			document.add(Chunk.NEWLINE);
			document.add(new Paragraph(Localization.getString(Strings.REPORT_INCOMES) + helpersService.getCurrencyString(budget.getIncomeSum()), fontGreen));
			document.add(new Paragraph(Localization.getString(Strings.REPORT_PAYMENTS) + helpersService.getCurrencyString(budget.getExpenditureSum()), fontRed));
			document.add(new Paragraph(Localization.getString(Strings.REPORT_BUDGET_REST) + helpersService.getCurrencyString(budget.getIncomeSum() + budget.getExpenditureSum()), fontBlack));
			document.add(Chunk.NEWLINE);
		}

		document.add(new Paragraph(Localization.getString(Strings.REPORT_HEADLINE_TRANSACTIONS_OVERVIEW), headerFont));
		document.add(Chunk.NEWLINE);

		if(reportConfiguration.getReportSettings().isSplitTables())
		{
			document.add(new Paragraph(Localization.getString(Strings.TITLE_INCOMES), smallHeaderFont));
			document.add(Chunk.NEWLINE);

			PdfPTable table = generateTable(reportConfiguration, 100, AmountType.INCOME);
			if(table != null)
			{
				document.add(table);
			}

			document.add(Chunk.NEWLINE);
			document.add(new Paragraph(Localization.getString(Strings.TITLE_EXPENDITURES), smallHeaderFont));
			document.add(Chunk.NEWLINE);

			table = generateTable(reportConfiguration,100, AmountType.EXPENDITURE);
			if(table != null)
			{
				document.add(table);
			}
		}
		else
		{
			PdfPTable table = generateTable(reportConfiguration, 100, AmountType.BOTH);
			if(table != null)
			{
				document.add(table);
			}
		}

		if(reportConfiguration.getReportSettings().isIncludeCategoryBudgets())
		{
			document.add(Chunk.NEWLINE);
			document.add(new Paragraph(Localization.getString(Strings.TITLE_CATEGORY_BUDGETS), smallHeaderFont));
			document.add(Chunk.NEWLINE);

			PdfPTable table = generateCategoryBudgets(reportConfiguration);
			if(table != null)
			{
				document.add(table);
			}
		}

		document.close();
		return byteArrayOutputStream.toByteArray();
	}

	private PdfPTable generateCategoryBudgets(ReportConfiguration reportConfiguration)
	{
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 8, Font.NORMAL, BaseColor.BLACK);

		//header cells
		PdfPCell cellHeaderCategory = new PdfPCell(new Phrase(Localization.getString(Strings.REPORT_CATEGORY), font));
		cellHeaderCategory.setBackgroundColor(GrayColor.LIGHT_GRAY);
		cellHeaderCategory.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cellHeaderCategory);
		PdfPCell cellHeaderAmount = new PdfPCell(new Phrase(Localization.getString(Strings.REPORT_AMOUNT), font));
		cellHeaderAmount.setBackgroundColor(GrayColor.LIGHT_GRAY);
		cellHeaderAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cellHeaderAmount);

		for(CategoryBudget budget : reportConfiguration.getCategoryBudgets())
		{
			PdfPCell cellName = new PdfPCell(new Phrase(budget.getCategory().getName(), font));
			cellName.setBackgroundColor(getBaseColor(Color.WHITE));
			cellName.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellName.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cellName);

			PdfPCell cellAmount = new PdfPCell(new Phrase(helpersService.getCurrencyString(budget.getBudget() / 100.0), font));
			cellAmount.setBackgroundColor(getBaseColor(Color.WHITE));
			cellAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
			cellAmount.setVerticalAlignment(Element.ALIGN_MIDDLE);
			table.addCell(cellAmount);
		}

		return table;
	}

	private String getProperty(Transaction transaction, ColumnType columnType, int position)
	{
		switch(columnType)
		{
			case AMOUNT:
				return helpersService.getCurrencyString(transaction.getAmount());
			case CATEGORY:
				return transaction.getCategory().getName();
			case DATE:
				return transaction.getDate().toString("dd.MM.YYYY");
			case DESCRIPTION:
				return transaction.getDescription();
			case TAGS:
				return transaction.getTags().stream().map(Tag::getName).collect(Collectors.joining(", "));
			case NAME:
				return transaction.getName();
			case POSITION:
				return String.valueOf(position);
			case RATING:
				return transaction.getAmount() > 0 ? "+" : "-";
			case REPEATING:
				if(transaction.isRepeating())
				{
					return Localization.getString(Strings.REPORT_REPEATING_YES);
				}
				else
				{
					return Localization.getString(Strings.REPORT_REPEATING_NO);
				}
			default:
				return null;
		}
	}

	private BaseColor getBaseColor(Color color)
	{
		return new BaseColor((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue());
	}
}