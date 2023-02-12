package com.itl.mes.core.api.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.itl.iap.common.base.exception.CommonException;
import com.itl.mes.core.api.dto.TeamUserQueryDto;
import com.itl.mes.core.api.entity.Team;
import com.itl.mes.core.api.vo.EmployeeVo;
import com.itl.mes.core.api.vo.TeamVo;
import org.apache.poi.ss.formula.functions.T;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 班组信息主表 服务类
 * </p>
 *
 * @author space
 * @since 2019-06-25
 */
public interface TeamService extends IService<Team> {

    List<Team> selectList();


    void save(TeamVo teamVo)throws CommonException;

    Team selectTeam(String team)throws CommonException;

    TeamVo getTeamVoByTeam(String team)throws CommonException;

    List<EmployeeVo> getEmployeeVo(String employeeCode, String group)throws CommonException;

    void delete(String team, Date modifyDate)throws CommonException;

    Page<Map<String, Object>> getUsersByTeam(TeamUserQueryDto params);

    void export(HttpServletRequest request, HttpServletResponse response);

    void saveByImport(List<TeamVo> list);
}
