package com.nexoservice.web.controller;

import com.nexoservice.exception.BusinessException;
import com.nexoservice.exception.DuplicateResourceException;
import com.nexoservice.exception.ResourceNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice(basePackages = "com.nexoservice.web.controller")
public class WebExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ModelAndView handleResourceNotFound(ResourceNotFoundException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alertaError", exception.getMessage());
        return new ModelAndView("redirect:" + rutaAnterior(request));
    }

    @ExceptionHandler({BusinessException.class, DuplicateResourceException.class})
    public ModelAndView handleBusinessException(RuntimeException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alertaError", exception.getMessage());
        return new ModelAndView("redirect:" + rutaAnterior(request));
    }

    @ExceptionHandler(BindException.class)
    public ModelAndView handleValidationException(BindException exception, HttpServletRequest request, RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("alertaError", mensajeValidacion(exception));
        return new ModelAndView("redirect:" + rutaAnterior(request));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException exception, HttpServletRequest request) {
        return errorView(HttpStatus.FORBIDDEN, exception.getMessage(), request.getRequestURI());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception exception, HttpServletRequest request) {
        return errorView(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Ocurrio un error inesperado al procesar la solicitud",
                request.getRequestURI()
        );
    }

    private ModelAndView errorView(HttpStatus status, String message, String path) {
        ModelAndView modelAndView = new ModelAndView("error/general");
        ModelMap model = modelAndView.getModelMap();
        model.addAttribute("status", status.value());
        model.addAttribute("error", status.getReasonPhrase());
        model.addAttribute("message", message);
        model.addAttribute("path", path);
        modelAndView.setStatus(status);
        return modelAndView;
    }

    private boolean esFormulario(HttpServletRequest request) {
        return "POST".equalsIgnoreCase(request.getMethod())
                || "PUT".equalsIgnoreCase(request.getMethod())
                || "PATCH".equalsIgnoreCase(request.getMethod());
    }

    private String rutaAnterior(HttpServletRequest request) {
        String referer = request.getHeader("Referer");
        String contextPath = request.getContextPath() == null ? "" : request.getContextPath();

        if (referer == null || referer.isBlank()) {
            return contextPath + "/dashboard";
        }

        String scheme = request.getScheme() + "://" + request.getServerName();
        int port = request.getServerPort();
        String base = scheme + ((port == 80 || port == 443) ? "" : ":" + port);

        if (referer.startsWith(base)) {
            String path = referer.substring(base.length());
            return path.isBlank() ? contextPath + "/dashboard" : path;
        }

        return contextPath + "/dashboard";
    }

    private String mensajeValidacion(BindException exception) {
        FieldError fieldError = exception.getBindingResult().getFieldError();

        if (fieldError == null) {
            return "Revisa los datos ingresados antes de continuar.";
        }

        String mensaje = fieldError.getDefaultMessage();
        return mensaje == null || mensaje.isBlank()
                ? "Revisa el campo " + fieldError.getField() + " antes de continuar."
                : mensaje;
    }
}
