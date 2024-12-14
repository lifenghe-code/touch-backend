package com.lfh.touch.service.impl;
import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lfh.touch.common.ErrorCode;
import com.lfh.touch.common.ResultUtils;
import com.lfh.touch.constant.CommonConstant;
import com.lfh.touch.exception.BusinessException;
import com.lfh.touch.mapper.ChatMapper;
import com.lfh.touch.model.domain.Chat;
import com.lfh.touch.model.domain.Friendship;
import com.lfh.touch.model.domain.User;
import com.lfh.touch.model.dto.user.UserAddFriendHandleRequest;
import com.lfh.touch.model.dto.user.UserAddFriendRequest;
import com.lfh.touch.model.dto.user.UserQueryRequest;
import com.lfh.touch.model.dto.user.UserUpdateMyRequest;
import com.lfh.touch.model.enums.UserRoleEnum;
import com.lfh.touch.model.vo.UserLoginVO;
import com.lfh.touch.model.vo.UserVO;
import com.lfh.touch.service.UserService;
import com.lfh.touch.utils.SqlUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import com.lfh.touch.mapper.UserMapper;
import com.lfh.touch.mapper.FriendshipMapper;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.lfh.touch.constant.UserConstant.USER_LOGIN_STATE;

/**
* @author li_fe
* @description 针对表【user(用户表)】的数据库操作Service实现
* @createDate 2024-12-03 19:21:40
*/
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    /**
     * 盐值，混淆密码
     */
    public static final String SALT = "lfh";
    @Resource
    FriendshipMapper friendshipMapper;
    @Resource
    ChatMapper chatMapper;
    @Override
    public long userRegister(String account, String password, String checkPassword){
        // 1. 校验
        if (StringUtils.isAnyBlank(account, password, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (account.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (password.length() < 6 || checkPassword.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!password.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        synchronized (account.intern()) {
            // 账户不能重复
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("account", account);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");
            }
            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
            // 3. 插入数据
            User user = new User();
            user.setAccount(account);
            user.setPassword(encryptPassword);
            user.setNickname(account);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getUid();
        }
    }


    @Override
    public UserLoginVO userLogin(String account, String password, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(account, password)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (account.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (password.length() < 6) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + password).getBytes());
        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("account", account);
        queryWrapper.eq("password", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);
        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }
        StpUtil.checkDisable(user.getUid());
        // 3. 记录用户的登录态
        StpUtil.login(user.getUid());
        // 在登录时缓存 user 对象
        StpUtil.getSession().set(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }
    @Override
    public UserVO getLoginUser(HttpServletRequest request){
        // 先判断是否已登录
        // 获取当前会话是否已经登录，返回true=已登录，false=未登录
        User user= new User();
        try {
            user = (User) StpUtil.getSession().get(USER_LOGIN_STATE);
        }catch (Exception e){
            log.error(e.toString());
        }

//        Integer uid= user.getUid();
//        User newUser = this.getById(uid);
        return getUserVO(user);
    }
    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        // 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getUid() == null) {
            return null;
        }
        // 从数据库查询（追求性能的话可以注释，直接走缓存）
        long userId = currentUser.getUid();
        return this.getById(userId);
    }

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    @Override
    public boolean isAdmin(HttpServletRequest request) {
        // 仅管理员可查询
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) userObj;
        return isAdmin(user);
    }

    @Override
    public boolean isAdmin(User user) {
        return user != null && UserRoleEnum.ADMIN.getValue().equals(user.getRole());
    }

    /**
     * 用户注销
     *
     *
     */
    @Override
    public boolean userLogout(HttpServletRequest request) {
        // 当前会话注销登录
        Object attribute = request.getSession().getAttribute(USER_LOGIN_STATE);
        User user = (User) attribute;
        StpUtil.logout(user.getUid());
        boolean login = StpUtil.isLogin();
        if(!login){
            return true;
        }
        else {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"注销失败");
        }
    }

    @Override
    public boolean userUpdate(UserUpdateMyRequest userUpdateMyRequest) {
        User user = new User();
        BeanUtils.copyProperties(userUpdateMyRequest,user);
        boolean b = this.updateById(user);
        if(!b){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"更新失败");
        }
        return true;
    }

    /**
     * 账号封禁
     * @param uid
     * @param disableTIme
     * @return
     */
    @Override
    public void userDisable(Integer uid, Integer disableTIme) {
        // 先踢下线
        StpUtil.kickout(uid);
        User userById = this.getById(uid);
        userById.setStatus(1);
        this.updateById(userById);

        StpUtil.disable(uid, disableTIme);
    }



    @Override
    public void userUntieDisable(Integer uid) {
        User userById = this.getById(uid);
        userById.setStatus(0);
        this.updateById(userById);
        StpUtil.untieDisable(uid);
    }

    /**
     * 查询用户
     * @param userQueryRequest
     * @return
     */
    @Override
    public User queryUser(UserQueryRequest userQueryRequest) {
        if(userQueryRequest == null){
            ResultUtils.error(ErrorCode.PARAMS_ERROR);
        }
        QueryWrapper<User> queryWrapper = this.getQueryWrapper(userQueryRequest);
        User user = this.baseMapper.selectOne(queryWrapper);
        return user;
    }

    /**
     * 添加好友
     * @param userAddFriendRequest
     * @return
     */
    @Override
    public boolean userAddFriend(UserAddFriendRequest userAddFriendRequest){
        Integer senderUid = userAddFriendRequest.getSenderUid();
        Integer receiverId = userAddFriendRequest.getReceiverUid();

        // 添加数据库
        Friendship friendship = new Friendship();
        friendship.setSenderId(senderUid);
        friendship.setReceiverId(receiverId);
        friendship.setStatus(0);
        friendshipMapper.insert(friendship);
        return true;
    }

    /**
     * 处理添加好友请求
     *
     * @param userAddFriendHandleRequest
     * @return
     */
    @Override
    public boolean userAddFriendHandle(UserAddFriendHandleRequest userAddFriendHandleRequest) {
        if(userAddFriendHandleRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        // 通知对方结果
        Integer result = userAddFriendHandleRequest.getHandle();
        Integer senderUid = userAddFriendHandleRequest.getSenderUid();
        Integer receiverUid = userAddFriendHandleRequest.getReceiverUid();

        // 成功添加

        // 结果入库

        QueryWrapper<Friendship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("sender_id",senderUid);
        queryWrapper.eq("receiver_id",receiverUid);
        Friendship friendship = friendshipMapper.selectOne(queryWrapper);
        friendship.setStatus(result);
        int update = friendshipMapper.updateById(friendship);

        if(!(update>0)){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"入库失败");
        }
        Chat chat = new Chat();
        chat.setSenderUid(senderUid);
        chat.setReceiverUid(receiverUid);
        chatMapper.insert(chat);
        return true;
    }

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    @Override
    public UserVO getUserVO(User user){
        if (user == null) {
            return null;
        }
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) {
            return new ArrayList<>();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    @Override
    public UserLoginVO getLoginUserVO(User user) {
        if (user == null) {
            return null;
        }
        UserLoginVO userLoginVO = new UserLoginVO();
        BeanUtils.copyProperties(user, userLoginVO);
        return userLoginVO;
    }

    @Override
    public QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest) {
        if (userQueryRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        Integer uid = userQueryRequest.getUid();
        String nickName = userQueryRequest.getNickname();
        String userAccount = userQueryRequest.getAccount();
        String sortField = userQueryRequest.getSortField();
        String sortOrder = userQueryRequest.getSortOrder();
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(uid != null, "uid", uid);
        queryWrapper.like(StringUtils.isNotBlank(nickName), "nickname", nickName);
        queryWrapper.like(StringUtils.isNotBlank(userAccount), "account", userAccount);
        queryWrapper.orderBy(SqlUtils.validSortField(sortField), sortOrder.equals(CommonConstant.SORT_ORDER_ASC),
                sortField);
        return queryWrapper;
    }

    /**
     * 根据uid获取用户
     * @param uid
     * @return
     */
    @Override
    public UserVO getUserByUid(Integer uid){
        if(uid <0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User user = this.getById(uid);
        return this.getUserVO(user);
    }

    @Override
    public List<UserVO> getAddFriendRequest(Integer uid) {
        QueryWrapper<Friendship> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("receiver_id", uid);
        queryWrapper.eq("status", 0);
        List<Friendship> list = friendshipMapper.selectList(queryWrapper);
        // 提取每个人uid
        List<Integer> senderUids = list.stream().map(Friendship::getSenderId)
                .toList();
        List<User> users = this.baseMapper.selectBatchIds(senderUids);
        return getUserVO(users);
    }
}




