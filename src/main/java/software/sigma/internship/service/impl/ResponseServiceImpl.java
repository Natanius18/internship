package software.sigma.internship.service.impl;

import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import software.sigma.internship.dto.AnswerDto;
import software.sigma.internship.dto.ResponseDto;
import software.sigma.internship.dto.StudentDto;
import software.sigma.internship.dto.TestDto;
import software.sigma.internship.entity.Answer;
import software.sigma.internship.entity.Response;
import software.sigma.internship.entity.Student;
import software.sigma.internship.entity.Test;
import software.sigma.internship.repo.AnswerRepository;
import software.sigma.internship.repo.ResponseRepository;
import software.sigma.internship.repo.TestRepository;
import software.sigma.internship.service.ResponseService;
import software.sigma.internship.test.passing.ScoreCounter;
import software.sigma.internship.validator.exception.AnswerNotFoundException;
import software.sigma.internship.validator.exception.TestNotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ResponseServiceImpl implements ResponseService {
    private final ResponseRepository responseRepository;
    private final AnswerRepository answerRepository;
    private final TestRepository testRepository;
    private final ModelMapper mapper;
    private final ScoreCounter scoreCounter;

    @Override
    public List<ResponseDto> findAll() {
        List<Response> responses = responseRepository.findAll();
        return responses.stream()
                .map(response -> {
                    ResponseDto responseDto = mapper.map(response, ResponseDto.class);
                    responseDto.setAnswers(null);
                    responseDto.getTest().setQuestions(null);
                    return responseDto;
                }).collect(Collectors.toList());
    }

    @Override
    public ResponseDto findById(Long id) {
        Response response = responseRepository.findById(id).orElseThrow(() -> new TestNotFoundException(id));
        return mapper.map(response, ResponseDto.class);
    }

    @Override
    public List<ResponseDto> findByStudent(StudentDto studentDto) {
        List<Response> responses = responseRepository.findAllByStudent(mapper.map(studentDto, Student.class));
        return responses.stream()
                .map(response -> {
                    ResponseDto responseDto = mapper.map(response, ResponseDto.class);
                    responseDto.getTest().setQuestions(null);
                    responseDto.setStudent(null);
                    return responseDto;
                }).collect(Collectors.toList());
    }

    @Override
    public ResponseDto save(ResponseDto response) {
        Long testId = response.getTest().getId();
        Test test = testRepository.findById(testId).orElseThrow(() -> new TestNotFoundException(testId));
        response.setTest(mapper.map(test, TestDto.class));

        response.setNumberOfTry(getNumberOfNewTry(response));
        response.setAnswers(getAnswerDtoList(response));

        response.setResult(scoreCounter.countResult(response));

        Response newResponse = responseRepository.save(mapper.map(response, Response.class));
        return mapToReturnResponseDto(newResponse);
    }

    @Override
    public void deleteById(Long id) {
        responseRepository.deleteById(id);
    }

    private ResponseDto mapToReturnResponseDto(Response newResponse) {
        ResponseDto responseDto = mapper.map(newResponse, ResponseDto.class);
        responseDto.setTest(null);
        responseDto.setAnswers(null);
        return responseDto;
    }

    private List<AnswerDto> getAnswerDtoList(ResponseDto response) {
        return response.getAnswers()
                .stream()
                .map(answerDto -> {
                    Answer answer = answerRepository.findById(answerDto.getId())
                            .orElseThrow(() -> new AnswerNotFoundException(answerDto.getId()));
                    return mapper.map(answer, AnswerDto.class);
                })
                .collect(Collectors.toList());
    }

    private int getNumberOfNewTry(ResponseDto response) {
        return responseRepository
                .getFirstByStudentIdOrderByNumberOfTryDesc(response.getStudent().getId()).orElse(new Response())
                .getNumberOfTry() + 1;
    }
}