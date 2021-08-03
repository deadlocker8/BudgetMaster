package de.deadlocker8.budgetmaster.images;

import com.google.gson.JsonObject;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Controller
@RequestMapping(Mappings.MEDIA)
public class MediaController extends BaseController
{
	private final ImageService imageService;

	@Autowired
	public MediaController(ImageService imageService)
	{
		this.imageService = imageService;
	}

	@GetMapping(value = {"/getAvailableImages", "/getAvailableImages/{selectedImageID}"})
	public String getAvailableImages(Model model, @PathVariable(value = "selectedImageID", required = false) Integer selectedImageID)
	{
		if(selectedImageID == null)
		{
			selectedImageID = -1;
		}

		model.addAttribute("availableImages", imageService.getRepository().findAll());
		model.addAttribute("selectedImageID", selectedImageID);
		return "helpers/availableImages";
	}

	@PostMapping("uploadImage")
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file)
	{
		boolean success = true;
		String localizedMessage = Localization.getString("upload.image.success");

		if(file.isEmpty())
		{
			success = false;
			localizedMessage = Localization.getString("upload.image.error.no.file");
		}
		else
		{
			try
			{
				imageService.saveImageFile(file);
			}
			catch(IOException e)
			{
				e.printStackTrace();
				success = false;
				localizedMessage = Localization.getString("upload.image.error", e.getMessage());
			}
			catch(InvalidFileExtensionException e)
			{
				e.printStackTrace();
				success = false;
				localizedMessage = e.getMessage();
			}
		}

		final JsonObject data = new JsonObject();
		data.addProperty("isUploadSuccessful", success);
		data.addProperty("localizedMessage", localizedMessage);

		return data.toString();
	}

	@GetMapping("/deleteImage/{ID}")
	@ResponseBody
	public String deleteImage(@PathVariable("ID") Integer ID)
	{
		boolean success = false;
		String localizedMessage = Localization.getString("delete.image.error.not.existing", ID);

		Optional<Image> imageOptional = imageService.getRepository().findById(ID);
		if(imageOptional.isPresent())
		{
			success = true;
			localizedMessage = Localization.getString("delete.image.success");
			imageService.deleteImage(imageOptional.get());
		}

		final JsonObject data = new JsonObject();
		data.addProperty("isDeleteSuccessful", success);
		data.addProperty("localizedMessage", localizedMessage);

		return data.toString();
	}
}
