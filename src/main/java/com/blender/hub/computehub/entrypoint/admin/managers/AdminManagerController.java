package com.blender.hub.computehub.entrypoint.admin.managers;

import com.blender.hub.computehub.entrypoint.admin.managers.provider.AdminManagerProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@AllArgsConstructor
public class AdminManagerController {
    AdminManagerProvider managerProvider;

    @RequestMapping("/admin/managers")
    public String getManagerIndex(Model model) {
        model.addAttribute("managers", managerProvider.listWireManagers());
        return "admin/managers";
    }
}
