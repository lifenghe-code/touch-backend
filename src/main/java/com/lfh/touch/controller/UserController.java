package com.lfh.touch.controller;

import cn.dev33.satoken.annotation.SaCheckLogin;
import com.lfh.touch.common.BaseResponse;
import com.lfh.touch.common.ErrorCode;
import com.lfh.touch.common.ResultUtils;
import com.lfh.touch.exception.BusinessException;
import com.lfh.touch.model.dto.user.*;
import com.lfh.touch.model.vo.UserLoginVO;
import com.lfh.touch.model.vo.UserVO;
import com.lfh.touch.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RequestMapping("user")
@RestController
@Slf4j
public class UserController {
    @Resource
    UserService userService;
    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String account = userRegisterRequest.getAccount();
        String password = userRegisterRequest.getPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(account, password, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(account, password, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<UserLoginVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String userAccount = userLoginRequest.getAccount();
        String userPassword = userLoginRequest.getPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        UserLoginVO userLoginVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(userLoginVO);
    }

    /**
     * 获取登录用户
     * @param request
     * @return
     */
    @SaCheckLogin
    @GetMapping("/get/login")
    public BaseResponse<UserVO> getLoginUser(HttpServletRequest request) {
        UserVO userVO = userService.getLoginUser(request);
        return ResultUtils.success(userVO);
    }

    /**
     * 查询用户
     * @param uid
     * @return
     */
    @SaCheckLogin
    @GetMapping("/get/{uid}")
    public BaseResponse<UserVO> getUserByUid(@PathVariable("uid") Integer uid) {
        UserVO userVO = userService.getUserByUid(uid);
        return ResultUtils.success(userVO);
    }

    /**
     * 信息更新
     * @param userUpdateMyRequest
     * @return
     */
    @SaCheckLogin
    @PostMapping("/update/my")
    public BaseResponse<Boolean> userUpdateMy( @RequestBody UserUpdateMyRequest userUpdateMyRequest) {

        boolean result = userService.userUpdate(userUpdateMyRequest);
        return ResultUtils.success(result);
    }

    /**
     * 推出登录
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {

        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 账号封禁
     * @param uid
     * @param disableTime
     * @return
     */
    @PostMapping("/disable")
    public BaseResponse<Boolean> userDisable(Integer uid,Integer disableTime) {
        userService.userDisable(uid, disableTime);
        return ResultUtils.success(true);
    }

    /**
     * 账号解封
     * @param uid
     * @return
     */
    @PostMapping("/untiedisable")
    public BaseResponse<Boolean> userUntieDisable(Integer uid) {
        userService.userUntieDisable(uid);
        return ResultUtils.success(true);
    }

    @PostMapping("/add-friend")
    public BaseResponse<Boolean> userAddFriend(@RequestBody UserAddFriendRequest userAddFriendRequest) {
        boolean b = userService.userAddFriend(userAddFriendRequest);
        return ResultUtils.success(b);
    }

    @PostMapping("/add-friend-handle")
    public BaseResponse<Boolean> userAddFriendHandle(@RequestBody UserAddFriendHandleRequest userAddFriendHandleRequest) {
        boolean b = userService.userAddFriendHandle(userAddFriendHandleRequest);
        return ResultUtils.success(b);
    }
    @GetMapping("/add-friend-request/{uid}")
    public BaseResponse<List<UserVO>> userAddFriendRequest(@PathVariable Integer uid){
        List<UserVO> addFriendRequest = userService.getAddFriendRequest(uid);
        return ResultUtils.success(addFriendRequest);
    }
}
