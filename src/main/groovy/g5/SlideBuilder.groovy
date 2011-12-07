package g5

import java.util.Map

class SlideBuilder implements GroovyInterceptable {

    SlideProxy proxy = new SlideProxy()
    
    def invokeMethod(String name, args) {
		def closure = null
		def attributes = null
		def value = null
        args.each { arg -> 
            switch (arg) { 
                case Closure: closure = arg; break
                case Map: attributes = arg; break
                default: value = arg
            }
        }
	
		def parentNode = proxy.node	
		proxy.node = new Node(parentNode, name, attributes, value)
		if (null != closure) {
			closure.delegate = this
		}
		proxy.closure = closure
	
        def metaMethod = proxy.metaClass.getMetaMethod(name, null) 
        if (null == metaMethod) {
			throw new MissingMethodException(name, proxy.class, new Object[0], false) 
        } else {
            metaMethod.invoke(proxy, null)
        }
		
		proxy.node = parentNode
    }
}
