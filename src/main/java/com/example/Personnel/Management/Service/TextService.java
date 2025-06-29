package com.example.Personnel.Management.Service;

import com.example.Personnel.Management.DTO.TextDTO;
import java.util.List;

public interface TextService {
    List<TextDTO> getAllTexts();
    TextDTO createText(TextDTO textDTO);
    void deleteText(Long id);
}
