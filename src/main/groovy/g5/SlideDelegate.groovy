package g5

import groovy.xml.MarkupBuilder

import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource

import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants

class SlideDelegate {
    
    private MarkupBuilder xslBuilder
    private LinkedList contexts
    
    SlideDelegate(LinkedList contexts) {
        this.contexts = contexts
    }
    
    Map getContext() {
        contexts.last()    
    }
    
    def presentation() {
        def xsl = new File('test.xsl')
        xslBuilder = new MarkupBuilder(xsl.newWriter())
        xslBuilder.mkp.xmlDeclaration(version: '1.0', encoding: context.encoding ?: 'utf-8')
        xslBuilder.'xsl:stylesheet'(
                version: '1.0',
                'xmlns:xsl': "http://www.w3.org/1999/XSL/Transform",
                'xmlns:fo': "http://www.w3.org/1999/XSL/Format") {
                    'xsl:output'(method: 'xml', indent: 'yes')
                    'xsl:template'(match: '/') {
                        'fo:root' {
                            'fo:layout-master-set' {
                                'fo:simple-page-master'(
                                        'master-name': 'slide',
                                        'page-height': '29.7cm',
                                        'page-width': '21.0cm',
                                        margin: '2cm') {
                                            'fo:region-body'()
                                        }
                            }
                            'fo:page-sequence'('master-reference': 'slide') {
                                context.closure.call()
                            }
                        }
                    }
                }
        new File('test.pdf').withOutputStream { out ->
            def fopFactory = FopFactory.newInstance()
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
        xslBuilder.mkp.yield(
            xslBuilder.'fo:flow'('flow-name': 'xsl-region-body') {
                context.closure.call()
            }
        )
    }
    
    def block() {
        xslBuilder.mkp.yield(
            xslBuilder.'fo:block'(context.value)
        )
    }
    
    def listItem() {
        xslBuilder.mkp.yield(
            xslBuilder.'fo:list-item' {
                'fo:list-item-label'(
                    ) {
                        'fo:block' context.label ?: '*'
                    }
                'fo:list-item-body'(
                    'start-indent': 'body-start()',
                    ) {
                        context.closure.call()
                    }
            }
        )
    }
    
    def list() {
        xslBuilder.mkp.yield(
            xslBuilder.'fo:list-block'(
            'provisional-distance-between-starts': '1em',
            //'provisional-label-separation': '0.25em'
                ){
                    context.closure.call()
                }
            )
    }

}
