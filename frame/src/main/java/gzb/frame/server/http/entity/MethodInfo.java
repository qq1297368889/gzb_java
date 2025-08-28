package gzb.frame.server.http.entity;

import gzb.frame.annotation.*;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.concurrent.Semaphore;

public class MethodInfo {
    GetMapping getMapping;
    PostMapping postMapping;
    PutMapping putMapping;
    DeleteMapping deleteMapping;
    RequestMapping requestMapping;
    DecoratorStart decoratorStart;
    DecoratorEnd decoratorEnd;
    DecoratorOpen decoratorOpen;
    ThreadInterval threadInterval;
    Limitation limitation;
    Semaphore semaphore=null;
    private Method method;
    private Class[]TypeClass;
    private String[]TypeName;

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public Limitation getLimitation() {
        return limitation;
    }

    public void setLimitation(Limitation limitation) {
        this.limitation = limitation;
    }

    public ThreadInterval getThreadInterval() {
        return threadInterval;
    }

    public void setThreadInterval(ThreadInterval threadInterval) {
        this.threadInterval = threadInterval;
    }

    public DecoratorOpen getDecoratorOpen() {
        return decoratorOpen;
    }

    public void setDecoratorOpen(DecoratorOpen decoratorOpen) {
        this.decoratorOpen = decoratorOpen;
    }

    public DecoratorStart getDecoratorStart() {
        return decoratorStart;
    }

    public void setDecoratorStart(DecoratorStart decoratorStart) {
        this.decoratorStart = decoratorStart;
    }

    public DecoratorEnd getDecoratorEnd() {
        return decoratorEnd;
    }

    public void setDecoratorEnd(DecoratorEnd decoratorEnd) {
        this.decoratorEnd = decoratorEnd;
    }

    public GetMapping getGetMapping() {
        return getMapping;
    }

    public void setGetMapping(GetMapping getMapping) {
        this.getMapping = getMapping;
    }

    public PostMapping getPostMapping() {
        return postMapping;
    }

    public void setPostMapping(PostMapping postMapping) {
        this.postMapping = postMapping;
    }

    public PutMapping getPutMapping() {
        return putMapping;
    }

    public void setPutMapping(PutMapping putMapping) {
        this.putMapping = putMapping;
    }

    public DeleteMapping getDeleteMapping() {
        return deleteMapping;
    }

    public void setDeleteMapping(DeleteMapping deleteMapping) {
        this.deleteMapping = deleteMapping;
    }

    public RequestMapping getRequestMapping() {
        return requestMapping;
    }

    public void setRequestMapping(RequestMapping requestMapping) {
        this.requestMapping = requestMapping;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class[] getTypeClass() {
        return TypeClass;
    }

    public void setTypeClass(Class[] typeClass) {
        TypeClass = typeClass;
    }

    public String[] getTypeName() {
        return TypeName;
    }

    public void setTypeName(String[] typeName) {
        TypeName = typeName;
    }

    @Override
    public String toString() {
        return "MethodInfo{" +
                "getMapping=" + getMapping +
                ", postMapping=" + postMapping +
                ", putMapping=" + putMapping +
                ", deleteMapping=" + deleteMapping +
                ", requestMapping=" + requestMapping +
                ", decoratorStart=" + decoratorStart +
                ", decoratorEnd=" + decoratorEnd +
                ", decoratorOpen=" + decoratorOpen +
                ", method=" + method +
                ", TypeClass=" + Arrays.toString(TypeClass) +
                ", TypeName=" + Arrays.toString(TypeName) +
                '}';
    }
}
