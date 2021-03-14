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

	@GetMapping("/getAvailableImages")
	public String getAvailableImages(Model model)
	{
		model.addAttribute("availableImages", imageService.getRepository().findAll());
		return "accounts/availableImages";
	}

	@PostMapping("uploadImage")
	@ResponseBody
	public String uploadImage(@RequestParam("file") MultipartFile file)
	{
		boolean success = true;
		String localizedMessage = Localization.getString("upload.image.success");

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

		final JsonObject data = new JsonObject();
		data.addProperty("isUploadSuccessful", success);
		data.addProperty("localizedMessage", localizedMessage);

		return data.toString();
	}
}
