/**
 * 체크 박스 설정 따라 변화
 */
function setControlChart() {  
    initScroll();
}


/**
 * 날짜 이동
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
 * 표시 하는 총 날짜 수 변화
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

