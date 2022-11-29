package com.xc.common.filter;

import com.xc.common.ResponseCode;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

public class ExceptionResolver implements HandlerExceptionResolver {
    private static final Logger log = LoggerFactory.getLogger(ExceptionResolver.class);

    public ModelAndView resolveException(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        log.error("==============================ExceptionResolver=================================");
        log.error("{} Exception by xiongcan", httpServletRequest.getRequestURI(), e);

        ModelAndView modelAndView = new ModelAndView(new MappingJackson2JsonView());
        modelAndView.addObject("status", Integer.valueOf(ResponseCode.ERROR.getCode()));
        modelAndView.addObject("msg", "Custom interface exception, please check the log for details");
        modelAndView.addObject("data", e.toString());

        return modelAndView;
    }
}
