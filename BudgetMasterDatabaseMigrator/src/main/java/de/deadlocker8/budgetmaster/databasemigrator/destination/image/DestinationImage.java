package de.deadlocker8.budgetmaster.databasemigrator.destination.image;

import javax.persistence.*;

@Entity
@Table(name = "image")
public class DestinationImage
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer ID;

	@Lob
	private byte[] image;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "file_extension")
	private Integer fileExtension;

	public DestinationImage(byte[] image, String fileName, Integer fileExtension)
	{
		this.image = image;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
	}

	public DestinationImage()
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