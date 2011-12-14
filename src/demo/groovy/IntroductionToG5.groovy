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

import g5.SlideBuilder

def slideBuilder = new SlideBuilder()

def name = { 
    def attrs = [color:'#3283A1']
    slideBuilder.inline(it ? attrs + it : attrs, "G5") 
}
def title = { slideBuilder.block 'font-size':'48pt', 'margin-bottom':'18pt', it }
def properNoun = { slideBuilder.inline 'font-style':'italic', "\"$it\"" }

def slides = slideBuilder 'font-family':'Verdana', {
    slide {
        title { inline "Introduction to "; name('font-size':'64pt') }
        block "Nagai Masato" 
        block 'font-size':'16pt', color:'gray', {
            block "Twitter: @nagai_masato"
            block "Blog: http://nagaimasato.blogspot.com/"
        }
    }
    slide {
        title { inline "What is "; name(); inline "?" }
        list 'font-size':'24pt', {
            listItem { 
                image 'http://groovy.codehaus.org/images/groovy-logo-medium.png' 
                inline "Slideshow System" 
            }
            listItem {
                inline "Allows you to create and show slides using "; properNoun "Groovy"
            }
            listItem {
                inline "Uses "; properNoun "XSL-FO"; inline " and "
                properNoun "Apache FOP"; inline " for formatting slides"
            }
        }
    }    
} 
slides.show()
