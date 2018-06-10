package cn.com.sgcc.marki_with_maven.modules;

import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.reflections.Reflections;

import cn.com.sgcc.marki_with_maven.Config;

public interface IPocBase {
	
	public Map info();
		
	public boolean match(Map infodict);
	
	public boolean verify(Map infodict);

    public static class Utils {

        // get payload classes by classpath scanning
        public static Set<Class<? extends IPocBase>> getPayloadClasses () {
            final Reflections reflections = new Reflections(IPocBase.class.getPackage().getName());
            Set<Class<? extends IPocBase>> payloadTypes = reflections.getSubTypesOf(IPocBase.class);
            for ( Iterator<Class<? extends IPocBase>> iterator = payloadTypes.iterator(); iterator.hasNext(); ) {
                Class<? extends IPocBase> pc = iterator.next();
                if ( pc.isInterface() || Modifier.isAbstract(pc.getModifiers()) ) {
                    iterator.remove();
                }
            }
            
            try {
				payloadTypes.add((Class<? extends IPocBase>) Class.forName("JAVAUnseriaLize"));
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            return payloadTypes;
        }
        
        public static Object getParameter(Map infodict, String para)
        {
        	return infodict.get(Config.CONFIGED +para) == null? infodict.get(para) : infodict.get(Config.CONFIGED+ para);
        }
        
        public final static HashMap<String, Object> configurable_para = new HashMap<String, Object>(){{
        	put("ip", "test");
        	put("service_type", "test");
        	put("service_version", "test");
        	put("port", "test");
        	
        }};
    }
}
