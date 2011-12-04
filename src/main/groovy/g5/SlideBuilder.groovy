package g5

import java.util.Map

class SlideBuilder implements GroovyInterceptable {

    LinkedList contexts = new LinkedList()
    SlideDelegate delegate = new SlideDelegate(contexts)
    
    def invokeMethod(String name, args) {
        Map context = contexts ? new HashMap(contexts.last()) : new HashMap()
        args.each { arg -> 
            switch (arg) { 
                case Closure:
                    arg.delegate = this 
                    context.put('closure', arg)
                    break
                case Map:
                    context.putAll(arg)
                    break
                default:
                    context.put('value', arg)
            }
        }
        contexts.add(context)
        def metaMethod = delegate.metaClass.getMetaMethod(name, null) 
        if (!metaMethod) {
            System.out.println("$name($delegateArgs) is not found.")    
        } else {
            metaMethod.invoke(delegate, null)
        }
        contexts.removeLast()
    }
}
