package de.deadlocker8.budgetmaster.images;

import java.util.Optional;

public enum ImageFileExtension
{
	PNG("png", "png"),
	JPG("jpg", "jpeg"),
	SVG("svg", "svg+xml");


	private final String fileExtension;
	private final String base64Type;

	ImageFileExtension(String fileExtension, String base64Type)
	{
		this.fileExtension = fileExtension;
		this.base64Type = base64Type;
	}

	public String getFileExtension()
	{
		return fileExtension;
	}

	public String getBase64Type()
	{
		return base64Type;
	}

	public static Optional<ImageFileExtension> getByExtension(String extension)
	{
		for(ImageFileExtension currentExtension : values())
		{
			if(currentExtension.getFileExtension().equalsIgnoreCase(extension))
			{
				return Optional.of(currentExtension);
			}
		}

		return Optional.empty();
	}

	@Override
	public String toString()
	{
		return "ImageFileExtension{" +
				"fileExtension='" + fileExtension + '\'' +
				", base64Type='" + base64Type + '\'' +
				"} " + super.toString();
	}
}
