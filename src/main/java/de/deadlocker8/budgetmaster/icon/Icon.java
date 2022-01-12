package de.deadlocker8.budgetmaster.icon;

import com.google.gson.annotations.Expose;
import de.deadlocker8.budgetmaster.accounts.Account;
import de.deadlocker8.budgetmaster.categories.Category;
import de.deadlocker8.budgetmaster.images.Image;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.utils.ProvidesID;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Icon implements ProvidesID
{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Expose
	private Integer ID;

	@ManyToOne(fetch = FetchType.LAZY)
	@Expose
	private Image image;

	@Expose
	private String builtinIdentifier;

	@Expose
	private String fontColor;

	@OneToOne(mappedBy = "iconReference", fetch = FetchType.LAZY)
	private Account referringAccount;

	@OneToOne(mappedBy = "iconReference", fetch = FetchType.LAZY)
	private Template referringTemplate;

	@OneToOne(mappedBy = "iconReference", fetch = FetchType.LAZY)
	private Category referringCategory;

	public Icon()
	{
	}

	public Icon(Image image)
	{
		this.image = image;
	}

	public Icon(String builtinIdentifier)
	{
		this.builtinIdentifier = builtinIdentifier;
	}

	public Icon(String builtinIdentifier, String fontColor)
	{
		this.builtinIdentifier = builtinIdentifier;
		this.fontColor = fontColor;
	}

	public Integer getID()
	{
		return ID;
	}

	public void setID(Integer ID)
	{
		this.ID = ID;
	}

	public Image getImage()
	{
		return image;
	}

	public void setImage(Image image)
	{
		this.image = image;
	}

	public String getBuiltinIdentifier()
	{
		return builtinIdentifier;
	}

	public void setBuiltinIdentifier(String builtinIdentifier)
	{
		this.builtinIdentifier = builtinIdentifier;
	}

	public String getFontColor()
	{
		return fontColor;
	}

	public void setFontColor(String fontColor)
	{
		this.fontColor = fontColor;
	}

	public boolean isBuiltinIcon()
	{
		return image == null;
	}

	public Account getReferringAccount()
	{
		return referringAccount;
	}

	public Template getReferringTemplate()
	{
		return referringTemplate;
	}

	public Category getReferringCategory()
	{
		return referringCategory;
	}


	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;
		Icon icon = (Icon) o;
		return Objects.equals(ID, icon.ID) && Objects.equals(image, icon.image) && Objects.equals(builtinIdentifier, icon.builtinIdentifier) && Objects.equals(fontColor, icon.fontColor);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(ID, image, builtinIdentifier, fontColor);
	}

	@Override
	public String toString()
	{
		return "Icon{" +
				"ID=" + ID +
				", image=" + image +
				", builtinIdentifier='" + builtinIdentifier + '\'' +
				", fontColor='" + fontColor + '\'' +
				'}';
	}
}
