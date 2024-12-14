package com.lfh.touch.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfh.touch.model.domain.Friendship;
import com.lfh.touch.service.FriendshipService;
import com.lfh.touch.mapper.FriendshipMapper;
import org.springframework.stereotype.Service;

/**
* @author li_fe
* @description 针对表【friendship(好友申请表)】的数据库操作Service实现
* @createDate 2024-12-04 18:43:50
*/
@Service
public class FriendshipServiceImpl extends ServiceImpl<FriendshipMapper, Friendship>
    implements FriendshipService{

}




