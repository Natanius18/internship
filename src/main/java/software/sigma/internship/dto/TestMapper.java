package software.sigma.internship.dto;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.sigma.internship.entity.Question;
import software.sigma.internship.entity.Test;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class TestMapper {
    private final QuestionMapper questionMapper;

    public Test toEntity(TestDto dto) {
        Test entity = new Test();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setTeacher(dto.getTeacher());
        List<Question> questions = dto.getQuestions().stream().map(questionMapper::toEntity).collect(Collectors.toList());
        entity.setQuestions(questions);
        return entity;
    }

    public TestDto toDto(Test entity) {
        TestDto dto = new TestDto();
        dto.setId(entity.getId());
        dto.setName(entity.getName());
        entity.getTeacher().setTests(null);
        dto.setTeacher(entity.getTeacher());
        List<QuestionDto> questions = entity.getQuestions().stream().map(question -> {
            QuestionDto questionDto = questionMapper.toDto(question);
            questionDto.setTest(null);
            return questionDto;
        }).collect(Collectors.toList());
        dto.setQuestions(questions);
        return dto;
    }
}