package g5

import org.junit.Test

class SlideBuilderTest {
  
    @Test
    void test() {
        def presentation = new SlideBuilder()
            .presentation 'font-family': 'Verdana', {
                slide {
                    list {
                        listItem {
                            block 'item #1'
                        }
                        listItem {
                            block 'item #2'
                            list label: '-', { 
                                listItem {
                                    block 'item #2.1'
                                }
                                listItem {
                                    block "item #2.2"
                                }
                            }
                        }
                    }
                }    
            } 
        println presentation
    }

}
