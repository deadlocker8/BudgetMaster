package de.deadlocker8.budgetmaster.images;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.accounts.Account;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.text.MessageFormat;
import java.util.Arrays;
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
	private String fileExtension;

	@OneToMany(mappedBy = "icon", fetch = FetchType.LAZY)
	private List<Account> referringAccounts;

	private static final String BASE_64_IMAGE_FORMAT = "data:image/{0};base64,{1}";

	public Image(@NotNull Byte[] image, String fileExtension)
	{
		this.image = image;
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
		return MessageFormat.format(BASE_64_IMAGE_FORMAT, fileExtension, encoded);
	}

	public String getFileExtension()
	{
		return fileExtension;
	}

	public void setFileExtension(String fileExtension)
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
				Arrays.equals(image, other.image) &&
				Objects.equals(fileExtension, other.fileExtension);
	}

	@Override
	public int hashCode()
	{
		int result = Objects.hash(ID, fileExtension);
		result = 31 * result + Arrays.hashCode(image);
		return result;
	}
}