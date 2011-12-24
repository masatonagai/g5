package g5

class SlideAttributes {
    
    def pageSize(int w, int h) {
        ['page-width':"${w}mm",'page-height':"${h}mm"]    
    }
    
    // PA series
    final PA0 = pageSize(1120, 840)
    final PA1 = pageSize(840, 560)
    final PA2 = pageSize(560, 420)
    final PA3 = pageSize(420, 280)
    final PA4 = pageSize(280, 210)
    final PA5 = pageSize(210, 140)
   
    // A series
    final A0 = pageSize(1189, 841)
    final A1 = pageSize(841, 594)
    final A2 = pageSize(594, 420)
    final A3 = pageSize(420, 297) 
    final A4 = pageSize(297, 210)
    final A5 = pageSize(210, 148)
    
}