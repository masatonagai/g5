package g5

import org.junit.Test

class SlideBuilderTest {
    
    @Test
    void test() {
        def g5 = new SlideBuilder()
        def presentation = g5.presentation {
            slide {
                list([
                    "Are you a Groovie?",
                    "Wanna write anything in Groovy?",
                    "Then write slides in Groovy!"
                ])
            }    
        } 
        println presentation
    }

}
