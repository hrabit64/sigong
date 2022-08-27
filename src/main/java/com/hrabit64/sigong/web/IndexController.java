package com.hrabit64.sigong.web;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.websocket.server.PathParam;

import static com.hrabit64.sigong.config.SigongConfig.AllowFileTypes;

@RequiredArgsConstructor
@Validated
@Controller
public class IndexController {

    @GetMapping
    public String index(Model model){
        model.addAttribute("whitelist",AllowFileTypes.toString());
        return "index";
    }

    @GetMapping("{downloadCode}")
    public String fileInfo(Model model, @PathVariable String downloadCode){
        model.addAttribute("whitelist",AllowFileTypes.toString());
        return "index";
    }

}
