package de.deadlocker8.budgetmaster.databasemigrator.source.image;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class SourceImage
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

	@Lob
	private byte[] image;

	private String fileName;

	private Integer fileExtension;

	public SourceImage(byte[] image, String fileName, Integer fileExtension)
	{
		this.image = image;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
	}

	public SourceImage()
	{
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public byte[] getImage()
	{
		return image;
	}

	public void setImage(byte[] image)
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

	public Integer getFileExtension()
	{
		return fileExtension;
	}

	public void setFileExtension(Integer fileExtension)
	{
		this.fileExtension = fileExtension;
	}

	@Override
	public String toString()
	{
		return "SourceImage{" +
				"ID=" + ID +
				", image='" + image + '\'' +
				", fileName='" + fileName + '\'' +
				", fileExtension='" + fileExtension + '\'' +
				'}';
	}
}