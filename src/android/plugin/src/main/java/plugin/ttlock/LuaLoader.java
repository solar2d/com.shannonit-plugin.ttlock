package plugin.ttlock;

import android.content.Context;

import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;

public class LuaLoader {

    private final LuaStub stub;

    public LuaLoader(Context context, CoronaRuntime runtime) {
        CoronaRuntimeTaskDispatcher dispatcher =
                new CoronaRuntimeTaskDispatcher(runtime);

        stub = new LuaStub(dispatcher);

        System.out.println("LuaLoader: Stub plugin loaded");
    }
}
