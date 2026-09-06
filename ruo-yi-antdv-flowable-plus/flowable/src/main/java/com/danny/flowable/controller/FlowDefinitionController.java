package com.danny.flowable.controller;

import com.danny.common.core.domain.AjaxResult;
import com.danny.common.core.domain.entity.SysRole;
import com.danny.common.core.domain.entity.SysUser;
import com.danny.flowable.domain.dto.FlowProcDefDto;
import com.danny.flowable.domain.dto.FlowSaveXmlVo;

import com.danny.flowable.service.IFlowDefinitionService;

import com.danny.system.service.ISysRoleService;
import com.danny.system.service.ISysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 工作流程定义
 * </p>
 *
 * @author XuanXuan
 * @date 2021-04-03
 */
@Slf4j
//@Api(tags = "流程定义")
@RestController
@RequestMapping("/flowable/definition")
public class FlowDefinitionController {

    @Autowired
    private IFlowDefinitionService flowDefinitionService;

    @Autowired
    private ISysUserService userService;

    @Resource
    private ISysRoleService sysRoleService;


    @GetMapping(value = "/list")
//    @ApiOperation(value = "流程定义列表", response = FlowProcDefDto.class)
    public AjaxResult list( @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize) {
        return AjaxResult.success(flowDefinitionService.list(pageNum, pageSize));
    }


//    @ApiOperation(value = "导入流程文件", notes = "上传bpmn20的xml文件")
    @PostMapping("/import")
    public AjaxResult importFile(@RequestParam(required = false) String name,
                                 @RequestParam(required = false) String category,
                                 MultipartFile file) {
        InputStream in = null;
        try {
            in = file.getInputStream();
            flowDefinitionService.importFile(name, category, in);
        } catch (Exception e) {
            log.error("导入失败:", e);
            return AjaxResult.success(e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("关闭输入流出错", e);
            }
        }

        return AjaxResult.success("导入成功");
    }


//    @ApiOperation(value = "读取xml文件") @ApiParam(value = "流程定义id")
    @GetMapping("/readXml/{deployId}")
    public AjaxResult readXml(@PathVariable(value = "deployId") String deployId) {
        try {
            return flowDefinitionService.readXml(deployId);
        } catch (Exception e) {
            return AjaxResult.error("加载xml文件异常");
        }

    }

//    @ApiOperation(value = "读取图片文件")@ApiParam(value = "流程定义id")
    @GetMapping("/readImage/{deployId}")
    public void readImage( @PathVariable(value = "deployId") String deployId, HttpServletResponse response) {
        OutputStream os = null;
        BufferedImage image = null;
        try {
            image = ImageIO.read(flowDefinitionService.readImage(deployId));
            response.setContentType("image/png");
            os = response.getOutputStream();
            if (image != null) {
                ImageIO.write(image, "png", os);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.flush();
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


//    @ApiOperation(value = "保存流程设计器内的xml文件")
    @PostMapping("/save")
    public AjaxResult save(@RequestBody FlowSaveXmlVo vo) {
        InputStream in = null;
        try {
            in = new ByteArrayInputStream(vo.getXml().getBytes(StandardCharsets.UTF_8));
            flowDefinitionService.importFile(vo.getName(), vo.getCategory(), in);
        } catch (Exception e) {
            log.error("导入失败:", e);
            return AjaxResult.success(e.getMessage());
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                log.error("关闭输入流出错", e);
            }
        }

        return AjaxResult.success("导入成功");
    }


//    @ApiOperation(value = "根据流程定义id启动流程实例")@ApiParam(value = "流程定义id") @ApiParam(value = "变量集合,json对象")
    @PostMapping("/start/{procDefId}")
    public AjaxResult start( @PathVariable(value = "procDefId") String procDefId,
                             @RequestBody Map<String, Object> variables) {
        return flowDefinitionService.startProcessInstanceById(procDefId, variables);

    }

//    @ApiOperation(value = "激活或挂起流程定义") @ApiParam(value = "1:激活,2:挂起", required = true) @ApiParam(value = "流程部署ID", required = true)
    @PutMapping(value = "/updateState")
    public AjaxResult updateState( @RequestParam Integer state,
                                   @RequestParam String deployId) {
        flowDefinitionService.updateState(state, deployId);
        return AjaxResult.success();
    }

//    @ApiOperation(value = "删除流程") @ApiParam(value = "流程部署ID", required = true)
    @DeleteMapping(value = "/delete")
    public AjaxResult delete( @RequestParam String deployId) {
        flowDefinitionService.delete(deployId);
        return AjaxResult.success();
    }

//    @ApiOperation(value = "指定流程办理人员列表")
    @GetMapping("/userList")
    public AjaxResult userList(SysUser user) {
        List<SysUser> list = userService.findList(user);
        return AjaxResult.success(list);
    }

//    @ApiOperation(value = "指定流程办理组列表")
    @GetMapping("/roleList")
    public AjaxResult roleList(SysRole role) {
        List<SysRole> list = sysRoleService.findList(role);
        return AjaxResult.success(list);
    }

}