package com.airdrop.controller;
import com.airdrop.service.ethService;
import com.airdrop.utils.readFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Controller
public class ethController {
    @Resource(name = "ethService")
    private ethService eth;
    @RequestMapping("GasPrice")
    @ResponseBody
    public void GasPrice(HttpServletResponse response) throws IOException {
        try {
            response.getWriter().print(eth.Price());
        } catch (Exception e) {
            response.getWriter().print(0);
            e.printStackTrace();
        }
    }
}
