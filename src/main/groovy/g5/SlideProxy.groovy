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
   
    private File xsl 
    private MarkupBuilder xslBuilder
    private Node node
    private Closure closure
    
    def call() {
        def xsl = new File('.g5.tmp')
        xslBuilder = new MarkupBuilder(xsl.newWriter())
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
                        'fo:layout-master-set' {
                            'fo:simple-page-master' 'master-name': 'slide',
                                'page-height': '29.7cm',
                                'page-width': '21.0cm',
                                'margin': '2cm', {
                                'fo:region-body'()
                            }
                        }
                        'fo:page-sequence'('master-reference': 'slide') {
                            'fo:flow'('flow-name': 'xsl-region-body') {
                                closure()    
                            }
                        }
                    }
                }
            }
        return new Slide(xsl)
    }

    def slide() {
        xslBuilder.'fo:block'(node.attributes() + ['break-before': 'page']) {
            closure()
        }
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
