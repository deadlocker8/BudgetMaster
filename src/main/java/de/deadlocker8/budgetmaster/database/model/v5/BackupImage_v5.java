package de.deadlocker8.budgetmaster.database.model.v5;

import de.deadlocker8.budgetmaster.images.ImageFileExtension;

import java.util.Objects;

public class BackupImage_v5
{
	private Integer ID;
	private Byte[] image;
	private String fileName;
	private ImageFileExtension fileExtension;

	public BackupImage_v5()
	{
	}

	public BackupImage_v5(Integer ID, Byte[] image, String fileName, ImageFileExtension fileExtension)
	{
		this.ID = ID;
		this.image = image;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public Byte[] getImage()
	{
		return image;
	}

	public void setImage(Byte[] image)
	{
		this.image = image;
	}

	public String getFileName()
	{
		return fileName;
	}

	public void setFileName(String fileName)
	{
		this.fileName = fileName;
	}

	public ImageFileExtension getFileExtension()
	{
		return fileExtension;
	}

	public void setFileExtension(ImageFileExtension fileExtension)
	{
		this.fileExtension = fileExtension;
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		BackupImage_v5 that = (BackupImage_v5) o;
		return Objects.equals(ID, that.ID) && Objects.equals(fileName, that.fileName) && fileExtension == that.fileExtension;
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, fileName, fileExtension);
	}

	@Override
	public String toString()
	{
		return "BackupImage_v5{" +
				"ID=" + ID +
				", fileName='" + fileName + '\'' +
				", fileExtension=" + fileExtension +
				'}';
	}
}
