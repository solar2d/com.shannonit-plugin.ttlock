//
//  LuaLoader.java
//  TTLockPlugin
//

package plugin.ttlock;

import com.ansca.corona.CoronaEnvironment;
import com.ansca.corona.CoronaRuntimeListener;
import com.naef.jnlua.LuaState;
import com.naef.jnlua.NamedJavaFunction;

/**
 * Implements the Lua interface for the TTLock plugin.
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class LuaLoader implements NamedJavaFunction {

    /**
     * Called when this plugin is being loaded via the Lua require() function.
     * @param L Reference to the Lua state that the require() function was called from.
     * @return Returns the number of values that the require() function will return.
     */
    @Override
    public int invoke(LuaState L) {
        // Register runtime listener ONCE
        CoronaEnvironment.addRuntimeListener(new LuaTTLock());

        // Create Lua table
        L.newTable();

        // Expose functions
        L.pushJavaFunction(new LuaTTLock.PingFunction());
        L.setField(-2, "ping");

        // Returning 1 indicates that Lua require() returns the above table
        return 1;
    }

    /**
     * Gets the name of the Lua function as it would appear in the Lua script.
     * @return Returns the name of the Lua library.
     */
    @Override
    public String getName() {
        return "plugin_ttlock";
    }
}
