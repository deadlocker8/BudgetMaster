package de.deadlocker8.budgetmaster.images;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.accounts.Account;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
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
	@Size(min = 1)
	@Expose
	private String imagePath;

	@OneToMany(mappedBy = "icon", fetch = FetchType.LAZY)
	private List<Account> referringAccounts;

	public Image(String imagePath)
	{
		this.imagePath = imagePath;
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

	public String getImagePath()
	{
		return imagePath;
	}

	public void setImagePath(String imagePath)
	{
		this.imagePath = imagePath;
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
				", imagePath='" + imagePath + '\'' +
				'}';
	}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Image image = (Image) o;
		return Objects.equals(ID, image.ID) && Objects.equals(imagePath, image.imagePath);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, imagePath);
	}
}