package com.example.manage.white_list.service;

import com.example.manage.entity.SysPersonnel;
import com.example.manage.util.entity.ReturnEntity;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @avthor 潘小章
 * @date 2023/3/31
 */

public interface IWhiteSysPersonnelService {
    ReturnEntity methodMaster(HttpServletRequest request,String name);
    List<SysPersonnel> myLeader(Integer roleId, Integer managementId);
    List<SysPersonnel> queryAll(Map map);
    void birthdayInform();
    void dimissionInform();
}
