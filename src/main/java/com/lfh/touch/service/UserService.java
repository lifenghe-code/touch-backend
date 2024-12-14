package com.lfh.touch.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lfh.touch.model.domain.User;
import com.lfh.touch.model.dto.user.UserAddFriendHandleRequest;
import com.lfh.touch.model.dto.user.UserAddFriendRequest;
import com.lfh.touch.model.dto.user.UserQueryRequest;
import com.lfh.touch.model.dto.user.UserUpdateMyRequest;
import com.lfh.touch.model.vo.UserLoginVO;
import com.lfh.touch.model.vo.UserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
* @author li_fe
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2024-12-03 19:21:40
*/
public interface UserService extends IService<User> {
    /**
     * 用户注册
     *
     * @param account   用户账户
     * @param password  用户密码
     * @param checkPassword 校验密码
     * @return 新用户 id
     */
    long userRegister(String account, String password, String checkPassword);

    /**
     * 用户登录
     *
     * @param account  用户账户
     * @param password 用户密码
     * @param request
     * @return 脱敏后的用户信息
     */
    UserLoginVO userLogin(String account, String password, HttpServletRequest request);


    /**
     * 获取当前登录用户
     * @param request
     *
     * @return
     */
    UserVO getLoginUser(HttpServletRequest request);

    /**
     * 获取当前登录用户（允许未登录）
     *
     * @param request
     * @return
     */
    User getLoginUserPermitNull(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param request
     * @return
     */
    boolean isAdmin(HttpServletRequest request);

    /**
     * 是否为管理员
     *
     * @param user
     * @return
     */
    boolean isAdmin(User user);

    /**
     * 用户注销
     *
     * @param request
     * @return
     */
    boolean userLogout(HttpServletRequest request);

    /**
     * 用户信息更新
     *
     * @param userUpdateMyRequest
     * @return
     */
    boolean userUpdate(UserUpdateMyRequest userUpdateMyRequest);
    /**
     * 账号封禁
     * @param uid
     * @param disableTime 封禁时长
     */
    void userDisable(Integer uid, Integer disableTime);
    /**
     * 账号解封
     * @param uid
     */
    void userUntieDisable(Integer uid);

    /**
     * 查询用户
     * @param userQueryRequest
     * @return
     */
    User queryUser(UserQueryRequest userQueryRequest);

    /**
     * 发送添加朋友请求
     * @param userAddFriendRequest
     * @return
     */
    boolean userAddFriend(UserAddFriendRequest userAddFriendRequest);

    /**
     * @param userAddFriendHandleRequest
     * @return
     */
    boolean userAddFriendHandle(UserAddFriendHandleRequest userAddFriendHandleRequest);
    /**
     * 获取脱敏的已登录用户信息
     *
     * @return
     */
    UserLoginVO getLoginUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param user
     * @return
     */
    UserVO getUserVO(User user);

    /**
     * 获取脱敏的用户信息
     *
     * @param userList
     * @return
     */
    List<UserVO> getUserVO(List<User> userList);

    /**
     * 获取查询条件
     *
     * @param userQueryRequest
     * @return
     */
    QueryWrapper<User> getQueryWrapper(UserQueryRequest userQueryRequest);

    /**
     * 添加用户的签到记录
     * @param userId
     * @return
     */

    //boolean addUserSignIn(long userId);

    /**
     * 获取用户某个年份的签到记录
     *
     * @param userId 用户 id
     * @param year   年份（为空表示当前年份）
     * @return 签到记录映射
     */
    //List<Integer> getUserSignInRecord(long userId, Integer year);
    /**
     * 根据uid获取用户
     * @param uid
     * @return
     */
    UserVO getUserByUid(Integer uid);

    /**
     * 获取好友申请列表
     * @param uid
     * @return
     */
    List<UserVO> getAddFriendRequest(Integer uid);
}
