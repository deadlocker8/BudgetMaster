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
	private boolean descending;
	private File savePath;
	private String currency;
	private DateTime date;

	public ReportGenerator(ArrayList<ReportItem> reportItems, ColumnOrder columnOrder, boolean splitTable, boolean includeCategoryBudgets, boolean descending, File savePath, String currency, DateTime date)
	{
		this.reportItems = reportItems;
		this.columnOrder = columnOrder;
		this.splitTable = splitTable;
		this.includeCategoryBudgets = includeCategoryBudgets;
		this.descending = descending;
		this.savePath = savePath;
		this.currency = currency;
		this.date = date;
	}

	public void generate() throws FileNotFoundException, DocumentException
	{
		Document document = new Document();
		PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(savePath));
		writer.setPageEvent(new HeaderFooterPageEvent());
		document.open();
		
		//header
		Font chapterFont = new Font(FontFamily.HELVETICA, 16, Font.BOLDITALIC);
        Font paragraphFont = new Font(FontFamily.HELVETICA, 12, Font.NORMAL);
        Chunk chunk = new Chunk("Monatsbericht - " + date.toString("MMMM yyyy"), chapterFont);
        Chapter chapter = new Chapter(new Paragraph(chunk), 1);
        chapter.setNumberDepth(0);
        chapter.add(Chunk.NEWLINE);
        chapter.add(new Paragraph("Buchungen", paragraphFont));
        document.add(chapter);
        document.add(Chunk.NEWLINE);
		
        //table
		int numberOfColumns = columnOrder.getColumns().size();
		if(numberOfColumns > 0)
		{
			PdfPTable table = new PdfPTable(numberOfColumns);
			table.setWidthPercentage(100);
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
				for(ColumnType column : columnOrder.getColumns())
				{
					PdfPCell cell = new PdfPCell(new Phrase(getProperty(currentItem, column), font));
					cell.setBackgroundColor(new BaseColor(Color.WHITE));
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					table.addCell(cell);
				}
			}

			
			document.add(table);
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
				//TODO icon
				return String.valueOf(reportItem.getRepeating());
			default:
				return null;
		}
	}
}