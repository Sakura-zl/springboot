package com.sakura.pmp.server.controller;

import com.google.common.collect.Maps;
import com.sakura.pmp.common.response.BaseResponse;
import com.sakura.pmp.common.response.StatusCode;
import com.sakura.pmp.common.utils.PageUtil;
import com.sakura.pmp.model.entity.AttendRecordEntity;
import com.sakura.pmp.server.common.PoiService;
import com.sakura.pmp.server.common.WebOperationService;
import com.sakura.pmp.server.service.AttendRecordService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author : lzl
 * @date : 16:52 2020/6/6
 */
@Controller
@RequestMapping("/attend/record")
public class AttendRecordController extends AbstractController {

    @Autowired
    private AttendRecordService attendRecordService;

    @Autowired
    private PoiService poiService;

    @Autowired
    private WebOperationService webOperationService;


    //列表
    @ResponseBody
    @RequestMapping("/list")
    public BaseResponse list(@RequestParam Map<String, Object> params){
        BaseResponse response=new BaseResponse(StatusCode.Success);
        Map<String,Object> resMap= Maps.newHashMap();

        try {

            PageUtil page = attendRecordService.queryPage(params);
            resMap.put("page", page);
        }catch (Exception e){
            response=new BaseResponse(StatusCode.Fail.getCode(),e.getMessage());
        }
        response.setData(resMap);
        return response;
    }


    //导出
    @RequestMapping("/export")
    public @ResponseBody String export(@RequestParam Map<String, Object> params, HttpServletResponse response){
        final String[] headers=new String[]{"ID","部门名称","姓名","日期","打卡状态","打卡开始时间","打卡结束时间","工时/小时"};
        try {
            String fileName=new StringBuilder("考勤明细-").append(new SimpleDateFormat("yyyyMMddHHmmss").format(new Date())).toString();
            String excelName=fileName+".xls";

            //针对于具体的业务模块-查询相应的数据，并做 “行转列映射” 的处理
            List<AttendRecordEntity> list=attendRecordService.selectAll(params);
            List<Map<Integer, Object>> listMap=attendRecordService.manageExport(list);

            //以下是通用的
            Workbook wb=poiService.fillExcelSheetData(listMap,headers,fileName);
            webOperationService.downloadExcel(response,wb,excelName);

            return excelName;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

}
