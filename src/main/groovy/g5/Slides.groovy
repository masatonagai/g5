package g5

import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource

import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants

class Slide {
    
    private File xsl
    
    Slide(File xsl) {
        this.xsl = xsl    
    }
    
    private resource(String name) {
        this.class.classLoader.getResource(name)
    }
    
    def show() {
        output(MimeConstants.MIME_FOP_AWT_PREVIEW)            
    }
   
    def save(String mime, String filename) {
        new File(filename).withOutputStream { out ->
            output(mime, out)
        }   
    } 
    
    def output(String mime, OutputStream out = null) {
        def fopFactory = FopFactory.newInstance()
        fopFactory.setUserConfig(new File(resource("fop.xconf").toURI()))
        def foUA = fopFactory.newFOUserAgent()
        def fop = fopFactory.newFop(mime, foUA, out)
        TransformerFactory.newInstance()
            .newTransformer(new StreamSource(xsl.newReader()))
            .transform(new StreamSource(new StringReader('<dummy/>')),
            new SAXResult(fop.defaultHandler))
    }
}

