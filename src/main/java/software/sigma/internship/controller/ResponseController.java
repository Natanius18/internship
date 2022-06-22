package software.sigma.internship.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import software.sigma.internship.dto.ResponseDto;
import software.sigma.internship.dto.StudentDto;
import software.sigma.internship.service.ResponseService;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/v1/responses")
public class ResponseController {
    private ResponseService service;

    @GetMapping(path = "/all")
    public List<ResponseDto> fetchList(){
        return service.findAll();
    }

    @GetMapping()
    public List<ResponseDto> fetchResponsesByStudent(@RequestBody StudentDto studentDto){
        return service.findByStudent(studentDto);
    }

    @GetMapping("/{id}")
    public ResponseDto fetch(@PathVariable Long id){
        return service.findById(id);
    }

    @PostMapping
    public ResponseDto save(@RequestBody ResponseDto response){
        return service.save(response);
    }

}