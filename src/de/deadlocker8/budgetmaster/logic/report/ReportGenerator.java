package de.deadlocker8.budgetmaster.logic.report;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.deadlocker8.budgetmaster.logic.Budget;
import de.deadlocker8.budgetmaster.logic.category.CategoryBudget;
import de.deadlocker8.budgetmaster.logic.utils.Fonts;
import de.deadlocker8.budgetmaster.logic.utils.Helpers;
import de.deadlocker8.budgetmaster.logic.utils.Strings;
import tools.Localization;

public class ReportGenerator
{
	private ArrayList<ReportItem> reportItems;
	private ArrayList<CategoryBudget> categoryBudgets;
	private ReportPreferences reportPreferences;
	private File savePath;
	private String currency;
	private DateTime date;
	private Budget budget;
	private final String FONT = Fonts.OPEN_SANS;
	
	public ReportGenerator(ArrayList<ReportItem> reportItems, ArrayList<CategoryBudget> categoryBudgets, ReportPreferences reportPreferences, File savePath, String currency, DateTime date, Budget budget)
	{	
		this.reportItems = reportItems;
		this.categoryBudgets = categoryBudgets;
		this.reportPreferences = reportPreferences;
		this.savePath = savePath;
		this.currency = currency;
		this.date = date;
		this.budget = budget;
	}

