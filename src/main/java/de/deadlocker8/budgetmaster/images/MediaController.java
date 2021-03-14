package de.deadlocker8.budgetmaster.images;

import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.utils.Mappings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

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
	public String handleImagePost(@RequestParam("file") MultipartFile file)
	{
		imageService.saveImageFile(file);
		return "redirect:/accounts";
	}
}
