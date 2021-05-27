package com.ngu.demo.restapi;

import com.ngu.demo.model.dto.DataDto;
import com.ngu.demo.model.entity.Datanya;
import com.ngu.demo.repository.DataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/data")
@CrossOrigin(origins = "http://localhost:3000")
public class ApiData {

    @Autowired
    private DataRepository dataRepository;

    @Autowired
    private ModelMapper modelMapper;

    public ApiData(DataRepository dataRepository) {
        this.dataRepository = dataRepository;
    }

    @GetMapping()
    public List<DataDto> getListData() {
        List<Datanya> dataList = dataRepository.findAll();
        List<DataDto> dataDtos =
                dataList.stream()
                        .map(data -> mapDataToDataDto(data))
                        .collect(Collectors.toList());
        return dataDtos;
    }

    private DataDto mapDataToDataDto(Datanya data) {
        DataDto dataDto = modelMapper.map(data, DataDto.class);
        return dataDto;
    }

    @GetMapping("/getFoto/{id}")
    public byte[] getFoto(@PathVariable Integer id) throws IOException {
        Datanya datanya = dataRepository.findById(id).get();
        String userFolderPath = "D:/img/";
        String pathFile = userFolderPath + datanya.getFile();
        Path paths = Paths.get(pathFile);
        byte[] foto = Files.readAllBytes(paths);
        return foto;
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
    @PostMapping()
    public DataDto saveData(@RequestBody DataDto dataDto) throws Exception {
        Datanya datanya = modelMapper.map(dataDto, Datanya.class);
//        String userFolderPath = "C:/Users/Lenovo/IMAGE/";
//        Path path = Paths.get(userFolderPath);
//        Path filePath = path.resolve(file.getOriginalFilename());
//        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
//        input.setFile(file.getOriginalFilename());
        datanya = dataRepository.save(datanya);
        DataDto dataDto1 = mapDataToDataDto(datanya);
        return dataDto1;
    }

    @DeleteMapping
    public void del(){
        dataRepository.deleteAll();
    }

}

