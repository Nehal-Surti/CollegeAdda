package com.example.android.collegeadda;

public class ChatMessage {

    private String text;
    private String name;
    boolean copy;

    public ChatMessage(){

    }

    public ChatMessage(String text, String name,boolean copy){
        this.text=text;
        this.name=name;
        this.copy=copy;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public boolean isCopy() {
        return copy;
    }

    public void setCopy(boolean copy) {
        this.copy = copy;
    }


}
