package com.example.Personnel.Management.Service.Impl;

import com.example.Personnel.Management.DTO.TextDTO;
import com.example.Personnel.Management.Entity.Text;
import com.example.Personnel.Management.Repository.TextRepository;
import com.example.Personnel.Management.Service.TextService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TextServiceImpl implements TextService {

    private final TextRepository textRepository;

    @Autowired
    public TextServiceImpl(TextRepository textRepository) {
        this.textRepository = textRepository;
    }

    @Override
    public List<TextDTO> getAllTexts() {
        return textRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public TextDTO createText(TextDTO textDTO) {
        Text text = new Text();
        text.setText(textDTO.getText());
        text.setCreatedAt(textDTO.getCreatedAt() != null ? textDTO.getCreatedAt() : java.time.LocalDateTime.now());
        Text savedText = textRepository.save(text);
        return convertToDTO(savedText);
    }

    @Override
    public void deleteText(Long id) {
        if (textRepository.existsById(id)) {
            textRepository.deleteById(id);
        } else {
            throw new RuntimeException("Không tìm thấy bản ghi có id: " + id);
        }
    }

    private TextDTO convertToDTO(Text text) {
        return new TextDTO(text.getText(), text.getCreatedAt());
    }
}
