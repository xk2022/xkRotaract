package com.xk.common.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by yuan on 2018/07/03
 */
@ControllerAdvice // 監聽controller類
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    // 捕获404错误
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handle404(NoHandlerFoundException ex, Model model) {
        System.out.println("Handling 404 error");

        model.addAttribute("message", "Page not found");
        return "error/404";  // 返回自定义的 404 页面
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handle500(HttpServletRequest request, Exception ex, Model model) {

        logger.error("Request URL : {}, Exception : {}", request.getRequestURI(), ex);
        // 记录错误信息
        logger.error("An unexpected error occurred: ", ex);
        model.addAttribute("message", ex.getMessage());
        return "error/500";  // 返回自定义的 500 页面
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("message", "上传失败，文件大小超过限制！");
        return "redirect:/upload-page"; // 替换为你的上传页面路径
    }

    /**
     * 异常处理
     *
     * @param request
     * @param e
     * @return
     */
//    @ExceptionHandler(Exception.class)
//    public ModelAndView exceptionHandler(HttpServletRequest request, Exception e) throws Exception {
//        logger.error("Request URL : {}, Exception : {}", request.getRequestURI(), e);
//        if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null) {
//            throw e;
//        }
//        ModelAndView mv = new ModelAndView();
//        mv.addObject("url", request.getRequestURL());
//        mv.addObject("exception", e);
//        mv.setViewName("error/error");
//        return mv;
//    }

}
