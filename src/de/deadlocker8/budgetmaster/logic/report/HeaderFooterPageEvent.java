package de.deadlocker8.budgetmaster.logic.report;

import org.joda.time.DateTime;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

import de.deadlocker8.budgetmaster.logic.utils.Strings;
import tools.Localization;

public class HeaderFooterPageEvent extends PdfPageEventHelper
{
	public void onStartPage(PdfWriter writer, Document document)
	{
		
	}

	public void onEndPage(PdfWriter writer, Document document)
	{
		Font font = new Font(FontFamily.HELVETICA, 8, Font.NORMAL, GrayColor.BLACK);

		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(Localization.getString(Strings.REPORT_FOOTER_LEFT), font), 100, 25, 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(Localization.getString(Strings.REPORT_FOOTER_CENTER, document.getPageNumber()), font), 300, 25, 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(DateTime.now().toString("dd.MM.YYYY"), font), 500, 25, 0);
	}
}