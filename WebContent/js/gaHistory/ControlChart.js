/**
 * üũ �ڽ� ���� ���� ��ȭ
 */
function setControlChart() {  
    initScroll();
}


/**
 * ��¥ �̵�
 */
function changeIndex(change) {
    if(change == 100)
        change = dayCount;
    else if(change == -100)
        change = -dayCount;

    index += change;

    if(index < startIndex)
        index = startIndex;
    if(index + dayCount >= size)
        index = size - dayCount;
    
    applyScroll(); 

    for(var i = 0; i < chartCount; i++) {        
            charts[i].draw();
    }
}

/**
 * ǥ�� �ϴ� �� ��¥ �� ��ȭ
 */
function changeDayCount(change) {
    var temp = dayCount;
    temp += change;
    
    if ( temp > size )
        return;
    
    applyScroll();

    if(temp >= 5 && temp <= 500) {
        dayCount = temp;

        if(index + dayCount >= size) {
            while(index + dayCount > size) {
                index--;
            }
        }
        
        chartWidthGap = chartWidth / (dayCount + 1);
        
        for(var i = 0; i < chartCount; i++) {
                charts[i].draw();            
        }
    }
}

