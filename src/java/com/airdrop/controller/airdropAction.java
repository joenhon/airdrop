package com.airdrop.controller;
import com.airdrop.service.airdropService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.ws.RequestWrapper;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;

@Controller
public class airdropAction {

    @Resource(name = "airdropService")
    private airdropService airService;

    @RequestMapping("airDrop")
    public String airDrop(@RequestParam(value="path")MultipartFile path,@RequestParam(value="addressPath")MultipartFile addressPath, HttpServletRequest request){
        if (request.getParameter("tokenContractAddress")==null){
            return "error";
        }
        String tokenContractAddress =request.getParameter("tokenContractAddress");
        /*if (request.getParameter("path")==null){
            return "error";
        }*/
        String path_=File(path,request);
        if (request.getParameter("value")==null){
            return "error";
        }
        BigInteger value=new BigInteger(request.getParameter("value"));
        /*if (request.getParameter("addressPath")==null){
            return "error";
        }*/
        String addressPath_=File(addressPath,request);
        String pwd=request.getParameter("pwd");
        if (request.getParameter("everyGas")==null){
            return "error";
        }
        BigInteger everyGas=new BigInteger(request.getParameter("everyGas"));
        if (request.getParameter("GasPrice")==null){
            return "error";
        }
        BigInteger GasPrice=new BigInteger(request.getParameter("GasPrice"));
        HttpSession session=request.getSession();
        try {
            session.setAttribute("hash",airService.airDrop_(tokenContractAddress,path_,value,addressPath_,pwd,everyGas,GasPrice));
        }catch (Exception e){
            e.printStackTrace();
            return "error";
        }
        return "index";
    }
    @RequestMapping("airDropValues")
    public String airDropValues(HttpServletRequest request){
        if (request.getParameter("tokenContractAddress")==null){
            return "error";
        }
        String tokenContractAddress =request.getParameter("tokenContractAddress");
        if (request.getParameter("path")==null){
            return "error";
        }
        String path=request.getParameter("path");
        if (request.getParameter("value")==null){
            return "error";
        }
        String value=request.getParameter("value");
        if (request.getParameter("addressPath")==null){
            return "error";
        }
        String addressPath=request.getParameter("addressPath");
        String pwd=request.getParameter("pwd");
        if (request.getParameter("everyGas")==null){
            return "error";
        }
        BigInteger everyGas=new BigInteger(request.getParameter("everyGas"));
        if (request.getParameter("GasPrice")==null){
            return "error";
        }
        return "index";
    }
    private String File(MultipartFile file,HttpServletRequest request) {
        //getSize()方法获取文件的大小来判断是否有上传文件
        if (file.getSize() > 0) {
            //获取保存上传文件的file文件夹绝对路径
            String path = request.getSession().getServletContext().getRealPath("address");
            //获取上传文件名
            String fileName = file.getOriginalFilename();
            File file_ = new File(path, fileName);
            try {
                file.transferTo(file_);
            }catch (Exception e){
                e.printStackTrace();
            }

            //保存上传之后的文件路径
            request.setAttribute("filePath", fileName);
            return path+"/"+fileName;
        }
        return null;
    }
}
