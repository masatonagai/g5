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
 
def sb = new SlideBuilder()

def nm = { def attrs = [color:'#3283A1']; sb.i(it ? attrs + it : attrs, "G5") }
def tt = { sb.b 'font-size':'48pt', 'margin-bottom':'18pt', it }
def pn = { sb.i 'font-style':'italic', "\"$it\"" }

def ss = sb 'font-family':'Verdana', {
    s {
        tt { i "Introduction to "; nm('font-size':'64pt') }
        b "Nagai Masato" 
        b 'font-size':'16pt', color:'gray', {
            b "Twitter: @nagai_masato"
            b "Blog: http://nagaimasato.blogspot.com/"
        }
    }
    s {
        tt { i "What is "; nm(); i "?" }
        ls 'font-size':'24pt', {
            li { 
                im 'http://groovy.codehaus.org/images/groovy-logo-medium.png' 
                i "Slideshow System" 
            }
            li { i "Allows you to create and show slides using "; pn "Groovy" }
            li { i "Uses "; pn "XSL-FO"; i " and "; pn "Apache FOP"; i " for formatting slides" }
        }
    }    
} 
ss()
