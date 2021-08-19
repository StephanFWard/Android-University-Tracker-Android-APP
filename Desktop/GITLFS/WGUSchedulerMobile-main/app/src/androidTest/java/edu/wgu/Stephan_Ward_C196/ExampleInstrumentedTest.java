package edu.wgu.Stephan_Ward_C196;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

/**
 * Android device module instrument test. See link below for information.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // The app context to be tested.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("edu.wgu.Stephan_Ward_C196", appContext.getPackageName());
    }
}