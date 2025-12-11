package plugin.ttlock;

import android.content.Context;

import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;

public class LuaLoader {

    private final LuaTTLock luaTTLock;
    private final CoronaRuntimeTaskDispatcher dispatcher;

    public LuaLoader(Context context, CoronaRuntime runtime) {
        dispatcher = new CoronaRuntimeTaskDispatcher(runtime);

        // Create TTLock wrapper (minimal version)
        luaTTLock = new LuaTTLock(context, dispatcher);

        // Optional test message
        System.out.println("LuaLoader: LuaTTLock instance created");
    }
}
