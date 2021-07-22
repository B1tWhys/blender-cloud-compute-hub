package com.blender.hub.computehub.adapter.util;

import com.blender.hub.computehub.core.util.ServerUriCalculator;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.net.URI;
import java.util.Map;

@Service
public class ServerUriCalculatorImpl implements ServerUriCalculator {
    @Override
    public URI urlTo(Method method) {
        return null;
    }

    @Override
    public URI urlTo(Method method, Map<String, Object> params) {
        return null;
    }
}
