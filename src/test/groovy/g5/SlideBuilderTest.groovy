package g5

import org.junit.Test

class SlideBuilderTest {
  
    @Test
    void sample() {
        def builder = new SlideBuilder()
        // custom element 
        def h1 = { builder.block 'font-size':'36pt', it }
        
        def presentation =
            builder.presentation 'font-family':'Verdana', 
                'font-size':'24pt', {
                slide 'text-align':'center', {
                    block {
                        h1 "G5"
                        block "Nagai Masato", {
                            inline color: 'red', "@nagai_masato"
                        }
                    }
                }
                slide {
                    h1 "What is G5?"
                    list {
                        listItem { block "Groovic Groovy-based Grooving-slide Generator for Groovies" }
                        listItem { block "XSL-FO-based"
                            list label:'-', { 
                                listItem {
                                    block "Supports many output formats"
                                }
                                listItem {
                                    block "You can choose your favorite formatter"
                                }
                            }
                        }
                    }
                }    
            } 
    }

}
