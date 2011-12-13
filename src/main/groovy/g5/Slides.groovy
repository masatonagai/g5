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
    
    private resource(String name) {
        this.class.classLoader.getResource(name)
    }
    
    def call() {
        show()    
    }
    
    def call(String mime, String filename) {
        export(mime, filename)    
    }
    
    def show() {
        output(MimeConstants.MIME_FOP_AWT_PREVIEW)            
    }
   
    def export(String mime, String filename) {
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

