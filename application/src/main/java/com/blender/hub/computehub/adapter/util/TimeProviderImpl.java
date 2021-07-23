package com.blender.hub.computehub.adapter.util;

import com.blender.hub.computehub.core.util.TimeProvider;
import org.joda.time.DateTime;

public class TimeProviderImpl implements TimeProvider {
    @Override
    public long now() {
        return DateTime.now().getMillis();
    }
}
