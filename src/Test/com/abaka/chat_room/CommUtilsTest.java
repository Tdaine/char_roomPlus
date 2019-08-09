package com.abaka.chat_room;

import com.abaka.chat_room.util.CommUtils;
import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class CommUtilsTest {

    @Test
    public void loadProperties() {
        Properties properties = CommUtils.loadProperties("datasource.properties");
        Assert.assertNotNull(properties);
    }
}