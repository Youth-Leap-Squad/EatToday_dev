package com.eat.today.event.albti.command.application.service;

import com.eat.today.event.albti.command.application.entity.AlbtiSurvey;
import com.eat.today.event.albti.command.domain.repository.AlbtiSurveyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AlbtiSurveyQueryService {

    private final AlbtiSurveyRepository albtiSurveyRepository;

    public List<AlbtiSurvey> getSurveyList() {
        return albtiSurveyRepository.findAll();
    }
}