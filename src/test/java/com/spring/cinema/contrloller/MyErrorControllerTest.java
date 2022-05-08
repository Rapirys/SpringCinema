package com.spring.cinema.contrloller;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyErrorControllerTest {


    @Test
    void handleError404() {
        MyErrorController myErrorController = new MyErrorController();

        HttpServletRequest request = mock(HttpServletRequest.class);
        Model model = mock(Model.class);
        when(request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE)).thenReturn("404");
        try {
            Assertions.assertThat(myErrorController.handleError(request, model)).isEqualTo("error");
        } catch (Exception e){
            Assertions.fail("Error controller throw error" ,e);
        }
        verify(model).addAttribute("error", "404");
    }

    @Test
    void handleError403() {
        MyErrorController myErrorController = new MyErrorController();
        try {
            Assertions.assertThat(myErrorController.handleError403()).isEqualTo("error403");
        } catch (Exception e){
            Assertions.fail("Error controller throw error" ,e);
        }

    }

}