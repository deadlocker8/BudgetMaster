package de.deadlocker8.budgetmaster.reports;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import de.deadlocker8.budgetmaster.services.DateFormatStyle;
import de.deadlocker8.budgetmaster.utils.DateHelper;
import de.deadlocker8.budgetmaster.utils.Strings;
import de.thecodelabs.utils.util.Localization;

import java.time.format.DateTimeFormatter;


public class HeaderFooterPageEvent extends PdfPageEventHelper
{
	@Override
	public void onStartPage(PdfWriter writer, Document document)
	{
		// empty
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document)
	{
		Font font = FontFactory.getFont(Fonts.OPEN_SANS, BaseFont.IDENTITY_H, BaseFont.EMBEDDED, 8, Font.NORMAL, BaseColor.BLACK);

		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(Localization.getString(Strings.REPORT_FOOTER_LEFT), font), 100, 25, 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(Localization.getString(Strings.REPORT_FOOTER_CENTER, document.getPageNumber()), font), 300, 25, 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(DateHelper.getCurrentDate().format(DateTimeFormatter.ofPattern(DateFormatStyle.LONG.getKey())), font), 500, 25, 0);
	}
}