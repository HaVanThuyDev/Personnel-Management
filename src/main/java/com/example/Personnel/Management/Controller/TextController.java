package com.example.Personnel.Management.Controller;

import com.example.Personnel.Management.DTO.TextDTO;
import com.example.Personnel.Management.Service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/texts")
public class TextController {

    private final TextService textService;

    @Autowired
    public TextController(TextService textService) {
        this.textService = textService;
    }

    @GetMapping
    public List<TextDTO> getAllTexts() {
        return textService.getAllTexts();
    }

    @PostMapping
    public TextDTO createText(@RequestBody TextDTO textDTO) {
        return textService.createText(textDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteText(@PathVariable Long id) {
        textService.deleteText(id);
    }
}
