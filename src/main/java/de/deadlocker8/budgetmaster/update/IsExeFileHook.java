package de.deadlocker8.budgetmaster.update;

import de.thecodelabs.utils.util.SystemUtils;
import org.springframework.boot.ApplicationHome;

import java.io.File;

public class IsExeFileHook implements SystemUtils.SystemFileHook
{
	@Override
	public boolean isFileType()
	{
		File source = new ApplicationHome().getSource();
		if(source == null)
		{
			return false;
		}

		String sourcePath = source.getAbsolutePath();
		return sourcePath.endsWith(".exe") || sourcePath.endsWith(".EXE");
	}
}
