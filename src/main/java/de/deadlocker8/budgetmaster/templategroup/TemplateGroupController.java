package de.deadlocker8.budgetmaster.templategroup;

import com.google.gson.JsonObject;
import de.deadlocker8.budgetmaster.controller.BaseController;
import de.deadlocker8.budgetmaster.templates.Template;
import de.deadlocker8.budgetmaster.templates.TemplateService;
import de.deadlocker8.budgetmaster.utils.Mappings;
import de.deadlocker8.budgetmaster.utils.ResourceNotFoundException;
import de.deadlocker8.budgetmaster.utils.WebRequestUtils;
import de.deadlocker8.budgetmaster.utils.notification.Notification;
import de.deadlocker8.budgetmaster.utils.notification.NotificationType;
import de.thecodelabs.utils.util.Localization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.WebRequest;

import java.util.Optional;


@Controller
@RequestMapping(Mappings.TEMPLATE_GROUPS)
public class TemplateGroupController extends BaseController
{
	private final TemplateGroupService templateGroupService;
	private final TemplateService templateService;

	@Autowired
	public TemplateGroupController(TemplateGroupService templateGroupService, TemplateService templateService)
	{
		this.templateGroupService = templateGroupService;
		this.templateService = templateService;
	}

	@GetMapping
	public String showTemplateGroups(Model model)
	{
		model.addAttribute("templateGroups", templateGroupService.getAllEntitiesAsc());
		return "templateGroups/templateGroups";
	}

	@GetMapping("/{ID}/requestDelete")
	public String requestDeleteTemplateGroup(Model model, @PathVariable("ID") Integer ID)
	{
		if(!templateGroupService.isDeletable(ID))
		{
			return "redirect:/templateGroups";
		}

		model.addAttribute("templateGroups", templateGroupService.getAllEntitiesAsc());
		model.addAttribute("templateGroupToDelete", templateGroupService.findById(ID).orElseThrow());
		return "templateGroups/deleteTemplateGroupModal";
	}

	@GetMapping("/{ID}/delete")
	public String deleteTemplateGroup(WebRequest request, @PathVariable("ID") Integer ID)
	{
		final TemplateGroup templateGroupToDelete = templateGroupService.getRepository().getById(ID);
		templateGroupService.deleteTemplateGroup(ID);

		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.template.group.delete.success", templateGroupToDelete.getName()), NotificationType.SUCCESS));

		return "redirect:/templateGroups";
	}

	@GetMapping("/newTemplateGroup")
	public String newTemplate(Model model)
	{
		final TemplateGroup emptyTemplateGroup = new TemplateGroup();
		model.addAttribute("templateGroup", emptyTemplateGroup);
		return "templateGroups/newTemplateGroup";
	}

	@PostMapping(value = "/newTemplateGroup")
	public String post(WebRequest request,
					   Model model,
					   @ModelAttribute("NewTemplateGroup") TemplateGroup templateGroup, BindingResult bindingResult)
	{
		templateGroup.setName(templateGroup.getName().trim());

		TemplateGroupValidator validator = new TemplateGroupValidator();
		validator.validate(templateGroup, bindingResult);

		if(bindingResult.hasErrors())
		{
			model.addAttribute("error", bindingResult);
			model.addAttribute("templateGroup", templateGroup);
			return "templateGroups/newTemplateGroup";
		}

		templateGroup.setType(TemplateGroupType.CUSTOM);

		templateGroupService.getRepository().save(templateGroup);

		WebRequestUtils.putNotification(request, new Notification(Localization.getString("notification.template.save.success", templateGroup.getName()), NotificationType.SUCCESS));
		return "redirect:/templateGroups";
	}

	@GetMapping("/{ID}/edit")
	public String editTemplateGroup(Model model, @PathVariable("ID") Integer ID)
	{
		Optional<TemplateGroup> templateGroupOptional = templateGroupService.findById(ID);
		if(templateGroupOptional.isEmpty())
		{
			throw new ResourceNotFoundException();
		}

		TemplateGroup templateGroup = templateGroupOptional.get();
		model.addAttribute("templateGroup", templateGroup);
		return "templateGroups/newTemplateGroup";
	}

	@PostMapping(value = "/moveTemplateToGroup")
	@ResponseBody
	public String moveTemplateToGroup(@RequestParam("templateID") String templateID,
									  @RequestParam("groupID") String groupID)
	{
		int templateIDParsed;
		int groupIDParsed;
		try
		{
			templateIDParsed = Integer.parseInt(templateID);
			groupIDParsed = Integer.parseInt(groupID);
		}
		catch(NumberFormatException e)
		{
			return createJsonResponse(false, Localization.getString("template.move.to.group.error.parse"));
		}

		Optional<Template> templateOptional = templateService.findById(templateIDParsed);
		if(templateOptional.isEmpty())
		{
			return createJsonResponse(false, Localization.getString("template.move.to.group.error.invalidTemplate"));
		}

		Optional<TemplateGroup> templateGroupOptional = templateGroupService.findById(groupIDParsed);
		if(templateGroupOptional.isEmpty())
		{
			return createJsonResponse(false, Localization.getString("template.move.to.group.error.invalidGroup"));

		}

		final Template template = templateOptional.get();
		final TemplateGroup templateGroup = templateGroupOptional.get();

		LOGGER.debug("Moving template with ID {} to group {}", templateID, groupID);
		template.setTemplateGroup(templateGroup);
		templateService.getRepository().save(template);

		return createJsonResponse(true, Localization.getString("template.move.to.group.success"));
	}

	private String createJsonResponse(boolean isSuccess, String localizedMessage)
	{
		final JsonObject data = new JsonObject();
		data.addProperty("success", isSuccess);
		data.addProperty("localizedMessage", localizedMessage);
		return data.toString();
	}
}