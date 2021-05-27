package com.wcp.coding.board;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wcp.coding.CodingBoardService;
import com.wcp.page.PageInfo;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(value = "/wcp/coding/board")
@RequiredArgsConstructor
public class CodingBoardController {

    private final Logger log = LoggerFactory.getLogger(this.getClass());
    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    private final CodingBoardService codingBoardService;

    @RequestMapping(value = "/", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> save(HttpServletRequest req,
                                            HttpServletResponse res,
                                            Principal principal,
                                            @RequestBody CodingBoard codingBoard)
    {
        try{
            codingBoard = codingBoardService.saveCodingPost(codingBoard, principal.getName());
            return new ResponseEntity<String>(gson.toJson(codingBoard.toMapForOpen()), HttpStatus.OK);
        }catch (Throwable t){
            t.printStackTrace();
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/{postId:[0-9]+}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> fetchById(HttpServletRequest req,
                                            HttpServletResponse res,
                                            @PathVariable("postId") String postId)
    {
        try{
            CodingBoardDto codingBoardDto = codingBoardService.fetchCodingPostById(postId);
            return new ResponseEntity<String>(gson.toJson(codingBoardDto), HttpStatus.OK);
        }catch (Throwable t){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/page/{pageNm:[0-9]+}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> fetchByPage(HttpServletRequest req,
                                            HttpServletResponse res,
                                            @PathVariable("pageNm") String pageNm)
    {
        try{
            List<CodingBoard> codingBoards = codingBoardService.fetchByCodingBoardPage(pageNm);
            return new ResponseEntity<String>(gson.toJson(codingBoards), HttpStatus.OK);
        }catch (Throwable t){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/page/range/{pageNm:[0-9]+}", method = RequestMethod.GET, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> count(HttpServletRequest req,
                                        HttpServletResponse res,
                                        @PathVariable("pageNm") String pageNm)
    {
        try{
            PageInfo pageInfo = codingBoardService.fetchCodingBoardPageList(pageNm);
            Map<String, Object> stringObjectMap = pageInfo.parsePageRangeToMap();
            return new ResponseEntity<String>(gson.toJson(stringObjectMap),HttpStatus.OK);
        }catch (Throwable t){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/all", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> fetchAll(HttpServletRequest req,
                                              HttpServletResponse res)
    {
        try{
            List<CodingBoard> codingBoards = codingBoardService.fetchAllCodingBoard();
            return new ResponseEntity<String>(gson.toJson(codingBoards), HttpStatus.OK);
        }catch (Throwable t){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> update(HttpServletRequest req,
                                           HttpServletResponse res,
                                         @RequestBody CodingBoard codingBoard)
    {
        try{
            codingBoard = codingBoardService.updateCodingBoard(codingBoard);
            return new ResponseEntity<String>(gson.toJson(codingBoard.toMapForOpen()), HttpStatus.OK);
        }catch (Throwable t){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/del", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> delete(HttpServletRequest req,
                                         HttpServletResponse res,
                                         @RequestBody CodingBoard codingBoard)
    {
        try{
            codingBoard = codingBoardService.deleteCodingBoard(codingBoard);
            return new ResponseEntity<String>(gson.toJson(codingBoard.toMapForOpen()), HttpStatus.OK);
        }catch (Throwable t){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/del/{postId:[0-9]+}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> delete(HttpServletRequest req,
                                         HttpServletResponse res,
                                         @PathVariable("postId") String postId)
    {
        try{
            codingBoardService.deleteCodingBoardById(postId);
            return new ResponseEntity<String>(HttpStatus.OK);
        }catch (Throwable t){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @RequestMapping(value = "/cnt", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    @ResponseBody
    public ResponseEntity<String> count(HttpServletRequest req,
                                         HttpServletResponse res)
    {
        try{
            Long postCnt = codingBoardService.codingBoardCount();
            return new ResponseEntity<String>(postCnt.toString(),HttpStatus.OK);
        }catch (Throwable t){
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
