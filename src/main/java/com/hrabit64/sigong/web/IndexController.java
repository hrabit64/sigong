package com.hrabit64.sigong.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public interface IndexController {

    String index(Model model);

}
