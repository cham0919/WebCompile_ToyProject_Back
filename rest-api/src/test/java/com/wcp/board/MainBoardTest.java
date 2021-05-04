package com.wcp.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wcp.board.main.MainBoard;
import com.wcp.board.main.MainBoardService;
import com.wcp.board.page.PageInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

public class MainBoardTest extends BoardTest {

    @Autowired
    MainBoardService mainBoardService;

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();


    @BeforeEach
    public void init() {
        MainBoard mainBoard = new MainBoard();
        mainBoard.setReply("1");
        mainBoard.setUserKey("test");
        mainBoard.setTitle("test");
        mainBoard.setContent("test");
        mainBoard.setCategory("test");
        mainBoard.setCount(1l);
        mainBoardService.save(mainBoard);

        this.postId = "1";
        this.currentPage = "1";
    }


    @Test
    public void fetchByIdTest(){
        MainBoard mainBoard = mainBoardService.fetchById(postId);
    }

    @Test
    public void fetchByRangeTest(){
        PageInfo pageInfo = mainBoardService.getPageList(currentPage);
        Map<String, Object> resultMap = pageInfo.parsePageRangeToMap();
    }

    @Test
    public void insertTest(){
        MainBoard mainBoard = new MainBoard();
        mainBoard.setReply("1");
        mainBoard.setUserKey("test");
        mainBoard.setTitle("test");
        mainBoard.setContent("test");
        mainBoard.setCategory("test");
        mainBoardService.save(mainBoard);
    }

    @Test
    public void modifyTest(){
        MainBoard mainBoard = new MainBoard();
        mainBoardService.update(mainBoard);
    }

    @Test
    public void deleteByIdTest(){
        mainBoardService.deleteById(postId);
    }
}
