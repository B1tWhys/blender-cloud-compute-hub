package com.blender.hub.computehub.core.util;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;

public interface ServerUriCalculator {
    URI urlTo(Method method);
    URI urlTo(Method method, Map<String, Object> params);
}
