package com.ngu.demo.restapi;

import com.ngu.demo.model.dto.DataDto;
import com.ngu.demo.model.entity.Datanya;
import com.ngu.demo.repository.DataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.attribute.standard.Media;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@CrossOrigin("localhost:3001")
@RequestMapping("/data")
public class ApiData {

    private final DataRepository dataRepository;

    private final ModelMapper modelMapper;

    public ApiData(DataRepository dataRepository, ModelMapper modelMapper) {
        this.dataRepository = dataRepository;
        this.modelMapper = modelMapper;
    }

    @GetMapping()
    public List<DataDto> getListData() {
        List<Datanya> dataList = dataRepository.findAll();
        List<DataDto> dataDtoList =
                dataList.stream()
                        .map(data -> mapDataToDataDto(data))
                        .collect(Collectors.toList());
        return dataDtoList;
    }

    private DataDto mapDataToDataDto(Datanya datanya) {
        DataDto dataDto = modelMapper.map(datanya, DataDto.class);
        return dataDto;
    }

    @GetMapping("/getFoto/{id}")
    public String getFoto(@PathVariable Integer id) throws IOException {
        Datanya datanya = dataRepository.findById(id).get();
        String userFolderPath = "D:/img/";
        String pathFile = userFolderPath + datanya.getFile();
        Path paths = Paths.get(pathFile);
        byte[] photo = Files.readAllBytes(paths);
        String encodedString = Base64.getEncoder().encodeToString(photo);
        return encodedString;
    }

    //
//    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
//    public DataDto editSave(@RequestPart(value = "datanya", required = true) DataDto dataDto,
//                             @RequestPart(value = "file", required = true) MultipartFile file) throws Exception {
//
//        String userFolderPath = "D:/img/";
//        Path path = Paths.get(userFolderPath);
//        Path filePath = path.resolve(file.getOriginalFilename());
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        System.out.println("Upload file with size" + file.getSize() + " with name :  " + file.getOriginalFilename());
//
//        Datanya datanya = modelMapper.map(dataDto, Datanya.class);
//        datanya.setId(dataDto.getId());
//        //String pat = filePath.toUri().getPath();
//        datanya.setFile(file.getOriginalFilename());
//        DataDto datanyaDtoDB = mapDataToDataDto(datanya);
//        return datanyaDtoDB;
//    }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE, value = "/saveFile")
    public DataDto saveData(@RequestPart(value = "data", required = true) DataDto dataDto,
                            @RequestPart(value = "file", required = true) MultipartFile file) throws Exception {
        Datanya datanya = modelMapper.map(dataDto, Datanya.class);
        String userFolderPath = "D:/img/";
        Path path = Paths.get(userFolderPath);
        Path filePath = path.resolve(file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        datanya.setFile(file.getOriginalFilename());
        datanya = dataRepository.save(datanya);
        DataDto dataDto1 = mapDataToDataDto(datanya);
        return dataDto1;
    }

    @PostMapping("/save")
    public DataDto saveData(@RequestBody DataDto dataDto) {
        Datanya datanya = modelMapper.map(dataDto, Datanya.class);
        datanya = dataRepository.save(datanya);
        DataDto dataDto1 = mapDataToDataDto(datanya);
        return dataDto1;
    }




    @GetMapping("/{id}")
    public DataDto getData(@PathVariable Integer id) {
        Datanya data = dataRepository.findById(id).get();
        DataDto dataDto = new DataDto();
        modelMapper.map(data, dataDto);
        dataDto.setId(data.getId());
        return dataDto;
    }

    @DeleteMapping
    public void del(){
        dataRepository.deleteAll();
    }

}

