package g5

import groovy.xml.MarkupBuilder

import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource

import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants

class SlideProxy {
    
    private MarkupBuilder xslBuilder
	Node node
	Closure closure
	
    def presentation() {
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
                        'fo:root'(
							'font-family': node.attribute('font-family') ?: 'serif'
						) {
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
                                closure()
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
                closure()
            }
        )
    }
    
    def block() {
        xslBuilder.mkp.yield(
            xslBuilder.'fo:block'(
				'text-decoration': node.attribute('text-decoration') ?: 'none', 
				node.value()
			)
        )
    }
    
    def listItem() {
        xslBuilder.mkp.yield(
            xslBuilder.'fo:list-item' {
                'fo:list-item-label'(
                    ) {
                        'fo:block' node.parent().attribute('label') ?: '*'
                    }
                'fo:list-item-body'(
                    'start-indent': 'body-start()',
                    ) {
                        closure()
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
                    closure()
                }
            )
    }

}
