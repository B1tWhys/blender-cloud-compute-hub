package com.blender.hub.computehub.entrypoint.admin.managers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AdminManagerController {
    @RequestMapping("/admin/managers")
    public String getManagerIndex() {
        return "admin/managers";
    }
}
