package cn.com.sgcc.marki_with_maven.modules;

import java.lang.reflect.Modifier;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

public interface IPocBase {
	
	public Map info();
		
	public boolean match(Map infodict);
	
	public boolean verify(Map infodict);

    public static class Utils {

        // get payload classes by classpath scanning
        public static Set<Class<? extends IPocBase>> getPayloadClasses () {
            final Reflections reflections = new Reflections(IPocBase.class.getPackage().getName());
            final Set<Class<? extends IPocBase>> payloadTypes = reflections.getSubTypesOf(IPocBase.class);
            for ( Iterator<Class<? extends IPocBase>> iterator = payloadTypes.iterator(); iterator.hasNext(); ) {
                Class<? extends IPocBase> pc = iterator.next();
                if ( pc.isInterface() || Modifier.isAbstract(pc.getModifiers()) ) {
                    iterator.remove();
                }
            }
            return payloadTypes;
        }
    }
}
