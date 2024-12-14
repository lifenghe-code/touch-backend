package com.lfh.touch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfh.touch.model.domain.ChatDetail;
import com.lfh.touch.service.ChatDetailService;
import org.springframework.stereotype.Service;
import com.lfh.touch.mapper.ChatDetailMapper;

import javax.annotation.Resource;

/**
* @author li_fe
* @description 针对表【chat_detail(聊天记录表)】的数据库操作Service实现
* @createDate 2024-12-03 19:25:57
*/
@Service
public class ChatDetailServiceImpl extends ServiceImpl<ChatDetailMapper, ChatDetail>
    implements ChatDetailService{

}




