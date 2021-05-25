package com.ngu.demo.restapi;

import com.ngu.demo.model.dto.DataDto;
import com.ngu.demo.model.entity.Datanya;
import com.ngu.demo.repository.DataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/upload")
public class ApiImage {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public DataDto editSave(@RequestPart(value = "datanya", required = true) DataDto dataDto,
                             @RequestPart(value = "file", required = true) MultipartFile file) throws Exception {

        Datanya datanya = modelMapper.map(dataDto, Datanya.class);
        String userFolderPath = "D:/img/";
        Path path = Paths.get(userFolderPath);
        Path filePath = path.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        datanya.setFile(file.getOriginalFilename());
        datanya = dataRepository.save(datanya);
        DataDto datanyaDtoDB = mapDataToDataDto(datanya);
        return datanyaDtoDB;
    }

    private DataDto mapDataToDataDto(Datanya data) {
        DataDto dataDto = modelMapper.map(data, DataDto.class);
        return dataDto;
    }

}

