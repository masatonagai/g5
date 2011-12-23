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
    
    private URL resource(String name) {
        this.class.classLoader.getResource(name)
    }
    
    void call() {
        show()    
    }
    
    void call(String mime, String filename) {
        export(mime, filename)    
    }
    
    void show() {
        output(MimeConstants.MIME_FOP_AWT_PREVIEW)            
    }
    
    void export(String mime, String filename) {
        new File(filename).withOutputStream { out ->
            output(mime, out)
        }   
    } 
    
    void output(String mime, OutputStream out = null) {
        def fopFactory = FopFactory.newInstance()
        fopFactory.setUserConfig(new File(resource("fop.xconf").toURI()))
        def foUA = fopFactory.newFOUserAgent()
        def fop = fopFactory.newFop(mime, foUA, out)
        xsl.withReader { r ->
            TransformerFactory.newInstance()
                .newTransformer(new StreamSource(r))
                .transform(new StreamSource(new StringReader('<dummy/>')),
                new SAXResult(fop.defaultHandler))
        }
    }
}

