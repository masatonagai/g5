package g5

import groovy.lang.Closure;
import groovy.xml.MarkupBuilder

import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource

import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants

class SlideProxy {
    
    private MarkupBuilder xslBuilder
    private Node node
    private Closure closure
    
    private resource(String name) {
        this.class.classLoader.getResource(name)
    }
    
    def slides() {
        def xsl = new File('test.xsl')
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
                        'fo:root' node.attributes(), {
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
        new File('test.pdf').withOutputStream { out ->
            def fopFactory = FopFactory.newInstance()
            fopFactory.setUserConfig(new File(resource("fop.xconf").toURI()))
            def foUA = fopFactory.newFOUserAgent()
            def fop = fopFactory.newFop(
                    MimeConstants.MIME_PDF, foUA, out)
            TransformerFactory.newInstance()
                    .newTransformer(new StreamSource(xsl.newReader()))
                    .transform(new StreamSource(new StringReader('<dummy/>')/*xml.newReader()*/),
                    new SAXResult(fop.defaultHandler))
        }
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
            'fo:list-item-body'(
                'start-indent': 'body-start()',
                ) {
                closure()
            }
        }
    }
   
    def list() {
        def attr = new HashMap(node.attributes())
        attr.remove('label')
        xslBuilder.'fo:list-block'(attr){
            closure()
        }
    }

}
