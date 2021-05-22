package com.example.multichoice_app.listener;

import com.example.multichoice_app.model.JSONExamLogObject;

public interface ChoiceCallback {
    void execute(int pos, JSONExamLogObject.QuestionLog questionLog );
}
