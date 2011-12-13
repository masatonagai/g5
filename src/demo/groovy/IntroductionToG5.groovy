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

def groovyColor = '#3483A1'
def title = { slideBuilder.block 'font-size':'48pt', 'margin-bottom':'18pt', it }

def slides = slideBuilder 'font-family':'Verdana', {
    slide {
        title { 
            inline "Introduction to "
            inline 'font-size':'64pt', color:groovyColor, "G5" 
        }
        block "Nagai Masato" 
        block 'font-size':'16pt', color:'gray', {
            block "Twitter: @nagai_masato"
            block "Blog: http://nagaimasato.blogspot.com/"
        }
    }
    slide {
        title "What is G5?"
        list {
            listItem { 
                image 'http://groovy.codehaus.org/images/groovy-logo-medium.png' 
                inline "Slideshow System" 
            }
            listItem "Allows you to create and show slides using Groovy"
            listItem "Uses XSL-FO and Apache FOP for formatting slides"
        }
    }    
} 
slides.show()
