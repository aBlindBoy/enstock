package com.xc.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MessageUtils {
    private static MessageSource messageSource;

    @Autowired
    public MessageUtils(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    public static   String get(String msgKey) {
        try {//
            log.info("获取到的多语言设置为：" + LocaleContextHolder.getLocale().toString());
            return messageSource.getMessage(msgKey, null, LocaleContextHolder.getLocale());
        } catch (
                Exception e) {
            return msgKey;
        }
    }
}
