package de.deadlocker8.budgetmaster.images;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.utils.ProvidesID;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Entity
public class Image implements ProvidesID
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@NotNull
	@Expose
	@Column(length = 16777215)
	@Lob
	private Byte[] image;

	@NotNull
	@Expose
	private String fileName;

	@NotNull
	@Expose
	private ImageFileExtension fileExtension;

	@OneToMany(mappedBy = "image", fetch = FetchType.LAZY)
	private List<Icon> referringIcons;

	private static final String BASE_64_IMAGE_FORMAT = "data:image/{0};base64,{1}";

	public Image(@NotNull Byte[] image, String fileName, ImageFileExtension fileExtension)
	{
		this.image = image;
		this.fileName = fileName;
		this.fileExtension = fileExtension;
	}

	public Image()
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

	public Byte[] getImage()
	{
		return image;
	}

	public void setImage(Byte[] image)
	{
		this.image = image;
	}

	public String getBase64EncodedImage()
	{
		final String encoded = Base64.encodeBase64String(ArrayUtils.toPrimitive(this.image));
		return MessageFormat.format(BASE_64_IMAGE_FORMAT, fileExtension.getBase64Type(), encoded);
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

	public List<Icon> getReferringIcons()
	{
		return referringIcons;
	}

	@Override
	public String toString()
	{
		return "Image{" +
				"ID=" + ID +
				", image='" + image + '\'' +
				", fileName='" + fileName + '\'' +
				", fileExtension='" + fileExtension + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Image other = (Image) o;
		return Objects.equals(ID, other.ID) &&
				Objects.equals(fileName, other.fileName) &&
				Objects.equals(fileExtension, other.fileExtension);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, fileName, fileExtension);
	}
}