	private Chapter generateHeader()
	{	
		Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 16, Font.BOLDITALIC, BaseColor.BLACK);
		Chunk chunk = new Chunk(Localization.getString(Strings.REPORT_HEADLINE, date.toString("MMMM yyyy")), font);
		Chapter chapter = new Chapter(new Paragraph(chunk), 1);
		chapter.setNumberDepth(0);
		chapter.add(Chunk.NEWLINE);
		return chapter;
	}

	private PdfPTable generateTable(int tableWidth, AmountType amountType)
	{
		int numberOfColumns = reportPreferences.getColumnOrder().getColumns().size();
		int totalIncome = 0;
		int totalPayment = 0;

		if(numberOfColumns > 0)
		{
			float[] proportions = new float[numberOfColumns];
			for(int i = 0; i < reportPreferences.getColumnOrder().getColumns().size(); i++)
			{
				proportions[i] = reportPreferences.getColumnOrder().getColumns().get(i).getProportion();
			}
			
			PdfPTable table = new PdfPTable(proportions);
			table.setWidthPercentage(tableWidth);
			Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 8, Font.NORMAL, GrayColor.BLACK);

			for(ColumnType column : reportPreferences.getColumnOrder().getColumns())
			{
				PdfPCell cell = new PdfPCell(new Phrase(column.getName(), font));
				cell.setBackgroundColor(GrayColor.LIGHT_GRAY);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);
			}

			for(ReportItem currentItem : reportItems)
			{
				if(currentItem.getAmount() > 0)
				{
					totalIncome += currentItem.getAmount();
					if(amountType == AmountType.PAYMENT)
					{
						continue;
					}
				}
				else
				{
					totalPayment += currentItem.getAmount();
					if(amountType == AmountType.INCOME)
					{
						continue;
					}
				}

				for(ColumnType column : reportPreferences.getColumnOrder().getColumns())
				{
					PdfPCell cell = new PdfPCell(new Phrase(getProperty(currentItem, column), font));
					cell.setBackgroundColor(new BaseColor(Color.WHITE));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);
				}
			}

			PdfPCell cellTotal;
			String total = "";
			switch(amountType)
			{
				case BOTH:
					String totalIncomeString = Helpers.getCurrencyString(totalIncome, currency);
					String totalPaymentString = Helpers.getCurrencyString(totalPayment, currency);
					total = Localization.getString(Strings.REPORT_SUM_TOTAL, totalIncomeString, totalPaymentString);
					break;
				case INCOME:
					total = Localization.getString(Strings.REPORT_SUM, Helpers.getCurrencyString(totalIncome, currency));
					break;
				case PAYMENT:
					total = Localization.getString(Strings.REPORT_SUM, Helpers.getCurrencyString(totalPayment, currency));
					break;
				default:
					break;
			}

			cellTotal = new PdfPCell(new Phrase(total, font));
			cellTotal.setBackgroundColor(new BaseColor(Color.WHITE));
			cellTotal.setColspan(numberOfColumns);
			cellTotal.setHorizontalAlignment(Element.ALIGN_RIGHT);
			table.addCell(cellTotal);

			return table;
		}
		return null;
	}

	public void generate() throws FileNotFoundException, DocumentException
	{
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(savePath));
		writer.setPageEvent(new HeaderFooterPageEvent());
		document.open();
		document.setMargins(50, 45, 50, 70);		
		Font headerFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 14, Font.BOLD, BaseColor.BLACK);
		Font smallHeaderFont = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);

		document.add(generateHeader());
		document.add(Chunk.NEWLINE);
		
		if(reportPreferences.isIncludeBudget())
		{			
			Font fontGreen = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.NORMAL, new BaseColor(36, 122, 45));			
			Font fontRed = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.NORMAL, BaseColor.RED);
			Font fontBlack = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 12, Font.BOLD, BaseColor.BLACK);
			
			document.add(new Paragraph("Budget", headerFont));
			document.add(Chunk.NEWLINE);
			document.add(new Paragraph("Einnahmen: " + Helpers.getCurrencyString(budget.getIncomeSum(), currency), fontGreen));
			document.add(new Paragraph("Ausgaben: " + Helpers.getCurrencyString(budget.getPaymentSum(), currency), fontRed));
			document.add(new Paragraph("Restbudget: " + Helpers.getCurrencyString(budget.getIncomeSum() + budget.getPaymentSum(), currency), fontBlack));			
			document.add(Chunk.NEWLINE);
		}
		
		document.add(new Paragraph(Localization.getString(Strings.REPORT_HEADLINE_PAYMENTS_OVERVIEW), headerFont));
		document.add(Chunk.NEWLINE);

		if(reportPreferences.isSplitTable())
		{
			document.add(new Paragraph(Localization.getString(Strings.TITLE_INCOMES), smallHeaderFont));
			document.add(Chunk.NEWLINE);
			
			PdfPTable table = generateTable(100, AmountType.INCOME);
			if(table != null)
			{
				document.add(table);
			}

			document.add(Chunk.NEWLINE);
			document.add(new Paragraph(Localization.getString(Strings.TITLE_PAYMENTS), smallHeaderFont));			
			document.add(Chunk.NEWLINE);
			
			table = generateTable(100, AmountType.PAYMENT);
			if(table != null)
			{
				document.add(table);
			}
		}
		else
		{
			PdfPTable table = generateTable(100, AmountType.BOTH);
			if(table != null)
			{
				document.add(table);
			}
		}

		if(reportPreferences.isIncludeCategoryBudgets())
		{
			document.add(Chunk.NEWLINE);
			document.add(new Paragraph(Localization.getString(Strings.TITLE_CATEGORY_BUDGETS), smallHeaderFont));
			document.add(Chunk.NEWLINE);
			
			PdfPTable table = generateCategoryBudgets();
			if(table != null)
			{
				document.add(table);
			}
		}

		document.close();
	}

	private PdfPTable generateCategoryBudgets()
	{
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100);
		Font font = FontFactory.getFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 8, Font.NORMAL, BaseColor.BLACK);
		
		//header cells
		PdfPCell cellHeaderCategory = new PdfPCell(new Phrase(Localization.getString(Strings.TITLE_CATEGORY), font));
		cellHeaderCategory.setBackgroundColor(GrayColor.LIGHT_GRAY);
		cellHeaderCategory.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cellHeaderCategory);
		PdfPCell cellHeaderAmount = new PdfPCell(new Phrase(Localization.getString(Strings.TITLE_AMOUNT), font));
		cellHeaderAmount.setBackgroundColor(GrayColor.LIGHT_GRAY);
		cellHeaderAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
		table.addCell(cellHeaderAmount);		

		for(CategoryBudget budget : categoryBudgets)
		{				
			PdfPCell cellName = new PdfPCell(new Phrase(budget.getCategory().getName(), font));
			cellName.setBackgroundColor(new BaseColor(Color.WHITE));
			cellName.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cellName);
			
			PdfPCell cellAmount = new PdfPCell(new Phrase(Helpers.getCurrencyString(budget.getBudget() / 100.0, currency), font));
			cellAmount.setBackgroundColor(new BaseColor(Color.WHITE));
			cellAmount.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cellAmount);
		}
		
		return table;
	}

	private String getProperty(ReportItem reportItem, ColumnType columnType)
	{
		switch(columnType)
		{
			case AMOUNT:
				return Helpers.getCurrencyString(reportItem.getAmount(), currency);
			case CATEGORY:	
				return reportItem.getCategory().getName();
			case DATE:			    
				return DateTime.parse(reportItem.getDate(), DateTimeFormat.forPattern("YYYY-MM-dd")).toString("dd.MM.YYYY");
			case DESCRIPTION:
				return reportItem.getDescription();
			case TAGS:
				return reportItem.getTags();
			case NAME:
				return reportItem.getName();
			case POSITION:
				return String.valueOf(reportItem.getPosition());
			case RATING:
				return reportItem.getAmount() > 0 ? "+" : "-";
			case REPEATING:	
				if(reportItem.getRepeating())
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
}