package plugin.ttlock;

import android.content.Context;

import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeTaskDispatcher;

import com.ansca.corona.CoronaActivity;
import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaLua;
import com.ansca.corona.CoronaRuntime;
import com.ansca.corona.CoronaRuntimeListener;
import com.ansca.corona.CoronaRuntimeTask;
import com.ansca.corona.permissions.PermissionState;
import com.ansca.corona.permissions.PermissionsServices;
import com.naef.jnlua.JavaFunction;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

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
