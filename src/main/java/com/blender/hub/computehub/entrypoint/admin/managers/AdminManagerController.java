package com.blender.hub.computehub.entrypoint.admin.managers;

import com.blender.hub.computehub.entrypoint.admin.managers.provider.AdminManagerProvider;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@AllArgsConstructor
public class AdminManagerController {
    AdminManagerProvider managerProvider;

    @RequestMapping("/admin/managers")
    public String getManagerIndex(@RequestParam(defaultValue = "5") int limit, Model model) {
        model.addAttribute("managers", managerProvider.listWireManagers(limit));
        return "admin/managers";
    }

    @PostMapping("/admin/managers")
    public RedirectView createManager() {
        managerProvider.create();
        return new RedirectView("/admin/managers");
    }
}
