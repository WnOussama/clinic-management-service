package com.nexym.clinic.utils;

import org.junit.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FormatUtilTest {

    @Test
    void should_is_filled_collection_return_true() {
        assertTrue(FormatUtil.isFilled(List.of(new Object())));
    }

    @Test
    void should_is_filled_collection_return_false() {
        assertFalse(FormatUtil.isFilled(List.of()));
    }

    @Test
    void should_is_filled_string_return_true() {
        assertTrue(FormatUtil.isFilled(List.of("content")));
    }

    @Test
    void should_is_filled_string_return_false() {
        assertFalse(FormatUtil.isFilled(""));
    }
}
