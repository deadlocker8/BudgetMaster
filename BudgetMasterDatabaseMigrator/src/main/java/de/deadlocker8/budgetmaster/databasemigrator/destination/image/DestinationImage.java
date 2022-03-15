package de.deadlocker8.budgetmaster.databasemigrator.destination.image;

import javax.persistence.*;
import java.util.Arrays;
import java.util.Objects;

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

	public DestinationImage()
	{
		// empty
	}

	public DestinationImage(Integer ID, byte[] image, String fileName, Integer fileExtension)
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
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		DestinationImage that = (DestinationImage) o;
		return Objects.equals(ID, that.ID) && Arrays.equals(image, that.image) && Objects.equals(fileName, that.fileName) && Objects.equals(fileExtension, that.fileExtension);
	}

	@Override
	public int hashCode()
	{
		int result = Objects.hash(ID, fileName, fileExtension);
		result = 31 * result + Arrays.hashCode(image);
		return result;
	}

	@Override
	public String toString()
	{
		return "DestinationImage{" +
				"ID=" + ID +
				", image='" + image + '\'' +
				", fileName='" + fileName + '\'' +
				", fileExtension='" + fileExtension + '\'' +
				'}';
	}
}