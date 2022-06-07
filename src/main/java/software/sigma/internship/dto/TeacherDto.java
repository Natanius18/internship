package software.sigma.internship.dto;

import lombok.Data;
import software.sigma.internship.entity.Teacher;

import java.util.List;


@Data
public class TeacherDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Teacher.Position position;
    private List<TestDto> tests;
}