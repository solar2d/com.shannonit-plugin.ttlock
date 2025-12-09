package plugin.ttlock;

import com.naef.jnlua.LuaState;
import com.naef.jnlua.JavaFunction;

public class LuaLoader implements JavaFunction {

    @Override
    public int invoke(LuaState L) {
        L.newTable();

        L.pushString("init");
        L.pushJavaFunction(init);
        L.setTable(-3);

        return 1;
    }

    private final JavaFunction init = new JavaFunction() {
        @Override
        public int invoke(LuaState L) {
            LuaTTLock.init();
            return 0;
        }
    };
}
