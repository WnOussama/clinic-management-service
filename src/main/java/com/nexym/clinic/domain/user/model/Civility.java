package com.nexym.clinic.domain.user.model;

public enum Civility {
    MR("Mr."), MRS("Mrs."), MS("Ms.");

    private final String title;

    private Civility(String t) {
        title = t;
    }
}