package rs.raf.Domaci3bek.security;

import io.jsonwebtoken.Claims;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import rs.raf.Domaci3bek.service.TokenService;

import java.lang.reflect.Method;
import java.util.Arrays;

@Aspect
@Configuration
public class SecurityAspect {
    //ovde radimo logiku da proverimo da li korisnik ima odredjene permisije ili nema
    @Autowired
    TokenService tokenService;

    @Around("@annotation(rs.raf.Domaci3bek.security.Can_read)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String token = null;
        for(int i = 0; i < methodSignature.getParameterNames().length; i++){
            if(methodSignature.getParameterNames()[i].equals("authorization")){
                if(joinPoint.getArgs()[i].toString().startsWith("Bearer")){
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }
        if(token == null){
            return new ResponseEntity<>("Niste autorizovani",HttpStatus.UNAUTHORIZED);
        }
        //ukoliko sam nasao token zelim da ga parsiram da vidim sta sve imamo u paylod-u
        Claims claims = tokenService.parseToken(token);
        if(claims == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
//        Can_read can_read = method.getAnnotation(Can_read.class);
        Boolean isCanRead = claims.get("can_read", Boolean.class);

            if(isCanRead != null && isCanRead){
                return joinPoint.proceed();
            }
        return new ResponseEntity<>("Korisnik nema permisiju", HttpStatus.FORBIDDEN);

    }

    @Around("@annotation(rs.raf.Domaci3bek.security.Can_Delete)")
    public Object around1(ProceedingJoinPoint joinPoint) throws Throwable{

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String token = null;
        for(int i = 0; i < methodSignature.getParameterNames().length; i++){
            if(methodSignature.getParameterNames()[i].equals("authorization")){
                if(joinPoint.getArgs()[i].toString().startsWith("Bearer")){
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }
        if(token == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Claims claims = tokenService.parseToken(token);
        if(claims == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Boolean isCanDelete = claims.get("can_delete", Boolean.class);
        if(isCanDelete != null && isCanDelete)
            return joinPoint.proceed();
        return new ResponseEntity<>("Korisnik nema permisiju",HttpStatus.FORBIDDEN);

    }

    @Around("@annotation(rs.raf.Domaci3bek.security.Can_Create)")
    public Object around2(ProceedingJoinPoint joinPoint) throws Throwable{

        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();

        String token = null;
        for(int i = 0; i < methodSignature.getParameterNames().length; i++){
            if(methodSignature.getParameterNames()[i].equals("authorization")){
                if(joinPoint.getArgs()[i].toString().startsWith("Bearer")){
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }
        if(token == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Claims claims = tokenService.parseToken(token);
        Boolean isCanCreate = claims.get("can_create", Boolean.class);
        if(isCanCreate != null && isCanCreate){
            return joinPoint.proceed();
        }
        return new ResponseEntity<>("Korisnik nema permisiju",HttpStatus.FORBIDDEN);
    }

    @Around("@annotation(rs.raf.Domaci3bek.security.Can_update)")
    public Object around3(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String token = null;
        for(int i = 0; i < methodSignature.getParameterNames().length; i++){
            if(methodSignature.getParameterNames()[i].equals("authorization")){
                if(joinPoint.getArgs()[i].toString().startsWith("Bearer")){
                    token = joinPoint.getArgs()[i].toString().split(" ")[1];
                }
            }
        }
        if(token == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Claims claims = tokenService.parseToken(token);
        if(claims == null)
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);

        Boolean isCanCreate = claims.get("can_create", Boolean.class);
        if(isCanCreate != null && isCanCreate){
            return joinPoint.proceed();
        }
        return new ResponseEntity<>("Korsnik nema permisiju", HttpStatus.FORBIDDEN);

    }
}
