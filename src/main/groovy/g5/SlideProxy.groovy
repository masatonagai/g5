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

import groovy.lang.Closure
import groovy.xml.MarkupBuilder

class SlideProxy {
   
    File xsl 
    MarkupBuilder xslBuilder
    Node node
    Closure closure
    SlideAttributes attrs = new SlideAttributes()
    boolean layouted
    int size
   
    def propertyMissing(String name) {
        attrs.@"$name"
    } 
   
    def call() {
        def xsl = File.createTempFile('.g5', '')
        xsl.deleteOnExit()
        
        xsl.withWriter { w ->
            xslBuilder = new MarkupBuilder(w)
            xslBuilder.mkp.xmlDeclaration(
                version: '1.0', 
                encoding: node.attribute('encoding') ?: 'utf-8'
            )
            xslBuilder.'xsl:stylesheet'(
                version: '1.0',
                'xmlns:xsl': "http://www.w3.org/1999/XSL/Transform",
                'xmlns:fo': "http://www.w3.org/1999/XSL/Format") {
                    'xsl:output'(method: 'xml', indent: 'yes')
                    'xsl:template'(match: '/') {
                        def defaultRootAttrs = ['font-size':'32pt']
                        'fo:root' defaultRootAttrs + node.attributes(), {
                            closure()
                        }
                    }
                }
        }
        return new Slide(xsl)
    }
    
    def layout() {
        xslBuilder.'fo:layout-master-set' {
            'fo:simple-page-master' PA4 +
                node.attributes() + ['master-name':'slide'], {
                    'fo:region-body'()
            }
        }    
        layouted = true
    }
    
    def slide() {
        def run = {
            xslBuilder.'fo:block'(node.attributes() + ['break-before': 'page']) {
                closure()
            }
        }
        if (!size) {
            if (!layouted) {
                layout()    
            }
            xslBuilder.'fo:page-sequence' 'master-reference': 'slide', {
                'fo:flow'('flow-name': 'xsl-region-body') {
                    run()
                }
            }
        } else {
            run()
        }
        size++
    }
    
    def block() {
        xslBuilder.'fo:block'(node.attributes(), node.value()) {
            closure()
        }
    }
    
    def inline() {
        xslBuilder.'fo:inline'(node.attributes(), node.value()) { 
            closure() 
        }
    }
    
    def listItem() {
        xslBuilder.'fo:list-item' {
            'fo:list-item-label' {
                'fo:block' node.parent().attribute('label') ?: '*'
            }
            'fo:list-item-body' 'start-indent':'body-start()', {
                'fo:block' node.attributes(), node.value(), {
                    closure()
                } 
            }
        }
    }
   
    def list() {
        def attrs = new HashMap(node.attributes())
        attrs.remove('label')
        xslBuilder.'fo:list-block'(attrs){
            closure()
        }
    }
    
    def table() {
        xslBuilder.'fo:table' node.attributes() + ['table-layout':'fixed', width:'100%'], {
            closure()
        }    
    }
    
    def tableColumn() {
        xslBuilder.'fo:table-column' node.attributes() 
    }
    
    def tableHeader() {
        xslBuilder.'fo:table-header' node.attributes(), {
            closure()    
        }    
    }
   
    def tableBody() {
        xslBuilder.'fo:table-body' node.attributes(), {
            closure()    
        }    
    } 
    
    def tableRow() {
        xslBuilder.'fo:table-row' node.attributes(), {
            closure()    
        }   
    }
    
    def tableCell() {
        xslBuilder.'fo:table-cell' node.attributes(), {
            'fo:block' node.value(), {
                closure()    
            }
        }
    }
    
    def image() {
        def attrs = node.attributes() + [src:"url(${node.value()})"]
        xslBuilder.'fo:external-graphic'(attrs)    
    }

}
