package g5

import org.junit.Test

class SlideBuilderTest {
    
    @Test
    void test() {
        def presentation = new SlideBuilder()
            .presentation encoding: 'iso-8859-1', {
                slide {
                    list {
                        listItem {
                            block 'text-decoration':'underline', "item 1" 
                            list label: '-', { 
                                listItem {
                                    block "item 1-1"
                                }
                                listItem {
                                    block "item 1-2"
                                }
                            }
                            
                        }
                        listItem {
                            block "item 2"
                        }
                        listItem {
                            block "item 3"
                        }
                    }
                }    
            } 
        println presentation
    }

}
