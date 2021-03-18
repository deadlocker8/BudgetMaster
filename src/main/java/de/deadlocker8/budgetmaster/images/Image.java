package de.deadlocker8.budgetmaster.images;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.accounts.Account;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.List;
import java.util.Objects;

@Entity
public class Image
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@NotNull
	@Expose
	@Lob
	private Byte[] image;

	@NotNull
	@Expose
	private String fileName;

	@NotNull
	@Expose
	private ImageFileExtension fileExtension;

	@OneToMany(mappedBy = "icon", fetch = FetchType.LAZY)
	private List<Account> referringAccounts;

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

	public List<Account> getReferringAccounts()
	{
		return referringAccounts;
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
		Image image = (Image) o;
		return Objects.equals(ID, image.ID) &&
				Objects.equals(fileName, image.fileName) &&
				Objects.equals(fileExtension, image.fileExtension);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, fileName, fileExtension);
	}
}