/*
 *  Copyright 2016 http://www.hswebframework.org
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 *
 */

package org.hswebframework.web.authorization.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.hswebframework.ezorm.core.param.TermType;
import org.hswebframework.web.authorization.Permission;
import org.hswebframework.web.authorization.annotation.Authorize;
import org.hswebframework.web.commons.entity.PagerResult;
import org.hswebframework.web.commons.entity.param.QueryParamEntity;
import org.hswebframework.web.controller.SimpleGenericEntityController;
import org.hswebframework.web.controller.message.ResponseMessage;
import org.hswebframework.web.entity.authorization.RoleEntity;
import org.hswebframework.web.entity.authorization.UserEntity;
import org.hswebframework.web.logging.AccessLogger;
import org.hswebframework.web.service.authorization.RoleService;
import org.hswebframework.web.service.authorization.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.hswebframework.web.controller.message.ResponseMessage.ok;

/**
 * 角色控制器
 *
 * @author zhouhao
 */
@RestController
@RequestMapping("${hsweb.web.mappings.role:role}")
@Authorize(permission = "role", description = "角色管理")
@Api(value = "角色管理",tags = "权限-角色管理")
public class RoleController implements SimpleGenericEntityController<RoleEntity, String, QueryParamEntity> {

    @Autowired
    private RoleService roleService;
    @Autowired
    private UserService userService;

    @Override
    public RoleService getService() {
        return roleService;
    }

    @PutMapping("/disable/{id:.+}")
    @Authorize(action = Permission.ACTION_DISABLE)
    @ApiOperation("禁用角色")
    public ResponseMessage disable(@PathVariable String id) {
        roleService.disable(id);
        return ok();
    }

    @PutMapping("/enable/{id}")
    @Authorize(action = Permission.ACTION_ENABLE)
    @ApiOperation("启用角色")
    public ResponseMessage enable(@PathVariable String id) {
        roleService.enable(id);
        return ok();
    }

    @Override
    public ResponseMessage<PagerResult<RoleEntity>> list(QueryParamEntity param) {
        PagerResult<RoleEntity> result = getService().selectPager(param);
        List<String> creatorIdList = result.getData().stream().map(role -> role.getCreatorId()).distinct().collect(Collectors.toList());
        List<UserEntity> userList = this.userService.selectByPk(creatorIdList);
        result.getData().stream().forEach(role -> {
            if(role.getCreatorId() != null){
                role.setCreator(userList.stream().filter(user -> user.getId().equals(role.getCreatorId())).findFirst().get());
            }
        });
        return ok(result);
    }


    @Authorize(action = Permission.ACTION_DELETE)
    @DeleteMapping("/batchDelete")
    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation(value = "批量删除")
    @AccessLogger(value = "批量删除")
    public ResponseMessage<String> batchDelete(@RequestParam @ApiParam("所有id拼成的串，以逗号隔开") String ids){
        ResponseMessage<String> result = ok("").code("1");
        String[] idsArray = ids.split(",");
        List<String> idsListString = Arrays.asList(idsArray);
        List<String> idsList = (List<String>)idsListString;
        List<RoleEntity> existList = this.getService().select(QueryParamEntity.empty().and("id", TermType.in, idsList));
        if(existList == null || existList.size() == 0) {
            result.setMessage("要删除的数据不存在");
            return result;
        }
        if(existList.size() < idsList.size()){
            result.setMessage("部分要删除的数据不存在");
            return result;
        }
        this.getService().batchDelete(idsList);
        result.code("0");
        result.setMessage("删除成功");
        return result;

    }
}
