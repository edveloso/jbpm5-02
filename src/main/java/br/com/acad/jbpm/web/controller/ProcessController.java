package br.com.acad.jbpm.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.drools.KnowledgeBase;
import org.drools.SystemEventListenerFactory;
import org.drools.impl.EnvironmentFactory;
import org.drools.persistence.jpa.JPAKnowledgeService;
import org.drools.runtime.Environment;
import org.drools.runtime.EnvironmentName;
import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.ProcessInstance;
import org.jbpm.process.workitem.wsht.LocalHTWorkItemHandler;
import org.jbpm.task.Group;
import org.jbpm.task.TaskService;
import org.jbpm.task.User;
import org.jbpm.task.query.TaskSummary;
import org.jbpm.task.service.local.LocalTaskService;
import org.jbpm.task.service.persistence.TaskSessionFactory;
import org.jbpm.task.utils.OnErrorAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.AbstractPlatformTransactionManager;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Handles requests for the application home page.
 */
@Controller
public class ProcessController {
	
	private static final Logger logger = LoggerFactory.getLogger(ProcessController.class);
	
	@Autowired
	private EntityManagerFactory jbpmEMF;
	
	@Autowired
	private KnowledgeBase kbase;
	
	@Autowired
	private AbstractPlatformTransactionManager jbpmTxManager;
	
	@Autowired
	private TaskService taskService;
	

	
	
	
	@Autowired
	private TaskSessionFactory springTaskSessionFactory;
	
	
	/**
	 * Simply selects the home view to render by returning its name.
	 */
	@Transactional
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public String home(Locale locale, Model model) {
		logger.info("Welcome home! The client locale is {}.", locale);
		
		/**
		 * 
		 * @begin
		 * 
		 */
//		TransactionManager transactionManager = (TransactionManager) new DroolsSpringTransactionManager( jbpmTxManager );
		Environment env = EnvironmentFactory.newEnvironment();  
		
//		EntityManagerFactory emf = Persistence.createEntityManagerFactory("org.hibernate.ejb.HibernatePersistence");
		env.set(EnvironmentName.ENTITY_MANAGER_FACTORY, jbpmEMF);

//		env.set(EnvironmentName.TRANSACTION_MANAGER, 
//				jbpmTxManager);

		StatefulKnowledgeSession ksession = JPAKnowledgeService.newStatefulKnowledgeSession( kbase, null, env );
		
		//try this
//		 org.jbpm.task.service.TaskService taskService = new org.jbpm.task.service.TaskService(jbpmEMF, SystemEventListenerFactory.getSystemEventListener());
		
	        
	        org.jbpm.task.service.TaskService taskService = 
	        	    new org.jbpm.task.service.TaskService(
	        	    		jbpmEMF, SystemEventListenerFactory.getSystemEventListener());

	        Map<String, User> users = new HashMap<String, User>();
	        users.put("veloso", new User("veloso"));
	        users.put("Administrator", new User("Administrator"));
	        
	        Map<String, Group> groups = new HashMap<String, Group>();
	        taskService.addUsersAndGroups(users, groups);
	        TaskService client = new LocalTaskService(taskService);
	        
	        	LocalTaskService localTaskService = new LocalTaskService(taskService);
	        	LocalHTWorkItemHandler humanTaskHandler = new LocalHTWorkItemHandler(
	        	    localTaskService, ksession, OnErrorAction.RETHROW);
	        	humanTaskHandler.connect();
	        	ksession.getWorkItemManager().registerWorkItemHandler(
	        	    "Human Task", humanTaskHandler);
	        
		
		//end try
		
		
//		
//		LocalHTItemHandlerImpl localHTWorkItemHandler = new LocalHTItemHandlerImpl(client, ksession);
//		localHTWorkItemHandler.connect();
		
		
		/**
		 *  end
		 * */
		
	
		
		ksession.getWorkItemManager().registerWorkItemHandler("Human Task", humanTaskHandler );
		
		ProcessInstance process = ksession.startProcess("com.sample.bpmn");
		Long id = process.getId();  
		
		List<TaskSummary> taskList = client.getTasksAssignedAsPotentialOwner("veloso", "en-UK");
		
		System.out.println(springTaskSessionFactory);
		
		System.out.println(taskService); 
		
		return "home";
	}

	public TaskSessionFactory getSpringTaskSessionFactory() {
		return springTaskSessionFactory;
	}

	public void setSpringTaskSessionFactory(
			TaskSessionFactory springTaskSessionFactory) {
		this.springTaskSessionFactory = springTaskSessionFactory;
	}

	public TaskService getTaskService() {
		return taskService;
	}

	public void setTaskService(TaskService taskService) {
		this.taskService = taskService;
	}

	public KnowledgeBase getKbase() {
		return kbase;
	}

	public void setKbase(KnowledgeBase kbase) {
		this.kbase = kbase;
	}

	public AbstractPlatformTransactionManager getJbpmTxManager() {
		return jbpmTxManager;
	}

	public void setJbpmTxManager(AbstractPlatformTransactionManager jbpmTxManager) {
		this.jbpmTxManager = jbpmTxManager;
	}

	
}
