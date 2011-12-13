package g5

def slideBuilder = new SlideBuilder()

// custom object
def h1 = { 
    slideBuilder.block 'font-size':'36pt', 'margin-bottom':'18pt', it
}

def slides = slideBuilder 'font-family':'Verdana', 'font-size':'24pt', {
    slide 'text-align':'center', {
        h1 { 
            inline color:'gray', "Introduction to " 
            inline 'font-size':'48pt', "G5" 
        }
        block "Nagai Masato"
        block 'font-size':'16pt', color:'gray', "@nagai_masato"
    }
    slide {
        h1 "What is G5?"
        list {
            listItem { block { 
                image 'http://groovy.codehaus.org/images/groovy-logo-medium.png' 
                inline "Slideshow System" 
            } }
            listItem { block "Uses XSL-FO"
                list label:'-', { 
                    listItem { b "Supports many output formats" }
                    listItem { b "You can choose your favorite formatter" }
                }
            }
        }
    }    
} 
slides.show() 
