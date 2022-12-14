package com.xc.service.impl;


import com.xc.common.ServerResponse;

import com.xc.dao.UserBankMapper;

import com.xc.pojo.User;

import com.xc.pojo.UserBank;

import com.xc.service.IUserBankService;

import com.xc.service.IUserService;


import com.xc.utils.MessageUtils;
import com.xc.vo.user.UserBankInfoVO;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

 @Service("iUserBankService")
 public class UserBankServiceImpl implements IUserBankService {

   @Autowired
   UserBankMapper userBankMapper;

   @Autowired
   IUserService iUserService;



   public UserBank findUserBankByUserId(Integer userId) { return this.userBankMapper.findUserBankByUserId(userId); }

   public ServerResponse addBank(UserBank bank, HttpServletRequest request) {

     User user = this.iUserService.getCurrentUser(request);

     UserBank dbBank = this.userBankMapper.findUserBankByUserId(user.getId());

     if (dbBank != null) {
       return ServerResponse.createByErrorMsg(MessageUtils.get("bank.existed"));
     }
     UserBank userBank = new UserBank();

     userBank.setUserId(user.getId());

     userBank.setBankName(bank.getBankName());

     userBank.setBankNo(bank.getBankNo());

     userBank.setBankAddress(bank.getBankAddress());

     userBank.setBankImg(bank.getBankImg());

     userBank.setBankPhone(bank.getBankPhone());

     userBank.setAddTime(new Date());

     int insertCount = this.userBankMapper.insert(userBank);

     if (insertCount > 0) {
       return ServerResponse.createBySuccess(MessageUtils.get("bank.add.success"));

     }

     return ServerResponse.createByErrorMsg(MessageUtils.get("bank.add.fail"));

   }

   public ServerResponse updateBank(UserBank bank, HttpServletRequest request) {

     User user = this.iUserService.getCurrentUser(request);

     UserBank dbBank = this.userBankMapper.findUserBankByUserId(user.getId());

     if (dbBank == null) {

       return ServerResponse.createByErrorMsg(MessageUtils.get("bank.modify.idNotFound"));

     }

     dbBank.setBankName(bank.getBankName());

     dbBank.setBankNo(bank.getBankNo());

     dbBank.setBankAddress(bank.getBankAddress());

     dbBank.setBankImg(bank.getBankImg());

     dbBank.setBankPhone(bank.getBankPhone());

     int updateCount = this.userBankMapper.updateByPrimaryKeySelective(dbBank);

     if (updateCount > 0) {

       return ServerResponse.createBySuccess(MessageUtils.get("bank.modify.success"));

     }

     return ServerResponse.createByErrorMsg(MessageUtils.get("bank.modify.fail"));
   }

   public ServerResponse getBankInfo(HttpServletRequest request) {

     User user = this.iUserService.getCurrentUser(request);

     UserBank dbBank = this.userBankMapper.findUserBankByUserId(user.getId());

     if (dbBank == null) {

       return ServerResponse.createByErrorMsg(MessageUtils.get("bank.query.notFound"));

     }

     UserBankInfoVO userBankInfoVO = new UserBankInfoVO();

     userBankInfoVO.setRealName(user.getRealName());

     userBankInfoVO.setBankName(dbBank.getBankName());

     userBankInfoVO.setBankAddress(dbBank.getBankAddress());

     userBankInfoVO.setBankNo(dbBank.getBankNo());

     return ServerResponse.createBySuccess(userBankInfoVO);

   }

   public ServerResponse updateBankByAdmin(UserBank userBank) {

     if (userBank.getId() == null) {

       return ServerResponse.createByErrorMsg("??????id??????");

     }

     int updateCount = this.userBankMapper.updateByPrimaryKeySelective(userBank);

     if (updateCount > 0) {

       return ServerResponse.createBySuccessMsg("????????????");

     }

     return ServerResponse.createByErrorMsg("????????????");

   }

   public ServerResponse getBank(Integer userId) { return ServerResponse.createBySuccess(this.userBankMapper.findUserBankByUserId(userId)); }

 }
