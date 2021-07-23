package com.blender.hub.computehub.port.util;

import com.blender.hub.computehub.usecase.util.TimeProvider;
import org.joda.time.DateTime;

public class TimeProviderImpl implements TimeProvider {
    @Override
    public long now() {
        return DateTime.now().getMillis();
    }
}
