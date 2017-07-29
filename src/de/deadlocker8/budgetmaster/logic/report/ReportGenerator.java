package de.deadlocker8.budgetmaster.logic.report;

import java.awt.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.joda.time.DateTime;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import de.deadlocker8.budgetmaster.logic.Helpers;

public class ReportGenerator
{
	private ArrayList<ReportItem> reportItems;
	private ColumnOrder columnOrder;
	private boolean splitTable;
	private boolean includeCategoryBudgets;
	private File savePath;
	private String currency;
	private DateTime date;

	public ReportGenerator(ArrayList<ReportItem> reportItems, ColumnOrder columnOrder, boolean splitTable, boolean includeCategoryBudgets, File savePath, String currency, DateTime date)
	{
		this.reportItems = reportItems;
		this.columnOrder = columnOrder;
		this.splitTable = splitTable;
		this.includeCategoryBudgets = includeCategoryBudgets;
		this.savePath = savePath;
		this.currency = currency;
		this.date = date;
	}

	private Chapter generateHeader()
	{
		Font chapterFont = new Font(FontFamily.HELVETICA, 16, Font.BOLDITALIC);
		Font paragraphFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL);
		Chunk chunk = new Chunk("Monatsbericht - " + date.toString("MMMM yyyy"), chapterFont);
		Chapter chapter = new Chapter(new Paragraph(chunk), 1);
		chapter.setNumberDepth(0);
		chapter.add(Chunk.NEWLINE);
		chapter.add(new Paragraph("Buchungsübersicht", paragraphFont));
		return chapter;
	}

	private PdfPTable generateTable(int tableWidth, AmountType amountType)
	{
		int numberOfColumns = columnOrder.getColumns().size();
		int totalIncome = 0;
		int totalPayment = 0;

		if(numberOfColumns > 0)
		{
			PdfPTable table = new PdfPTable(numberOfColumns);
			table.setWidthPercentage(tableWidth);
			Font font = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.BLACK);

			for(ColumnType column : columnOrder.getColumns())
			{
				// TODO get string for enum type
				PdfPCell cell = new PdfPCell(new Phrase(column.toString(), font));
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
				
				for(ColumnType column : columnOrder.getColumns())
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
					String totalIncomeString = String.valueOf(Helpers.NUMBER_FORMAT.format(totalIncome / 100.0).replace(".", ",")) + " " + currency;
					String totalPaymentString = String.valueOf(Helpers.NUMBER_FORMAT.format(totalPayment / 100.0).replace(".", ",")) + " " + currency;
					total = "Einnahmen: " + totalIncomeString + " / Ausgaben: " + totalPaymentString;
					break;
				case INCOME:
					total = "Summe: " + String.valueOf(Helpers.NUMBER_FORMAT.format(totalIncome / 100.0).replace(".", ",")) + " " + currency;					
					break;
				case PAYMENT:
					total = "Summe: " + String.valueOf(Helpers.NUMBER_FORMAT.format(totalPayment / 100.0).replace(".", ",")) + " " + currency;					
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

		document.add(generateHeader());
		document.add(Chunk.NEWLINE);

		if(splitTable)
		{
			Font paragraphFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL);

			document.add(new Paragraph("Einnahmen", paragraphFont));
			document.add(Chunk.NEWLINE);
			PdfPTable table = generateTable(100, AmountType.INCOME);
			if(table != null)
			{
				document.add(table);
			}

			document.add(Chunk.NEWLINE);
			document.add(new Paragraph("Ausgaben", paragraphFont));
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

		document.close();
	}

	private String getProperty(ReportItem reportItem, ColumnType columnType)
	{
		switch(columnType)
		{
			case AMOUNT:
				return String.valueOf(Helpers.NUMBER_FORMAT.format(reportItem.getAmount() / 100.0).replace(".", ",")) + " " + currency;
			case CATEGORY:
				return reportItem.getCategory().getName();
			case DATE:
				return reportItem.getDate();
			case DESCRIPTION:
				return reportItem.getDescription();
			case NAME:
				return reportItem.getName();
			case POSITION:
				return String.valueOf(reportItem.getPosition());
			case RATING:
				return reportItem.getAmount() > 0 ? "+" : "-";
			case REPEATING:
				// TODO icon
				return String.valueOf(reportItem.getRepeating());
			default:
				return null;
		}
	}
}