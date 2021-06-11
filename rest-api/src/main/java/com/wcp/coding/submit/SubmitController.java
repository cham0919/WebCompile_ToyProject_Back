package com.wcp.coding.submit;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "/wcp/coding/submit")
@RequiredArgsConstructor
public class SubmitController {

    private final Logger log = LoggerFactory.getLogger(SubmitController.class);
    private final Gson gson = new GsonBuilder().setPrettyPrinting()
            .disableHtmlEscaping()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
            .create();

    private final SubmitHistoryServiceImpl submitHistoryService;

    @RequestMapping(value = "/{postId:[0-9]+}", method = RequestMethod.POST, produces = "application/json; charset=utf-8")
    public ResponseEntity<String> registerSubmitHistory(HttpServletRequest req,
                                                HttpServletResponse res,
                                                 @PathVariable("postId") String postId,
                                                 @RequestBody SubmitHistoryDto dto)
    {
        try{
            String userKey = SecurityContextHolder.getContext().getAuthentication().getName();
            dto = submitHistoryService.registerSubmitHistory(dto, postId, userKey);
            return new ResponseEntity<String>(gson.toJson(dto), HttpStatus.OK);
        }catch (Throwable t){
            log.error("submission error",t);
            return new ResponseEntity<String>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
