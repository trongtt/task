/*
 * Copyright (C) 2015 eXo Platform SAS.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */

package org.exoplatform.task.management.service;

import org.exoplatform.commons.api.settings.SettingService;
import org.exoplatform.commons.api.settings.SettingValue;
import org.exoplatform.commons.api.settings.data.Context;
import org.exoplatform.commons.api.settings.data.Scope;
import org.exoplatform.task.management.model.ViewState;
import org.exoplatform.task.management.model.ViewType;

import javax.inject.Inject;

public class ViewStateService {
  public static final Scope TASK_APP_SCOPE = Scope.APPLICATION.id("taskManagement");

  private final SettingService settingService;

  @Inject
  public ViewStateService(SettingService settingService) {
    this.settingService = settingService;
  }

  public ViewType getViewType(String listId) {
    SettingValue<String> value = (SettingValue<String>) settingService.get(Context.USER, TASK_APP_SCOPE, listId + ".viewType");
    if (value != null) {
      return ViewType.valueOf(value.getValue());
    }
    return ViewType.LIST;
  }

  public void setViewType(String listId, ViewType viewType) {
    settingService.set(Context.USER, TASK_APP_SCOPE, listId + ".viewType", SettingValue.create(viewType.toString()));
  }

  public String getOrderBy(String listId) {
    SettingValue<String> value = (SettingValue<String>) settingService.get(Context.USER, TASK_APP_SCOPE, listId + ".orderBy");
    if (value != null) {
      return value.getValue();
    }
    return null;
  }

  public void setOrderBy(String listId, String orderBy) {
    settingService.set(Context.USER, TASK_APP_SCOPE, listId + ".orderBy", SettingValue.create(orderBy));
  }

  public String getGroupBy(String listId) {
    SettingValue<String> value = (SettingValue<String>) settingService.get(Context.USER, TASK_APP_SCOPE, listId + ".groupBy");
    if (value != null) {
      return value.getValue();
    }
    return null;
  }

  public void setGroupBy(String listId, String groupBy) {
    settingService.set(Context.USER, TASK_APP_SCOPE, listId + ".groupBy", SettingValue.create(groupBy));
  }

  public ViewState.Filter getFilter(String listId) {
    ViewState.Filter filter = new ViewState.Filter(listId);

    SettingValue<String> value = (SettingValue<String>) settingService.get(Context.USER, TASK_APP_SCOPE, listId + ".filter.enabled");
    if (value != null) {
      filter.setEnabled(Boolean.valueOf(value.getValue()));
    }

    value = (SettingValue<String>) settingService.get(Context.USER, TASK_APP_SCOPE, listId + ".filter.keyword");
    if (value != null) {
      filter.setKeyword(value.getValue());
    }

    value = (SettingValue<String>) settingService.get(Context.USER, TASK_APP_SCOPE, listId + ".filter.status");
    if (value != null) {
      filter.setStatus(Long.valueOf(value.getValue()));
    }

    value = (SettingValue<String>) settingService.get(Context.USER, TASK_APP_SCOPE, listId + ".filter.showCompleted");
    if (value != null) {
      filter.setShowCompleted(Boolean.valueOf(value.getValue()));
    }

    return filter;
  }

  public void saveFilter(ViewState.Filter fd) {
    settingService.set(Context.USER, TASK_APP_SCOPE, fd.getId() + ".filter.enabled", SettingValue.create(String.valueOf(fd.isEnabled())));
    settingService.set(Context.USER, TASK_APP_SCOPE, fd.getId() + ".filter.keyword", SettingValue.create(fd.getKeyword()));
    if (fd.getStatus() != null) {
      settingService.set(Context.USER, TASK_APP_SCOPE, fd.getId() + ".filter.status", SettingValue.create(String.valueOf(fd.getStatus())));
    } else {
      settingService.remove(Context.USER, TASK_APP_SCOPE, fd.getId() + ".filter.status");
    }
    settingService.set(Context.USER, TASK_APP_SCOPE, fd.getId() + ".filter.showCompleted", SettingValue.create(String.valueOf(fd.isShowCompleted())));
//    settingService.set(Context.USER, TASK_APP_SCOPE, listId + ".filter.due", SettingValue.create(fd.getDue().toString()));
//    settingService.set(Context.USER, TASK_APP_SCOPE, listId + ".filter.labels", SettingValue.create(fd.getLabel().toArray().toString()));
//    settingService.set(Context.USER, TASK_APP_SCOPE, listId + ".filter.assignees", SettingValue.create(fd.getAssignees().toArray().toString()));
  }
}
