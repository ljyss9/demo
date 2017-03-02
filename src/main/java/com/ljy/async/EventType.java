package com.ljy.async;

/**
 * Created by ljy on 2017/2/19.
 */
public enum EventType
{
        LIKE(0),
        COMMENT(1),
        LOGIN(2),
        MAIL(3);

        private int value;
        EventType(int value){
            this.value = value;
        }

        public int getValue(){
            return this.value;
        }

}
