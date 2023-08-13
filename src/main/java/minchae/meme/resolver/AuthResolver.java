package minchae.meme.resolver;

import minchae.meme.entity.UserSession;
import minchae.meme.exception.Unauthorized;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

public class AuthResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
//        if (Objects.equals(webRequest.getParameter("accessToken"), "aaaa")) throw new Unauthorized();

        //todo 데이터베이스에 있는 사용자인지 확인작업

        return UserSession.builder()
                .accessToken(webRequest.getParameter("accessToken"))
                .build();
    }
}
