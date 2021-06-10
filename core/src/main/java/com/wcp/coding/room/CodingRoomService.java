package com.wcp.coding.room;

import com.wcp.common.base.CRUDService;
import com.wcp.common.base.PageService;

public interface CodingRoomService extends CRUDService<CodingRoom, CodingRoomDto>, PageService<CodingRoom, CodingRoomDto> {


    CodingRoomDto save(CodingRoomDto dto, String userKey);




}
