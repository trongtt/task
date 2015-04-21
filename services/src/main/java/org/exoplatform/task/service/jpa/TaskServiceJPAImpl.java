/* 
* Copyright (C) 2003-2015 eXo Platform SAS.
*
* This program is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* This program is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see http://www.gnu.org/licenses/ .
*/
package org.exoplatform.task.service.jpa;

import org.exoplatform.task.dao.TaskHandler;
import org.exoplatform.task.dao.jpa.CommentDAOImpl;
import org.exoplatform.task.dao.jpa.ProjectDAOImpl;
import org.exoplatform.task.dao.jpa.StatusDAOImpl;
import org.exoplatform.task.dao.jpa.TaskDAOImpl;
import org.exoplatform.task.domain.Task;
import org.exoplatform.task.factory.ExoEntityManagerFactory;
import org.exoplatform.task.service.GroupByService;
import org.exoplatform.task.service.TaskService;
import org.exoplatform.task.service.impl.AbstractTaskService;
import org.exoplatform.task.service.impl.GroupByProject;
import org.exoplatform.task.service.impl.GroupByStatus;
import org.exoplatform.task.service.impl.GroupByTag;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by The eXo Platform SAS
 * Author : Thibault Clement
 * tclement@exoplatform.com
 * 4/8/15
 */
@Singleton
public class TaskServiceJPAImpl extends AbstractTaskService implements TaskService {

  private static final Logger LOG = Logger.getLogger("TaskServiceJPATestImpl");
  private final List<GroupByService> groupByServices;

  private EntityManager entityManager;
  
  @Inject
  private TaskHandler taskDAO;

  public TaskServiceJPAImpl() {
    pHandler = new ProjectDAOImpl(this);
    tHandler = new TaskDAOImpl(this);
    cHandler = new CommentDAOImpl(this);
    sHandler = new StatusDAOImpl(this);
    
    this.groupByServices = new ArrayList<GroupByService>();
    this.groupByServices.add(new GroupByStatus(this));
    this.groupByServices.add(new GroupByProject(this));
    this.groupByServices.add(new GroupByTag(this));
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }
  
  @Override
  public void save(Task task) {
    taskDAO.beginTransaction();
    taskDAO.update(task);
    taskDAO.commitAndCloseTransaction();
  }

  //To add in interface
  public void update(Task task) {
    taskDAO.beginTransaction();
    Task persistedTask = taskDAO.find(task.getId());
    persistedTask.setTitle(task.getTitle());
    //TO DO: add all set methods...
    taskDAO.update(persistedTask);
    taskDAO.commitAndCloseTransaction();
  }

  @Override
  public Task findTaskById(long id) {
    taskDAO.beginTransaction();
    Task task = taskDAO.find(id);
    taskDAO.closeTransaction();
    return task;
  }

  @Override
  public List<Task> findAllTask() {
    taskDAO.beginTransaction();
    List<Task> tasks = taskDAO.findAll();
    taskDAO.closeTransaction();
    return tasks;
  }

  @Override
  public void remove(Task task) {
    taskDAO.beginTransaction();
    taskDAO.delete(task);
    taskDAO.commitAndCloseTransaction();
  }

  @Override
  public List<GroupByService> getGroupByServices() {
    return Collections.unmodifiableList(this.groupByServices);
  }

  public void startRequest() {
    EntityManagerFactory entityManagerFactory = ExoEntityManagerFactory.getEntityManagerFactory();
    entityManager = entityManagerFactory.createEntityManager();
    entityManager.getTransaction().begin();
  }

  public void endRequest() {
    entityManager.getTransaction().commit();
    entityManager.close();
  }
}

