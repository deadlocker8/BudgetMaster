package de.deadlocker8.budgetmaster.images;

import com.google.gson.JsonObject;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.icon.Icon;
import de.deadlocker8.budgetmaster.icon.IconService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.thecodelabs.utils.util.Localization;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
	private static class ModelAttributes
	{
		public static final String ALL_ENTITIES = "availableImages";
		public static final String SELECTED_IMAGE_ID = "selectedImageID";
	}

	private static class ReturnValues
	{
		public static final String ALL_ENTITIES = "helpers/availableImages";
	}

	private final IconService iconService;
	private final ImageService imageService;

	@Autowired
	public MediaController(IconService iconService, ImageService imageService)
	{
		this.iconService = iconService;
		this.imageService = imageService;
	}

	@GetMapping(value = {"/getAvailableImages", "/getAvailableImages/{selectedImageID}"})
	public String getAvailableImages(Model model, @PathVariable(value = "selectedImageID", required = false) Integer selectedImageID)
	{
		if(selectedImageID == null)
		{
			selectedImageID = -1;
		}

		model.addAttribute(ModelAttributes.ALL_ENTITIES, imageService.getRepository().findAll());
		model.addAttribute(ModelAttributes.SELECTED_IMAGE_ID, selectedImageID);
		return ReturnValues.ALL_ENTITIES;
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
				LOGGER.error("Image upload failed", e);
				success = false;
				localizedMessage = Localization.getString("upload.image.error", e.getMessage());
			}
			catch(InvalidFileExtensionException e)
			{
				LOGGER.error("Image upload failed", e);
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

	@GetMapping("/getImageByIconID/{ID}")
	public ResponseEntity<byte[]> getImageByIconID(@PathVariable("ID") Integer iconID)
	{
		Optional<Icon> iconOptional = iconService.getRepository().findById(iconID);
		if(iconOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		final Image image = iconOptional.get().getImage();

		final HttpHeaders headers = new HttpHeaders();
		headers.setContentType(image.getFileExtension().getMediaType());

		final byte[] bytes = ArrayUtils.toPrimitive(image.getImage());
		return new ResponseEntity<>(bytes, headers, HttpStatus.CREATED);
	}
}
