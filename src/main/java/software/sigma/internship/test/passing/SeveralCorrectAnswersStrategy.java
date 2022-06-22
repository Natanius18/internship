package software.sigma.internship.test.passing;

import software.sigma.internship.dto.AnswerDto;
import software.sigma.internship.dto.QuestionDto;

import java.util.List;

public class SeveralCorrectAnswersStrategy implements CounterStrategy {
    @Override
    public float count(QuestionDto questionDto, List<AnswerDto> response) {
        float score = 0;
        List<AnswerDto> rightAnswers = questionDto.getAnswers();
        for (AnswerDto answer : rightAnswers) {
            if (answer.isCorrect() && response.contains(answer) ||
                    !answer.isCorrect() && !response.contains(answer)) {
                score++;
            }
        }
        return score / rightAnswers.size();
    }
}