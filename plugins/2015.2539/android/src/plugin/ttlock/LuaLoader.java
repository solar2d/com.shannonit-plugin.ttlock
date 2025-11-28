package plugin.ttlock;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.JavaFunction;

public class LuaLoader implements JavaFunction {

    @Override
    public int invoke(LuaState L) {
        L.newTable();
        L.pushJavaFunction(new HelloFunction());
        L.setField(-2, "hello");
        return 1;
    }

    static class HelloFunction implements JavaFunction {
        @Override
        public int invoke(LuaState L) {
            L.pushString("Hello from com.shannonit.ttlock plugin!");
            return 1;
        }
    }
}
