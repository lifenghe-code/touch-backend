package com.lfh.touch.mapper;

import com.lfh.touch.model.domain.ChatDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
* @author li_fe
* @description 针对表【chat_detail(聊天记录表)】的数据库操作Mapper
* @createDate 2024-12-04 18:40:17
* @Entity com.lfh.touch.model.domain.ChatDetail
*/

public interface ChatDetailMapper extends BaseMapper<ChatDetail> {

}




