package com.blender.hub.computehub.entrypoint.api;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Data
public class CreateManagerRequest implements Serializable {
    @NotBlank(message = "managerType is required")
    String managerType;
}
