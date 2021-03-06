package com.wcp.coding.submit;


import com.wcp.coding.test.CodingTest;
import com.wcp.coding.test.CodingTestRepository;
import com.wcp.mapper.SubmitHistoryMapper;
import com.wcp.user.User;
import com.wcp.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubmitHistoryServiceImpl implements SubmitHistoryService{

    private final Logger log = LoggerFactory.getLogger(SubmitHistoryServiceImpl.class);

    private final SubmitHistoryRepository submitHistoryRepository;
    private final CodingTestRepository codingTestRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public SubmitHistoryDto registerSubmitHistory(SubmitHistoryDto dto, String postId, String userKey){
        SubmitHistory submitHistory = SubmitHistoryMapper.SUBMIT_HISTORY_MAPPER.toEntity(dto);
        User user = userRepository.getOne(Long.valueOf(userKey));
        CodingTest codingTest =  codingTestRepository.getOne(Long.valueOf(postId));
        submitHistory.setUser(user)
                .setCodingTest(codingTest);
        submitHistoryRepository.save(submitHistory);
        return dto;
    }

    @Override
    public SubmitHistoryDto save(SubmitHistoryDto dto) {
        SubmitHistory entity = SubmitHistoryMapper.SUBMIT_HISTORY_MAPPER.toEntity(dto);
        submitHistoryRepository.save(entity);
        return dto;
    }

    @Override
    public SubmitHistoryDto fetchById(String id) {
        SubmitHistory entity = submitHistoryRepository.findById(Long.valueOf(id)).get();
        return SubmitHistoryMapper.SUBMIT_HISTORY_MAPPER.toDto(entity);
    }

    @Override
    public List<SubmitHistoryDto> fetchAll() {
       List<SubmitHistory> entitys =  submitHistoryRepository.findAll();
       List<SubmitHistoryDto> dtos = new ArrayList<>();
       entitys.forEach(v -> {
            dtos.add(
                    SubmitHistoryMapper.SUBMIT_HISTORY_MAPPER.toDto(v)
            );
        });
        return dtos;
    }

    @Override
    @Transactional
    public SubmitHistoryDto update(SubmitHistoryDto dto) {
        SubmitHistory entity = submitHistoryRepository.findById(Long.valueOf(dto.getKey())).get();
        SubmitHistoryMapper.SUBMIT_HISTORY_MAPPER.updateFromDto(dto, entity);
        return dto;
    }

    @Override
    public SubmitHistoryDto delete(SubmitHistoryDto dto) {
        SubmitHistory submitHistory = SubmitHistoryMapper.SUBMIT_HISTORY_MAPPER.toEntity(dto);
        submitHistoryRepository.delete(submitHistory);
        return dto;
    }

    @Override
    public void deleteById(String id) {
        submitHistoryRepository.deleteById(Long.valueOf(id));
    }

    @Override
    public Long count() {
        return submitHistoryRepository.count();
    }
}
