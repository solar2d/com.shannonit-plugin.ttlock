package plugin.ttlock;

import android.content.Context;

import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;

public class LuaLoader {

    private final LuaTTLock luaTTLock;

    public LuaLoader(Context context, CoronaRuntime runtime) {
        CoronaRuntimeTaskDispatcher dispatcher =
                new CoronaRuntimeTaskDispatcher(runtime);

        // Create the LuaTTLock stub instance
        luaTTLock = new LuaTTLock(dispatcher);

        // Log to confirm the loader is invoked
        System.out.println("LuaLoader: LuaTTLock stub loaded");
    }
}
