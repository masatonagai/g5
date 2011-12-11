package g5

import org.junit.Test

class SlideBuilderTest {
  
    @Test
    void sample() {
        def slideBuilder = new SlideBuilder()
        
        // custom object
        def h1 = { 
            slideBuilder.block 'font-size':'36pt', 'margin-bottom':'18pt', it
        }
        
        def slides = slideBuilder.slides 'font-family':'Verdana', 
                'font-size':'24pt', {
                slide 'text-align':'center', {
                    h1 { 
                        inline color:'gray', "Introduction to " 
                        inline 'font-size':'48pt', "G5" 
                    }
                    block "Nagai Masato"
                    block 'font-size':'16pt', color:'gray', "@nagai_masato"
                }
                // use shortcuts
                sl {
                    h1 "What is G5?"
                    ls {
                        li { b "Groovy Slideshow System" }
                        li { b "Uses XSL-FO"
                            ls label:'-', { 
                                li { b "Supports many output formats" }
                                li { b "You can choose your favorite formatter" }
                            }
                        }
                    }
                }    
            } 
    }

}
