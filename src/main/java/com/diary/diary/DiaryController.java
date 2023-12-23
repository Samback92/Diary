package com.diary.diary;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class DiaryController {

    private final DiaryRepository repository;

    public DiaryController(DiaryRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/")
    public String index(Model model) {
        List<Diary> diaries = repository.findAllByDateBefore(LocalDate.now());
        diaries = diaries.stream()
            .sorted(Comparator.comparing(Diary::getDate).reversed())
            .collect(Collectors.toList());
        model.addAttribute("diaries", diaries);
        model.addAttribute("diary", new Diary());
        System.out.println("Diaries: " + diaries.size());
        return "index";
    }


    @PostMapping("/new-post")
    public String addPost(@ModelAttribute Diary diary) {
        repository.save(diary);
        System.out.println("Ny post från " + diary);
        return "redirect:/";
    }

    // För att ta bort från indexsidan och omdirigera tillbaka till indexsidan
    @PostMapping("/deleteFromIndex/{id}")
    public String deletePostFromIndex(@PathVariable Long id) {
        System.out.println("delete mapping from index " + id);
        repository.deleteById(id);
        return "redirect:/";
    }

    // För att ta bort från postssidan och omdirigera tillbaka till postssidan
    @PostMapping("/deleteFromPosts/{id}")
    public String deletePostFromPosts(@PathVariable Long id) {
        System.out.println("delete mapping from posts " + id);
        repository.deleteById(id);
        return "redirect:/posts";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") long id, Model model) {
        Diary diary = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid diary Id:" + id));

        model.addAttribute("diary", diary);
        return "edit";
    }

    @PostMapping("/edit/{id}")
    public String updateDiary(@PathVariable("id") long id, @ModelAttribute Diary diary, Model model) {
        diary.setId(id);
        repository.save(diary);
        model.addAttribute("diaries", repository.findAll());
        System.out.println("uppdate mapping " + diary.getContent());
        return "redirect:/posts";
    }

    @GetMapping("/posts")
    public String showAllPosts(Model model) {
        List<Diary> diaries = (List<Diary>) repository.findAllByOrderByDateDesc();
        model.addAttribute("diaries", diaries);
        return "posts";
    }

}

