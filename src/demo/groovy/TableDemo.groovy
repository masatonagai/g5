import g5.SlideBuilder

def slideBuilder = new SlideBuilder()

// overwrite tableCell
def tableCell = { 
    slideBuilder.tableCell padding:'4px 8px 4px 8px', it
}

def slides = slideBuilder {
    slide {
        block 'font-size':'24pt', {
            block "File Extensions of"
            block 'text-align':'right', "Programming Languages"
        }
        table 'font-size':'18pt', {
            tableColumn 'column-width':'40%'; tableColumn 'column-width':'60%'
            tableHeader 'background-color':'gray', { 
                tableCell "Language";     tableCell "File Extensions" 
            }
            tableBody 'background-color':'lightGray', {
                tableRow { tableCell "groovy"     ;tableCell "g, gy, gvy, groovy" }
                tableRow { tableCell "ruby"       ;tableCell "rb" }
                tableRow { tableCell "python"     ;tableCell "py" }
            }
        }
    }
}
slides.show()