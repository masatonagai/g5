/*
 * Copyright 2011 Nagai Masato
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package g5

import java.util.Map

/**
 * SlideBuilder allows you to create and show slides using Groovy.
 * 
 * @author Nagai Masato
 */
class SlideBuilder implements GroovyInterceptable {
    private static Closure DO_NOTHING = {} 
    private SlideProxy proxy = new SlideProxy()
    
    private Map alias = [
        s: 'slide',
        ls: 'list',
        li: 'listItem',
        b: 'block',
        i: 'inline',
        im: 'image',
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
        try {
            proxy.node = new Node(parentNode, name, attributes, value)
            if (null != closure) {
                closure.delegate = this
            }
            proxy.closure = closure
        
            def metaMethod = proxy.metaClass.getMetaMethod(name, null) 
            if (null == metaMethod) {
                throw new MissingMethodException(name, proxy.class, new Object[0], false) 
            }
            return metaMethod.invoke(proxy, null)
        } finally { 
            proxy.node = parentNode
        }
    }
}
