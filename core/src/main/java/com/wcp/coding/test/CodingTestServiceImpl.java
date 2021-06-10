package com.wcp.coding.test;

import com.wcp.coding.inputFile.CodeInputFile;
import com.wcp.coding.inputFile.CodeInputFileRepository;
import com.wcp.coding.inputFile.CodeInputFileService;
import com.wcp.coding.room.CodingRoom;
import com.wcp.coding.room.CodingRoomDto;
import com.wcp.coding.room.CodingRoomRepository;
import com.wcp.coding.room.CodingRoomService;
import com.wcp.mapper.CodingRoomMapper;
import com.wcp.mapper.CodingTestMapper;
import com.wcp.page.PageCalculator;
import com.wcp.page.PageCount;
import com.wcp.page.PageInfo;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CodingTestServiceImpl implements CodingTestService{

    private final Logger log = LoggerFactory.getLogger(CodingTestServiceImpl.class);
    private final CodeInputFileService codeInputFileService;
    private final CodeInputFileRepository codeInputFileRepository;
    private final PageCalculator pageCalculator;

    private final CodingTestRepository codingTestRepository;
    private final CodingRoomRepository codingRoomRepository;


    @Override
    public void registerContent(MultiPartDto multiPartDto, String postId) throws Exception {
        CodingTest codingTest = CodingTestMapper.INSTANCE.toEntity(multiPartDto);
        MultipartFile file = multiPartDto.getFile();
        if(file == null || file.isEmpty()) {
            log.error("Only one file must be attached.");
            throw new FileUploadException();
        }
        CodeInputFile codeInputFile = codeInputFileService.multiPartToEntity(file);
        registerContent(codingTest, codeInputFile, postId);
    }

    @Transactional
    public void registerContent(CodingTest codingTest, CodeInputFile codeInputFile, String postId){
        if (StringUtils.isEmpty(postId) || !StringUtils.isNumeric(postId)) {
            throw new IllegalArgumentException("id should not be empty or String. Please Check postId : "+ postId);
        }
        // CodingTest 등록
        CodingRoom codingRoom = codingRoomRepository.getOne(Long.valueOf(postId));
        codingTest.setCodingRoom(codingRoom);
        codingTestRepository.save(codingTest);

        //CodeInputFile 등록
        codeInputFile.setCodingTest(codingTest);
        codeInputFileRepository.save(codeInputFile);
    }


    @Override
    public CodingTestDto save(CodingTestDto dto){
        CodingTest codingTest = CodingTestMapper.INSTANCE.toEntity(dto);
        codingTestRepository.save(codingTest);
        return dto;
    }


    @Override
    public List<CodingTestDto> fetchByPage(String currentPage) {
        if (StringUtils.isEmpty(currentPage) || !StringUtils.isNumeric(currentPage)) {
            throw new IllegalArgumentException("id should not be empty or String. Please Check currentPage : "+ currentPage);
        }
        List<CodingTest> codingTests = fetchByPage(Integer.valueOf(currentPage));
        List<CodingTestDto> dtos = new ArrayList<>();
        codingTests.forEach(v -> {
            dtos.add(
                    CodingTestMapper.INSTANCE.toDto(v)
            );
        });
        return dtos;
    }

    @Override
    public PageInfo fetchPageList(String currentPage) {
        if (StringUtils.isEmpty(currentPage) || !StringUtils.isNumeric(currentPage)) {
            throw new IllegalArgumentException("currentPage should not be empty or String. Please Check currentPage : "+ currentPage);
        }
        PageInfo pageInfo = PageInfo.of()
                .setCurrentPage(Integer.valueOf(currentPage))
                .setTotalPostCount(count());
        return pageCalculator.fetchPageList(pageInfo, PageCount.CODING_TEST);
    }

    @Override
    public List<CodingTest> fetchByPage(int currentPage) {
        Page<CodingTest> codingTestPage = codingTestRepository
                .findAll(PageRequest
                        .of(currentPage - 1, PageCount.CODING_TEST.getPostCount(), Sort.by(Sort.Direction.ASC, "key")));
        return codingTestPage.getContent();
    }


    @Override
    public CodingTestDto fetchById(String id){
        if (StringUtils.isEmpty(id) || !StringUtils.isNumeric(id)) {
            throw new IllegalArgumentException("id should not be empty or String. Please Check Id : "+ id);
        }
        CodingTest codingTest = fetchById(Long.valueOf(id));
        return CodingTestMapper.INSTANCE.toDto(codingTest);
    }

    public CodingTest fetchById(Long id) {
        return codingTestRepository.findById(id).get();
    }

    @Override
    public List<CodingTestDto> fetchAll() {
        List<CodingTest> codingTests = codingTestRepository.findAll();
        List<CodingTestDto> dtos = new ArrayList<>();
        codingTests.forEach(v -> {
            dtos.add(
                    CodingTestMapper.INSTANCE.toDto(v)
            );
        });
        return dtos;
    }

    @Override
    @Transactional
    public CodingTestDto update(CodingTestDto dto) {
        String id = dto.getKey();
        if (StringUtils.isEmpty(id) || !StringUtils.isNumeric(id)) {
            throw new IllegalArgumentException("id should not be empty or String. Please Check Id : "+ id);
        }
        CodingTest codingTest = fetchById(Long.valueOf(id));
        CodingTestMapper.INSTANCE.updateFromDto(dto, codingTest);
        return dto;
    }

    @Override
    public CodingTestDto delete(CodingTestDto dto) {
        CodingTest codingTest = CodingTestMapper.INSTANCE.toEntity(dto);
        codingTestRepository.delete(codingTest);
        return dto;
    }

    @Override
    public void deleteById(String id){
        if (StringUtils.isEmpty(id) || !StringUtils.isNumeric(id)) {
            throw new IllegalArgumentException("id should not be empty or String. Please Check Id : "+ id);
        }
        deleteById(Long.valueOf(id));
    }

    private void deleteById(Long id) {
        codingTestRepository.deleteById(id);
    }

    @Override
    public Long count() {
        return codingTestRepository.count();
    }
}
