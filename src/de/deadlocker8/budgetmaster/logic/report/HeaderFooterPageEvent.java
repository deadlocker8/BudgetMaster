package de.deadlocker8.budgetmaster.logic.report;

import org.joda.time.DateTime;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

public class HeaderFooterPageEvent extends PdfPageEventHelper
{
	public void onStartPage(PdfWriter writer, Document document)
	{
		
	}

	public void onEndPage(PdfWriter writer, Document document)
	{
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("BudgetMaster Monatsbericht"), 100, 25, 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase("Seite " + document.getPageNumber()), 300, 25, 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(DateTime.now().toString("dd.MM.YYYY")), 500, 25, 0);
	}
}