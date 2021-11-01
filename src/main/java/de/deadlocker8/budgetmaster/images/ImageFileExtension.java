package de.deadlocker8.budgetmaster.images;

import org.springframework.http.MediaType;

import java.util.Optional;

public enum ImageFileExtension
{
	PNG("png", "png", MediaType.IMAGE_PNG),
	JPG("jpg", "jpeg", MediaType.IMAGE_JPEG),
	SVG("svg", "svg+xml", MediaType.valueOf("image/svg+xml"));

	private final String fileExtension;
	private final String base64Type;
	private final MediaType mediaType;

	ImageFileExtension(String fileExtension, String base64Type, MediaType mediaType)
	{
		this.fileExtension = fileExtension;
		this.base64Type = base64Type;
		this.mediaType = mediaType;
	}

	public String getFileExtension()
	{
		return fileExtension;
	}

	public String getBase64Type()
	{
		return base64Type;
	}

	public MediaType getMediaType()
	{
		return mediaType;
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
				", mediaType=" + mediaType +
				"} " + super.toString();
	}
}
