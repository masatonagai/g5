package g5

import java.util.Map

class SlideBuilder implements GroovyInterceptable {
    private static Closure DO_NOTHING = {} 
    private SlideProxy proxy = new SlideProxy()
    
    private Map alias = [
        sl: 'slide',
        ls: 'list',
        li: 'listItem',
        b: 'block',
        i: 'inline',
    ]
    
    def invokeMethod(String name, args) {
        name = alias[name] ?: name
        def closure = DO_NOTHING
        def attributes = Collections.emptyMap()
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
