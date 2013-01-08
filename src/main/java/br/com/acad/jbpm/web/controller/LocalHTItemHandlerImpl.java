package br.com.acad.jbpm.web.controller;

import org.drools.runtime.KnowledgeRuntime;
import org.jbpm.task.TaskService;
import org.jbpm.task.utils.OnErrorAction;

public class LocalHTItemHandlerImpl extends GenericHTWorkItemHandler{

    private boolean registeredTaskEvents = false;
    public LocalHTItemHandlerImpl(KnowledgeRuntime session) {
        super(session);
        this.setLocal(true);
    }
    
    public LocalHTItemHandlerImpl(TaskService client, KnowledgeRuntime session) {
        super(client, session, null);
        this.setLocal(true);
    }

    public LocalHTItemHandlerImpl(KnowledgeRuntime session, OnErrorAction action) {
        super(session, action);
        this.setLocal(true);
    }
    
    public LocalHTItemHandlerImpl(TaskService client, KnowledgeRuntime session, OnErrorAction action) {
        super(client, session, action);
        this.setLocal(true);
    }
    
    public LocalHTItemHandlerImpl(TaskService client, KnowledgeRuntime session, OnErrorAction action, ClassLoader classLoader) {
        super(client, session, action, classLoader);
        this.setLocal(true);
    }
  
    @Override
    public void connect(){
        if(!registeredTaskEvents){
            registerTaskEvents();
            this.registeredTaskEvents = true;
        }
    }
    
}
