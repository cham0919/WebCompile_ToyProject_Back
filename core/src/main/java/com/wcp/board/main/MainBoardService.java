package com.wcp.board.main;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainBoardService {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private MainBoardManager mainBoardManager;

    public void savePost(MainBoard mainBoard){
        mainBoardManager.savePost(mainBoard);
    }

}
