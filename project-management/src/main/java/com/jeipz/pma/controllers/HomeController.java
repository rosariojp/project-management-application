package com.jeipz.pma.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jeipz.pma.dto.ChartData;
import com.jeipz.pma.dto.EmployeeProject;
import com.jeipz.pma.entities.Project;
import com.jeipz.pma.services.EmployeeService;
import com.jeipz.pma.services.ProjectService;

@Controller
public class HomeController {

	@Value("${version}")
	private String version;
	
	@Autowired
	ProjectService proService;
	
	@Autowired
	EmployeeService empService;
	
	@GetMapping("/")
	public String displayHomePage(Model model) throws JsonProcessingException {
		
		model.addAttribute("versionNumber", version);
		
		List<Project> projects = proService.getAll();
		model.addAttribute("projectsList", projects);
		
		List<ChartData> projectData = proService.getProjectStatus();

		// Convert project Data to json structure for use in javascript
		ObjectMapper objectMapper = new ObjectMapper();
		String jsonString = objectMapper.writeValueAsString(projectData);
		model.addAttribute("projectStatusCount", jsonString);
		
		List<EmployeeProject> employeesProjectCount = empService.getEmployeeProjects();
		model.addAttribute("employeesListProjectsCount", employeesProjectCount);
		return "main/home";
	}

}
