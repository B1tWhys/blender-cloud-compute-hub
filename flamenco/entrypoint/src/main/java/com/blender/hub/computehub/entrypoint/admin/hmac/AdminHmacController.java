package com.blender.hub.computehub.entrypoint.admin.hmac;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/admin/hmacs")
@AllArgsConstructor
public class AdminHmacController {
    AdminHmacProvider hmacProvider;

    @RequestMapping(method = RequestMethod.GET)
    public String listHmacs(Model model) {
        model.addAttribute("hmacs", hmacProvider.listHmacSecrets());
        return "admin/hmacs";
    }
}
