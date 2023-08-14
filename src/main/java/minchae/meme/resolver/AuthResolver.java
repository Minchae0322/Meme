package minchae.meme.resolver;

import lombok.RequiredArgsConstructor;
import minchae.meme.auth.Encode;
import minchae.meme.entity.Session;
import minchae.meme.exception.Unauthorized;
import minchae.meme.service.SessionService;
import minchae.meme.session.UserSession;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.Objects;

@RequiredArgsConstructor
public class AuthResolver implements HandlerMethodArgumentResolver {



    private final SessionService sessionService;
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(UserSession.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        if (webRequest.getParameter("accessToken") != null || Objects.equals(webRequest.getParameter("accessToken"), "")) {
            throw new Unauthorized();
        }


        Session session = sessionService.getSessionByAccessToken(webRequest.getParameter("accessToken"));
        //todo 데이터베이스에 있는 사용자인지 확인작업

        return new UserSession(session.getUser().getId());
    }
}
