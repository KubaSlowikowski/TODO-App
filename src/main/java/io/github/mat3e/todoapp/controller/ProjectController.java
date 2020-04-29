package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.logic.ProjectService;
import io.github.mat3e.todoapp.model.ProjectStep;
import io.github.mat3e.todoapp.model.projection.ProjectWriteModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/projects")
class ProjectController {
    private final ProjectService service;

    ProjectController(final ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model) {
        model.addAttribute("project", new ProjectWriteModel());
        return "projects"; //nazwa MVC View
    }

    @PostMapping
    String addProject(@ModelAttribute("project") ProjectWriteModel current, Model model) {
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("message", "Dodano projekt!");
        return "projects";
    }

    @PostMapping(params = "addStep") //reakcja na przycisk
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) { // @ModelAttribute - dla tego modelu ('project') my coś zmieniamy
        current.getSteps().add(new ProjectStep());
        return "projects";
    }
}