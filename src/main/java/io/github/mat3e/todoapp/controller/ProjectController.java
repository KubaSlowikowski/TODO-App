package io.github.mat3e.todoapp.controller;

import io.github.mat3e.todoapp.logic.ProjectService;
import io.github.mat3e.todoapp.model.Project;
import io.github.mat3e.todoapp.model.ProjectStep;
import io.github.mat3e.todoapp.model.projection.ProjectWriteModel;
import io.micrometer.core.annotation.Timed;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

@PreAuthorize("hasRole('ROLE_ADMIN')")
//@PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
@Controller
@RequestMapping("/projects")
class ProjectController {
    private final ProjectService service;

    ProjectController(final ProjectService service) {
        this.service = service;
    }

    @GetMapping
    String showProjects(Model model, Authentication auth) {
//        if (auth!=null && auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            model.addAttribute("project", new ProjectWriteModel());
            return "projects"; //nazwa MVC View
//        }
//        return "index";
    }

    @PostMapping
    String addProject(
            @ModelAttribute("project") @Valid ProjectWriteModel current,
            BindingResult bindingResult,
            Model model
    ) {
        if(bindingResult.hasErrors()) {
            return "projects";
        }
        service.save(current);
        model.addAttribute("project", new ProjectWriteModel());
        model.addAttribute("projects", getProjects()); //umożliwia zaktualizowanie listy projektów bez konieczności odświeżania strony
        model.addAttribute("message", "Dodano projekt!");
        return "projects";
    }

    @PostMapping(params = "addStep") //reakcja na przycisk
    String addProjectStep(@ModelAttribute("project") ProjectWriteModel current) { // @ModelAttribute - dla tego modelu ('project') my coś zmieniamy
        current.getSteps().add(new ProjectStep());
        return "projects";
    }

    @Timed(value = "project.create.group", histogram = true, percentiles = {0.5, 0.95, 0.99}) //wszystkie metody mające tą adnotację, wysyłają dodatkowe informacje
    @PostMapping("/{id}")
    String createGroup(
            @ModelAttribute("project") ProjectWriteModel current,
            Model model,
            @PathVariable int id,
            @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm") LocalDateTime deadline //@DateTimeFormat <- format z jakim ma przychodzic data
    ) {
        try {
            service.createGroup(id,deadline);
            model.addAttribute("message","Dodano grupę!");
        } catch (IllegalStateException | IllegalArgumentException e) {
            model.addAttribute("message","Błąd podczas tworzenia grupy!");
        }
        return "projects";
    }

    @ModelAttribute("projects")
    List<Project> getProjects() {
        return service.findAll();
    }
}